package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import edu.edina.library.util.SmartGamepad;

@TeleOp
//@Disabled
public class TestLiftServos extends LinearOpMode {
    private final double maxLiftTicks = 2000;

    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        Servo rightLiftServo = hardwareMap.get(Servo.class, "rightLiftServo");
        Servo leftLiftServo = hardwareMap.get(Servo.class, "leftLiftServo");
        DcMotorEx topLiftMotor = hardwareMap.get(DcMotorEx.class, "topLiftMotor");
        DcMotorEx bottomLiftMotor = hardwareMap.get(DcMotorEx.class, "bottomLiftMotor");

        topLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        topLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        topLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        bottomLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bottomLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bottomLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftLiftServo.setPosition(0);
        rightLiftServo.setPosition(0);

        waitForStart();

        while (opModeIsActive()) {
            pad1.update();

            if (gamepad1.left_bumper) {
                topLiftMotor.setPower(.25);
                bottomLiftMotor.setPower(.25);
            } else if (gamepad1.right_bumper) {
                topLiftMotor.setPower(-0.25);
                bottomLiftMotor.setPower(-0.25);
            } else {
                topLiftMotor.setPower(0);
                bottomLiftMotor.setPower(0);
            }

            double servoPosition = (1/maxLiftTicks) * topLiftMotor.getCurrentPosition() - 0.26;
            leftLiftServo.setPosition(servoPosition);
            rightLiftServo.setPosition(servoPosition);

            telemetry.addData("Top Lift Motor Current Position: ", topLiftMotor.getCurrentPosition());
            telemetry.addData("Bottom Lift Motor Current Position: ", bottomLiftMotor.getCurrentPosition());
            telemetry.addData("Servo Current Position: ", servoPosition);
            telemetry.addData("Left Bumper Pressed?: ", gamepad1.left_bumper);
            telemetry.addData("Right Bumper Pressed?: ", gamepad1.right_bumper);
            telemetry.update();
        }
    }
}
