package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.PoCMecanumDrive;

import edu.edina.library.actions.roadrunner.ActionManager;
import edu.edina.library.enums.Alliance;
import edu.edina.library.enums.ParkLocation;
import edu.edina.library.enums.PropLocation;
import edu.edina.library.util.PoCHuskyLens;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.SmartGamepad;

@Autonomous
//@Disabled
public class RedAudienceCenter extends LinearOpMode {
    protected RobotHardware hardware;
    protected ActionManager manager;
    protected PoCMecanumDrive drive;
    protected PoCHuskyLens poCHuskyLens;
    protected PropLocation propLocation = PropLocation.Center;

    private boolean makeSecondTrip = false;
    private boolean yellowPixel = false;
    private boolean dropOnBackdrop = false;
    private boolean dropOnBackstage = false;
    private boolean useCamera = true;

    private ParkLocation parkLocation = ParkLocation.None;

    private long delayTime = 0;

    protected void initHardware() {
        hardware = new RobotHardware(hardwareMap);
        manager = new ActionManager(hardware);

        drive = new PoCMecanumDrive(hardware.leftFront,
                hardware.leftBack, hardware.rightBack, hardware.rightFront,
                hardware.par0, hardware.par1, hardware.perp,
                hardware.externalImu, hardware.expansionImu, hardware.voltageSensor, hardware.beamBreak, getStartPose());

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
        return RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_WHITE;
    }

