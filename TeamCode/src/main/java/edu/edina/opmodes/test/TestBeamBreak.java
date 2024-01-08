package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.SmartGamepad;

@TeleOp
//@Disabled
public class TestBeamBreak extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        RobotHardware hardware = new RobotHardware(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Beam Break", hardware.beamBreak.getState());
            telemetry.update();
        }
    }
}
