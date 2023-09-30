package edu.edina.library.subsystems;

import edu.edina.library.enums.HangerState;
import edu.edina.library.util.Robot;

public class RobotHanger extends Subsystem {
    private Robot robot;
    private HangerState hangerState;

    public RobotHanger(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {
        hangerState = HangerState.Idle;
    }

    @Override
    public void start() {}

    @Override
    public void update() {
        if (robot.Started) {
            switch (hangerState) {
                case Retracting:
                    robot.RobotHardware.robotHangerMotor.setPower(robot.RobotState.hangerRetractingPower);
                    break;
                case Extending:
                    robot.RobotHardware.robotHangerMotor.setPower(robot.RobotState.hangerExtendingPower);
                    break;
                case Idle:
                    robot.RobotHardware.robotHangerMotor.setPower(0);
                    break;
            }
        }
    }
    public void setProperties(boolean toggleExtend, boolean toggleRetract) {
        if (toggleExtend) {
            hangerState = HangerState.Extending;
        } else if (toggleRetract) {
            hangerState = HangerState.Retracting;
        } else {
            hangerState = HangerState.Idle;
        }
    }
}
