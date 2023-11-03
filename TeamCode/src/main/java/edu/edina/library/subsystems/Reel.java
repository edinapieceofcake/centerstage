package edu.edina.library.subsystems;

import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class Reel extends Subsystem {
    Robot robot;
    public Reel(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {

    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        RobotState state = RobotState.getInstance();
        RobotHardware hardware = robot.RobotHardware;
        RobotConfiguration config = RobotConfiguration.getInstance();

        switch (state.currentLiftSlideState) {
            case Extending:
                hardware.reelMotor.setPower(0.1);
                break;
            case Retracting:
                hardware.reelMotor.setPower(-0.6);
                break;
            case Idle:
                hardware.reelMotor.setPower(0);
                break;
        }
    }
}
