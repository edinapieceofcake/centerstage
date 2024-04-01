package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import edu.edina.library.util.PoCMotor;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.SmartGamepad;

@TeleOp
//@Disabled
public class TestHangMotor2 extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        RobotHardware hardware = new RobotHardware(hardwareMap);
        PoCMotor hangMotor = new PoCMotor(hardware.robotHangerMotor);

        waitForStart();

        hangMotor.setPower(0);
        while (opModeIsActive()) {
            pad1.update();

            if (gamepad1.left_trigger != 0) {
                hangMotor.setPower(1);
            } else if (gamepad1.right_trigger != 0) {
                hangMotor.setPower(-1);
            } else {
                hangMotor.setPower(0);
            }

            telemetry.addData("Triggers control ths motor", "");
            telemetry.addData("HangMotor power", hangMotor.getPower());
            telemetry.update();
        }
    }
}
