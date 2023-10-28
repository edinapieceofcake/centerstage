package edu.edina.library.subsystems;

import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.TwistServoState;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;

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
        if (robot.Started) {
            switch (robot.RobotState.currentLiftDriveState) {
                case Drive:
                    robot.RobotHardware.angleClawServo.setPosition(RobotConfiguration.getInstance().angleClawDrivePosition);
                    break;
            }
            switch (leftClawState) {
                case Opened:
                    robot.RobotHardware.leftClawServo.setPosition(RobotConfiguration.getInstance().clawLeftOpenPosition);
                    break;
                case Closed:
                    robot.RobotHardware.leftClawServo.setPosition(RobotConfiguration.getInstance().clawLeftClosedPosition);
                    break;
            }

            switch (rightClawState) {
                case Opened:
                    robot.RobotHardware.rightClawServo.setPosition(RobotConfiguration.getInstance().clawRightOpenPosition);
                    break;
                case Closed:
                    robot.RobotHardware.rightClawServo.setPosition(RobotConfiguration.getInstance().clawRightClosedPosition);
                    break;
            }

            switch (robot.RobotState.twistServoState) {
                case Pickup:
                    robot.RobotHardware.twistClawServo.setPosition(RobotConfiguration.getInstance().twistClawServoPickUpPosition);
                    robot.RobotHardware.angleClawServo.setPosition(RobotConfiguration.getInstance().angleClawPickupPosition);
                    break;
                case DropOff:
                    robot.RobotHardware.twistClawServo.setPosition(RobotConfiguration.getInstance().twistClawServoDropOffPosition);
                    robot.RobotHardware.angleClawServo.setPosition(RobotConfiguration.getInstance().angleClawDropOffPosition);
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
            robot.RobotState.twistServoState = TwistServoState.DropOff;
        } else if (rightDpad) {
            robot.RobotState.twistServoState = TwistServoState.Pickup;
        }
    }
}
