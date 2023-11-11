package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import edu.edina.library.enums.PropLocation;
import edu.edina.library.util.PoCHuskyLens;

//@TeleOp
public class TestHuskyLens extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        PoCHuskyLens poCHuskyLens = new PoCHuskyLens(hardwareMap, telemetry);
        PropLocation lastLocation = PropLocation.Left;

        poCHuskyLens.init();

        sleep(5000);
        while (!isStarted()) {
            poCHuskyLens.update();

            lastLocation = poCHuskyLens.getPropLocation();
            telemetry.addData("Location", lastLocation);

            telemetry.update();
        }

        telemetry.addData("Last location", lastLocation);
        telemetry.update();
        sleep(10000);
    }
}
