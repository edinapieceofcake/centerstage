package edu.edina.library.subsystems;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.DropOffOrientation;
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
    public void stop() {
        started = false;
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
                    if (state.twistServoState == TwistServoState.Pickup) {
                        hardware.leftClawServo.setPosition(config.clawLeftOpenPosition);
                    } else {
                        hardware.leftClawServo.setPosition(config.clawLeftOpenDropPosition);
                    }

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
                    if (state.twistServoState == TwistServoState.Pickup) {
                        hardware.rightClawServo.setPosition(config.clawRightOpenPosition);
                    } else {
                        hardware.rightClawServo.setPosition(config.clawRightOpenDropPosition);
                    }
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
                    if (state.twistServoState == TwistServoState.Pickup) {
                        hardware.autoClawServo.setPosition(config.autoClawServoOpenDropPosition);
                    } else {
                        hardware.autoClawServo.setPosition(config.autoClawServoOpenDropPosition);
                    }
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
                case LeftAutoDropOff:
                    switch (state.currentLiftServoState) {
                        case One:
                            hardware.twistClawServo.setPosition(config.leftAutoHighTwistClawServoDropOffPosition);
                            break;
                        default:
                            hardware.twistClawServo.setPosition(config.leftAutoMediumTwistClawServoDropOffPosition);
                            break;
                    }
                    break;
                case LeftDropOff:
                    switch (state.currentLiftServoState) {
                        case One:
                            hardware.twistClawServo.setPosition(config.leftTeleopHighTwistClawServoDropOffPosition);
                            break;
                        default:
                            hardware.twistClawServo.setPosition(config.leftTeleopMediumTwistClawServoDropOffPosition);
                            break;
                    }
                    break;
                case RightDropOff:
                    switch (state.currentLiftServoState) {
                        case One:
                            hardware.twistClawServo.setPosition(config.rightHighTwistClawServoDropOffPosition);
                            break;
                        default:
                            hardware.twistClawServo.setPosition(config.rightMediumTwistClawServoDropOffPosition);
                            break;
                    }
                    break;
            }

            switch (state.angleClawState) {
                case Drive:
                    hardware.angleClawServo.setPosition(config.angleClawDrivePosition);
                    break;
                case Stack:
                    hardware.angleClawServo.setPosition(config.angleClawStackPickupPosition);
                    break;
                case Pickup:
                    hardware.angleClawServo.setPosition(config.angleClawPickupPosition);
                    break;
                case CenterDropOff:
                    switch (state.currentLiftServoState) {
                        case One:
                            hardware.angleClawServo.setPosition(config.angleClawHighDropOffPosition);
                            break;
                        default:
                            hardware.angleClawServo.setPosition(config.angleClawMediumDropOffPosition);
                            break;
                    }
                    break;
                case LeftAutoDropOff:
                    switch (state.currentLiftServoState) {
                        case One:
                            hardware.angleClawServo.setPosition(config.leftAutoHighAngleClawServoDropOffPosition);
                            break;
                        default:
                            hardware.angleClawServo.setPosition(config.leftAutoMediumAngleClawServoDropOffPosition);
                            break;
                    }
                    break;
                case LeftDropOff:
                    switch (state.currentLiftServoState) {
                        case One:
                            hardware.angleClawServo.setPosition(config.leftTeleopHighAngleClawServoDropOffPosition);
                            break;
                        default:
                            hardware.angleClawServo.setPosition(config.leftTeleopMediumAngleClawServoDropOffPosition);
                        break;
                    }
                    break;
                case RightDropOff:
                    switch (state.currentLiftServoState) {
                        case One:
                            hardware.angleClawServo.setPosition(config.rightHighAngleClawServoDropOffPosition);
                            break;
                        default:
                            hardware.angleClawServo.setPosition(config.rightMediumAngleClawServoDropOffPosition);
                            break;
                    }
                    break;
            }
        }
    }
}
