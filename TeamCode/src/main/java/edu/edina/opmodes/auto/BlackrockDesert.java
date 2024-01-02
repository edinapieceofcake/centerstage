package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

import edu.edina.library.actions.roadrunner.ActionManager;
import edu.edina.library.enums.Alliance;
import edu.edina.library.enums.ParkLocation;
import edu.edina.library.enums.PropLocation;
import edu.edina.library.util.PoCHuskyLens;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.SmartGamepad;

@Autonomous
public class BlackrockDesert extends LinearOpMode {
    protected RobotHardware hardware;
    protected ActionManager manager;
    protected MecanumDrive drive;
    protected RevBlinkinLedDriver.BlinkinPattern pattern;
    protected PoCHuskyLens poCHuskyLens;
    protected PropLocation propLocation;

    protected void initHardware() {
        hardware = new RobotHardware(hardwareMap);
        manager = new ActionManager(hardware);

        drive = new MecanumDrive(hardware.leftFront,
                hardware.leftBack, hardware.rightBack, hardware.rightFront,
                hardware.par0, hardware.par1, hardware.perp,
                hardware.externalImu, hardware.voltageSensor, getStartPose());

        // uncomment this and comment out the above if it doesn't work right
        //drive = new MecanumDrive(hardwareMap, startPose);

        // Heartbeat Red to signify Red alliance
        pattern = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_GRAY;
        hardware.blinkinLedDriver.setPattern(pattern);

        PropLocation lastLocation = PropLocation.Idle;

        // HuskyLens Init
        poCHuskyLens = new PoCHuskyLens(hardware.huskyLens, telemetry, getAlliance());
        poCHuskyLens.init();

        hardware.dropServosForAutonomous();
        hardware.droneLaunchServo.setPosition(RobotConfiguration.getInstance().droneLauncherArmedPosition);

        manager.init();
        manager.start();
    }

    protected Alliance getAlliance() {
        return Alliance.Red;
    }

    protected RevBlinkinLedDriver.BlinkinPattern getUnsuccessfulPropMatchColor() {
        return RevBlinkinLedDriver.BlinkinPattern.GREEN;
    }

    protected RevBlinkinLedDriver.BlinkinPattern getSuccessfulPropMatchColor() {
        return RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_GRAY;
    }

    protected Pose2d getStartPose() {
        return new Pose2d(14.5, -64, Math.toRadians(90));
    }

    @Override
    public void runOpMode() throws InterruptedException {
        ParkLocation parkLocation = ParkLocation.Corner;
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        long delayTime = 0;

        initHardware();

        Actions.runBlocking(new ParallelAction(
                manager.closeRightClaw(),
                manager.closeLeftClaw()
        ));

        hardware.lights.setPower(1);

        while (!isStarted()) {
            pad1.update();

            telemetry.addData("Press A for corner, Y for center park", "");
            if (pad1.a) {
                parkLocation = ParkLocation.Corner;
            } else if (pad1.y) {
                parkLocation = ParkLocation.Center;
            }

            telemetry.addData("Current Park Location", parkLocation);
            telemetry.addData("Press left bumper to increase delay, right number to decrease delay.", "");
            if (pad1.left_bumper) {
                delayTime += 1000;
            } else if (pad1.right_bumper) {
                delayTime -= 1000;
            }

            telemetry.addData("Delay in seconds", delayTime / 1000);

            poCHuskyLens.update();

            // Find Prop Location
            propLocation = poCHuskyLens.getPropLocation();

            telemetry.addData("Location", propLocation);
            telemetry.update();

            // Show solid pattern if block seen, otherwise heartbeat
            if (propLocation != PropLocation.None) {
                pattern = getSuccessfulPropMatchColor();
            } else {
                pattern = getUnsuccessfulPropMatchColor();
            }

            hardware.blinkinLedDriver.setPattern(pattern);
        }

        hardware.lights.setPower(0);

        if (opModeIsActive()) {
            // Signal GREEN for successful run
            pattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
            hardware.blinkinLedDriver.setPattern(pattern);
            if (delayTime > 0) {
                sleep(delayTime);
            }

            runPaths(parkLocation);

            while (opModeIsActive()) {
                telemetry.addData("Mode executed", "");
                telemetry.update();
                idle();
            }

            pattern = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_WHITE;
            hardware.blinkinLedDriver.setPattern(pattern);
        }
    }

