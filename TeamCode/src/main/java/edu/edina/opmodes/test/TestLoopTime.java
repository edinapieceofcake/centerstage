package edu.edina.opmodes.test;

import android.util.Log;

import com.outoftheboxrobotics.photoncore.Photon;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import edu.edina.library.util.SmartGamepad;

@TeleOp
//@Disabled
public class TestLoopTime extends OpMode {
    DcMotorEx lights;
    SmartGamepad pad1;
    @Override
    public void init() {
        pad1 = new SmartGamepad(gamepad1);
        lights = hardwareMap.get(DcMotorEx.class, "lights");
    }

    @Override
    public void loop() {
//            pad1.update();
//
//            if (pad1.left_bumper) {
//                lights.setPower(lights.getPower() + .1);
//            }
//
//            if (pad1.right_bumper) {
//                lights.setPower(0);
//            }
//
//            telemetry.addData("Bumpers control the light", "left increases, right turns off");
//            telemetry.addData("Light power", lights.getPower());
//            telemetry.update();
        Log.d("LOOP_TIME", String.format("Light power %f", lights.getPower()));
    }
}
