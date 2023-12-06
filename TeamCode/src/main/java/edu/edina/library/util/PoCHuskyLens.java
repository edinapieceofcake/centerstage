package edu.edina.library.util;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

import edu.edina.library.enums.Alliance;
import edu.edina.library.enums.PropLocation;

public class PoCHuskyLens {
    private final int READ_PERIOD = 1;

    private Alliance alliance;

    private HuskyLens huskyLens;

    private Telemetry telemetry;

    private Deadline rateLimit = new Deadline(READ_PERIOD, TimeUnit.SECONDS);

    public PropLocation propLocation = PropLocation.None;

    public PoCHuskyLens(HuskyLens huskyLens, Telemetry telemetry, Alliance alliance){
        this.huskyLens = huskyLens;
        this.telemetry = telemetry;
        this.alliance = alliance;
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

        if (!rateLimit.hasExpired()) {
            return;
        }

        rateLimit.reset();

        HuskyLens.Block[] blocks = huskyLens.blocks();

        if (blocks.length == 0) {
            propLocation = PropLocation.None;
            telemetry.addData("No blocks", "");
        } else {
            telemetry.addData("Block count", blocks.length);

            for (int i = 0; i < blocks.length; i++) {
                HuskyLens.Block currentBlock = blocks[i];
                telemetry.addData("Block", currentBlock.toString());

                if (currentBlock.id == alliance.value) {
                    telemetry.addData("Matched block", "");
                    if (currentBlock.x < 100) {
                        propLocation = PropLocation.Left;
                    } else if (currentBlock.x >= 100 && currentBlock.x <= 220) {
                        propLocation = PropLocation.Center;
                    } else if (currentBlock.x > 220) {
                        propLocation = PropLocation.Right;
                    } else {
                        propLocation = PropLocation.None;
                    }
                } else {
                    telemetry.addData("Skipped block", "");
                    propLocation = PropLocation.None;
                }
            }
        }
    }

    public PropLocation getPropLocation() {
        return propLocation;
    }
}
