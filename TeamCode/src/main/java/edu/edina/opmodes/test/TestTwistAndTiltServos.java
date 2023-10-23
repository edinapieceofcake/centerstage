package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import edu.edina.library.util.SmartGamepad;

@TeleOp
//@Disabled
public class TestTwistAndTiltServos extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        Servo twistClawServo = hardwareMap.get(Servo.class, "twistClawServo");
        Servo angleClawServo = hardwareMap.get(Servo.class, "angleClawServo");

        twistClawServo.setPosition(.5);
        angleClawServo.setPosition(.5);

        waitForStart();

        while (opModeIsActive()) {
            pad1.update();

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

            telemetry.addData("Dpad left/right controls twist servo", "");
            telemetry.addData("x, b controls the angle servo", "");
            telemetry.addData("Twist Claw Position: ", twistClawServo.getPosition());
            telemetry.addData("Angle Claw Position: ", angleClawServo.getPosition());

            telemetry.update();
        }
    }
}
