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
public class RedAudienceCenter extends LinearOpMode {
    protected RobotHardware hardware;
    protected ActionManager manager;
    protected MecanumDrive drive;
    protected RevBlinkinLedDriver.BlinkinPattern pattern;
    protected PoCHuskyLens poCHuskyLens;
    protected PropLocation propLocation = PropLocation.Center;

    private boolean makeSecondTrip = false;
    private boolean yellowPixel = false;
    private boolean dropOnBackdrop = false;
    private boolean dropOnBackstage = false;
    private boolean useCamera = false;

    private ParkLocation parkLocation = ParkLocation.None;

    protected void initHardware() {
        hardware = new RobotHardware(hardwareMap);
        manager = new ActionManager(hardware);

        drive = new MecanumDrive(hardware.leftFront,
                hardware.leftBack, hardware.rightBack, hardware.rightFront,
                hardware.par0, hardware.par1, hardware.perp,
                hardware.externalImu, hardware.expansionImu, hardware.voltageSensor, getStartPose());

        // uncomment this and comment out the above if it doesn't work right
        //drive = new MecanumDrive(hardwareMap, startPose);

        // Heartbeat Red to signify Red alliance
        pattern = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_GRAY;
        hardware.blinkinLedDriver.setPattern(pattern);

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
        return new Pose2d(-42, -64, Math.toRadians(90));
    }

    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        long delayTime = 0;

        initHardware();

        hardware.lights.setPower(1);

