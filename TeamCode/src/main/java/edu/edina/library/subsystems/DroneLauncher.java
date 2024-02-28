package edu.edina.library.subsystems;

import edu.edina.library.enums.DroneLauncherState;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import  edu.edina.library.util.RobotState;

public class DroneLauncher implements Subsystem {
    private RobotState state = RobotState.getInstance();

    private Robot robot;

    public DroneLauncher(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {
        this.state.droneState = DroneLauncherState.Armed;
        this.robot.RobotHardware.droneLaunchServo.setPosition(RobotConfiguration.getInstance().droneLauncherArmedPosition);
    }

    @Override
    public void start() { }

    @Override
    public void update() {
        if (robot.Started) {
            switch (state.droneState) {
                case Armed:
                    this.robot.RobotHardware.droneLaunchServo.setPosition(RobotConfiguration.getInstance().droneLauncherArmedPosition);
                    break;
                case Launched:
                default:
                    this.robot.RobotHardware.droneLaunchServo.setPosition(RobotConfiguration.getInstance().droneLauncherLaunchedPosition);
                    break;
            }
        }
    }
}