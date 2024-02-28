package edu.edina.library.domain;

import edu.edina.library.util.RobotState;

public class MecanumDrive {
    public static void setProperties(double leftStickX, double leftStickY, double rightStickX){
        RobotState state = RobotState.getInstance();
        state.leftStickX = ScaleMotorCube(leftStickX);
        state.leftStickY = ScaleMotorCube(leftStickY);
        state.rightStickX = ScaleMotorCube(rightStickX);
    }

    public static double ScaleMotorCube(double joyStickPosition) {
        return (double) Math.pow(joyStickPosition, 3.0);
    }
}
