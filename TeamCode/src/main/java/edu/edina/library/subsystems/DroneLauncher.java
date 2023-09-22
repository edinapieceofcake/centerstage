package edu.edina.library.subsystems;

import edu.edina.opmodes.teleop.Robot;

public class DroneLauncher extends Subsystem {
    private Robot robot;
    private boolean launchDrone;

    public DroneLauncher(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {
        this.robot.RobotHardware.droneLaunchServo.setPosition(this.robot.RobotState.droneLauncherArmedPosition);
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        if (robot.Started) {
            if (launchDrone) {
                this.robot.RobotHardware.droneLaunchServo.setPosition(this.robot.RobotState.droneLauncherLaunchedPosition);
            } else {
                this.robot.RobotHardware.droneLaunchServo.setPosition(this.robot.RobotState.droneLauncherArmedPosition);
            }
        }
    }

    public void setProperties(boolean launchDrone) {
        this.launchDrone = launchDrone;
    }
}
