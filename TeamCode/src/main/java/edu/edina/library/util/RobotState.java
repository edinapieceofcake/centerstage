package edu.edina.library.util;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

import edu.edina.library.enums.LiftState;

public class RobotState {
    public int currentTopMotorPosition;
    public int currentBottomMotorPosition;
    public LiftState currentLiftState;
    public double currentLiftServoPosition;

    public RobotState() {
    }

   public void telemetry(Telemetry telemetry) {
        telemetry.addData("Top Motor LiftPosition", currentTopMotorPosition);
        telemetry.addData("Bottom Motor LiftPosition", currentBottomMotorPosition);
        telemetry.addData("LiftState", currentLiftState);
        telemetry.addData("Current Lift Servo Position", currentLiftServoPosition);
    }
}
