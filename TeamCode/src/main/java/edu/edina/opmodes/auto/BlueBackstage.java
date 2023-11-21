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
import edu.edina.library.enums.DropOffState;
import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftSlideState;
import edu.edina.library.enums.ParkLocation;
import edu.edina.library.enums.PickUpState;
import edu.edina.library.enums.PropLocation;
import edu.edina.library.subsystems.Claw;
import edu.edina.library.subsystems.Lift;
import edu.edina.library.util.PoCHuskyLens;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

@Autonomous
public class BlueBackstage extends LinearOpMode {
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

        Pose2d startPose = new Pose2d(8, 64, Math.toRadians(270));

        // use out version of the drive based off the hardware that we created above.
        drive = new MecanumDrive(hardware.leftFront,
                hardware.leftBack, hardware.rightBack, hardware.rightFront,
                hardware.par0, hardware.par1, hardware.perp,
                hardware.imu, hardware.voltageSensor, startPose);

        // Heartbeat Red to signify Red alliance
        pattern = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_RED;
        hardware.blinkinLedDriver.setPattern(pattern);

        // HuskyLens Init
        poCHuskyLens = new PoCHuskyLens(hardware.huskyLens, telemetry, 1);
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
                                .splineTo(new Vector2d(38,44), Math.toRadians(0))
                                .build(),
                        new SleepAction(1)));
                break;
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(38,37), Math.toRadians(0))
                                .build(),
                        new SleepAction(1)));
                break;
            case Right:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(38,28), Math.toRadians(0))
                                .build(),
                        new SleepAction(1)));
                break;
            default:
                break;
        }

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
                                .splineTo(new Vector2d(25, 34), Math.toRadians(180))
                                .build(),
                        sleep1sAction)
                );
                break;
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                //.setReversed(true)
                                .splineTo(new Vector2d(18, 26), Math.toRadians(180))
                                .build(),
                        new SleepAction(1))
                );
                break;
            case Right:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                //.setReversed(true)
                                .splineTo(new Vector2d(4, 34), Math.toRadians(180))
                                .build(),
                        new SleepAction(1))
                );
                break;
            default:
                break;
        }

        state.leftClawState = ClawState.Opened;
        claw.update();
        sleep(1000);

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

        sleep(2000);
        while (!isStarted()) {
            poCHuskyLens.update();

            propLocation = poCHuskyLens.getPropLocation();
            telemetry.addData("Location", propLocation);

            state.telemetry(telemetry, hardware);
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