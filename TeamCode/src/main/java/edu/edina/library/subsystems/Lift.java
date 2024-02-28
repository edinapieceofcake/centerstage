package edu.edina.library.subsystems;

import static edu.edina.library.enums.LiftDriveState.Hang;
import static edu.edina.library.enums.LiftDriveState.HighDropOff;
import static edu.edina.library.enums.LiftDriveState.LowDropOff;
import static edu.edina.library.enums.LiftDriveState.Manual;

import android.util.Log;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PwmControl;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.DropOffOrientation;
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
    private Deadline zeroSwitchTimeout = new Deadline(1000, TimeUnit.MILLISECONDS);

    public Lift(RobotHardware hardware, boolean isTeleop) {
        this.hardware = hardware;
        this.isTeleop = isTeleop;
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
        state.liftServoRange = LiftServoRange.Low;
        state.currentLowDropOffPosition = config.liftLowDropOffPosition;
        state.currentHighDropOffPostiion = config.liftMediumDropOffPosition;
        state.dropOffOrientation = DropOffOrientation.Center;
        state.currentLowLiftDelay = state.lowLiftDelay;
        state.currentHighLiftDelay = state.mediumLiftDelay;
        hardware.leftLiftServo.setPosition(config.startingLeftLiftServoPosition);
        hardware.rightLiftServo.setPosition(config.startingRightLiftServoPosition);
    }

    @Override
    public void start() {
        started = true;
    }

    @Override
    public void stop() {
        started = false;
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
                case Latch:
                    hardware.leftLiftServo.setPosition(config.leftLatchServoPosition);
                    hardware.rightLiftServo.setPosition(config.rightLatchServoPosition);
                    break;
                case Hang:
                    hardware.leftLiftServo.setPosition(config.startingLeftLiftServoPosition);
                    hardware.rightLiftServo.setPosition(config.startingRightLiftServoPosition);
                    state.currentLiftServoState = LiftServoState.Hung;
                    break;
                case Hung:
                    hardware.leftLiftServo.setPwmDisable();
                    hardware.rightLiftServo.setPwmDisable();
                    break;
            }

            hardware.topLiftMotor.setPower(state.currentLiftSlidePower);
            hardware.bottomLiftMotor.setPower(state.currentLiftSlidePower);
            hardware.topLiftMotor.setTargetPosition(state.currentTopMotorTargetPosition);
            hardware.bottomLiftMotor.setTargetPosition(state.currentBottomMotorTargetPosition);
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
                    // if we are above the bottom of the hubs, don't let the lift back down into it so push it out a bit
                    state.currentTopMotorTargetPosition = config.minimumExtensionBeforeRaisingLiftInTicks - 10;
                    state.currentBottomMotorTargetPosition = config.minimumExtensionBeforeRaisingLiftInTicks - 10;
                    state.currentLiftSlidePower = config.liftExtendingPower;
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
                state.currentLiftServoState = LiftServoState.Latch;
                state.highLiftDelay.reset();
            }
        }

        if (state.hangState == HangState.LiftArm) {
            if (state.highLiftDelay.hasExpired()) {
                state.currentLiftServoState = LiftServoState.Latch;
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
                    state.currentLowLiftDelay.reset();
                } else {
                    state.currentLiftServoState = LiftServoState.High;
                    state.currentHighLiftDelay.reset();
                }
            }
        }

        if (state.dropOffState == DropOffState.LiftArm) {
            if (state.currentLiftDriveState == LiftDriveState.LowDropOff) {
                if (state.currentLowLiftDelay.hasExpired()) {
                    state.currentLiftServoState = LiftServoState.Low;
                    state.currentTopMotorTargetPosition = state.currentLowDropOffPosition;
                    state.currentBottomMotorTargetPosition = state.currentLowDropOffPosition;
                    state.dropOffState = DropOffState.SecondExtension;

                    state.secondExtensionTimeout.reset();
                }
            } else {
                if (state.currentHighLiftDelay.hasExpired()) {
                    state.currentLiftServoState = LiftServoState.High;
                    state.currentTopMotorTargetPosition = state.currentHighDropOffPostiion;
                    state.currentBottomMotorTargetPosition = state.currentHighDropOffPostiion;
                    state.dropOffState = DropOffState.SecondExtension;

                    state.secondExtensionTimeout.reset();
                }
            }
        }

        if (state.dropOffState == DropOffState.SecondExtension) {
            // twist the claw as soon as we get to 600 just in case the batter doesn't have enough to get to the high position
            if (state.currentTopMotorPosition < (config.liftTwistPosition)) {
                if (state.dropOffOrientation == DropOffOrientation.Left) {
                    state.twistServoState = TwistServoState.LeftDropOff;
                    state.angleClawState = AngleClawState.LeftDropOff;
                } else if (state.dropOffOrientation == DropOffOrientation.LeftAuto) {
                    state.twistServoState = TwistServoState.LeftAutoDropOff;
                    state.angleClawState = AngleClawState.LeftAutoDropOff;
                } else if (state.dropOffOrientation == DropOffOrientation.Right) {
                    state.twistServoState = TwistServoState.RightDropOff;
                    state.angleClawState = AngleClawState.RightDropOff;
                } else {
                    state.twistServoState = TwistServoState.CenterDropOff;
                    state.angleClawState = AngleClawState.CenterDropOff;
                }
            }

            if (state.currentLiftDriveState == LiftDriveState.LowDropOff) {
                if (state.secondExtensionTimeout.hasExpired()) {
                    state.dropOffState = DropOffState.Finished;
                    state.currentLiftSlideState = LiftSlideState.Idle;
                    state.lastKnownLiftState = LowDropOff;
                } else {
                    if(PoCMath.between(state.currentTopMotorPosition, state.currentLowDropOffPosition - 10, state.currentLowDropOffPosition + 10)){
                        state.dropOffState = DropOffState.Finished;
                        state.currentLiftSlideState = LiftSlideState.Idle;
                        state.lastKnownLiftState = LowDropOff;
                    }
                }
            } else {
                if (state.secondExtensionTimeout.hasExpired()) {
                    state.dropOffState = DropOffState.Finished;
                    state.currentLiftSlideState = LiftSlideState.Idle;
                    state.lastKnownLiftState = HighDropOff;
                } else if (state.currentTopMotorPosition < (state.currentHighDropOffPostiion + 10)) {
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
                state.currentHighLiftDelay.reset();
                state.currentLowLiftDelay.reset();
                state.hangerState = HangerState.Store;
            }
        }

        if (state.pickUpState == PickUpState.DropArm) {
            if (state.lastKnownLiftState == HighDropOff) {
                if (state.currentHighLiftDelay.hasExpired()) {
                    state.hangerState = HangerState.Store;

                    state.currentLiftServoState = LiftServoState.Start;
                    state.pickUpState = PickUpState.SecondRetraction;
                }
            } else {
                if (state.currentLowLiftDelay.hasExpired()) {
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
