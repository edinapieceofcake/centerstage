package edu.edina.library.subsystems;

import edu.edina.library.enums.ClawState;
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
        }
    }

    public void setProperties(boolean toggleLeftClaw, boolean toggleRightClaw) {
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
    }
}
