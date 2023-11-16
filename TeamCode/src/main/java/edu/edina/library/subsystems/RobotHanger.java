package edu.edina.library.subsystems;

import edu.edina.library.enums.HangerState;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;

public class RobotHanger implements Subsystem {
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
                    robot.RobotHardware.robotHangerMotor.setPower(RobotConfiguration.getInstance().hangerRetractingPower);
                    break;
                case Extending:
                    robot.RobotHardware.robotHangerMotor.setPower(RobotConfiguration.getInstance().hangerExtendingPower);
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
