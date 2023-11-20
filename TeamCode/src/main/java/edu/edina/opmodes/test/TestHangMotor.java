package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import edu.edina.library.util.SmartGamepad;

@TeleOp
//@Disabled
public class TestHangMotor extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        double motorPower = .25;
        DcMotorEx hangMotor = hardwareMap.get(DcMotorEx.class, "robotHangerMotor");

        hangMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hangMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hangMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();

        while (opModeIsActive()) {
            pad1.update();

            if (pad1.left_bumper) {
                motorPower += 0.01;
            }

            if (pad1.right_bumper) {
                motorPower = 0.01;
            }

            if (gamepad1.left_trigger != 0) {
                hangMotor.setPower(Math.abs(motorPower));
            } else if (gamepad1.right_trigger != 0) {
                hangMotor.setPower(-Math.abs(motorPower));
            } else {
                hangMotor.setPower(0);
            }

            telemetry.addData("Triggers control the hang motor", "");
            telemetry.addData("Press the bumpers to increase and decrease the motor power", "");
            telemetry.addData("Current Power", motorPower);
            telemetry.addData("Hang Motor Current Position: ", hangMotor.getCurrentPosition());
            telemetry.addData("Hang Motor Speed, Current: ", "%f, %f", hangMotor.getPower(), hangMotor.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.update();
        }
    }
}
