package edu.edina.library.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import edu.edina.opmodes.teleop.Robot;

public class RobotHardware {
    public DcMotor motor;
    public Servo servo1;
    public Servo servo2;

    public RobotHardware(HardwareMap map, Robot robot) {
        motor = map.get(DcMotor.class, "lift");
        servo1 = map.servo.get("servo1");
        servo2 = map.get(Servo.class, "servo2");

        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setTargetPosition(robot.RobotState.LiftClosingTargetPosition);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(.5);

        servo1.setPosition(robot.RobotState.ServoOneClosedPosition);
        servo2.setPosition(robot.RobotState.ServoTwoClosedPosition);
    }
}
