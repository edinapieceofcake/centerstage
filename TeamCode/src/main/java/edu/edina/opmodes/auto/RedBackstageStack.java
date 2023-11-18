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
import edu.edina.library.util.PoCHuskyLens;
import edu.edina.library.util.RobotHardware;

@Autonomous
public class RedBackstageStack extends LinearOpMode {
    RobotHardware hardware;
    protected MecanumDrive drive;

    RevBlinkinLedDriver.BlinkinPattern pattern;

    PoCHuskyLens poCHuskyLens;
    PropLocation propLocation;

    double delta1 = 0;

    private SleepAction sleep1sAction = new SleepAction(1);


    protected void initHardware() {
        // test hardware construction and use in an empty action
        hardware = new RobotHardware(hardwareMap);

        Pose2d startPose = new Pose2d(8, -64, Math.toRadians(90));

        // use out version of the drive based off the hardware that we created above.
        drive = new MecanumDrive(hardware.leftFront,
                hardware.leftBack, hardware.rightBack, hardware.rightFront,
                hardware.par0, hardware.par1, hardware.perp,
                hardware.imu, hardware.voltageSensor, startPose);

        // uncomment this and comment out the above if it doesn't work right
        //drive = new MecanumDrive(hardwareMap, startPose);

        // Heartbeat Red to signify Red alliance
        pattern = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_RED;
        hardware.blinkinLedDriver.setPattern(pattern);

        // HuskyLens Init
        PropLocation lastLocation = PropLocation.Idle;
        poCHuskyLens = new PoCHuskyLens(hardware.huskyLens, telemetry, 2);
        poCHuskyLens.init();

        sleep(2000);
        while (!isStarted()) {
            poCHuskyLens.update();

            propLocation = poCHuskyLens.getPropLocation();
            telemetry.addData("Location", propLocation);

            telemetry.update();
            sleep(2000);
        }
    }

    protected void runPaths(ParkLocation parkLocation) {

        switch(propLocation) {
            case Left:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(48,-29), Math.toRadians(0))
                                .build(),
                        new SleepAction(1)));
                delta1 = 9;
                break;
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(48,-35.5), Math.toRadians(0))
                                .build(),
                        new SleepAction(1)));
                break;
            case Right:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(48,-42), Math.toRadians(0))
                                .build(),
                        new SleepAction(1)));
                delta1 = 9;
                break;
            default:
                break;
        }


        Actions.runBlocking(new SequentialAction(
                drive.actionBuilder(drive.pose)
                        .turnTo(Math.toRadians(180))
                        .build()));

        // where to put the purple pixel?
        switch (propLocation) {
            case Left:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                //.setReversed(true)
                                .splineTo(new Vector2d(10, -34), Math.toRadians(180))
                                .build(),
                        //depositMech.moveLift(300),
                        //sleep1sAction,
                        //depositMech.dropPixels(),
                        new SleepAction(1))
                );
                break;
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                //.setReversed(true)
                                .splineTo(new Vector2d(24, -25), Math.toRadians(180))
                                .build(),
                        //depositMech.dropPixels(),
                        //new SleepAction(1),
                        //depositMech.moveLift(300),
                        new SleepAction(1))
                );
                break;
            case Right:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                //.setReversed(true)
                                .splineTo(new Vector2d(36, -34), Math.toRadians(180))
                                .build(),
                        //depositMech.dropPixels(),
                        //new SleepAction(1),
                        //depositMech.moveLift(300),
                        new SleepAction(1))
                );
                break;
            default:
                break;
        }

        // head for center lane
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        .setReversed(true)
                        .splineTo(new Vector2d(36, -12), Math.toRadians(180))
                        .setReversed(false)
                        .splineTo(new Vector2d(-52, -12), Math.toRadians(180))
                        .build());

        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        .setReversed(true)
                        .splineTo(new Vector2d(36, -12), Math.toRadians(180))
                        .build());

        // where to park?
        switch (parkLocation) {
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                        .setReversed(true)
                        .splineTo(new Vector2d(60, -14), Math.toRadians(0))
                        .build()));
                break;
            case Corner:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                        .setReversed(true)
                        .splineTo(new Vector2d(60, -60), Math.toRadians(0))
                        .build()));
                break;
            default:
                break;
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
            runPaths(ParkLocation.Corner);
        }

    }
}
