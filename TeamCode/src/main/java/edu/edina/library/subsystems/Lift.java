package edu.edina.library.subsystems;

import static edu.edina.library.enums.LiftDriveState.Hang;
import static edu.edina.library.enums.LiftDriveState.HighDropOff;
import static edu.edina.library.enums.LiftDriveState.LowDropOff;
import static edu.edina.library.enums.LiftDriveState.Manual;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PwmControl;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.DropOffState;
import edu.edina.library.enums.HangState;
import edu.edina.library.enums.HangerState;
import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftServoRange;
import edu.edina.library.enums.LiftServoState;
import edu.edina.library.enums.LiftSlideState;
import edu.edina.library.enums.PickUpState;
import edu.edina.library.enums.TwistServoState;
import edu.edina.library.util.PoCMath;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class Lift implements Subsystem {
    private RobotHardware hardware;
    private boolean started = false;
    private boolean isTeleop;
    private boolean liftMotorReset = false;
    private Deadline lowLiftDelay = new Deadline(300, TimeUnit.MILLISECONDS);
    private Deadline mediumLiftDelay = new Deadline(500, TimeUnit.MILLISECONDS);
    private Deadline highLiftDelay = new Deadline(600, TimeUnit.MILLISECONDS);
    private Deadline currentLowLiftDelay;
    private Deadline currentHighLiftDelay;
    private Deadline secondExtensionTimeout = new Deadline(1000, TimeUnit.MILLISECONDS);
    private Deadline zeroSwitchTimeout = new Deadline(1000, TimeUnit.MILLISECONDS);

    public Lift(RobotHardware hardware, boolean isTeleop) {
        this.hardware = hardware;
        this.isTeleop = isTeleop;
    }

    public Lift(Robot robot) {
        this.hardware = robot.RobotHardware;
        this.isTeleop = true;
    }

    @Override
    public void init() {
        RobotState state = RobotState.getInstance();
        RobotConfiguration config = RobotConfiguration.getInstance();

        state.currentLiftDriveState = LiftDriveState.Manual;
        state.currentLiftSlidePower = 0;
        state.currentLiftServoState = LiftServoState.Start;
        state.currentTopMotorTargetPosition = 0;
        state.currentBottomMotorTargetPosition = 0;
        currentLowLiftDelay = lowLiftDelay;
        currentHighLiftDelay = mediumLiftDelay;
        hardware.leftLiftServo.setPosition(config.startingLeftLiftServoPosition);
        hardware.rightLiftServo.setPosition(config.startingRightLiftServoPosition);
    }

    @Override
    public void start() {
        started = true;
    }

    @Override
    public void update() {
        RobotState state = RobotState.getInstance();
        RobotConfiguration config = RobotConfiguration.getInstance();

        if (started) {
            state.currentTopMotorPosition = hardware.topLiftMotor.getCurrentPosition();
            state.currentBottomMotorPosition = hardware.bottomLiftMotor.getCurrentPosition();
            state.currentLiftLength = -0.031914 * state.currentTopMotorPosition + 12.117;
            state.currentLiftAngle = 51.111 * hardware.leftLiftServo.getPosition() + 20.956;
            state.currentLiftHeight = state.currentLiftLength * Math.sin(Math.toRadians(state.currentLiftAngle));

            switch (state.currentLiftDriveState) {
                case Manual:
                    manualControl();
                    break;
                case Hang:
                    hangRobot();
                    break;
                case LowDropOff:
                case HighDropOff:
                    dropOffPixel();
                    break;
                case Pickup:
                case Drive:
                    driveOrPickup();
                    break;
            }

            if (isTeleop) {
                if (!hardware.liftSwitch.getState()) {
                    if (!liftMotorReset) {
                        state.currentLiftSlidePower = 0;
                        state.currentTopMotorTargetPosition = 0;
                        state.currentBottomMotorTargetPosition = 0;
                        hardware.topLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        hardware.topLiftMotor.setTargetPosition(state.currentTopMotorTargetPosition);
                        hardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        hardware.topLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                        hardware.topLiftMotor.setPower(state.currentLiftSlidePower);
                        hardware.bottomLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        hardware.bottomLiftMotor.setTargetPosition(state.currentBottomMotorTargetPosition);
                        hardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        hardware.bottomLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                        hardware.bottomLiftMotor.setPower(state.currentLiftSlidePower);
                        liftMotorReset = true;
                    }
                } else {
                    liftMotorReset = false;
                }
            }

            switch (state.currentLiftServoState) {
                case Start:
                    hardware.leftLiftServo.setPosition(config.startingLeftLiftServoPosition);
                    hardware.rightLiftServo.setPosition(config.startingRightLiftServoPosition);
                    break;
                case Low:
                    if (state.liftServoRange == LiftServoRange.Low) {
                        hardware.leftLiftServo.setPosition(config.leftLowDropOffServoPosition);
                        hardware.rightLiftServo.setPosition(config.rightLowDropOffServoPosition);
                    } else {
                        hardware.leftLiftServo.setPosition(config.leftMediumDropOffServoPosition);
                        hardware.rightLiftServo.setPosition(config.rightMediumDropOffServoPosition);
                    }
                    break;
                case High:
                    if (state.liftServoRange == LiftServoRange.Low) {
                        hardware.leftLiftServo.setPosition(config.leftMediumDropOffServoPosition);
                        hardware.rightLiftServo.setPosition(config.rightMediumDropOffServoPosition);
                    } else {
                        hardware.leftLiftServo.setPosition(config.leftHighDropOffServoPosition);
                        hardware.rightLiftServo.setPosition(config.rightHighDropOffServoPosition);
                    }
                    break;
                case Hang:
                    hardware.leftLiftServo.setPosition(config.startingLeftLiftServoPosition);
                    hardware.rightLiftServo.setPosition(config.startingRightLiftServoPosition);
                    try {
                        Thread.sleep(72);
                    } catch (Exception ex) {}
                    ((PwmControl) hardware.leftLiftServo).setPwmDisable();
                    ((PwmControl) hardware.rightLiftServo).setPwmDisable();
                    break;
            }

            hardware.topLiftMotor.setPower(state.currentLiftSlidePower);
            hardware.bottomLiftMotor.setPower(state.currentLiftSlidePower);
            hardware.topLiftMotor.setTargetPosition(state.currentTopMotorTargetPosition);
            hardware.bottomLiftMotor.setTargetPosition(state.currentBottomMotorTargetPosition);
        }
    }

    public void setProperties(double rightTrigger, double leftTrigger, boolean a, boolean x, boolean y, boolean b, boolean gm2y,
                              boolean dpadUp, boolean dpadDown) {
        RobotState state = RobotState.getInstance();

        if (leftTrigger != 0) {
            state.currentLiftDriveState = Manual;
            state.currentLiftSlideState = LiftSlideState.Retracting;
            state.currentTriggerStrength = Math.abs(leftTrigger);
        } else if (rightTrigger != 0) {
            state.currentLiftDriveState = Manual;
            state.currentLiftSlideState = LiftSlideState.Extending;
            state.currentTriggerStrength = Math.abs(rightTrigger);
        } else {
            state.currentLiftSlideState = LiftSlideState.Idle;
        }

        if (a) {
            // Pickup
            if (state.lastKnownLiftState == LiftDriveState.Drive || state.lastKnownLiftState == LiftDriveState.Pickup) {
                state.angleClawState = AngleClawState.Pickup;
                state.lastKnownLiftState = LiftDriveState.Pickup;
                state.pickUpState = PickUpState.Finished;
                state.currentLiftSlideState = LiftSlideState.Idle;
                state.currentLiftDriveState = Manual;
            } else {
                state.pickUpState = PickUpState.Start;
                state.currentLiftDriveState = LiftDriveState.Pickup;
                state.currentLiftSlideState = LiftSlideState.Retracting;
            }
        }

        if (x) {
            // drive
            if (state.lastKnownLiftState == LiftDriveState.Drive  || state.lastKnownLiftState == LiftDriveState.Pickup) {
                state.angleClawState = AngleClawState.Drive;
                state.lastKnownLiftState = LiftDriveState.Drive;
                state.pickUpState = PickUpState.Finished;
                state.currentLiftSlideState = LiftSlideState.Idle;
                state.currentLiftDriveState = Manual;
            } else {
                state.pickUpState = PickUpState.Start;
                state.currentLiftDriveState = LiftDriveState.Drive;
                state.currentLiftSlideState = LiftSlideState.Retracting;
            }
        }

        if (y) {
            // low
            if (state.lastKnownLiftState == LowDropOff) {
                state.currentLiftDriveState = Manual;
            } else {
                state.currentLiftDriveState = LiftDriveState.LowDropOff;
                if (state.lastKnownLiftState == HighDropOff) {
                    state.dropOffState = DropOffState.FirstExtension;
                } else {
                    state.dropOffState = DropOffState.Start;
                }

                state.currentLiftSlideState = LiftSlideState.Extending;
            }
        }

        if (b) {
            // high
            if (state.lastKnownLiftState == LiftDriveState.HighDropOff) {
                state.currentLiftDriveState = Manual;
            } else {
                state.currentLiftDriveState = LiftDriveState.HighDropOff;
                if (state.lastKnownLiftState == LiftDriveState.LowDropOff) {
                    state.dropOffState = DropOffState.FirstExtension;
                } else {
                    state.dropOffState = DropOffState.Start;
                }

                state.currentLiftSlideState = LiftSlideState.Extending;
            }
        }

        if (gm2y) {
            state.currentLiftDriveState = Hang;
            state.hangState = HangState.Start;
            state.currentLiftSlideState = LiftSlideState.Extending;
        }

        if (dpadUp) {
            state.liftServoRange = LiftServoRange.High;
            currentLowLiftDelay = mediumLiftDelay;
            currentHighLiftDelay = highLiftDelay;
        }

        if (dpadDown) {
            state.liftServoRange = LiftServoRange.Low;
            currentLowLiftDelay = lowLiftDelay;
            currentHighLiftDelay = mediumLiftDelay;
        }
    }

    private void manualControl() {
        RobotState state = RobotState.getInstance();
        RobotConfiguration config = RobotConfiguration.getInstance();

        if (state.currentLiftSlideState == LiftSlideState.Retracting) {
            if (!hardware.liftSwitch.getState()) {
                state.currentLiftSlidePower = config.superSlowLiftRetractingPower;
            } else {
                if ((state.currentLiftServoState != LiftServoState.Start) &&
                        (state.currentTopMotorPosition > config.minimumExtensionBeforeRaisingLiftInTicks)) {
                    // if we are above the bottom of the hubs, don't let the lift back down into it
                    state.currentLiftSlidePower = 0;
                    state.currentLiftSlideState = LiftSlideState.Idle;
                } else {
                    int currentPosition = state.currentTopMotorPosition;
                    int newPosition = currentPosition + (int)(config.liftRetractingStep * state.currentTriggerStrength);

                    state.currentTopMotorTargetPosition = newPosition;
                    state.currentBottomMotorTargetPosition = newPosition;
                    state.currentLiftSlidePower = config.liftExtendingPower;
                }
            }
        } else if (state.currentLiftSlideState == LiftSlideState.Extending) {
            int currentPosition = state.currentTopMotorPosition;
            int newPosition = currentPosition - (int)(config.liftExtenstionStep * state.currentTriggerStrength);

            state.currentTopMotorTargetPosition = newPosition;
            state.currentBottomMotorTargetPosition = newPosition;
            state.currentLiftSlidePower = config.liftExtendingPower;
        }
    }

    private void hangRobot() {
        RobotState state = RobotState.getInstance();
        RobotConfiguration config = RobotConfiguration.getInstance();

        if (state.hangState == HangState.Start) {
            state.currentTopMotorTargetPosition = config.minimumExtensionBeforeRaisingLiftInTicks;
            state.currentBottomMotorTargetPosition = config.minimumExtensionBeforeRaisingLiftInTicks;
            state.currentLiftSlidePower = config.liftExtendingPower;

            state.hangerState = HangerState.Hang;

            state.twistServoState = TwistServoState.Pickup;
            state.angleClawState = AngleClawState.Drive;
            state.hangState = HangState.FirstExtension;
        }

        if (state.hangState == HangState.FirstExtension) {
            if (state.currentTopMotorPosition < (config.minimumExtensionBeforeRaisingLiftInTicks + 10)) {
                state.hangState = HangState.LiftArm;
                state.liftServoRange = LiftServoRange.Low;
                state.currentLiftServoState = LiftServoState.Low;
                highLiftDelay.reset();
            }
        }

        if (state.hangState == HangState.LiftArm) {
            if (highLiftDelay.hasExpired()) {
                state.currentLiftServoState = LiftServoState.Low;
                state.hangState = HangState.RaiseHanger;
            }
        }

        if (state.hangState == HangState.RaiseHanger) {
            if (state.currentHangerPosition < (config.hangMotorHangPosition + 10)) {
                state.hangState = HangState.Finished;
                state.lastKnownLiftState = Hang;
            }
        }
    }

    private void dropOffPixel() {
        RobotState state = RobotState.getInstance();
        RobotConfiguration config = RobotConfiguration.getInstance();

        if (state.dropOffState == DropOffState.Start) {
            state.currentTopMotorTargetPosition = config.minimumExtensionBeforeRaisingLiftInTicks;
            state.currentBottomMotorTargetPosition = config.minimumExtensionBeforeRaisingLiftInTicks;
            state.currentLiftSlidePower = config.liftExtendingPower;

            if (state.currentLiftDriveState == LiftDriveState.LowDropOff) {
                state.hangerState = HangerState.LowDrop;
            } else {
                state.hangerState = HangerState.HighDrop;
            }

            state.dropOffState = DropOffState.FirstExtension;
        }

        if (state.dropOffState == DropOffState.FirstExtension) {
            if (state.lastKnownLiftState == LowDropOff || state.lastKnownLiftState == HighDropOff) {
                // switch the robot hanger position if we are transitioning from low to high or high to low
                if (state.currentLiftDriveState == LiftDriveState.LowDropOff) {
                    state.hangerState = HangerState.LowDrop;
                } else {
                    state.hangerState = HangerState.HighDrop;
                }
            }

            if (state.currentTopMotorPosition < (config.minimumExtensionBeforeRaisingLiftInTicks + 10)) {
                state.dropOffState = DropOffState.LiftArm;
                if (state.currentLiftDriveState == LiftDriveState.LowDropOff) {
                    state.currentLiftServoState = LiftServoState.Low;
                    currentLowLiftDelay.reset();
                } else {
                    state.currentLiftServoState = LiftServoState.High;
                    currentHighLiftDelay.reset();
                }
            }
        }

        if (state.dropOffState == DropOffState.LiftArm) {
            if (state.currentLiftDriveState == LiftDriveState.LowDropOff) {
                if (currentLowLiftDelay.hasExpired()) {
                    state.currentLiftServoState = LiftServoState.Low;
                    state.currentTopMotorTargetPosition = config.liftLowDropOffPosition;
                    state.currentBottomMotorTargetPosition = config.liftLowDropOffPosition;
                    state.dropOffState = DropOffState.SecondExtension;

                    secondExtensionTimeout.reset();
                }
            } else {
                if (currentHighLiftDelay.hasExpired()) {
                    state.currentLiftServoState = LiftServoState.High;
                    state.currentTopMotorTargetPosition = config.liftHighDropOffPosition;
                    state.currentBottomMotorTargetPosition = config.liftHighDropOffPosition;
                    state.dropOffState = DropOffState.SecondExtension;

                    secondExtensionTimeout.reset();
                }
            }
        }

        if (state.dropOffState == DropOffState.SecondExtension) {
            // twist the claw as soon as we get to 600 just in case the batter doesn't have enough to get to the high position
            if (state.currentTopMotorPosition < (config.liftTwistPosition)) {
                state.twistServoState = TwistServoState.CenterDropOff;
                state.angleClawState = AngleClawState.CenterDropOff;
            }

            if (state.currentLiftDriveState == LiftDriveState.LowDropOff) {
                if (secondExtensionTimeout.hasExpired()) {
                    state.dropOffState = DropOffState.Finished;
                    state.currentLiftSlideState = LiftSlideState.Idle;
                    state.lastKnownLiftState = LowDropOff;
                } else {
                    if(PoCMath.between(state.currentTopMotorPosition, config.liftLowDropOffPosition - 10, config.liftLowDropOffPosition + 10)){
                        state.dropOffState = DropOffState.Finished;
                        state.currentLiftSlideState = LiftSlideState.Idle;
                        state.lastKnownLiftState = LowDropOff;
                    }
                }
            } else {
                if (secondExtensionTimeout.hasExpired()) {
                    state.dropOffState = DropOffState.Finished;
                    state.currentLiftSlideState = LiftSlideState.Idle;
                    state.lastKnownLiftState = HighDropOff;
                } else if (state.currentTopMotorPosition < (config.liftHighDropOffPosition + 10)) {
                    state.dropOffState = DropOffState.Finished;
                    state.currentLiftSlideState = LiftSlideState.Idle;
                    state.lastKnownLiftState = HighDropOff;
                }
            }
        }
    }

    private void driveOrPickup() {
        RobotState state = RobotState.getInstance();
        RobotConfiguration config = RobotConfiguration.getInstance();

        if (state.pickUpState == PickUpState.Start) {
            state.currentTopMotorTargetPosition = config.minimumExtensionBeforeRaisingLiftInTicks;
            state.currentBottomMotorTargetPosition = config.minimumExtensionBeforeRaisingLiftInTicks;
            state.currentLiftSlidePower = config.liftRetractingPower;

            state.pickUpState = PickUpState.FirstRetraction;
            state.twistServoState = TwistServoState.Pickup;
            state.angleClawState = AngleClawState.Drive;
        }

        if (state.pickUpState == PickUpState.FirstRetraction) {
            if (state.currentTopMotorPosition > (config.minimumExtensionBeforeRaisingLiftInTicks - 10)) {
                state.pickUpState = PickUpState.DropArm;
                state.currentLiftServoState = LiftServoState.Start;
                currentHighLiftDelay.reset();
                currentLowLiftDelay.reset();
                state.hangerState = HangerState.Store;
            }
        }

        if (state.pickUpState == PickUpState.DropArm) {
            if (state.lastKnownLiftState == HighDropOff) {
                if (currentHighLiftDelay.hasExpired()) {
                    state.hangerState = HangerState.Store;

                    state.currentLiftServoState = LiftServoState.Start;
                    state.pickUpState = PickUpState.SecondRetraction;
                }
            } else {
                if (currentLowLiftDelay.hasExpired()) {
                    state.hangerState = HangerState.Store;

                    state.currentLiftServoState = LiftServoState.Start;
                    state.pickUpState = PickUpState.SecondRetraction;
                }
            }
        }

        if (state.pickUpState == PickUpState.SecondRetraction) {
            state.currentLiftSlidePower = config.slowLiftRetractingPower;
            state.currentTopMotorTargetPosition = config.liftDrivePosition;
            state.currentBottomMotorTargetPosition = config.liftDrivePosition;

            state.pickUpState = PickUpState.WaitForSecondRetraction;
            zeroSwitchTimeout.reset();
        }

        if (state.pickUpState == PickUpState.WaitForSecondRetraction) {
            if (!hardware.liftSwitch.getState() || zeroSwitchTimeout.hasExpired()) {
                if (state.currentLiftDriveState == LiftDriveState.Drive) {
                    state.angleClawState = AngleClawState.Drive;
                    state.lastKnownLiftState = LiftDriveState.Drive;
                } else {
                    state.angleClawState = AngleClawState.Pickup;
                    state.lastKnownLiftState = LiftDriveState.Pickup;
                }

                state.pickUpState = PickUpState.Finished;
                state.currentLiftSlideState = LiftSlideState.Idle;
                state.currentLiftDriveState = Manual;
                state.currentLiftSlidePower = 0;
            }
        }
    }
}
