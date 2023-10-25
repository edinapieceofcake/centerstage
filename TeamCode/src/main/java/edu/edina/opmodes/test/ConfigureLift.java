package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import edu.edina.library.subsystems.DroneLauncher;
import edu.edina.library.util.SmartGamepad;

@TeleOp
//@Disabled
public class ConfigureLift extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        final double droneStart = 0.6;
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        DcMotorEx topLiftMotor = hardwareMap.get(DcMotorEx.class, "topLiftMotor");
        DcMotorEx bottomLiftMotor = hardwareMap.get(DcMotorEx.class, "bottomLiftMotor");

        Servo rightLiftServo = hardwareMap.get(Servo.class, "rightLiftServo");
        Servo leftLiftServo = hardwareMap.get(Servo.class, "leftLiftServo");

        Servo leftClawServo = hardwareMap.get(Servo.class, "leftClawServo");
        Servo rightClawServo = hardwareMap.get(Servo.class, "rightClawServo");

        Servo twistClawServo = hardwareMap.get(Servo.class, "twistClawServo");
        Servo angleClawServo = hardwareMap.get(Servo.class, "angleClawServo");

        DigitalChannel liftSwitch =hardwareMap.get(DigitalChannel.class, "liftSwitch");

        Servo droneLauncher = hardwareMap.get(Servo.class, "droneLaunchServo");

        droneLauncher.setPosition(droneStart);

        rightLiftServo.setPosition(0.25);
        leftLiftServo.setPosition(0.25);

        twistClawServo.setPosition(.96);
        angleClawServo.setPosition(.32);

//        left claw closed position is 0.83
//        right claw closed position is 0.25
        leftClawServo.setPosition(.92);
        rightClawServo.setPosition(.08);

        topLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        topLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        topLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        topLiftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        bottomLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bottomLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bottomLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        while (opModeIsActive()) {
            pad1.update();


            if (pad1.dpad_up) {
                leftClawServo.setPosition(leftClawServo.getPosition() + .01);
            }

            if (pad1.dpad_down) {
                leftClawServo.setPosition(leftClawServo.getPosition() - .01);
            }

            if (pad1.y) {
                rightClawServo.setPosition(rightClawServo.getPosition() + .01);
            }

            if (pad1.a) {
                rightClawServo.setPosition(rightClawServo.getPosition() - .01);
            }

            if (gamepad1.left_trigger != 0) {
                topLiftMotor.setPower(.25);
                bottomLiftMotor.setPower(.25);
            } else if (gamepad1.right_trigger != 0) {
                topLiftMotor.setPower(-0.25);
                bottomLiftMotor.setPower(-0.25);
            } else {
                topLiftMotor.setPower(0);
                bottomLiftMotor.setPower(0);
            }

            if (pad1.left_bumper) {
                rightLiftServo.setPosition(rightLiftServo.getPosition() + .01);
                leftLiftServo.setPosition(leftLiftServo.getPosition() + .01);
            }

            if (pad1.right_bumper) {
                rightLiftServo.setPosition(rightLiftServo.getPosition() - .01);
                leftLiftServo.setPosition(leftLiftServo.getPosition() - .01);
            }

            if (pad1.dpad_left) {
                twistClawServo.setPosition(twistClawServo.getPosition() + .01);
            }

            if (pad1.dpad_right) {
                twistClawServo.setPosition(twistClawServo.getPosition() - .01);
            }

            if (pad1.x) {
                angleClawServo.setPosition(angleClawServo.getPosition() + .01);
            }

            if (pad1.b) {
                angleClawServo.setPosition(angleClawServo.getPosition() - .01);
            }

            if (gamepad1.left_stick_button) {
                droneLauncher.setPosition(droneStart);
            }

            if (gamepad1.right_stick_button) {
                droneLauncher.setPosition(0);
            }

            telemetry.addData("Triggers control the lift motors", "");
            telemetry.addData("Bumpers control the lift servos", "");
            telemetry.addData("Dpad up/down controls the left claw", "");
            telemetry.addData("y, a controls the right claw", "");
            telemetry.addData("Dpad left/right controls twist servo", "");
            telemetry.addData("x, b controls the angle servo", "");
            telemetry.addData("press left stick, press right stick controls the drone launcher", "");

            telemetry.addData("=======================================================", "");

            telemetry.addData("Left Claw Position: ", leftClawServo.getPosition());
            telemetry.addData("Right Claw Position: ", rightClawServo.getPosition());
            telemetry.addData("Top Lift Motor Current Position: ", topLiftMotor.getCurrentPosition());
            telemetry.addData("Bottom Lift Motor Current Position: ", bottomLiftMotor.getCurrentPosition());
            telemetry.addData("Left Lift Position: ", leftLiftServo.getPosition());
            telemetry.addData("Right Lift Position: ", rightLiftServo.getPosition());
            telemetry.addData("Twist Claw Position: ", twistClawServo.getPosition());
            telemetry.addData("Angle Claw Position: ", angleClawServo.getPosition());
            telemetry.addData("Lift Switch: ", liftSwitch.getState());

            telemetry.update();
        }
    }
}
