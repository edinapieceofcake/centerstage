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

import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.ParkLocation;
import edu.edina.library.enums.PropLocation;
import edu.edina.library.subsystems.Claw;
import edu.edina.library.subsystems.Lift;
import edu.edina.library.util.PoCHuskyLens;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

@Autonomous
public class RedAudienceWithBackStage extends LinearOpMode {
    private RobotHardware hardware;
    private Claw claw;
    private Lift lift;
    protected MecanumDrive drive;
    RevBlinkinLedDriver.BlinkinPattern pattern;
    PoCHuskyLens poCHuskyLens;
    PropLocation propLocation;

    double delta1 = 9;

    private SleepAction sleep1sAction = new SleepAction(1);


    protected void initHardware() {
        // test hardware construction and use in an empty action
        hardware = new RobotHardware(hardwareMap);

        Pose2d startPose = new Pose2d(-42, -64, Math.toRadians(90));

        // use out version of the drive based off the hardware that we created above.
        drive = new MecanumDrive(hardware.leftFront,
                hardware.leftBack, hardware.rightBack, hardware.rightFront,
                hardware.par0, hardware.par1, hardware.perp,
                hardware.imu, hardware.voltageSensor, startPose);

        // Heartbeat Red to signify Red alliance
        pattern = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_RED;
        hardware.blinkinLedDriver.setPattern(pattern);

        // HuskyLens Init
        poCHuskyLens = new PoCHuskyLens(hardware.huskyLens, telemetry, 2);
        poCHuskyLens.init();

        claw = new Claw(hardware);
        lift = new Lift(hardware, false);
        hardware.dropServosForAutonomous();
    }

    protected void runPaths(ParkLocation parkLocation) {
        RobotState state = RobotState.getInstance();

        switch(propLocation) {
            case Left:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(46, -29), Math.toRadians(180))
                                .build(),
                        new SleepAction(1)));
                break;
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(46, -35.5), Math.toRadians(90))
                                .build(),
                        new SleepAction(1)));
                break;
            case Right:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(46, -42), Math.toRadians(0))
                                .build(),
                        new SleepAction(1)));
                break;
            default:
                break;
        }

        // place purple on the ground
        state.leftClawState = ClawState.Opened;
        claw.update();
        sleep(1000);

        // where to put the yellow pixel?
        switch (propLocation) {
            case Left:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(-30, -10), Math.toRadians(0))
                                .splineTo(new Vector2d(28, -10), Math.toRadians(0))
                                .splineTo(new Vector2d(38, -35), Math.toRadians(180))
                                .build(),
                        sleep1sAction)
                );
                break;
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                //.splineTo(new Vector2d(-30, -57), Math.toRadians(0))
                                //.splineTo(new Vector2d(0, -57), Math.toRadians(0))
                                //.splineTo(new Vector2d(40, -57), Math.toRadians(0))
                                //.splineTo(new Vector2d(45, -40), Math.toRadians(180))
                                // run down the middle if we can fix the drift
//                                .setReversed(true)
                                .splineTo(new Vector2d(-55, -35), Math.toRadians(90))
                                .splineTo(new Vector2d(-45, -10), Math.toRadians(0))
                                .splineTo(new Vector2d(28, -10), Math.toRadians(0))
                                .splineTo(new Vector2d(38, -40), Math.toRadians(180))
                                .build(),
                        new SleepAction(1))
                );
                break;
            case Right:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(-30, -55), Math.toRadians(0))
                                .splineTo(new Vector2d(0, -55), Math.toRadians(0))
                                .splineTo(new Vector2d(40, -55), Math.toRadians(0))
                                .splineTo(new Vector2d(45, -40), Math.toRadians(180))
                                // run down the middle if we can fix the drift
//                                .setReversed(true)
//                                .splineTo(new Vector2d(-35, -10), Math.toRadians(0))
//                                .splineTo(new Vector2d(28, -10), Math.toRadians(0))
//                                .splineTo(new Vector2d(38, -45), Math.toRadians(180))
                                .build(),
                        new SleepAction(1))
                );
                break;
            default:
                break;
        }

        Actions.runBlocking(new SequentialAction(
                drive.actionBuilder(drive.pose)
                        .turnTo(Math.toRadians(0))
                        .build()));

        /*
        state.lastKnownLiftState = LiftDriveState.Drive;
        state.currentLiftDriveState = LiftDriveState.LowDropOff;
        state.currentLiftSlideState = LiftSlideState.Extending;
        state.dropOffState = DropOffState.Start;

        while (state.dropOffState != DropOffState.Finished) {
            lift.update();
            claw.update();
            idle();
        }

        state.telemetry(telemetry, hardware);
        telemetry.update();

        Actions.runBlocking(drive.actionBuilder(drive.pose).lineToX(44).build());

        state.rightClawState = ClawState.Opened;
        claw.update();
        sleep(2000);

        Actions.runBlocking(drive.actionBuilder(drive.pose).lineToX(38).build());

        state.pickUpState = PickUpState.Start;
        state.lastKnownLiftState = LiftDriveState.LowDropOff;
        state.currentLiftDriveState = LiftDriveState.Drive;
        state.currentLiftSlideState = LiftSlideState.Retracting;

        while (state.pickUpState != PickUpState.Finished) {
            lift.update();
            claw.update();
            idle();
        }

        state.lastKnownLiftState = LiftDriveState.Drive;

        // where to park?
        switch (parkLocation) {
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(54, 14), Math.toRadians(0))
                                .build()));
                break;
            case Corner:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(54, 60), Math.toRadians(0))
                                .build()));
                break;
            default:
                break;
        }
         */
    }

    @Override
    public void runOpMode() throws InterruptedException {
        RobotState state = RobotState.getInstance();
        initHardware();

        claw.init();
        claw.start();
        lift.init();
        lift.start();

        state.leftClawState = ClawState.Closed;
        state.rightClawState = ClawState.Closed;
        claw.update();

        while (!isStarted()) {
            poCHuskyLens.update();

            propLocation = PropLocation.Center;
            telemetry.addData("Location", propLocation);

            telemetry.update();
            sleep(2000);
        }

        if (opModeIsActive()) {
            // Signal GREEN for successful run
            pattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
            hardware.blinkinLedDriver.setPattern(pattern);

            runPaths(ParkLocation.Corner);
        }

    }
}