package edu.edina.library.subsystems;

import edu.edina.library.enums.DroneLauncherState;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import  edu.edina.library.util.RobotState;

public class DroneLauncher implements Subsystem {
    private RobotState state = RobotState.getInstance();
    private boolean started = false;
    private RobotHardware hardware;

    public DroneLauncher(RobotHardware hardware) {
        this.hardware = hardware;
    }

    @Override
    public void init() {
        this.state.droneState = DroneLauncherState.Armed;
        this.hardware.droneLaunchServo.setPosition(RobotConfiguration.getInstance().droneLauncherArmedPosition);
    }

    @Override
    public void start() { started = true; }

    @Override
    public void stop() {
        started = false;
    }

    @Override
    public void update() {
        if (started) {
            switch (state.droneState) {
                case Armed:
                    this.hardware.droneLaunchServo.setPosition(RobotConfiguration.getInstance().droneLauncherArmedPosition);
                    break;
                case Launched:
                default:
                    this.hardware.droneLaunchServo.setPosition(RobotConfiguration.getInstance().droneLauncherLaunchedPosition);
                    break;
            }
        }
    }
}