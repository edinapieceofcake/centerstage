package edu.edina.library.subsystems;

import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.TwistServoState;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class Claw extends Subsystem {
    private Robot robot;
    private ClawState leftClawState;
    private ClawState rightClawState;

    public Claw(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {
        rightClawState = ClawState.Opened;
        leftClawState = ClawState.Opened;
        robot.RobotHardware.leftClawServo.setPosition(RobotConfiguration.getInstance().clawLeftOpenPosition);
        robot.RobotHardware.rightClawServo.setPosition(RobotConfiguration.getInstance().clawRightOpenPosition);
        robot.RobotHardware.twistClawServo.setPosition(RobotConfiguration.getInstance().twistClawServoPickUpPosition);
        robot.RobotHardware.angleClawServo.setPosition(RobotConfiguration.getInstance().angleClawPickupPosition);
    }

    @Override
    public void start() {
    }

    @Override
    public void update() {
        RobotState state = RobotState.getInstance();
        RobotHardware hardware = robot.RobotHardware;
        RobotConfiguration config = RobotConfiguration.getInstance();

        if (robot.Started) {
//            if ((state.currentLiftHeight > config.minimumHeightToTwistServoInInches) &&
//                (state.currentTopMotorPosition < config.minimumExtensionBeforeTwistingInTicks)){
//                // make sure we are at a minimum distance and height. height won't work by itself as we could be
//                // tucked in and hit the hubs
//               state.twistServoState = TwistServoState.DropOff;
//            } else {
//               state.twistServoState = TwistServoState.Pickup;
//            }

            switch (state.currentLiftDriveState) {
                case Drive:
                    hardware.angleClawServo.setPosition(config.angleClawDrivePosition);
                    break;
                case Pickup:
                    hardware.angleClawServo.setPosition(config.angleClawPickupPosition);
                    break;
            }

            switch (leftClawState) {
                case Opened:
                    hardware.leftClawServo.setPosition(config.clawLeftOpenPosition);
                    break;
                case Closed:
                    hardware.leftClawServo.setPosition(config.clawLeftClosedPosition);
                    break;
            }

            switch (rightClawState) {
                case Opened:
                    hardware.rightClawServo.setPosition(config.clawRightOpenPosition);
                    break;
                case Closed:
                    hardware.rightClawServo.setPosition(config.clawRightClosedPosition);
                    break;
            }

            switch (state.twistServoState) {
                case Pickup:
                    hardware.twistClawServo.setPosition(config.twistClawServoPickUpPosition);
                    break;
                case DropOff:
                    hardware.twistClawServo.setPosition(config.twistClawServoDropOffPosition);
                    break;
            }

            switch (state.angleClawState) {
                case Drive:
                    hardware.angleClawServo.setPosition(config.angleClawDrivePosition);
                    break;
                case Pickup:
                    hardware.angleClawServo.setPosition(config.angleClawPickupPosition);
                    break;
                case DropOff:
                    hardware.angleClawServo.setPosition(config.angleClawDropOffPosition);
                    break;
            }
        }
    }

    public void setProperties(boolean toggleLeftClaw, boolean toggleRightClaw, boolean leftDpad, boolean rightDpad) {
        if (toggleLeftClaw) {
            if (leftClawState == ClawState.Opened) {
                leftClawState = ClawState.Closed;
            } else {
                leftClawState = ClawState.Opened;
            }
        }

        if (toggleRightClaw) {
            if (rightClawState == ClawState.Opened) {
                rightClawState = ClawState.Closed;
            } else {
                rightClawState = ClawState.Opened;
            }
        }

        if (leftDpad) {
           RobotState.getInstance().twistServoState = TwistServoState.DropOff;
        } else if (rightDpad) {
           RobotState.getInstance().twistServoState = TwistServoState.Pickup;
        }
    }
}
