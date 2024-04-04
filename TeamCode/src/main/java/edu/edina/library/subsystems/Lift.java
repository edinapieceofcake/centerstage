package edu.edina.library.subsystems;

import static edu.edina.library.enums.LiftDriveState.Hang;
import static edu.edina.library.enums.LiftDriveState.DropOff;
import static edu.edina.library.enums.LiftDriveState.Manual;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.DropOffOrientation;
import edu.edina.library.enums.DropOffState;
import edu.edina.library.enums.HangState;
import edu.edina.library.enums.HangerState;
import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftServoState;
import edu.edina.library.enums.LiftSlideState;
import edu.edina.library.enums.PickUpState;
import edu.edina.library.enums.TwistServoState;
import edu.edina.library.util.PoCMath;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class Lift implements Subsystem {
    private RobotHardware hardware;
    private boolean started = false;
    private boolean isTeleop;
    private boolean liftMotorReset = false;
    private Deadline zeroSwitchTimeout = new Deadline(1000, TimeUnit.MILLISECONDS);
    private Deadline hangWait = new Deadline(100, TimeUnit.MILLISECONDS);

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
        state.currentLiftMotorDropOffPosition = config.liftDropOffPositionOne;
        state.currentLiftServoStateDropOffPosition = LiftServoState.One;

        state.dropOffOrientation = DropOffOrientation.Center;
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
                case DropOff:
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

            if (state.lastKnownLiftState == DropOff) {
                if (state.liftDPadChanged) {
                    // apply dpad changes right away
                    state.currentTopMotorTargetPosition = state.currentLiftMotorDropOffPosition;
                    state.currentBottomMotorTargetPosition = state.currentLiftMotorDropOffPosition;

                    state.currentLiftServoState = state.currentLiftServoStateDropOffPosition;
                    state.liftDPadChanged = false;
                }
            }

            switch (state.currentLiftServoState) {
                case Start:
                    hardware.leftLiftServo.setPosition(config.startingLeftLiftServoPosition);
                    hardware.rightLiftServo.setPosition(config.startingRightLiftServoPosition);
                    break;
                case One:
                    hardware.leftLiftServo.setPosition(config.leftOne);
                    hardware.rightLiftServo.setPosition(config.rightOne);
                    break;
                case Two:
                case Three:
                    hardware.leftLiftServo.setPosition(config.leftTwo);
                    hardware.rightLiftServo.setPosition(config.rightTwo);
                    break;
                case Four:
                case Five:
                case Six:
                    hardware.leftLiftServo.setPosition(config.leftThree);
                    hardware.rightLiftServo.setPosition(config.rightThree);
                    break;
                case Seven:
                case Eight:
                    hardware.leftLiftServo.setPosition(config.leftFour);
                    hardware.rightLiftServo.setPosition(config.rightFour);
                    break;
                case Nine:
                    hardware.leftLiftServo.setPosition(config.leftFive);
                    hardware.rightLiftServo.setPosition(config.rightFive);
                    break;
                case Latch:
                    hardware.leftLiftServo.setPosition(config.leftLatchServoPosition);
                    hardware.rightLiftServo.setPosition(config.rightLatchServoPosition);
                    break;
                case Hang:
                    hardware.leftLiftServo.setPosition(config.startingLeftLiftServoPosition);
                    hardware.rightLiftServo.setPosition(config.startingRightLiftServoPosition);
                    state.currentLiftServoState = LiftServoState.Hung;
                    hangWait.reset();
                    break;
                case Hung:
                    if (hangWait.hasExpired()) {
                        hardware.leftLiftServo.setPwmDisable();
                        hardware.rightLiftServo.setPwmDisable();
                    }
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
                state.currentLiftSlidePower = 0;
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
                state.currentLiftServoState = LiftServoState.Latch;
                state.hangLiftDelay.reset();
            }
        }

        if (state.hangState == HangState.LiftArm) {
            if (state.hangLiftDelay.hasExpired()) {
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
            state.currentLiftSlidePower = config.liftExtendingPower;

            state.hangerState = HangerState.DropOff;
            state.currentTopMotorTargetPosition = state.currentLiftMotorDropOffPosition;
            state.currentBottomMotorTargetPosition = state.currentLiftMotorDropOffPosition;

            state.dropOffState = DropOffState.FirstExtension;
            state.pusherState = ClawState.Closed;
        }

        if (state.dropOffState == DropOffState.FirstExtension) {
            if (state.currentTopMotorPosition < (config.minimumExtensionBeforeRaisingLiftInTicks + 10)) {
                state.dropOffState = DropOffState.LiftArm;
            }
        }

        if (state.dropOffState == DropOffState.LiftArm) {
            state.dropOffState = DropOffState.SecondExtension;
            state.currentLiftServoState = state.currentLiftServoStateDropOffPosition;
            state.secondExtensionTimeout.reset();
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

            if (state.secondExtensionTimeout.hasExpired()) {
                state.dropOffState = DropOffState.Finished;
                state.currentLiftSlideState = LiftSlideState.Idle;
                state.lastKnownLiftState = DropOff;
            } else if(PoCMath.between(state.currentTopMotorPosition, state.currentLiftMotorDropOffPosition - 10, state.currentLiftMotorDropOffPosition + 10)){
                state.dropOffState = DropOffState.Finished;
                state.currentLiftSlideState = LiftSlideState.Idle;
                state.lastKnownLiftState = DropOff;
            }
        }
    }

    private void driveOrPickup() {
        RobotState state = RobotState.getInstance();
        RobotConfiguration config = RobotConfiguration.getInstance();
        
        if (state.pickUpState == PickUpState.Start) {
            state.currentTopMotorTargetPosition = config.minimumExtensionBeforeRetractingLiftInTicks;
            state.currentBottomMotorTargetPosition = config.minimumExtensionBeforeRetractingLiftInTicks;
            state.currentLiftSlidePower = config.liftRetractingPower;

            state.pickUpState = PickUpState.FirstRetraction;
            state.twistServoState = TwistServoState.Pickup;
            state.angleClawState = AngleClawState.Drive;
            state.pusherState = ClawState.Closed;
        }

        if (state.pickUpState == PickUpState.FirstRetraction) {
            if (state.currentTopMotorPosition > (config.minimumExtensionBeforeRetractingLiftInTicks - 10)) {
                state.pickUpState = PickUpState.DropArm;
                state.currentLiftServoState = LiftServoState.Start;
                state.hangerState = HangerState.Store;
            }
        }

        if (state.pickUpState == PickUpState.DropArm) {
            state.hangerState = HangerState.Store;
            state.currentLiftServoState = LiftServoState.Start;
            state.pickUpState = PickUpState.SecondRetraction;
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
