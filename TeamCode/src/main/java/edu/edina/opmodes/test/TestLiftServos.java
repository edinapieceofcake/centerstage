package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import edu.edina.library.util.Smartgamepad;

@TeleOp
//@Disabled
public class TestLiftServos extends LinearOpMode {
    private final double maxLiftTicks = 2000;

    @Override
    public void runOpMode() throws InterruptedException {
        Smartgamepad pad1 = new Smartgamepad(gamepad1);
        Servo rightLiftServo = hardwareMap.get(Servo.class, "rightLiftServo");
        Servo leftLiftServo = hardwareMap.get(Servo.class, "leftLiftServo");
        DcMotorEx topLiftMotor = hardwareMap.get(DcMotorEx.class, "topLiftMotor");

        topLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        topLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        topLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftLiftServo.setPosition(0);
        rightLiftServo.setPosition(0);

        waitForStart();

        while (opModeIsActive()) {
            pad1.update();

            if (gamepad1.left_bumper) {
                topLiftMotor.setPower(.25);
            } else if (gamepad1.right_bumper) {
                topLiftMotor.setPower(-0.25);
            } else {
                topLiftMotor.setPower(0);
            }

            double servoPosition = (1/maxLiftTicks) * topLiftMotor.getCurrentPosition();
            leftLiftServo.setPosition(servoPosition);
            rightLiftServo.setPosition(servoPosition);

            telemetry.addData("Top Lift Motor Current Position: ", topLiftMotor.getCurrentPosition());
            telemetry.update();
        }
    }
}