    protected Pose2d getStartPose() {
        return new Pose2d(-42, -64, Math.toRadians(90));
    }

    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);

        initHardware();

        Actions.runBlocking(manager.positionTheClawToPickupPixels());

        // Turn on prop illumination
        hardware.lights.setPower(1);

        while (!isStarted()) {
            pad1.update();

            telemetry.addData("A for P only", "");
            telemetry.addData("X for P, Y, 1W and park in corner", "");
            telemetry.addData("Y for P, Y, 1W and park in center", "");
            telemetry.addData("DPAD-UP for P, Y, 3Ws on backdrop park in front", "");
            telemetry.addData("DPAD-DN for P, Y, 3Ws and park in center", "");
            telemetry.addData("L-BUMPER to increase delay, R-BUMPER to decrease delay.", "");
            telemetry.addData("L-TRIGGER to close claws, L-TRIGGER to open", "");
            telemetry.addData("LEFT-STICK-DOWN : manual rotate prop position", "");
            telemetry.addData("RIGHT-STICK-DOWN : manual or auto camera", "");

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

            // Delay - Max of 4000ms, Min of 0ms
            if (pad1.left_bumper) {
                delayTime += (delayTime > 3000) ? 0 : 1000;
            } else if (pad1.right_bumper) {
                delayTime -= (delayTime < 1000) ? 0 : 1000;
            }

            // If we have ANY delay, don't allow second trip
            makeSecondTrip = (delayTime > 0) ? false : makeSecondTrip;

            // Close the claws
            if (gamepad1.left_trigger != 0) {
                Actions.runBlocking(
                        new SequentialAction(
                                new ParallelAction(
                                        manager.closeRightClaw(),
                                        manager.closeLeftClaw(),
                                        manager.openAutoClaw()
                                ),
                                manager.positionTheClawToDriveWithPixels())
                );
            }

            // Open the claws
            if (gamepad1.right_trigger != 0) {
                Actions.runBlocking(new ParallelAction(
                        manager.openRightClaw(),
                        manager.openLeftClaw(),
                        manager.openAutoClaw(),
                        manager.positionTheClawToPickupPixels()
                ));
            }

            // Select whether camera is live or not
            if (pad1.right_stick_button) {
                if (useCamera) {
                    useCamera = false;
                } else {
                    useCamera = true;
                }
            }

            // Find Prop Location
            if (useCamera) {
                // Find Prop Location
                poCHuskyLens.update();
                propLocation = poCHuskyLens.getPropLocation();
                if (propLocation == PropLocation.None) {
                    propLocation = PropLocation.Right;
                }
            } else {
                if (pad1.left_stick_button) {
                    switch (propLocation) {
                        case None:
                            propLocation = PropLocation.Left;
                            break;
                        case Left:
                            propLocation = PropLocation.Center;
                            break;
                        case Center:
                            propLocation = PropLocation.Right;
                            break;
                        case Right:
                            propLocation = PropLocation.Left;
                            break;
                        case Idle:
                            propLocation = PropLocation.Left;
                            break;
                        default:
                            propLocation = PropLocation.Center;
                            break;
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
        }

        // Turn off prop lighting
        hardware.lights.setPower(0);

        if (opModeIsActive()) {

            hardware.startCurrentMonitor();

            runPaths();

            hardware.stopCurrentMonitor();
        }
    }

    protected void runPaths() {
        Vector2d propDropLocation;
        Vector2d backdropDropLocation;

        // Determine location for purple pixel
        switch(propLocation) {
            case Left:
                propDropLocation = new Vector2d(-40, -38);
                backdropDropLocation = new Vector2d(49,-32);
                break;
            case Right:
                propDropLocation = new Vector2d(-30, -38);
                backdropDropLocation = new Vector2d(49.5,-45);
                break;
            case Center:
            default:
                propDropLocation = new Vector2d(-31, -33);
                backdropDropLocation = new Vector2d(49,-39);
                break;
        }

        // Run to drop PURPLE pixel
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
            case Left:
                Actions.runBlocking(
                        new SequentialAction(
                                drive.actionBuilder(drive.pose)
                                        .splineTo(propDropLocation, Math.toRadians(135))
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

        // If we want to drop Yellow..
        if (yellowPixel) {
            if (propLocation == PropLocation.Left) {
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(-35, -12.5, Math.toRadians(180)), Math.toRadians(45))
                                .build()
                );
            } else if (propLocation == PropLocation.Center) {
                // Drive to Stack Pick up 1st white
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(-48, -18, Math.toRadians(180)), Math.toRadians(90))
                                .splineToSplineHeading(new Pose2d(-48, -10, Math.toRadians(180)), Math.toRadians(90))
                                .build()
                );
            } else {
                // Drive to Stack Pick up 1st white
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(-48, -10, Math.toRadians(180)), Math.toRadians(90))
                                .build()
                );
            }

            drive.turnBeamBreakOn();

            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    manager.runLiftToPosition(-210),
                                    manager.positionTheClawToPickupPixels()
                            ),
                            drive.actionBuilder(drive.pose)
                                    // Head to Stacks
                                    .lineToX(-58)
                                    .stopAndAdd(manager.closeLeftClaw())
                                    .lineToX(-57.5)
                                    .stopAndAdd(manager.raiseLiftAfterStackPickup())
                                    .build()
                    )
            );

            drive.turnBeamBreakOff();
            drive.turnErrorPoseStopOn();

            // Check to see if there is delay - if so, run special version with wait during return
            if (delayTime > 0) {  // Yes, there's a delay
                // drive to backstage - 1st trip with delay at center field
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks VIA C-Row
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
                                .waitSeconds(delayTime/1000)
                                .afterDisp(0, manager.getLiftReadyToDropThePixelHighOnTheWall())
                                .splineToConstantHeading(backdropDropLocation, Math.toRadians(0))
                                .lineToX(51.5)
                                .afterDisp(0, new SequentialAction(
                                        manager.openRightClaw(),
                                        new SleepAction(0.25),
                                        manager.openLeftClaw()
                                ))
                                .build()
                );
            } else {
                // drive to backstage - 1st trip - no delay
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks VIA C-Row
                                .lineToX(-48)
                                .afterDisp(0,
                                        new ParallelAction(
                                                manager.lowerLiftForDriving(),
                                                manager.zeroLift(),
                                                manager.positionTheClawToDriveWithPixels()
                                        ))
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -11), Math.toRadians(0)), Math.toRadians(0))
                                .afterDisp(25, manager.getLiftReadyToDropThePixelHighOnTheWall())
                                .splineToConstantHeading(new Vector2d(10, -11), Math.toRadians(0))
                                .splineToConstantHeading(backdropDropLocation, Math.toRadians(0))
                                .lineToX(51.5)
                                .afterDisp(0, new SequentialAction(
                                        manager.openRightClaw(),
                                        new SleepAction(0.25),
                                        manager.openLeftClaw()
                                ))
                                .build()
                );
            }

            // If we're done making stack trips
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

        // If we are making a second trip to the stacks
        if (makeSecondTrip) {
            // go get other white pixels
            if ((propLocation == PropLocation.Left || propLocation == PropLocation.Center) && dropOnBackdrop) {
                dropOnBackdrop = false;
                dropOnBackstage = true;
            }

            Actions.runBlocking(
                    new ParallelAction(
                            new SequentialAction(
                                    new SleepAction(.2),
                                    manager.getLiftReadyToDrive()
                            ),
                            drive.actionBuilder(drive.pose)
                                    .lineToX(44)
                                    .setReversed(true)
                                    .splineToSplineHeading(new Pose2d(10, -11.5, Math.toRadians(-180)), Math.toRadians(180))
                                    .splineToConstantHeading(new Vector2d(-54, -11.5), Math.toRadians(180))
                                    .build()
                    )
            );

            // Reach out, grab pixels, close the claws
            drive.turnBeamBreakOn();

            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    manager.positionTheClawToPickupPixels(),
                                    manager.runLiftToPosition(-125)
                            ),
                            drive.actionBuilder(drive.pose)
                                    // Head to Stacks
                                    .lineToX(-62)
                                    .build(),
                            new ParallelAction(
                                    manager.closeLeftClaw(),
                                    manager.closeAutoClaw()
                            ),
                            new SleepAction(0.2)
                    )
            );

            drive.turnBeamBreakOff();

            // If we're going to drop on the background
            if (dropOnBackdrop) {
                // drive to backstage - 2nd trip
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
                                .afterDisp(0, manager.getLiftReadyToDropPixelFromLeft())
                                .splineTo(new Vector2d(48, -19), Math.toRadians(-25))
                                .stopAndAdd(manager.openLeftClaw())
                                .afterTime(0.1, manager.openAutoClaw())
                                .waitSeconds(0.25)
                                .build());

                // back away and pack up
                Actions.runBlocking(
                        new SequentialAction(
                                drive.actionBuilder(drive.pose)
                                        .lineToX(43)
                                        .build(),
                                manager.getLiftReadyToDrive()
                        )
                );
            }

            // If we are dropping on Backstage
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
                                .splineTo(new Vector2d(10, -11), Math.toRadians(0))
                                .splineTo(new Vector2d(48, -12), Math.toRadians(0))
                                .stopAndAdd(manager.openLeftClaw())
                                .afterTime(0.1, manager.openAutoClaw())
                                .waitSeconds(0.25)
                                .lineToX(44)
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
                                .splineTo(new Vector2d(58, -60), Math.toRadians(0))
                                .build()));
                break;
            default:
                break;
        }
    }
}