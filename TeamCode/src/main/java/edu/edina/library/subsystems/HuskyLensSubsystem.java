package edu.edina.library.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.hardware.dfrobot.HuskyLens;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.PropLocation;
import edu.edina.library.util.Robot;

public class HuskyLensSubsystem implements Subsystem{
    private final int READ_PERIOD = 0;
    private HuskyLensSubsystem huskyLens;
    private PropLocation propLocation;
    private Robot robot;
    private HuskyLens.Block propBlock;
    private Deadline rateLimit;

    public HuskyLensSubsystem(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {
        propLocation = PropLocation.Idle;
        if (!robot.RobotHardware.huskyLens.knock()) {
            telemetry.addData(">>", "Problem communicating with " + robot.RobotHardware.huskyLens.getDeviceName());
        } else {
            telemetry.addData(">>", "Press start to continue");
        }

        robot.RobotHardware.huskyLens.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION);

        telemetry.update();
    }

    @Override
    public void start() {
//        rateLimit = new Deadline(READ_PERIOD, TimeUnit.SECONDS);
//        rateLimit.expire();
    }

    @Override
    public void update() {
//        while (!rateLimit.hasExpired())
//        rateLimit.reset();

        HuskyLens.Block[] blocks = robot.RobotHardware.huskyLens.blocks();
        telemetry.addData("Block count", blocks.length);
        for (int i = 0; i < blocks.length; i++) {
            propBlock = blocks[i];
            telemetry.addData("Block", blocks[i].toString());
        }

        telemetry.update();
    }

    public PropLocation getPropLocation() {
        if (propBlock.x < 100) {
            propLocation = PropLocation.Left;
        } else if (propBlock.x > 220) {
            propLocation = PropLocation.Right;
        } else if (propBlock.x >= 100 && propBlock.x <= 220) {
            propLocation = PropLocation.Center;
        } else {
            propLocation = PropLocation.Idle;
        }
        return propLocation;
    }
}
