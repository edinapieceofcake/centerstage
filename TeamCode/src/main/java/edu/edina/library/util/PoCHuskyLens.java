package edu.edina.library.util;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

import edu.edina.library.enums.PropLocation;

public class PoCHuskyLens {
    private final int READ_PERIOD = 1;

    private int blockId;

    private HuskyLens huskyLens;

    private Telemetry telemetry;

    private Deadline rateLimit = new Deadline(READ_PERIOD, TimeUnit.SECONDS);

    public PropLocation propLocation = PropLocation.None;

    public PoCHuskyLens(HuskyLens huskyLens, Telemetry telemetry, int blockId){
        this.huskyLens = huskyLens;
        this.telemetry = telemetry;
        this.blockId = blockId;
    }

    public void init() {
        rateLimit.expire();

        if (!huskyLens.knock()) {
            telemetry.addData(">>", "Problem communicating with " + huskyLens.getDeviceName());
        } else {
            telemetry.addData(">>", "Press start to continue");
        }

        huskyLens.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);

        telemetry.update();
    }

    public void update() {
        double smallestRatio = 9999.0;
        int smallestBlockLocation = -1;

        if (!rateLimit.hasExpired()) {
            return;
        }

        rateLimit.reset();

        HuskyLens.Block[] blocks = huskyLens.blocks();
        telemetry.addData("Block count", blocks.length);

        for (int i = 0; i < blocks.length; i++) {
            double ratio = (1.0 - (blocks[i].x / blocks[i].y));
            telemetry.addData("Block", blocks[i].toString());
            telemetry.addData("Ratio, Picked Location", "%f %d %d", ratio, smallestBlockLocation, blockId);

            if (blocks[i].id == blockId) {
                telemetry.addData("Matched block", "");
                if (ratio < smallestRatio) {
                    smallestRatio = ratio;
                    smallestBlockLocation = i;
                }
            } else {
                telemetry.addData("Skipped block", "");
            }
        }

        if (smallestBlockLocation != -1) {
            HuskyLens.Block propBlock = blocks[smallestBlockLocation];

            if (propBlock.x < 100) {
                propLocation = PropLocation.Left;
            } else if (propBlock.x >= 100 && propBlock.x <= 220) {
                propLocation = PropLocation.Center;
            } else if (propBlock.x > 220) {
                propLocation = PropLocation.Right;
            } else {
                propLocation = PropLocation.None;
            }
        }
    }

    public PropLocation getPropLocation() {
        return propLocation;
    }
}
