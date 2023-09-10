package edu.edina.library.subsystems;

import edu.edina.library.util.ClosingState;
import edu.edina.library.util.LiftState;
import edu.edina.library.util.OpeningState;
import edu.edina.opmodes.teleop.Robot;

public class Lift  extends Subsystem{
    private Robot robot;

    public Lift(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {}
    @Override
    public void update() {
        if (robot.Started) {
            if (robot.RobotState.LiftState == LiftState.Open) {
                if (robot.RobotState.OpeningState == OpeningState.Idle) {
                    robot.RobotState.OpeningState = OpeningState.ServoOneOpening;
                    robot.RobotState.Servo1Position = robot.RobotState.ServoOneOpenPosition;
                } else if (robot.RobotState.OpeningState == OpeningState.ServoOneOpening) {
                    if (round(robot.RobotHardware.servo1.getPosition()) == robot.RobotState.ServoOneOpenPosition) {
                        robot.RobotState.OpeningState = OpeningState.RunningLift;
                        robot.RobotHardware.motor.setTargetPosition(robot.RobotState.LiftOpenTargetPosition);
                        robot.RobotHardware.motor.setPower(.5);
                    }
                } else if (robot.RobotState.OpeningState == OpeningState.RunningLift) {
                    if (!robot.RobotHardware.motor.isBusy()) {
                        robot.RobotState.Servo2Position = robot.RobotState.ServoTwoOpenPosition;
                        robot.RobotState.OpeningState = OpeningState.ServoTwoOpening;
                    }
                } else if (robot.RobotState.OpeningState == OpeningState.ServoTwoOpening) {
                    if (round(robot.RobotHardware.servo2.getPosition()) == robot.RobotState.ServoTwoOpenPosition) {
                        robot.RobotState.OpeningState = OpeningState.Idle;
                        robot.RobotState.LiftState = LiftState.Idle;
                    }
                }
            }
            else if (robot.RobotState.LiftState == LiftState.Close) {
                if (robot.RobotState.ClosingState == ClosingState.Idle) {
                    robot.RobotState.ClosingState = ClosingState.ServoTwoClosing;
                    robot.RobotState.Servo2Position = robot.RobotState.ServoTwoClosedPosition;
                } else if (robot.RobotState.ClosingState == ClosingState.ServoTwoClosing) {
                    if (round(robot.RobotHardware.servo2.getPosition()) == robot.RobotState.ServoTwoClosedPosition) {
                        robot.RobotState.ClosingState = ClosingState.RunningLift;
                        robot.RobotHardware.motor.setTargetPosition(robot.RobotState.LiftClosingTargetPosition);
                        robot.RobotHardware.motor.setPower(.5);
                    }
                } else if (robot.RobotState.ClosingState == ClosingState.RunningLift) {
                    if (!robot.RobotHardware.motor.isBusy()) {
                        robot.RobotState.Servo1Position = robot.RobotState.ServoOneClosedPosition;
                        robot.RobotState.ClosingState = ClosingState.ServoOneClosing;
                    }
                } else if (robot.RobotState.ClosingState == ClosingState.ServoOneClosing) {
                    if (round(robot.RobotHardware.servo1.getPosition()) == robot.RobotState.ServoOneClosedPosition) {
                        robot.RobotState.ClosingState = ClosingState.Idle;
                        robot.RobotState.LiftState = LiftState.Idle;
                    }
                }
            } else if (robot.RobotState.LiftState == LiftState.Idle) {
                robot.RobotHardware.motor.setPower(0);
            }

            robot.RobotHardware.servo1.setPosition(robot.RobotState.Servo1Position);
            robot.RobotHardware.servo2.setPosition(robot.RobotState.Servo2Position);
		}
    }

    public void setLiftProperties(boolean servoOneUp, boolean servoOneDown, boolean openLift, boolean closeLift) {
        if (servoOneUp || servoOneDown) {
            robot.RobotState.LiftState = LiftState.Idle;
            robot.RobotState.OpeningState = OpeningState.Idle;
            robot.RobotState.ClosingState = ClosingState.Idle;

            if (servoOneUp) {
                robot.RobotState.Servo1Position += 0.1;
            }

            if (servoOneDown) {
                robot.RobotState.Servo1Position -= 0.1;
            }
        }

        if (openLift) {
            robot.RobotState.LiftState = LiftState.Open;
        }

        if (closeLift) {
            robot.RobotState.LiftState = LiftState.Close;
        }
    }

    private double round(double originalValue) {
        return ((int)(originalValue * 100)) / 100.0;
    }
}
