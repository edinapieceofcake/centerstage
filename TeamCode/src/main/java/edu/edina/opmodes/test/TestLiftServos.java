package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import edu.edina.library.util.SmartGamepad;

@TeleOp
//@Disabled
public class TestLiftServos extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        Servo rightLiftServo = hardwareMap.get(Servo.class, "rightLiftServo");
        Servo leftLiftServo = hardwareMap.get(Servo.class, "leftLiftServo");

        rightLiftServo.setPosition(.5);
        leftLiftServo.setPosition(.5);

        waitForStart();

        while (opModeIsActive()) {
            pad1.update();

            if (pad1.left_bumper) {
                rightLiftServo.setPosition(rightLiftServo.getPosition() + .01);
                leftLiftServo.setPosition(leftLiftServo.getPosition() + .01);
            }

            if (pad1.right_bumper) {
                rightLiftServo.setPosition(rightLiftServo.getPosition() - .01);
                leftLiftServo.setPosition(leftLiftServo.getPosition() - .01);
            }

            telemetry.addData("Bumpers control the lift servos", "");
            telemetry.addData("Left Lift Position: ", leftLiftServo.getPosition());
            telemetry.addData("Right Lift Position: ", rightLiftServo.getPosition());

            telemetry.update();
        }
    }
}
