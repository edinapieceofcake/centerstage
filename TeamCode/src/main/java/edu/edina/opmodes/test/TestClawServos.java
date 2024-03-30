package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import edu.edina.library.util.SmartGamepad;

@TeleOp
@Disabled
public class TestClawServos extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        Servo leftClawServo = hardwareMap.get(Servo.class, "leftClawServo");
        Servo rightClawServo = hardwareMap.get(Servo.class, "rightClawServo");
        Servo pushServo = hardwareMap.get(Servo.class, "pushServo");

        leftClawServo.setPosition(.5);
        rightClawServo.setPosition(.5);

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
            if (pad1.b) {
                pushServo.setPosition(pushServo.getPosition() - .01);
            }
            if (pad1.x) {
                pushServo.setPosition(pushServo.getPosition() + .01);
            }


            telemetry.addData("Dpad up/down controls the left claw", "");
            telemetry.addData("y, a controls the right claw", "");
            telemetry.addData("x/b to control pixel pusher", "");
            telemetry.addData("Left Claw Position: ", leftClawServo.getPosition());
            telemetry.addData("Right Claw Position: ", rightClawServo.getPosition());
            telemetry.addData("Pixel Pusher Position: ", pushServo.getPosition());

            telemetry.update();
        }
    }
}
