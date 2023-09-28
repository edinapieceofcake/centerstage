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
    }

    @Override
    public void start() {
        robot.RobotHardware.leftClawServo.setPosition(robot.RobotState.clawLeftOpenPosition);
        robot.RobotHardware.rightClawServo.setPosition(robot.RobotState.clawRightOpenPosition);
    }

    @Override
    public void update() {
        if (robot.Started) {
            switch (leftClawState) {
                case Opened:
                    robot.RobotHardware.leftClawServo.setPosition(robot.RobotState.clawLeftOpenPosition);
                    break;
                case Closed:
                    robot.RobotHardware.leftClawServo.setPosition(robot.RobotState.clawLeftClosedPosition);
                    break;
            }

            switch (rightClawState) {
                case Opened:
                    robot.RobotHardware.rightClawServo.setPosition(robot.RobotState.clawRightOpenPosition);
                    break;
                case Closed:
                    robot.RobotHardware.rightClawServo.setPosition(robot.RobotState.clawRightClosedPosition);
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
