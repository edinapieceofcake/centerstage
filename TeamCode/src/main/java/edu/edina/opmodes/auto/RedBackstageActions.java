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

import edu.edina.library.enums.ParkLocation;
import edu.edina.library.enums.PropLocation;

@Autonomous
public class RedBackstageActions extends LinearOpMode {
    protected MecanumDrive drive;
    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;

    protected void initHardware() {
        Pose2d startPose = new Pose2d(8, -64, Math.toRadians(90));
        drive = new MecanumDrive(hardwareMap, startPose);
        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");

        // Heartbeat Red to signify Red alliance
        pattern = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_RED;
        blinkinLedDriver.setPattern(pattern);
    }

    protected void runPaths(PropLocation propLocation, ParkLocation parkLocation) {
        Actions.runBlocking(new SequentialAction(
               drive.actionBuilder(drive.pose)
                       .splineTo(new Vector2d(48,-35), Math.toRadians(0))
                       .build(),
               new SleepAction(1))
        );

        // where to put the purple pixel?
        switch (propLocation) {
            case Left:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(1, -44), Math.toRadians(-90))
                                .build(),
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
        blinkinLedDriver.setPattern(pattern);

        if (opModeIsActive()) {
            runPaths(PropLocation.Center, ParkLocation.Corner);
        }

    }
}
