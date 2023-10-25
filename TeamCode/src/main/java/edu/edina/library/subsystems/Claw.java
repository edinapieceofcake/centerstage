package edu.edina.library.subsystems;

import edu.edina.library.enums.ClawState;
import edu.edina.library.util.Robot;

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
        robot.RobotHardware.leftClawServo.setPosition(robot.RobotConfiguration.clawLeftOpenPosition);
        robot.RobotHardware.rightClawServo.setPosition(robot.RobotConfiguration.clawRightOpenPosition);
        robot.RobotHardware.twistClawServo.setPosition(robot.RobotConfiguration.twistClawServoPickUpPosition);
        robot.RobotHardware.angleClawServo.setPosition(robot.RobotConfiguration.angleClawPickupPosition);
    }

    @Override
    public void start() {
    }

    @Override
    public void update() {
        if (robot.Started) {
            switch (leftClawState) {
                case Opened:
                    robot.RobotHardware.leftClawServo.setPosition(robot.RobotConfiguration.clawLeftOpenPosition);
                    break;
                case Closed:
                    robot.RobotHardware.leftClawServo.setPosition(robot.RobotConfiguration.clawLeftClosedPosition);
                    break;
            }

            switch (rightClawState) {
                case Opened:
                    robot.RobotHardware.rightClawServo.setPosition(robot.RobotConfiguration.clawRightOpenPosition);
                    break;
                case Closed:
                    robot.RobotHardware.rightClawServo.setPosition(robot.RobotConfiguration.clawRightClosedPosition);
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
