package edu.edina.library.subsystems;

import static edu.edina.library.enums.LiftDriveState.Manual;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

import edu.edina.library.enums.DropOffState;
import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftServoState;
import edu.edina.library.enums.TwistServoState;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;

public class Lift extends Subsystem{
    private Robot robot;
    private final double maxLiftTicks = 600;
    private boolean liftMotorReset = false;
    private Deadline liftLimit = new Deadline(100, TimeUnit.MILLISECONDS);
    private Deadline liftDelay = new Deadline(2500, TimeUnit.MILLISECONDS);

    public Lift(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {
        robot.RobotState.currentLiftDriveState = LiftDriveState.Manual;
        robot.RobotState.currentLiftSlidePower = 0;
    }

    @Override
    public void start() {
        robot.RobotState.currentLiftServoPosition = RobotConfiguration.getInstance().startingLiftServoPosition;
        robot.RobotHardware.leftLiftServo.setPosition(.5);
        robot.RobotHardware.rightLiftServo.setPosition(.5);
        liftLimit.expire();
    }

    @Override
    public void update() {
        if (robot.Started) {
            robot.RobotState.currentLiftLength = 1 * robot.RobotHardware.topLiftMotor.getCurrentPosition() + 0; // replace 1 &  0 with M & B
            robot.RobotState.currentLiftAngle = 1 * robot.RobotHardware.leftLiftServo.getPosition() + 0; // replace 1 &  0 with M & B
            robot.RobotState.currentTopMotorPosition = robot.RobotHardware.topLiftMotor.getCurrentPosition();
            robot.RobotState.currentBottomMotorPosition = robot.RobotHardware.bottomLiftMotor.getCurrentPosition();

            if (robot.RobotState.currentLiftDriveState == Manual) {
                robot.RobotHardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.RobotHardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.RobotHardware.topLiftMotor.setPower(robot.RobotState.currentLiftSlidePower);
                robot.RobotHardware.bottomLiftMotor.setPower(robot.RobotState.currentLiftSlidePower);
            } else {
                if (robot.RobotState.currentLiftDriveState == LiftDriveState.LowDropOff) {
                    if (robot.RobotState.dropOffState == DropOffState.Start) {
                        robot.RobotHardware.topLiftMotor.setTargetPosition(RobotConfiguration.getInstance().liftLowDropOffPostion);
                        robot.RobotHardware.bottomLiftMotor.setTargetPosition(RobotConfiguration.getInstance().liftLowDropOffPostion);
                        robot.RobotHardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        robot.RobotHardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        robot.RobotHardware.topLiftMotor.setPower(1);
                        robot.RobotHardware.bottomLiftMotor.setPower(1);
                        robot.RobotState.dropOffState = DropOffState.FirstExtension;
                    } else if (robot.RobotState.dropOffState == DropOffState.FirstExtension) {
                        if (robot.RobotHardware.topLiftMotor.getCurrentPosition() < (RobotConfiguration.getInstance().liftLowDropOffPostion + 10)) {
                            robot.RobotState.dropOffState = DropOffState.LiftArm;
                            robot.RobotState.currentLiftServoPosition = 0.5;
                            liftDelay.reset();
                        }
                    } else if (robot.RobotState.dropOffState == DropOffState.LiftArm) {
                        if (liftDelay.hasExpired()) {
                            robot.RobotHardware.topLiftMotor.setTargetPosition(RobotConfiguration.getInstance().liftLowDropOffPostion);
                            robot.RobotHardware.bottomLiftMotor.setTargetPosition(RobotConfiguration.getInstance().liftLowDropOffPostion);
                            robot.RobotHardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            robot.RobotHardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            robot.RobotHardware.topLiftMotor.setPower(1);
                            robot.RobotHardware.bottomLiftMotor.setPower(1);
                            robot.RobotState.dropOffState = DropOffState.SecondExtension;
                        }
                    } else if (robot.RobotState.dropOffState == DropOffState.SecondExtension) {
                        if (robot.RobotHardware.topLiftMotor.getCurrentPosition() < (RobotConfiguration.getInstance().liftLowDropOffPostion + 10)) {
                            robot.RobotState.dropOffState = DropOffState.Finished;
                        }
                    }
                }
                switch (robot.RobotState.currentLiftDriveState) {
                    case Pickup:
                        robot.RobotState.liftTargetPosition = RobotConfiguration.getInstance().liftPickupPosition;
                        break;
                    case Drive:
                        robot.RobotState.liftTargetPosition = RobotConfiguration.getInstance().liftDrivePosition;
                        break;
                    case LowDropOff:
                        robot.RobotState.liftTargetPosition = RobotConfiguration.getInstance().liftLowDropOffPostion;
                        break;
                    case HighDropOff:
                        robot.RobotState.liftTargetPosition = RobotConfiguration.getInstance().liftHighDropOffPosition;
                        break;
                }

                robot.RobotHardware.topLiftMotor.setTargetPosition(robot.RobotState.liftTargetPosition);
                robot.RobotHardware.bottomLiftMotor.setTargetPosition(robot.RobotState.liftTargetPosition);
                robot.RobotHardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.RobotHardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.RobotHardware.topLiftMotor.setPower(1);
                robot.RobotHardware.bottomLiftMotor.setPower(1);
            }
            
            if (liftLimit.hasExpired()) {
                switch (robot.RobotState.currentLiftServoState) {
                    case Rising:
                        if (robot.RobotState.currentLiftLength > RobotConfiguration.getInstance().minimumExtensionBeforeRaisingLift) {
                            robot.RobotState.currentLiftServoPosition += .025;
                        }
                        break;
                    case Falling:
                        robot.RobotState.currentLiftServoPosition -= .025;
                        break;
                }

                liftLimit.reset();
            }

            if (!robot.RobotHardware.liftSwitch.getState()) {
                if (!liftMotorReset) {
                    robot.RobotHardware.topLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    robot.RobotHardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.RobotHardware.topLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    robot.RobotHardware.bottomLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    robot.RobotHardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.RobotHardware.bottomLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    liftMotorReset = true;
                }
            } else {
                liftMotorReset = false;
            }

            robot.RobotHardware.leftLiftServo.setPosition(robot.RobotState.currentLiftServoPosition);
            robot.RobotHardware.rightLiftServo.setPosition(robot.RobotState.currentLiftServoPosition);
        }
    }

    public void setProperties(double rightTrigger, double leftTrigger, boolean a, boolean x, boolean y, boolean b,
                              boolean dpadUp, boolean dpadDown) {
        if (leftTrigger != 0) {
            robot.RobotState.currentLiftSlidePower = leftTrigger;
            robot.RobotState.currentLiftDriveState = Manual;
        } else if (rightTrigger != 0) {
            robot.RobotState.currentLiftSlidePower = -rightTrigger;
            robot.RobotState.currentLiftDriveState = Manual;
        } else {
            robot.RobotState.currentLiftSlidePower = 0;
        }

        if (a) {
            robot.RobotState.currentLiftDriveState = LiftDriveState.Pickup;
        } else if (x) {
            robot.RobotState.currentLiftDriveState = LiftDriveState.Drive;
        } else if (y) {
            robot.RobotState.currentLiftDriveState = LiftDriveState.LowDropOff;
            robot.RobotState.dropOffState = DropOffState.Start;
        } else if (b) {
            robot.RobotState.currentLiftDriveState = LiftDriveState.HighDropOff;
        }

        if (dpadUp) {
            robot.RobotState.currentLiftServoState = LiftServoState.Rising;
        } else if (dpadDown) {
            robot.RobotState.currentLiftServoState = LiftServoState.Falling;
        } else {
            robot.RobotState.currentLiftServoState = LiftServoState.Idle;
        }
    }

    private double round(double originalValue) {
        return ((int)(originalValue * 100)) / 100.0;
    }
}
