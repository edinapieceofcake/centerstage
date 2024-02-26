package edu.edina.library.subsystems;

import edu.edina.library.enums.DroneLauncherState;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;

public class DroneLauncher implements Subsystem {
    private Robot robot;
    private DroneLauncherState state;

    public DroneLauncher(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {
        this.state = DroneLauncherState.Armed;
        this.robot.RobotHardware.droneLaunchServo.setPosition(RobotConfiguration.getInstance().droneLauncherArmedPosition);
    }

    @Override
    public void start() { }

    @Override
    public void update() {
        if (robot.Started) {
            switch (state) {
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
    public void setProperties(boolean launchDrone) {
        if (launchDrone) {
            if (state == DroneLauncherState.Launched) {
                state = DroneLauncherState.Armed;
            } else {
                state = DroneLauncherState.Launched;
            }
        }
    }

    @Override
    public void stop() {}
}