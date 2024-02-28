package edu.edina.library.domain;

import static edu.edina.library.subsystems.MecanumDrive.ScaleMotorCube;
import edu.edina.library.util.RobotState;

public class MecanumDrive {
    public static void setProperties(double leftStickX, double leftStickY, double rightStickX){
        RobotState state = RobotState.getInstance();
        state.leftStickX = ScaleMotorCube(leftStickX);
        state.leftStickY = ScaleMotorCube(leftStickY);
        state.rightStickX = ScaleMotorCube(rightStickX);
    }
}
