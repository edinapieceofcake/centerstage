package edu.edina.library.subsystems;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.TwistServoState;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class Claw implements Subsystem {
    private boolean started = false;
    private RobotHardware hardware;

    public Claw(RobotHardware hardware) {
        this.hardware = hardware;
    }

    public Claw(Robot robot) {
        this.hardware = robot.RobotHardware;
    }

    @Override
    public void init() {
        RobotState state = RobotState.getInstance();

        state.rightClawState = ClawState.Opened;
        state.leftClawState = ClawState.Opened;
        state.autoClawState = ClawState.Opened;
        state.angleClawState = AngleClawState.Drive;
        state.twistServoState = TwistServoState.Pickup;
        hardware.leftClawServo.setPosition(RobotConfiguration.getInstance().clawLeftOpenPosition);
        hardware.rightClawServo.setPosition(RobotConfiguration.getInstance().clawRightOpenPosition);
        hardware.autoClawServo.setPosition(RobotConfiguration.getInstance().autoClawServoOpenPosition);
        hardware.twistClawServo.setPosition(RobotConfiguration.getInstance().twistClawServoPickUpPosition);
        hardware.angleClawServo.setPosition(RobotConfiguration.getInstance().angleClawDrivePosition);
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
//            if ((state.currentLiftDriveState == LiftDriveState.Manual) && (state.currentLiftServoState == LiftServoState.Start)) {
//                if (state.currentLiftHeight > config.minimumHeightToTwistServoInInches) {
//                    state.twistServoState = TwistServoState.DropOff;
//                } else {
//                    state.twistServoState = TwistServoState.Pickup;
//                }
//            }

            switch (state.currentLiftDriveState) {
                case Drive:
                    hardware.angleClawServo.setPosition(config.angleClawDrivePosition);
                    break;
                case Pickup:
                    hardware.angleClawServo.setPosition(config.angleClawPickupPosition);
                    break;
            }

            switch (state.leftClawState) {
                case Opened:
                    hardware.leftClawServo.setPosition(config.clawLeftOpenPosition);
                    hardware.leftClawRed.setState(false);
                    hardware.leftClawGreen.setState(true);
                    break;
                case Closed:
                    hardware.leftClawServo.setPosition(config.clawLeftClosedPosition);
                    hardware.leftClawRed.setState(true);
                    hardware.leftClawGreen.setState(false);
                    break;
            }

            switch (state.rightClawState) {
                case Opened:
                    hardware.rightClawServo.setPosition(config.clawRightOpenPosition);
                    hardware.rightClawRed.setState(false);
                    hardware.rightClawGreen.setState(true);
                    break;
                case Closed:
                    hardware.rightClawServo.setPosition(config.clawRightClosedPosition);
                    hardware.rightClawRed.setState(true);
                    hardware.rightClawGreen.setState(false);
                    break;
            }

            switch(state.autoClawState){
                case Opened:
                    hardware.autoClawServo.setPosition(config.autoClawServoOpenPosition);
                    break;
                case Closed:
                    hardware.autoClawServo.setPosition(config.autoClawServoClosePosition);
                    break;
            }

            switch (state.twistServoState) {
                case Pickup:
                    hardware.twistClawServo.setPosition(config.twistClawServoPickUpPosition);
                    break;
                case CenterDropOff:
                    hardware.twistClawServo.setPosition(config.twistClawServoDropOffPosition);
                    break;
                case LeftDropOff:
                    hardware.twistClawServo.setPosition(config.leftTwistClawDropOffPosition);
                    break;
                case RightDropOff:
                    hardware.twistClawServo.setPosition(config.rightTwistClawDropOffPosition);
                    break;
            }

            switch (state.angleClawState) {
                case Drive:
                    hardware.angleClawServo.setPosition(config.angleClawDrivePosition);
                    break;
                case Pickup:
                    hardware.angleClawServo.setPosition(config.angleClawPickupPosition);
                    break;
                case CenterDropOff:
                    switch (state.currentLiftServoState) {
                        case High:
                            hardware.angleClawServo.setPosition(config.angleClawHighDropOffPosition);
                            break;
                        default:
                            hardware.angleClawServo.setPosition(config.angleClawLowDropOffPosition);
                            break;
                    }
                    break;
                case LeftDropOff:
                    switch (state.currentLiftServoState) {
                        case High:
                            hardware.angleClawServo.setPosition(config.leftHighAngleClawServoDropOffPosition);
                            break;
                        default:
                            hardware.angleClawServo.setPosition(config.leftLowAngleClawServoDropOffPosition);
                            break;
                    }
                    break;
                case RightDropOff:
                    switch (state.currentLiftServoState) {
                        case High:
                            hardware.angleClawServo.setPosition(config.rightHighAngleClawServoDropOffPosition);
                            break;
                        default:
                            hardware.angleClawServo.setPosition(config.rightLowAngleClawServoDropOffPosition);
                            break;
                    }
                    break;

            }
        }
    }

    public void setProperties(boolean toggleLeftClaw, boolean toggleRightClaw, boolean toggleAutoClaw, boolean leftDpad, boolean rightDpad, boolean leftAngleDrop, boolean rightAngleDrop) {
        RobotState state = RobotState.getInstance();

        if (toggleLeftClaw) {
            if (state.leftClawState == ClawState.Opened) {
                state.leftClawState = ClawState.Closed;
            } else {
                state.leftClawState = ClawState.Opened;
            }
        }

        if (toggleRightClaw) {
            if (state.rightClawState == ClawState.Opened) {
                state.rightClawState = ClawState.Closed;
            } else {
                state.rightClawState = ClawState.Opened;
            }
        }

        if (toggleRightClaw) {
            if (state.autoClawState == ClawState.Opened) {
                state.autoClawState = ClawState.Closed;
            } else {
                state.autoClawState = ClawState.Opened;
            }
        }

        if (leftDpad) {
            RobotState.getInstance().twistServoState = TwistServoState.CenterDropOff;
        } else if (rightDpad) {
            RobotState.getInstance().twistServoState = TwistServoState.Pickup;
        }

        if (leftAngleDrop) {
            if (state.twistServoState == TwistServoState.CenterDropOff) {
                state.twistServoState = TwistServoState.LeftDropOff;
            } else if (state.twistServoState == TwistServoState.RightDropOff) {
                state.twistServoState = TwistServoState.CenterDropOff;
            }
        }

        if (rightAngleDrop) {
            if (state.twistServoState == TwistServoState.CenterDropOff) {
                state.twistServoState = TwistServoState.RightDropOff;
            } else if (state.twistServoState == TwistServoState.LeftDropOff) {
                state.twistServoState = TwistServoState.CenterDropOff;
            }
        }
    }
}
