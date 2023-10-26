package edu.edina.library.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;

import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftSlideState;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;

public class Lift extends Subsystem{
    private Robot robot;
    private final double maxLiftTicks = 600;
    private boolean liftMotorReset = false;

    public Lift(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {
        robot.RobotState.currentLiftSlideState = LiftSlideState.Idle;
    }

    @Override
    public void start() {
        robot.RobotState.currentLiftServoPosition = RobotConfiguration.getInstance().startingLiftServoPosition;
    }

    @Override
    public void update() {
        if (robot.Started) {
            robot.RobotState.currentTopMotorPosition = robot.RobotHardware.topLiftMotor.getCurrentPosition();
            robot.RobotState.currentBottomMotorPosition = robot.RobotHardware.bottomLiftMotor.getCurrentPosition();

            if (robot.RobotState.currentLiftDriveState == LiftDriveState.Manual) {
                robot.RobotHardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.RobotHardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                switch (robot.RobotState.currentLiftSlideState) {
                    case Retracting:
                        robot.RobotHardware.topLiftMotor.setPower(RobotConfiguration.getInstance().liftRetractingPower);
                        robot.RobotHardware.bottomLiftMotor.setPower(RobotConfiguration.getInstance().liftRetractingPower);
                        break;
                    case Extending:
                        robot.RobotHardware.topLiftMotor.setPower(RobotConfiguration.getInstance().liftExtendingPower);
                        robot.RobotHardware.bottomLiftMotor.setPower(RobotConfiguration.getInstance().liftExtendingPower);
                        break;
                    case Idle:
                        robot.RobotHardware.topLiftMotor.setPower(0);
                        robot.RobotHardware.bottomLiftMotor.setPower(0);
                        break;
                }
            } else {

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

    public void setProperties(boolean closeLift, boolean openLift, boolean a, boolean x, boolean y, boolean b) {
        if (openLift) {
            robot.RobotState.currentLiftSlideState = LiftSlideState.Extending;
            robot.RobotState.currentLiftDriveState = LiftDriveState.Manual;
        } else if (closeLift) {
            robot.RobotState.currentLiftSlideState = LiftSlideState.Retracting;
            robot.RobotState.currentLiftDriveState = LiftDriveState.Manual;
        } else {
            robot.RobotState.currentLiftSlideState = LiftSlideState.Idle;
        }

        if (a) {
            robot.RobotState.currentLiftDriveState = LiftDriveState.Pickup;
        } else if (x) {
            robot.RobotState.currentLiftDriveState = LiftDriveState.Drive;
        } else if (y) {
            robot.RobotState.currentLiftDriveState = LiftDriveState.LowDropOff;
        } else if (b) {
            robot.RobotState.currentLiftDriveState = LiftDriveState.HighDropOff;
        }
    }

    private double round(double originalValue) {
        return ((int)(originalValue * 100)) / 100.0;
    }
}
