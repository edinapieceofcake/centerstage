package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import edu.edina.library.util.SmartGamepad;

@TeleOp
//@Disabled
public class TestClawServos extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        Servo leftClawServo = hardwareMap.get(Servo.class, "leftClawServo");
        Servo rightClawServo = hardwareMap.get(Servo.class, "rightClawServo");

        leftClawServo.setPosition(.5);
        rightClawServo.setPosition(.5);

        waitForStart();

        while (opModeIsActive()) {
            pad1.update();

            if (pad1.dpad_left) {
                leftClawServo.setPosition(leftClawServo.getPosition() + .01);
            }

            if (pad1.dpad_right) {
                leftClawServo.setPosition(leftClawServo.getPosition() - .01);
            }

            if (pad1.x) {
                rightClawServo.setPosition(rightClawServo.getPosition() + .01);
            }

            if (pad1.b) {
                rightClawServo.setPosition(rightClawServo.getPosition() - .01);
            }

            telemetry.addData("Left Claw Position: ", leftClawServo.getPosition());
            telemetry.addData("Right Claw Position: ", rightClawServo.getPosition());

            telemetry.update();
        }
    }
}
