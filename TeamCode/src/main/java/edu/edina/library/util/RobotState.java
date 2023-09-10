package edu.edina.library.util;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import edu.edina.opmodes.teleop.Robot;

public class RobotState {
    private Robot robot;
    public double Servo1Position;
    public double Servo2Position;
    public int LiftOpenTargetPosition;
    public int LiftClosingTargetPosition;
    public LiftState LiftState;
    public OpeningState OpeningState;
    public ClosingState ClosingState;
    public double ServoOneOpenPosition;
    public double ServoOneClosedPosition;
    public double ServoTwoOpenPosition;
    public double ServoTwoClosedPosition;
    
    public RobotState(Robot robot) {
        this.robot = robot;
        Servo1Position = 0.0;
        Servo2Position = 0.0;
        LiftState = edu.edina.library.util.LiftState.Idle;
        OpeningState = edu.edina.library.util.OpeningState.Idle;
        ClosingState = edu.edina.library.util.ClosingState.Idle;
        ServoOneOpenPosition = 0.75;
        ServoOneClosedPosition = 0.25;
        ServoTwoOpenPosition = 0.9;
        ServoTwoClosedPosition = 0.1;
        LiftOpenTargetPosition = 3000;
        LiftClosingTargetPosition = 0;
    }

    public void telemetry(Telemetry telemetry) {
        telemetry.addData("servo1", Servo1Position);
        telemetry.addData("servo1", this.robot.RobotHardware.servo1.getPosition());
        telemetry.addData("servo2", Servo2Position);
        telemetry.addData("servo2", this.robot.RobotHardware.servo2.getPosition());
        telemetry.addData("Motor", this.robot.RobotHardware.motor.getCurrentPosition());
        telemetry.addData("State", this.LiftState);
        telemetry.addData("OpeningState", this.OpeningState);
        telemetry.addData("ClosingState", this.ClosingState);
    }
}
