package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.SmartGamepad;

@TeleOp
//@Disabled
public class TestHangMotor extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        RobotHardware hardware = new RobotHardware(hardwareMap);
        double motorPower = .25;
        DcMotorEx hangMotor = hardwareMap.get(DcMotorEx.class, "robotHangerMotor");
        ServoImplEx rightLiftServo = hardwareMap.get(ServoImplEx.class, "leftLiftServo");
        ServoImplEx leftLiftServo = hardwareMap.get(ServoImplEx.class, "rightLiftServo");

        hangMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hangMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hangMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightLiftServo.setPosition(0.1);
        leftLiftServo.setPosition(0.96);

        waitForStart();

        while (opModeIsActive()) {
            pad1.update();

            if (pad1.left_bumper) {
                motorPower += 0.01;
            }

            if (pad1.right_bumper) {
                motorPower -= 0.01;
            }

            if (gamepad1.left_trigger != 0) {
                hangMotor.setPower(Math.abs(motorPower));
            } else if (gamepad1.right_trigger != 0) {
                hangMotor.setPower(-Math.abs(motorPower));
            } else {
                hangMotor.setPower(0);
            }

            if (pad1.dpad_left || pad1.dpad_right) {
                // low
                leftLiftServo.setPosition(.53);
                rightLiftServo.setPosition(.51);
            } else if (pad1.dpad_down) {
                // drive
                leftLiftServo.setPosition(.96);
                rightLiftServo.setPosition(.1);
            } else if (pad1.dpad_up) {
                // high
                leftLiftServo.setPosition(.33);
                rightLiftServo.setPosition(.68);
            }

            if (pad1.y) {
                hardware.homeHangMotor();
            }

            telemetry.addData("Triggers control the hang motor", "");
            telemetry.addData("Press the bumpers to increase and decrease the motor power", "");
            telemetry.addData("Press the dpad up to high servo position, down to store, left or right to middle position", "");
            telemetry.addData("Press the y button to home the hanger motor", "");

            telemetry.addData("Current Power", motorPower);
            telemetry.addData("Hang Motor Current Position: ", hangMotor.getCurrentPosition());
            telemetry.addData("Hang Motor Speed, Current: ", "%f, %f", hangMotor.getPower(), hangMotor.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.update();
        }
    }
}
