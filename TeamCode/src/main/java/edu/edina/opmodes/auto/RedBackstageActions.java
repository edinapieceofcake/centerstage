package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

import edu.edina.library.actions.roadrunner.DropPixelAtBackBoard;
import edu.edina.library.enums.ParkLocation;
import edu.edina.library.enums.PropLocation;
import edu.edina.library.util.RobotHardware;

@Autonomous
public class RedBackstageActions extends LinearOpMode {
    protected MecanumDrive drive;
    RevBlinkinLedDriver.BlinkinPattern pattern;
    private DropPixelAtBackBoard dropPixelAtBackBoard;
    RobotHardware hardware;

    protected void initHardware() {
        // test hardware construction and use in an empty action
        hardware = new RobotHardware(hardwareMap);
        dropPixelAtBackBoard = new DropPixelAtBackBoard(hardware);

        Pose2d startPose = new Pose2d(8, -64, Math.toRadians(90));

        // use out version of the drive based off the hardware that we created above.
        drive = new org.firstinspires.ftc.teamcode.MecanumDrive(hardware.leftFront,
                hardware.leftBack, hardware.rightBack, hardware.rightFront,
                hardware.par0, hardware.par1, hardware.perp,
                hardware.imu, hardware.voltageSensor, startPose);

        // uncomment this and comment out the above if it doesn't work right
        //drive = new MecanumDrive(hardwareMap, startPose);

        // Heartbeat Red to signify Red alliance
        pattern = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_RED;
        hardware.blinkinLedDriver.setPattern(pattern);
    }

    protected void runPaths(PropLocation propLocation, ParkLocation parkLocation) {
        // build ahead of time and try that out
        SequentialAction firstStep = new SequentialAction(
                drive.actionBuilder(drive.pose)
                        .splineTo(new Vector2d(48,-35), Math.toRadians(0))
                        .build(),
                new SleepAction(1));

        Actions.runBlocking(firstStep);

        // where to put the purple pixel?
        switch (propLocation) {
            case Left:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(1, -44), Math.toRadians(-90))
                                .build(),
                        dropPixelAtBackBoard.dropPixels(),
                        new SleepAction(1))
                );
                break;
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(12.5, -36), Math.toRadians(-90))
                                .build(),
                        new SleepAction(1))
                );
                break;
            case Right:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(23, -44), Math.toRadians(-90))
                                .build(),
                        new SleepAction(1))
                );
                break;
        }

        // where to park?
        switch (parkLocation) {
            case Center:
                drive.actionBuilder(drive.pose)
                        .setReversed(true)
                        .splineTo(new Vector2d(60, -14), Math.toRadians(0))
                        .build();
            case Corner:
                drive.actionBuilder(drive.pose)
                        .setReversed(true)
                        .splineTo(new Vector2d(60, -60), Math.toRadians(0))
                        .build();
        }

    }

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();

        waitForStart();

        // Signal GREEN for successful run
        pattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
        hardware.blinkinLedDriver.setPattern(pattern);

        if (opModeIsActive()) {
            runPaths(PropLocation.Center, ParkLocation.Corner);
        }

    }
}
