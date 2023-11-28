package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import edu.edina.library.subsystems.DroneLauncher;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.SmartGamepad;

@TeleOp
//@Disabled
public class ConfigureLift extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        final double droneStart = 0.7;
        SmartGamepad pad1 = new SmartGamepad(gamepad1);

        DcMotorEx par0 = hardwareMap.get(DcMotorEx.class, "rightFront");
        DcMotorEx par1 = hardwareMap.get(DcMotorEx.class, "rightBack");
        DcMotorEx perp = hardwareMap.get(DcMotorEx.class, "leftFront");

        DcMotorEx topLiftMotor = hardwareMap.get(DcMotorEx.class, "topLiftMotor");
        DcMotorEx bottomLiftMotor = hardwareMap.get(DcMotorEx.class, "bottomLiftMotor");

        ServoImplEx rightLiftServo = hardwareMap.get(ServoImplEx.class, "leftLiftServo");
        ServoImplEx leftLiftServo = hardwareMap.get(ServoImplEx.class, "rightLiftServo");

        ServoImplEx leftClawServo = hardwareMap.get(ServoImplEx.class, "leftClawServo");
        ServoImplEx rightClawServo = hardwareMap.get(ServoImplEx.class, "rightClawServo");

        ServoImplEx twistClawServo = hardwareMap.get(ServoImplEx.class, "twistClawServo");
        ServoImplEx angleClawServo = hardwareMap.get(ServoImplEx.class, "angleClawServo");

        DigitalChannel liftSwitch =hardwareMap.get(DigitalChannel.class, "liftSwitch");

        ServoImplEx droneLauncher = hardwareMap.get(ServoImplEx.class, "droneLaunchServo");

        droneLauncher.setPosition(droneStart);

        rightLiftServo.setPosition(0.1);
        leftLiftServo.setPosition(0.96);

        twistClawServo.setPosition(.96);
        angleClawServo.setPosition(.42);

//        left claw closed position is 0.83
//        right claw closed position is 0.25
        leftClawServo.setPosition(.92);
        rightClawServo.setPosition(.08);

        topLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        topLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        topLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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

            if (gamepad1.right_trigger != 0) {
                topLiftMotor.setPower(-1);
                bottomLiftMotor.setPower(-1);
            } else if (gamepad1.left_trigger != 0) {
                // intake
                topLiftMotor.setPower(0.5);
                bottomLiftMotor.setPower(0.5);
            } else {
                topLiftMotor.setPower(0);
                bottomLiftMotor.setPower(0);
            }

            if (pad1.right_bumper) {
                // moving up
//                rightLiftServo.setPosition(rightLiftServo.getPosition() - .01);
//                leftLiftServo.setPosition(leftLiftServo.getPosition() + .01);
                leftLiftServo.setPosition(.53);
                rightLiftServo.setPosition(.51);
            } else if (pad1.left_bumper) {
                // intake
//                rightLiftServo.setPosition(rightLiftServo.getPosition() + .01);
//                leftLiftServo.setPosition(leftLiftServo.getPosition() - .01);
                leftLiftServo.setPosition(.96);
                rightLiftServo.setPosition(.1);
            }

            if (pad1.dpad_left) {
//                DropOff
//                twistClawServo.setPosition(twistClawServo.getPosition() + .01);
                twistClawServo.setPosition(0.28);
                angleClawServo.setPosition(.77);
            }

            if (pad1.dpad_right) {
//                Pick Up
//                twistClawServo.setPosition(twistClawServo.getPosition() - .01);
                twistClawServo.setPosition(0.96);
                angleClawServo.setPosition(0.37);
            }

            if (pad1.x) {
                angleClawServo.setPosition(angleClawServo.getPosition() + .1);
//                angleClawServo.setPosition(1);
            }


            if (pad1.b) {
                angleClawServo.setPosition(angleClawServo.getPosition() - .1);
//                angleClawServo.setPosition(0);
            }

            if (gamepad1.left_stick_button) {
                droneLauncher.setPosition(0.7);
            }

            if (gamepad1.right_stick_button) {
                droneLauncher.setPosition(0.1);
            }

            telemetry.addData("Triggers control the reel motor", "");
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
//            telemetry.addData("Twist Claw Position: ", twistClawServo.getPosition());
//            telemetry.addData("Angle Claw Position: ", angleClawServo.getPosition());
//            telemetry.addData("Lift Switch: ", liftSwitch.getState());
//            telemetry.addData("Servo Manufacturer", leftLiftServo.getManufacturer().name());
//            telemetry.addData("Par0 (rightFront)(0)", par0.getCurrentPosition());
//            telemetry.addData("Par1 (rightBack)(3)", par1.getCurrentPosition());
//            telemetry.addData("Perp (leftFront)(1)", perp.getCurrentPosition());

            telemetry.update();
        }
    }
}