    protected void runPaths(ParkLocation parkLocation) {

        Vector2d propDropLocation;
        Pose2d backdropDropLocation;
        double propDropAngle = 90.0;

        // Comment out when actually using camera!!
        propLocation = PropLocation.Left;

        // Determine location for purple pixel
        switch(propLocation) {
            case Left:
                propDropLocation = new Vector2d(15.5, -43);
                propDropAngle = 135.0;
                break;
            case Center:
                propDropLocation = new Vector2d(16.5, -35.5);
                break;
            case Right:
                propDropLocation = new Vector2d(31.5, -43);
                break;
            default:
                propDropLocation = new Vector2d(16.5, -35.5);  // default to Center if all goes bad
                break;
        }

        // Determine location for yellow pixel
        switch (propLocation) {
            case Left:
                backdropDropLocation = new Pose2d(48,-32, Math.toRadians(0));
                break;
            case Center:
                backdropDropLocation = new Pose2d(48,-38, Math.toRadians(0));
                break;
            case Right:
                backdropDropLocation = new Pose2d(48,-47, Math.toRadians(0));
                break;
            default:
                backdropDropLocation = new Pose2d(48,-38, Math.toRadians(0)); // default to center if all goes bad
                break;
        }

        switch (propLocation) {
            case Right:
                // Execute drive to prop drop spot and drop
                Actions.runBlocking(
                        new SequentialAction(
                                drive.actionBuilder(drive.pose)
                                        .lineToY(-50)
                                        .splineTo(propDropLocation, Math.toRadians(propDropAngle))
                                        .build(),
                                manager.openLeftClaw()
                        )
                );
                break;
            default:
                // Execute drive to prop drop spot and drop
                Actions.runBlocking(
                        new SequentialAction(
                                drive.actionBuilder(drive.pose)
                                        .splineTo(propDropLocation, Math.toRadians(propDropAngle))
                                        .build(),
                                manager.openLeftClaw()
                        )
                );
                break;
        }

        // Drive to backdrop
        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                drive.actionBuilder(drive.pose)
                                        .setReversed(true)
                                        .waitSeconds(.5)
                                        .splineToSplineHeading(backdropDropLocation, Math.toRadians(0))
                                        .lineToX(53.5)
                                        .build(),
                                new SequentialAction(
                                        manager.getLiftReadyToDropThePixelLowOnTheWall()
                                )
                        ),
                        manager.openRightClaw()
                )
        );

        // back away and pack up
        Actions.runBlocking(
                new ParallelAction(
                        drive.actionBuilder(drive.pose)
                                .lineToX(52)
                                .build(),
                        manager.getLiftReadyToDrive()
                )
        );

        // drive to stack - 1st trip
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Head to Stacks VIA A-Row
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(20, -11, Math.toRadians(-180)), Math.toRadians(180))
                        .setReversed(false)
                        .splineTo(new Vector2d(-44, -11), Math.toRadians(180))
                        .build());

        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                manager.runLiftToPosition(-163),
                                manager.positionTheClawToPickupPixels()
                        ),
                        drive.actionBuilder(drive.pose)
                                .lineToX(-51.25)
                                .build(),
                        new ParallelAction(
                                manager.closeAutoClaw(),
                                manager.closeLeftClaw()
                        ),
                        manager.raiseLiftAfterStackPickup()
                )
        );

        // drive to backstage - 1st trip
        Actions.runBlocking(
                new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .lineToX(-44)
                                .afterDisp(0,
                                        new ParallelAction(
                                                manager.lowerLiftForDriving(),
                                                manager.zeroLift(),
                                                manager.positionTheClawToDriveWithPixels()
                                        ))
                                .splineToSplineHeading(new Pose2d(-11, -11, Math.toRadians(0)), Math.toRadians(0))
                                .setReversed(false)
                                .splineTo(new Vector2d(40, -12), Math.toRadians(0))
                                .splineTo(new Vector2d(64,-14), Math.toRadians(0))
                                .build(),
                        new ParallelAction(
                            manager.openAutoClaw(),
                            manager.openLeftClaw()
                        )
                )
        );

        // drive to stack - 2nd trip
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Head to Stacks VIA A-Row
                        .lineToX(60)
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(24, -16, Math.toRadians(-180)), Math.toRadians(-180))
                        .setReversed(false)
                        .splineTo(new Vector2d(-44, -11), Math.toRadians(180))
                        .build());

        // Extend and pick up two pixels
        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                manager.runLiftToPosition(-90),
                                manager.positionTheClawToPickupPixels()
                        ),
                        drive.actionBuilder(drive.pose)
                                .lineToX(-53)
                                .build(),
                        new ParallelAction(
                                manager.closeAutoClaw(),
                                manager.closeLeftClaw()
                        ),
                        manager.raiseLiftAfterStackPickup()
                )
        );

        // drive to backstage - 2nd trip
        Actions.runBlocking(
                new SequentialAction(
                    drive.actionBuilder(drive.pose)
                            // Head to Stacks VIA A-Row
                            .lineToX(-44)
                            .afterDisp(0,
                                    new ParallelAction(
                                            manager.lowerLiftForDriving(),
                                            manager.zeroLift(),
                                            manager.positionTheClawToDriveWithPixels()
                                    ))
                            .splineToSplineHeading(new Pose2d(-12, -14, Math.toRadians(0)), Math.toRadians(0))
                            .setReversed(false)
                            .splineTo(new Vector2d(40, -14), Math.toRadians(0))
                            .splineTo(new Vector2d(64,-14), Math.toRadians(-45))
                            .build(),
                    new ParallelAction(
                            manager.openAutoClaw(),
                            manager.openLeftClaw()
                    )
                )
        );
    }
}