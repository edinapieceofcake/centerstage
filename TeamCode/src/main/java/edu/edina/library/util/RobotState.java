package edu.edina.library.util;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftSlideState;

public class RobotState {
    public int currentTopMotorPosition;
    public int currentBottomMotorPosition;
    public LiftSlideState currentLiftSlideState;
    public double currentLiftServoPosition;
    public LiftDriveState currentLiftDriveState;

    public RobotState() {
        currentLiftSlideState = LiftSlideState.Idle;
        currentLiftDriveState = LiftDriveState.Manual;
    }

    public void telemetry(Telemetry telemetry) {
        telemetry.addData("Top Motor LiftPosition", currentTopMotorPosition);
        telemetry.addData("Bottom Motor LiftPosition", currentBottomMotorPosition);
        telemetry.addData("LiftSlideState", currentLiftSlideState);
        telemetry.addData("LiftDriveState", currentLiftDriveState);
        telemetry.addData("Current Lift Servo Position", currentLiftServoPosition);
    }
}
