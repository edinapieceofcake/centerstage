package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import edu.edina.library.util.SmartGamepad;

@TeleOp
@Disabled
public class TestContinuousServo extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        CRServo continuousServo = hardwareMap.get(CRServo.class, "continuousServo");

        continuousServo.setPower(0);

        waitForStart();

        while (opModeIsActive()) {
            pad1.update();

            if (pad1.dpad_left) {
                continuousServo.setPower(continuousServo.getPower() - .01);
            }

            if (pad1.dpad_right) {
                continuousServo.setPower(continuousServo.getPower() + .01);
            }

            telemetry.addData("Left Claw Position: ", continuousServo.getPower());

            telemetry.update();
        }
    }
}
