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
public class TestLights extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        DcMotorEx lights = hardwareMap.get(DcMotorEx.class, "lights");

        waitForStart();

        while (opModeIsActive()) {
            pad1.update();

            if (pad1.left_bumper) {
                lights.setPower(lights.getPower() + .1);
            }

            if (pad1.right_bumper) {
                lights.setPower(0);
            }

            telemetry.addData("Triggers control the light", "left on, right off");
            telemetry.addData("Top Lift Motor Current Position: ", "%f %f", lights.getPower(), lights.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.update();
        }
    }
}
