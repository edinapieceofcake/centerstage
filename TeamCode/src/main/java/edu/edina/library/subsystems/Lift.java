package edu.edina.library.subsystems;

import static edu.edina.library.enums.LiftDriveState.Hang;
import static edu.edina.library.enums.LiftDriveState.HighDropOff;
import static edu.edina.library.enums.LiftDriveState.LowDropOff;
import static edu.edina.library.enums.LiftDriveState.Manual;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.DropOffState;
import edu.edina.library.enums.HangState;
import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftServoState;
import edu.edina.library.enums.LiftSlideState;
import edu.edina.library.enums.PickUpState;
import edu.edina.library.enums.TwistServoState;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class Lift implements Subsystem, Action {
    private RobotHardware hardware;
    private boolean started = false;
    private boolean isTeleop = true;
    private boolean liftMotorReset = false;
    private Deadline lowLiftDelay = new Deadline(100, TimeUnit.MILLISECONDS);
    private Deadline highLiftDelay = new Deadline(300, TimeUnit.MILLISECONDS);
    private Deadline secondExtensionTimeout = new Deadline(1000, TimeUnit.MILLISECONDS);

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
        state.currentLeftLiftServoPosition = config.startingLeftLiftServoPosition;
        state.currentRightLiftServoPosition = config.startingRightLiftServoPosition;
        hardware.leftLiftServo.setPosition(state.currentLeftLiftServoPosition);
        hardware.rightLiftServo.setPosition(state.currentRightLiftServoPosition);
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
            state.currentLiftLength = -0.031914 * hardware.topLiftMotor.getCurrentPosition() + 12.117;
            state.currentLiftAngle = 51.111 * hardware.leftLiftServo.getPosition() + 20.956;
            state.currentLiftHeight = state.currentLiftLength * Math.sin(Math.toRadians(state.currentLiftAngle));
            state.currentTopMotorPosition = hardware.topLiftMotor.getCurrentPosition();
            state.currentBottomMotorPosition = hardware.bottomLiftMotor.getCurrentPosition();

            if (state.currentLiftDriveState == Manual) {
                hardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                hardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                if (state.currentLiftSlideState == LiftSlideState.Retracting) {
                    if (!hardware.liftSwitch.getState()) {
                        state.currentLiftSlidePower = .1;
                    } else {
                        if ((state.currentLiftServoState != LiftServoState.Start) && (state.currentTopMotorPosition > config.minimumExtensionBeforeRaisingLiftInTicks)) {
                            // if we are above the bottom of the hubs, don't let the lift back down into it
                            state.currentLiftSlidePower = 0;
                            state.currentLiftSlideState = LiftSlideState.Idle;
                        }
                    }
                }

                hardware.topLiftMotor.setPower(state.currentLiftSlidePower);
                hardware.bottomLiftMotor.setPower(state.currentLiftSlidePower);
            } else {
                if (state.currentLiftDriveState == LiftDriveState.Hang) {
                    if (state.hangState == HangState.Start) {
                        hardware.topLiftMotor.setTargetPosition(config.minimumExtensionBeforeRaisingLiftInTicks);
                        hardware.bottomLiftMotor.setTargetPosition(config.minimumExtensionBeforeRaisingLiftInTicks);
                        hardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        hardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        hardware.topLiftMotor.setPower(config.liftExtendingPower);
                        hardware.bottomLiftMotor.setPower(config.liftExtendingPower);
                        state.hangState = HangState.FirstExtension;
                    }

                    if (state.hangState == HangState.FirstExtension) {
                        if (hardware.topLiftMotor.getCurrentPosition() < (config.minimumExtensionBeforeRaisingLiftInTicks + 10)) {
                            state.hangState = HangState.LiftArm;
                            state.currentLeftLiftServoPosition = config.leftHighDropOffServoPosition;
                            state.currentRightLiftServoPosition = config.rightHighDropOffServoPosition;
                            state.currentLiftServoState = LiftServoState.High;
                            highLiftDelay.reset();
                        }
                    }

                    if (state.hangState == HangState.LiftArm) {
                        if (highLiftDelay.hasExpired()) {
                            hardware.robotHangerMotor.setTargetPosition(config.hangMotorHangPosition);
                            hardware.robotHangerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                            hardware.robotHangerMotor.setPower(config.hangerExtendingPower);
                            state.hangState = HangState.RaiseHanger;
                        }
                    }

                    if (state.hangState == HangState.RaiseHanger) {
                        if (hardware.robotHangerMotor.getCurrentPosition() < (config.hangMotorHangPosition + 10)) {
                            state.hangState = HangState.Finished;
                            state.lastKnownLiftState = Hang;
                        }
                    }
                } else if ((state.currentLiftDriveState == LiftDriveState.LowDropOff) ||
                        (state.currentLiftDriveState == LiftDriveState.HighDropOff)) {
                    if (state.dropOffState == DropOffState.Start) {
                        hardware.topLiftMotor.setTargetPosition(config.minimumExtensionBeforeRaisingLiftInTicks);
                        hardware.bottomLiftMotor.setTargetPosition(config.minimumExtensionBeforeRaisingLiftInTicks);
                        hardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        hardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        hardware.topLiftMotor.setPower(config.liftExtendingPower);
                        hardware.bottomLiftMotor.setPower(config.liftExtendingPower);
                        if (state.currentLiftDriveState == LiftDriveState.LowDropOff) {
                            hardware.robotHangerMotor.setTargetPosition(config.hangMotorLowDropOffPosition);
                        } else {
                            hardware.robotHangerMotor.setTargetPosition(config.hangMotorHighDropOffPosition);
                        }

                        hardware.robotHangerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        hardware.robotHangerMotor.setPower(config.hangerExtendingPower);
                        state.dropOffState = DropOffState.FirstExtension;
                    }

                    if (state.dropOffState == DropOffState.FirstExtension) {
                        if (hardware.topLiftMotor.getCurrentPosition() < (config.minimumExtensionBeforeRaisingLiftInTicks + 10)) {
                            state.dropOffState = DropOffState.LiftArm;
                            if (state.currentLiftDriveState == LiftDriveState.LowDropOff) {
                                state.currentLeftLiftServoPosition = config.leftLowDropOffServoPosition;
                                state.currentRightLiftServoPosition = config.rightLowDropOffServoPosition;
                                state.currentLiftServoState = LiftServoState.Medium;
                                lowLiftDelay.reset();
                            } else {
                                state.currentLeftLiftServoPosition = config.leftHighDropOffServoPosition;
                                state.currentRightLiftServoPosition = config.rightHighDropOffServoPosition;
                                state.currentLiftServoState = LiftServoState.High;
                                highLiftDelay.reset();
                            }
                        }
                    }

                    if (state.dropOffState == DropOffState.LiftArm) {
                        if (state.currentLiftDriveState == LiftDriveState.LowDropOff) {
                            if (lowLiftDelay.hasExpired()) {
                                hardware.topLiftMotor.setTargetPosition(config.liftLowDropOffPosition);
                                hardware.bottomLiftMotor.setTargetPosition(config.liftLowDropOffPosition);
                                hardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                                hardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                                state.dropOffState = DropOffState.SecondExtension;
                                secondExtensionTimeout.reset();;
                            }
                        } else {
                            if (highLiftDelay.hasExpired()) {
                                hardware.topLiftMotor.setTargetPosition(config.liftHighDropOffPosition);
                                hardware.bottomLiftMotor.setTargetPosition(config.liftHighDropOffPosition);
                                hardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                                hardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                                state.dropOffState = DropOffState.SecondExtension;
                                secondExtensionTimeout.reset();;
                            }
                        }
                    }

                    if (state.dropOffState == DropOffState.SecondExtension) {
                        // twist the claw as soon as we get to 600 just in case the batter doesn't have enough to get to the high position
                        if (hardware.topLiftMotor.getCurrentPosition() < (config.liftTwistPosition)) {
                            state.twistServoState = TwistServoState.DropOff;
                            state.angleClawState = AngleClawState.DropOff;
                        }

                        if (state.currentLiftDriveState == LiftDriveState.LowDropOff) {
                            if (secondExtensionTimeout.hasExpired()) {
                                state.dropOffState = DropOffState.Finished;
                                state.currentLiftSlideState = LiftSlideState.Idle;
                                state.lastKnownLiftState = LowDropOff;
                            } else {
                                if (state.lastKnownLiftState == HighDropOff) {
                                    if (hardware.topLiftMotor.getCurrentPosition() > (config.liftLowDropOffPosition - 10)) {
                                        state.dropOffState = DropOffState.Finished;
                                        state.currentLiftSlideState = LiftSlideState.Idle;
                                        state.lastKnownLiftState = LowDropOff;
                                    }
                                } else {
                                    if (hardware.topLiftMotor.getCurrentPosition() < (config.liftLowDropOffPosition + 10)) {
                                        state.dropOffState = DropOffState.Finished;
                                        state.currentLiftSlideState = LiftSlideState.Idle;
                                        state.lastKnownLiftState = LowDropOff;
                                    }
                                }
                            }
                        } else {
                            if (secondExtensionTimeout.hasExpired()) {
                                state.dropOffState = DropOffState.Finished;
                                state.currentLiftSlideState = LiftSlideState.Idle;
                                state.lastKnownLiftState = HighDropOff;
                            } else if (hardware.topLiftMotor.getCurrentPosition() < (config.liftHighDropOffPosition + 10)) {
                                state.dropOffState = DropOffState.Finished;
                                state.currentLiftSlideState = LiftSlideState.Idle;
                                state.lastKnownLiftState = HighDropOff;
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
                        hardware.topLiftMotor.setPower(config.liftRetractingPower);
                        hardware.bottomLiftMotor.setPower(config.liftRetractingPower);
                        hardware.robotHangerMotor.setTargetPosition(config.hangMotorStorePosition);
                        hardware.robotHangerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        hardware.robotHangerMotor.setPower(config.hangerExtendingPower);
                        state.pickUpState = PickUpState.FirstRetraction;
                        state.twistServoState = TwistServoState.Pickup;
                        state.angleClawState = AngleClawState.Drive;
                    }

                    if (state.pickUpState == PickUpState.FirstRetraction) {
                        if (hardware.topLiftMotor.getCurrentPosition() > (config.minimumExtensionBeforeRaisingLiftInTicks - 10)) {
                            state.pickUpState = PickUpState.DropArm;
                            state.currentLeftLiftServoPosition = config.startingLeftLiftServoPosition;
                            state.currentRightLiftServoPosition = config.startingRightLiftServoPosition;
                            highLiftDelay.reset();
                            lowLiftDelay.reset();
                        }
                    }

                    if (state.pickUpState == PickUpState.DropArm) {
                        if (state.lastKnownLiftState == HighDropOff) {
                            if (highLiftDelay.hasExpired()) {
                                state.currentLiftServoState = LiftServoState.Start;
                                state.pickUpState = PickUpState.SecondRetraction;
                            }
                        } else {
                            if (lowLiftDelay.hasExpired()) {
                                state.currentLiftServoState = LiftServoState.Start;
                                state.pickUpState = PickUpState.SecondRetraction;
                            }
                        }
                    }

                    if (state.pickUpState == PickUpState.SecondRetraction) {
                        hardware.topLiftMotor.setPower(config.slowLiftRetractingPower);
                        hardware.bottomLiftMotor.setPower(config.slowLiftRetractingPower);
                        hardware.topLiftMotor.setTargetPosition(config.liftDrivePosition);
                        hardware.bottomLiftMotor.setTargetPosition(config.liftDrivePosition);
                        state.pickUpState = PickUpState.WaitForSecondRetraction;
                    }

                    if (state.pickUpState == PickUpState.WaitForSecondRetraction) {
                        if (!hardware.liftSwitch.getState()) {
                            if (state.currentLiftDriveState == LiftDriveState.Drive) {
                                state.angleClawState = AngleClawState.Drive;
                                state.lastKnownLiftState = LiftDriveState.Drive;
                            } else {
                                state.angleClawState = AngleClawState.Pickup;
                                state.lastKnownLiftState = LiftDriveState.Pickup;
                            }

                            hardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                            hardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                            hardware.topLiftMotor.setPower(0);
                            hardware.bottomLiftMotor.setPower(0);

                            state.pickUpState = PickUpState.Finished;
                            state.currentLiftSlideState = LiftSlideState.Idle;
                            state.currentLiftDriveState = Manual;
                            state.currentLiftSlidePower = 0;
                        }
                    }
                }
            }

            if (isTeleop) {
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
            }

            hardware.leftLiftServo.setPosition(state.currentLeftLiftServoPosition);
            hardware.rightLiftServo.setPosition(state.currentRightLiftServoPosition);
        }
    }

    public void setProperties(double rightTrigger, double leftTrigger, boolean a, boolean x, boolean y, boolean b, boolean gm2y) {
        RobotState state = RobotState.getInstance();
        RobotConfiguration config = RobotConfiguration.getInstance();

        if (leftTrigger != 0) {
            state.currentLiftDriveState = Manual;
            state.currentLiftSlideState = LiftSlideState.Retracting;
            state.currentLiftSlidePower = leftTrigger * .5;
        } else if (rightTrigger != 0) {
            state.currentLiftDriveState = Manual;
            state.currentLiftSlideState = LiftSlideState.Extending;
            state.currentLiftSlidePower = -rightTrigger;
        } else {
            state.currentLiftSlideState = LiftSlideState.Idle;
            state.currentLiftSlidePower = 0;
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
            state.currentLiftDriveState = LiftDriveState.LowDropOff;
            if (state.lastKnownLiftState == LiftDriveState.LowDropOff  || state.lastKnownLiftState == HighDropOff) {
                state.dropOffState = DropOffState.FirstExtension;
            } else {
                state.dropOffState = DropOffState.Start;
            }

            state.currentLiftSlideState = LiftSlideState.Extending;
        }

        if (b) {
            // high
            state.currentLiftDriveState = LiftDriveState.HighDropOff;
            if (state.lastKnownLiftState == LiftDriveState.LowDropOff  || state.lastKnownLiftState == LiftDriveState.HighDropOff) {
                state.dropOffState = DropOffState.FirstExtension;
            } else {
                state.dropOffState = DropOffState.Start;
            }

            state.currentLiftSlideState = LiftSlideState.Extending;
        }
    }

    private double round(double originalValue) {
        return ((int)(originalValue * 100)) / 100.0;
    }

    @Override
    public void preview(@NonNull Canvas fieldOverlay) {
        Action.super.preview(fieldOverlay);
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        RobotState state = RobotState.getInstance();

        update();
        if ((state.dropOffState == DropOffState.Finished) && (state.currentLiftDriveState == LowDropOff)) {
            return false;
        }

        return true;
    }

    public Action moveLiftToLowPosition() {
        RobotState state = RobotState.getInstance();

        state.currentLiftDriveState = LiftDriveState.LowDropOff;
        state.dropOffState = DropOffState.Start;
        state.currentLiftSlideState = LiftSlideState.Extending;

        return this;
    }
}