        while (!isStarted()) {
            pad1.update();

            telemetry.addData("A for P only", "");
            telemetry.addData("X for P, Y, 1 W and park in corner", "");
            telemetry.addData("Y for P, Y, 1 W and park in center", "");
            telemetry.addData("dpad up for P, Y, 3 Ws on backdrop park in front", "");
            telemetry.addData("dpad down for P, Y, 3 Ws and park in corner", "");
            telemetry.addData("left bumper to increase delay, right bumper to decrease delay.", "");
            telemetry.addData("left trigger to close claws, right trigger to open", "");

            if (pad1.a) {
                yellowPixel = false;
                makeSecondTrip = false;
                dropOnBackdrop = false;
                dropOnBackstage = false;
                parkLocation = ParkLocation.None;
            }

            if (pad1.x) {
                parkLocation = ParkLocation.Corner;
                yellowPixel = true;
                makeSecondTrip = false;
                dropOnBackdrop = false;
                dropOnBackstage = false;
            }

            if (pad1.y) {
                parkLocation = ParkLocation.Center;
                yellowPixel = true;
                makeSecondTrip = false;
                dropOnBackdrop = false;
                dropOnBackstage = false;
            }

            if (pad1.dpad_up) {
                yellowPixel = true;
                makeSecondTrip = true;
                dropOnBackdrop = true;
                dropOnBackstage = false;
                parkLocation = ParkLocation.None;
            }

            if (pad1.dpad_down) {
                yellowPixel = true;
                makeSecondTrip = true;
                dropOnBackdrop = false;
                dropOnBackstage = true;
                parkLocation = ParkLocation.None;
            }

            if (pad1.left_bumper) {
                delayTime += 1000;
            } else if (pad1.right_bumper) {
                delayTime -= 1000;
            }

            if (gamepad1.left_trigger != 0) {
                Actions.runBlocking(new ParallelAction(
                        manager.closeRightClaw(),
                        manager.closeLeftClaw()
                ));
            }

            if (gamepad1.right_trigger != 0) {
                Actions.runBlocking(new ParallelAction(
                        manager.openRightClaw(),
                        manager.openLeftClaw()
                ));
            }

            poCHuskyLens.update();

            if (useCamera) {
                // Find Prop Location
                propLocation = poCHuskyLens.getPropLocation();
            } else {
                if (pad1.left_stick_button) {
                    if (propLocation == PropLocation.Left) {
                        propLocation = PropLocation.Center;
                    } else if (propLocation == PropLocation.Center) {
                        propLocation = PropLocation.Right;
                    } else if (propLocation == PropLocation.Right) {
                        propLocation = PropLocation.Left;
                    }
                }
            }

            telemetry.addData("==========================", "");
            telemetry.addData("Drop Yellow Pixel", yellowPixel);
            telemetry.addData("Make Second Trip", makeSecondTrip);
            telemetry.addData("Current Park Location", parkLocation);
            telemetry.addData("Drop on backdrop", dropOnBackdrop);
            telemetry.addData("Drop on center", dropOnBackstage);
            telemetry.addData("Delay in seconds", delayTime / 1000);
            telemetry.addData("Location", propLocation);
            telemetry.addData("Use Camera", useCamera);
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

            runPaths();

            pattern = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_WHITE;
            hardware.blinkinLedDriver.setPattern(pattern);
        }
    }

    protected void runPaths() {
        Vector2d propDropLocation;
        Pose2d backdropDropLocation;
        Pose2d secondBackdropDropLocation;

        // Determine location for purple pixel
        switch(propLocation) {
            case Left:
                propDropLocation = new Vector2d(-46, -40);
                break;
            case Center:
                propDropLocation = new Vector2d(-30, -36);
                break;
            case Right:
                propDropLocation = new Vector2d(-30, -37);
                break;
            default:
                propDropLocation = new Vector2d(-38, -33);  // default to Center if all goes bad
                break;
        }

        // Determine location for yellow pixel
        switch (propLocation) {
            case Left:
                backdropDropLocation = secondBackdropDropLocation = new Pose2d(50,-32, Math.toRadians(0));
                break;
            case Center:
                backdropDropLocation = secondBackdropDropLocation = new Pose2d(50,-38, Math.toRadians(0));
                break;
            case Right:
                backdropDropLocation = new Pose2d(50,-47, Math.toRadians(0));
                secondBackdropDropLocation = new Pose2d(50,-40, Math.toRadians(0));
                break;
            default:
                backdropDropLocation = secondBackdropDropLocation = new Pose2d(50,-38, Math.toRadians(0)); // default to center if all goes bad
                break;
        }

        switch (propLocation) {
            case Right:
                Actions.runBlocking(
                        new SequentialAction(
                                drive.actionBuilder(drive.pose)
                                        .splineTo(propDropLocation, Math.toRadians(45))
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
                                        .splineTo(propDropLocation, Math.toRadians(90))
                                        .build(),
                                manager.openLeftClaw()
                        )
                );
                break;
        }

        if (yellowPixel) {
            if (propLocation == PropLocation.Left) {
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(-35, -11, Math.toRadians(180)), Math.toRadians(90))
                                .build()
                );
            } else if (propLocation == PropLocation.Center) {
                // Drive to Stack Pick up 1st white
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(-48, -18, Math.toRadians(180)), Math.toRadians(90))
                                .splineToSplineHeading(new Pose2d(-48, -11, Math.toRadians(180)), Math.toRadians(90))
                                .build()
                );
            } else {
                // Drive to Stack Pick up 1st white
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(-48, -11, Math.toRadians(180)), Math.toRadians(90))
                                .build()
                );
            }

            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    manager.runLiftToPosition(-245),
                                    manager.positionTheClawToPickupPixels()
                            ),
                            drive.actionBuilder(drive.pose)
                                    // Head to Stacks
                                    .lineToX(-55.5)
                                    .build(),
                            manager.closeLeftClaw(),
                            new SleepAction(.2),
                            manager.raiseLiftAfterStackPickup()
                    )
            );

            // drive to backstage - 1st trip
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            // Head to Stacks VIA A-Row
                            .lineToX(-48)
                            .afterDisp(0,
                                    new ParallelAction(
                                            manager.lowerLiftForDriving(),
                                            manager.zeroLift(),
                                            manager.positionTheClawToDriveWithPixels()
                                    ))
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(new Vector2d(-35, -11), Math.toRadians(0)), Math.toRadians(0))
                            .splineTo(new Vector2d(10, -11), Math.toRadians(0))
                            .afterDisp(0, manager.getLiftReadyToDropThePixelHighOnTheWall())
                            .splineToSplineHeading(backdropDropLocation, Math.toRadians(0))
                            .afterDisp(0, new SequentialAction(
                                    manager.openRightClaw(),
                                    new SleepAction(0.25),
                                    manager.openLeftClaw()
                            ))
                            .build()
            );

            if (!makeSecondTrip) {
                // back away and pack up
                Actions.runBlocking(
                        new ParallelAction(
                                drive.actionBuilder(drive.pose)
                                        .lineToX(44)
                                        .build(),
                                new SequentialAction(
                                        new SleepAction(.2),
                                        manager.getLiftReadyToDrive()
                                )
                        )
                );
            }
        }

        if (makeSecondTrip) {
            // go get other white pixels
            Actions.runBlocking(
                    new ParallelAction(
                            new SequentialAction(
                                    new SleepAction(.2),
                                    manager.getLiftReadyToDrive()
                            ),
                            drive.actionBuilder(drive.pose)
                                    .lineToX(44)
                                    .setReversed(true)
                                    .splineToSplineHeading(new Pose2d(10, -11, Math.toRadians(-180)), Math.toRadians(180))
                                    .splineTo(new Vector2d(-52, -11), Math.toRadians(180))
                                    .build()
                    )
            );

            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    manager.positionTheClawToPickupPixels(),
                                    manager.runLiftToPosition(-145)
                            ),
                            drive.actionBuilder(drive.pose)
                                    // Head to Stacks
                                    .lineToX(-60)
                                    .build(),
                            new ParallelAction(
                                    manager.closeLeftClaw(),
                                    manager.closeAutoClaw()
                            ),
                            new SleepAction(.2)
                    )
            );

            if (dropOnBackdrop) {
                // drive to backstage - 2nd trip
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks VIA A-Row
                                .lineToX(-48)
                                .afterDisp(0,
                                        new ParallelAction(
                                                manager.zeroLift(),
                                                manager.positionTheClawToDriveWithPixels()
                                        ))
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -11), Math.toRadians(0)), Math.toRadians(0))
                                .splineTo(new Vector2d(10, -11), Math.toRadians(0))
                                .afterDisp(0, manager.getLiftReadyToDropThePixelHighOnTheWall())
                                .splineToSplineHeading(secondBackdropDropLocation, Math.toRadians(0))
                                .afterDisp(0, new SequentialAction(
                                        manager.openAutoClaw(),
                                        manager.openLeftClaw()
                                ))
                                .build());

                // back away and pack up
                Actions.runBlocking(
                        new SequentialAction(
                                drive.actionBuilder(drive.pose)
                                        .lineToX(44)
                                        .build(),
                                manager.getLiftReadyToDrive()
                        )
                );
            }

            if (dropOnBackstage) {
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks VIA A-Row
                                .lineToX(-48)
                                .afterDisp(0,
                                        new ParallelAction(
                                                manager.lowerLiftForDriving(),
                                                manager.zeroLift(),
                                                manager.positionTheClawToDriveWithPixels()
                                        ))
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -11), Math.toRadians(0)), Math.toRadians(0))
                                .splineTo(new Vector2d(54, -11), Math.toRadians(0))
                                .afterDisp(0, new SequentialAction(
                                        manager.openAutoClaw(),
                                        manager.openLeftClaw()
                                ))
                                .lineToX(50)
                                .build());

            }
        }

        // park
        switch (parkLocation) {
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(58, -14), Math.toRadians(0))
                                .build()));
                break;
            case Corner:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(58, -64), Math.toRadians(0))
                                .build()));
                break;
            default:
                break;
        }
    }
}