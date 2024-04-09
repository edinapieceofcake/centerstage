package edu.edina.library.domain;

import static edu.edina.library.subsystems.MecanumDrive.ScaleMotorCube;

import edu.edina.library.enums.ClawState;
import edu.edina.library.util.RobotState;

public class MecanumDrive {
    public static void setProperties(double leftStickX, double leftStickY, double rightStickX){
        RobotState state = RobotState.getInstance();

        if (state.pusherState == ClawState.Closed) {
            state.leftStickX = ScaleMotorCube(leftStickX) * 0.8;
            state.leftStickY = ScaleMotorCube(leftStickY) * 0.8;
            state.rightStickX = ScaleMotorCube(rightStickX) / 1.5;
        } else {
            state.leftStickX = ScaleMotorCube(leftStickX) * 0.5;
            state.leftStickY = ScaleMotorCube(leftStickY) * 0.5;
            state.rightStickX = ScaleMotorCube(rightStickX) / 1.5;
        }
    }
}
