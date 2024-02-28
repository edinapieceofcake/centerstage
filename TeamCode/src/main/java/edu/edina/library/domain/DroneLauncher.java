package edu.edina.library.domain;

import edu.edina.library.enums.DroneLauncherState;
import edu.edina.library.util.RobotState;

public class DroneLauncher {
    public static void setProperties(boolean launchDrone) {
        RobotState state = RobotState.getInstance();
        if (launchDrone) {
            if (state.droneState == DroneLauncherState.Launched) {
                state.droneState = DroneLauncherState.Armed;
            } else {
                state.droneState = DroneLauncherState.Launched;
            }
        }
    }
}