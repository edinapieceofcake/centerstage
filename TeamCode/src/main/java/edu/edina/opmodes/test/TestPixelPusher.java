package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import edu.edina.library.util.SmartGamepad;

@TeleOp
@Disabled
public class TestPixelPusher extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        Servo pixelMover = hardwareMap.get(Servo.class, "pixelLiftServo");

        waitForStart();

        pixelMover.setPosition(.5);
        while (opModeIsActive()) {
            pad1.update();

            if (pad1.left_bumper) {
                pixelMover.setPosition(pixelMover.getPosition() + .01);
            }

            if (pad1.right_bumper) {
                pixelMover.setPosition(pixelMover.getPosition() - .01);
            }

            telemetry.addData("Bumpers control ths position", "left increases, right decreases");
            telemetry.addData("PixelPusher position", pixelMover.getPosition());
            telemetry.update();
        }
    }
}
