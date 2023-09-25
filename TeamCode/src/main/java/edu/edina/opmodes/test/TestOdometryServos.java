package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import edu.edina.library.util.Smartgamepad;

@TeleOp
//@Disabled
public class TestOdometryServos extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Smartgamepad pad1 = new Smartgamepad(gamepad1);
        Servo par0Servo = hardwareMap.get(Servo.class, "par0Servo");
        Servo par1Servo = hardwareMap.get(Servo.class, "par1Servo");
        Servo perpServo = hardwareMap.get(Servo.class, "perpServo");

        par0Servo.setPosition(.5);
        par1Servo.setPosition(.5);
        perpServo.setPosition(.5);

        waitForStart();

        while (opModeIsActive()) {
            pad1.update();

            if (pad1.dpad_up) {
                par0Servo.setPosition(par0Servo.getPosition() + .01);
            }

            if (pad1.dpad_down) {
                par0Servo.setPosition(par0Servo.getPosition() - .01);
            }

            if (pad1.left_bumper) {
                perpServo.setPosition(par0Servo.getPosition() + .01);
            }

            if (pad1.right_bumper) {
                perpServo.setPosition(par0Servo.getPosition() - .01);
            }

            if (pad1.y) {
                par1Servo.setPosition(par0Servo.getPosition() + .01);
            }

            if (pad1.a) {
                par1Servo.setPosition(par0Servo.getPosition() - .01);
            }

            telemetry.addData("par0 Servo", par0Servo.getPosition());
            telemetry.addData("par1 Servo", par1Servo.getPosition());
            telemetry.addData("perp Servo", perpServo.getPosition());

            telemetry.update();
        }
    }
}
