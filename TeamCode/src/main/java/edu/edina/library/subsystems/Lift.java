package edu.edina.library.subsystems;

import edu.edina.library.enums.HangerState;
import edu.edina.library.enums.LiftState;
import edu.edina.library.util.Robot;

public class Lift extends Subsystem{
    private Robot robot;
    private LiftState liftState;
    private final double maxLiftTicks = 2000;

    public Lift(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {liftState = LiftState.Idle;}

    @Override
    public void start() {}

    @Override
    public void update() {
        if (robot.Started) {
            switch (liftState) {
                case Retracting:
                    robot.RobotHardware.topLiftMotor.setPower(robot.RobotState.liftRetractingPower);
                    robot.RobotHardware.bottomLiftMotor.setPower(robot.RobotState.liftRetractingPower);
                    break;
                case Extending:
                    robot.RobotHardware.topLiftMotor.setPower(robot.RobotState.liftExtendingPower);
                    robot.RobotHardware.bottomLiftMotor.setPower(robot.RobotState.liftExtendingPower);
                    break;
                case Idle:
                    robot.RobotHardware.topLiftMotor.setPower(0);
                    robot.RobotHardware.bottomLiftMotor.setPower(0);
                    break;
            }

            robot.RobotState.currentLiftPosition = robot.RobotHardware.topLiftMotor.getCurrentPosition();
            robot.RobotState.currentLiftServoPosition = (1/maxLiftTicks) * robot.RobotState.currentLiftPosition - 0.26;
            robot.RobotHardware.leftLiftServo.setPosition(robot.RobotState.currentLiftServoPosition);
            robot.RobotHardware.rightLiftServo.setPosition(robot.RobotState.currentLiftServoPosition);

        }
    }

    public void setProperties(boolean openLift, boolean closeLift) {
        if (openLift) {
            liftState = LiftState.Extending;
        } else if (closeLift) {
            liftState = LiftState.Retracting;
        } else {
            liftState = LiftState.Idle;
        }
    }

    private double round(double originalValue) {
        return ((int)(originalValue * 100)) / 100.0;
    }
}
