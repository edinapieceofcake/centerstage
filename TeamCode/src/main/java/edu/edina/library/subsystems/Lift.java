package edu.edina.library.subsystems;

import static edu.edina.library.enums.LiftDriveState.Manual;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

import edu.edina.library.enums.DropOffState;
import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftServoState;
import edu.edina.library.enums.LiftSlideState;
import edu.edina.library.enums.PickUpState;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class Lift extends Subsystem{
    private Robot robot;
    private boolean liftMotorReset = false;
    private Deadline liftLimit = new Deadline(100, TimeUnit.MILLISECONDS);
    private Deadline lowLiftDelay = new Deadline(2000, TimeUnit.MILLISECONDS);
    private Deadline highLiftDelay = new Deadline(3000, TimeUnit.MILLISECONDS);

    public Lift(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {
        RobotState state = RobotState.getInstance();
        RobotHardware hardware = robot.RobotHardware;
        RobotConfiguration config = RobotConfiguration.getInstance();

        state.currentLiftDriveState = LiftDriveState.Manual;
        state.currentLiftSlidePower = 0;
        state.currentLeftLiftServoPosition = config.startingLeftLiftServoPosition;
        state.currentRightLiftServoPosition = config.startingRightLiftServoPosition;
        hardware.leftLiftServo.setPosition(state.currentLeftLiftServoPosition);
        hardware.rightLiftServo.setPosition(state.currentRightLiftServoPosition);
    }

    @Override
    public void start() {
        liftLimit.expire();
    }

    @Override
    public void update() {
        RobotState state = RobotState.getInstance();
        RobotHardware hardware = robot.RobotHardware;
        RobotConfiguration config = RobotConfiguration.getInstance();

        if (robot.Started) {
            state.currentLiftLength = -0.031914 * hardware.topLiftMotor.getCurrentPosition() + 12.117;
            state.currentLiftAngle = 75 * hardware.leftLiftServo.getPosition() - 0.5;
            state.currentLiftHeight = state.currentLiftLength * Math.sin(Math.toRadians(state.currentLiftAngle));
            state.currentTopMotorPosition = hardware.topLiftMotor.getCurrentPosition();
            state.currentBottomMotorPosition = hardware.bottomLiftMotor.getCurrentPosition();

            if (state.currentLiftDriveState == Manual) {
                hardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                hardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                hardware.topLiftMotor.setPower(state.currentLiftSlidePower);
                hardware.bottomLiftMotor.setPower(state.currentLiftSlidePower);
            } else {
                if ((state.currentLiftDriveState == LiftDriveState.LowDropOff) ||
                    (state.currentLiftDriveState == LiftDriveState.HighDropOff)) {
                    if (state.dropOffState == DropOffState.Start) {
                        hardware.topLiftMotor.setTargetPosition(config.minimumExtensionBeforeRaisingLiftInTicks);
                        hardware.bottomLiftMotor.setTargetPosition(config.minimumExtensionBeforeRaisingLiftInTicks);
                        hardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        hardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        hardware.topLiftMotor.setPower(config.liftExtendingPower);
                        hardware.bottomLiftMotor.setPower(config.liftExtendingPower);
                        state.dropOffState = DropOffState.FirstExtension;
                    } else if (state.dropOffState == DropOffState.FirstExtension) {
                        if (hardware.topLiftMotor.getCurrentPosition() < (config.minimumExtensionBeforeRaisingLiftInTicks + 10)) {
                            state.dropOffState = DropOffState.LiftArm;
                            if (state.currentLiftDriveState == LiftDriveState.LowDropOff) {
                                state.currentLeftLiftServoPosition = config.leftLowDropOffServoPosition;
                                state.currentRightLiftServoPosition = config.rightLowDropOffServoPosition;
                                lowLiftDelay.reset();
                            } else {
                                state.currentLeftLiftServoPosition = config.leftHighDropOffServoPosition;
                                state.currentRightLiftServoPosition = config.rightHighDropOffServoPosition;
                                highLiftDelay.reset();
                            }
                        }
                    } else if (state.dropOffState == DropOffState.LiftArm) {
                        if (state.currentLiftDriveState == LiftDriveState.LowDropOff) {
                            if (lowLiftDelay.hasExpired()) {
                                hardware.topLiftMotor.setTargetPosition(config.liftLowDropOffPosition);
                                hardware.bottomLiftMotor.setTargetPosition(config.liftLowDropOffPosition);
                                state.dropOffState = DropOffState.SecondExtension;
                            }
                        } else {
                            if (highLiftDelay.hasExpired()) {
                                hardware.topLiftMotor.setTargetPosition(config.liftHighDropOffPosition);
                                hardware.bottomLiftMotor.setTargetPosition(config.liftHighDropOffPosition);
                                state.dropOffState = DropOffState.SecondExtension;
                            }

                        }
                    } else if (state.dropOffState == DropOffState.SecondExtension) {
                        if (state.currentLiftDriveState == LiftDriveState.LowDropOff) {
                            if (hardware.topLiftMotor.getCurrentPosition() < (config.liftLowDropOffPosition + 10)) {
                                state.dropOffState = DropOffState.Finished;
                                state.currentLiftSlideState = LiftSlideState.Idle;
                                state.currentLiftDriveState = Manual;
                            }
                        } else {
                            if (hardware.topLiftMotor.getCurrentPosition() < (config.liftHighDropOffPosition + 10)) {
                                state.dropOffState = DropOffState.Finished;
                                state.currentLiftSlideState = LiftSlideState.Idle;
                                state.currentLiftDriveState = Manual;
                            }
                        }
                    }
                } else if ((state.currentLiftDriveState == LiftDriveState.Pickup) ||
                        (state.currentLiftDriveState == LiftDriveState.Drive)) {
                    if (state.pickUpState == PickUpState.Start) {
                        hardware.topLiftMotor.setTargetPosition(config.minimumExtensionBeforeRaisingLiftInTicks);
                        hardware.bottomLiftMotor.setTargetPosition(config.minimumExtensionBeforeRaisingLiftInTicks);
                        hardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        hardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        hardware.topLiftMotor.setPower(config.liftExtendingPower);
                        hardware.bottomLiftMotor.setPower(config.liftExtendingPower);
                        state.pickUpState = PickUpState.FirstRetraction;
                    } else if (state.pickUpState == PickUpState.FirstRetraction) {
                        if (hardware.topLiftMotor.getCurrentPosition() < (config.minimumExtensionBeforeRaisingLiftInTicks + 10)) {
                            state.pickUpState = PickUpState.DropArm;
                            state.currentLeftLiftServoPosition = config.startingLeftLiftServoPosition;
                            state.currentRightLiftServoPosition = config.startingRightLiftServoPosition;
                            highLiftDelay.reset();
                        }
                    } else if (state.pickUpState == PickUpState.DropArm) {
                        if (highLiftDelay.hasExpired()) {
                            state.pickUpState = PickUpState.SecondRetraction;
                        }
                    } else if (state.pickUpState == PickUpState.SecondRetraction) {
                        if (state.currentLiftDriveState == LiftDriveState.Drive) {
                            hardware.topLiftMotor.setTargetPosition(config.liftDrivePosition);
                            hardware.bottomLiftMotor.setTargetPosition(config.liftDrivePosition);
                        } else {
                            hardware.topLiftMotor.setTargetPosition(config.liftPickupPosition);
                            hardware.bottomLiftMotor.setTargetPosition(config.liftPickupPosition);
                        }
                        state.pickUpState = PickUpState.WaitForSecondRetraction;
                    } else if (state.pickUpState == PickUpState.WaitForSecondRetraction) {
                        if (state.currentLiftDriveState == LiftDriveState.Drive) {
                            if (hardware.topLiftMotor.getCurrentPosition() < (config.liftDrivePosition + 10)) {
                                state.pickUpState = PickUpState.Finished;
                                state.currentLiftSlideState = LiftSlideState.Idle;
                                state.currentLiftDriveState = Manual;
                            }
                        } else {
                            if (hardware.topLiftMotor.getCurrentPosition() < (config.liftPickupPosition + 10)) {
                                state.pickUpState = PickUpState.Finished;
                                state.currentLiftSlideState = LiftSlideState.Idle;
                                state.currentLiftDriveState = Manual;
                            }
                        }
                    }
                }

                switch (state.currentLiftDriveState) {
                    case Pickup:
                        state.liftTargetPosition = config.liftPickupPosition;
                        break;
                    case Drive:
                        state.liftTargetPosition = config.liftDrivePosition;
                        break;
                }
            }
            
            if (liftLimit.hasExpired()) {
                switch (state.currentLiftServoState) {
                    case Rising:
                        if (state.currentTopMotorPosition < config.minimumExtensionBeforeRaisingLiftInTicks) {
                            // we have to be out these ticks before we can even raise it so we don't hit the hubs
                            state.currentLeftLiftServoPosition -= 0.025;
                            state.currentRightLiftServoPosition += 0.025;
                        }
                        break;
                    case Falling:
                        state.currentLeftLiftServoPosition += 0.025;
                        state.currentRightLiftServoPosition -= 0.025;
                        break;
                }

//                state.currentLeftLiftServoPosition = Math.max(RobotConfiguration.getInstance().startingLeftLiftServoPosition, Math.min(1.0, state.currentLeftLiftServoPosition));
//                state.currentRightLiftServoPosition = Math.max(RobotConfiguration.getInstance().startingRightLiftServoPosition, Math.min(1.0, state.currentRightLiftServoPosition));
                liftLimit.reset();
            }

            if (!hardware.liftSwitch.getState()) {
                if (!liftMotorReset) {
                    hardware.topLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    hardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    hardware.topLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    hardware.bottomLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    hardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    hardware.bottomLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    liftMotorReset = true;
                }
            } else {
                liftMotorReset = false;
            }

            hardware.leftLiftServo.setPosition(state.currentLeftLiftServoPosition);
            hardware.rightLiftServo.setPosition(state.currentRightLiftServoPosition);
        }
    }

    public void setProperties(double rightTrigger, double leftTrigger, boolean a, boolean x, boolean y, boolean b,
                              boolean dpadUp, boolean dpadDown) {
        RobotState state = RobotState.getInstance();
        RobotHardware hardware = robot.RobotHardware;
        RobotConfiguration config = RobotConfiguration.getInstance();

        if (leftTrigger != 0) {
            state.currentLiftDriveState = Manual;
            state.currentLiftSlideState = LiftSlideState.Retracting;
            if ((hardware.leftLiftServo.getPosition()  > config.liftServoPositionAtBottomOfHubs) &&
                (state.currentTopMotorPosition > config.minimumExtensionBeforeRaisingLiftInTicks)) {
                // if we are above the bottom of the hubs, don't let the lift back down into it
                state.currentLiftSlidePower = 0;
            } else {
                state.currentLiftSlidePower = leftTrigger * .25;
            }
        } else if (rightTrigger != 0) {
            state.currentLiftSlidePower = -rightTrigger;
            state.currentLiftDriveState = Manual;
            state.currentLiftSlideState = LiftSlideState.Extending;
        } else {
            state.currentLiftSlidePower = 0;
            state.currentLiftSlideState = LiftSlideState.Idle;
        }

        if (a) {
            state.currentLiftDriveState = LiftDriveState.Pickup;
            state.pickUpState = PickUpState.Start;
            state.currentLiftSlideState = LiftSlideState.Retracting;
        } else if (x) {
            state.currentLiftDriveState = LiftDriveState.Drive;
            state.pickUpState = PickUpState.Start;
            state.currentLiftSlideState = LiftSlideState.Retracting;
        } else if (y) {
            state.currentLiftDriveState = LiftDriveState.LowDropOff;
            state.dropOffState = DropOffState.Start;
            state.currentLiftSlideState = LiftSlideState.Extending;
        } else if (b) {
            state.currentLiftDriveState = LiftDriveState.HighDropOff;
            state.dropOffState = DropOffState.Start;
            state.currentLiftSlideState = LiftSlideState.Extending;
        }

        if (dpadUp) {
            state.currentLiftServoState = LiftServoState.Rising;
        } else if (dpadDown) {
            state.currentLiftServoState = LiftServoState.Falling;
        } else {
            state.currentLiftServoState = LiftServoState.Idle;
        }
    }

    private double round(double originalValue) {
        return ((int)(originalValue * 100)) / 100.0;
    }
}
