package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.SmartGamepad;

@TeleOp
@Disabled
public class TestHangMotor extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        RobotHardware hardware = new RobotHardware(hardwareMap);
        double motorPower = .25;

        hardware.rightLiftServo.setPosition(0.1);
        hardware.leftLiftServo.setPosition(0.96);

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
                hardware.robotHangerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                hardware.robotHangerMotor.setPower(-Math.abs(motorPower));
            } else if (gamepad1.right_trigger != 0) {
                hardware.robotHangerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                hardware.robotHangerMotor.setPower(Math.abs(motorPower));
            } else if (!hardware.hangMotorHoming && hardware.robotHangerMotor.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
                hardware.robotHangerMotor.setPower(0);
            }

            if (pad1.dpad_left || pad1.dpad_right) {
                // medium
                hardware.robotHangerMotor.setTargetPosition(RobotConfiguration.getInstance().hangMotorLowDropOffPosition);
                hardware.robotHangerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                hardware.robotHangerMotor.setPower(1);
                sleep(500);
                hardware.leftLiftServo.setPosition(.53);
                hardware.rightLiftServo.setPosition(.51);
            } else if (pad1.dpad_down) {
                // drive
                hardware.leftLiftServo.setPosition(.96);
                hardware.rightLiftServo.setPosition(.1);
                hardware.robotHangerMotor.setTargetPosition(RobotConfiguration.getInstance().hangMotorStorePosition);
                hardware.robotHangerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                hardware.robotHangerMotor.setPower(1);
            } else if (pad1.dpad_up) {
                hardware.robotHangerMotor.setTargetPosition(RobotConfiguration.getInstance().hangMotorHighDropOffPosition);
                hardware.robotHangerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                hardware.robotHangerMotor.setPower(1);
                // high
                sleep(700);
                hardware.leftLiftServo.setPosition(.33);
                hardware.rightLiftServo.setPosition(.68);
            }

            if (pad1.y) {
                hardware.homeHangMotorAsync();
            }

            if (pad1.b) {
                hardware.homeHangMotor(telemetry);
            }

            if (pad1.a) {
                ((PwmControl)hardware.leftLiftServo).setPwmDisable();
                ((PwmControl)hardware.rightLiftServo).setPwmDisable();
            }

            telemetry.addData("Triggers control the hang motor", "");
            telemetry.addData("Press the bumpers to increase and decrease the motor power", "");
            telemetry.addData("Press the dpad up to high servo position, down to store, left or right to middle position", "");
            telemetry.addData("Press the y button to home the hanger motor async", "");
            telemetry.addData("Press the b button to home the hanger motor", "");
            telemetry.addData("Press a to kill the PWM signal", "");
            telemetry.addData("Hanger switch", hardware.hangSwitch.getState());
            telemetry.addData("Triggers", "%f %f", gamepad1.left_trigger, gamepad1.right_trigger);

            telemetry.addData("Current Power", motorPower);
            telemetry.addData("Hang Motor Current Position: ", hardware.robotHangerMotor.getCurrentPosition());
            telemetry.addData("Hang Motor Mode", hardware.robotHangerMotor.getMode());
            telemetry.addData("Hang Motor Speed, Current: ", "%f, %f", hardware.robotHangerMotor.getPower(), hardware.robotHangerMotor.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.update();
        }
    }
}
