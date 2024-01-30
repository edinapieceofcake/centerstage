package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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
public class RedAudienceCenterNew extends LinearOpMode {
    protected RobotHardware hardware;
    protected ActionManager manager;
    protected PoCMecanumDrive drive;
    protected PoCHuskyLens poCHuskyLens;

    protected PropLocation propLocation = PropLocation.Center;
    private ParkLocation parkLocation = ParkLocation.Center;

    private boolean useCamera = true;

    private boolean makeSecondTrip = false;
    private boolean yellowPixel = false;

    private long delayTime = 0;

    private boolean dropOnBackdrop = false;
    private boolean dropOnBackstage = false;

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

    protected Pose2d getStartPose() {
        return new Pose2d(-42, -64, Math.toRadians(90));
    }

    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);

        initHardware();

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

            // Close claws
            if (gamepad1.left_trigger != 0) {
                Actions.runBlocking(new ParallelAction(
                        manager.closeRightClaw(),
                        manager.closeLeftClaw()
                ));
            }

            // Open claws
            if (gamepad1.right_trigger != 0) {
                Actions.runBlocking(new ParallelAction(
                        manager.openRightClaw(),
                        manager.openLeftClaw()
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
        Pose2d propDropLocation;
        Vector2d backdropLocation;
        double propDropAngle = 90;
        double stackTangent = 180;

        // Determine location for purple pixel
        switch(propLocation) {
            case Left:
                propDropLocation = new Pose2d(-39, -38, Math.toRadians(135));
                propDropAngle = 135;
                stackTangent = 45;
                backdropLocation = new Vector2d(49,-36);
                break;
            case Center:
                propDropLocation = new Pose2d(-30, -36, Math.toRadians(90));
                propDropAngle = 90;
                stackTangent = 180;
                backdropLocation = new Vector2d(49,-43);
                break;
            case Right:
                propDropLocation = new Pose2d(-30, -37, Math.toRadians(45));
                propDropAngle = 65;
                stackTangent = 180;
                backdropLocation = new Vector2d(49.5,-46);
                break;
            default:
                propDropLocation = new Pose2d(-38, -33, Math.toRadians(90));  // default to Center if all goes bad
                backdropLocation = new Vector2d(49,-38); // default to center if all goes bad
                break;
        }

        // Drop the Purple
        purplePath(propDropLocation, propDropAngle);  // A

        if (yellowPixel || makeSecondTrip) {
            purpleToStack(stackTangent);
        }

        if (makeSecondTrip) {
            purpleToStack(stackTangent); // B
            stackToYellowToStack(backdropLocation); // D
            stackToAngleDrop(); // E
        } else {
            if (yellowPixel) {
                stackToYellow(backdropLocation); // C
            }
        }

        // Park
        park(parkLocation); // PK

    }

    // PATH TO GO FROM START TO PURPLE DROP
    // (A)
    void purplePath(Pose2d propLocation, double propAngle) {
        // Run to drop PURPLE pixel
        Actions.runBlocking(
            drive.actionBuilder(drive.pose)
                .splineToSplineHeading(propLocation, Math.toRadians(propAngle))
                .endTrajectory()
                .stopAndAdd(manager.openLeftClaw())
                .build()
        );
    }

    // PATH TO GO FROM PURPLE DROP TO STACK
    // (B)
    void purpleToStack(double stackTangent) {
        Actions.runBlocking(
            drive.actionBuilder(drive.pose)
                .splineToSplineHeading(new Pose2d(-50, -11, Math.toRadians(180)), Math.toRadians(stackTangent))

                // Prepare for grabbing - Trip 1
                .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                .afterDisp(0, manager.runLiftToPosition(-123))
                .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                .lineToX(-53)
                .build()
        );
    }

    // PATH TO GO FROM STACK TO BACKDROP TO DROP YELLOW
    // (C)
    void stackToYellow(Vector2d backdropLocation) {
        // Run to backdrop.
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Back away and pack up
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))

                        // Move in and grab pixels until beam break
                        .afterTime(0, manager.closeAutoClaw())
                        .afterTime(0, manager.closeLeftClaw())
                        .afterTime(0, manager.closeRightClaw())

                        .lineToX(-56.5)
                        .stopAndAdd(manager.raiseLiftAfterStackPickup())
                        .waitSeconds(delayTime/1000) // Optional Wait
                        .lineToX(-53)
                        .afterDisp(3, manager.lowerLiftForDriving())
                        .afterDisp(3, manager.zeroLift())
                        .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                        // Return to backdrop and angle drop
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, -11), Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(25, manager.getLiftReadyToDropThePixelHighOnTheWall())
                        .splineToConstantHeading(new Vector2d(10, -11), Math.toRadians(0))
                        .splineToConstantHeading(backdropLocation, Math.toRadians(0))
                        .stopAndAdd(manager.openRightClaw())
                        .afterTime(0.25, manager.openLeftClaw())
                        .waitSeconds(0.25)
        );
    }

    // STACK TO YELLOW TO STACK
    // (D)
    void stackToYellowToStack(Vector2d backdropLocation) {
        // Run to backdrop.
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Back away and pack up
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))

                        // Move in and grab pixels until beam break
                        .afterTime(0, manager.closeAutoClaw())
                        .afterTime(0, manager.closeLeftClaw())
                        .afterTime(0, manager.closeRightClaw())

                        .lineToX(-56.5)
                        .stopAndAdd(manager.raiseLiftAfterStackPickup())
                        .waitSeconds(delayTime/1000)
                        .lineToX(-53)
                        .afterDisp(3, manager.lowerLiftForDriving())
                        .afterDisp(3, manager.zeroLift())
                        .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                        // Return to backdrop and angle drop
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, -11), Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(25, manager.getLiftReadyToDropThePixelHighOnTheWall())
                        .splineToConstantHeading(new Vector2d(10, -11), Math.toRadians(0))
                        .splineToConstantHeading(backdropLocation, Math.toRadians(0))
                        .stopAndAdd(manager.openRightClaw())
                        .afterTime(0.25, manager.openLeftClaw())
                        .waitSeconds(0.25)

                        // Back up and pack up
                        .lineToX(50)
                        .afterDisp(0, manager.getLiftReadyToDrive())

                        // Drive to stacks - first trip
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(24, -10.5, Math.toRadians(180)), Math.toRadians(180))
                        .splineTo(new Vector2d(-44, -10.5), Math.toRadians(180))

                        // Prepare for grabbing - Trip 1
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                        .afterDisp(0, manager.runLiftToPosition(-123))
                        .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                        .lineToX(-53)
                        .build()
            );
    }

    // STACK TO ANGLE DROP
    // (E)
    void stackToAngleDrop () {
        Actions.runBlocking(
            drive.actionBuilder(drive.pose)
            // Move in and grab pixels until beam break
            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))

            .afterTime(0, manager.closeAutoClaw())
            .afterTime(0, manager.closeLeftClaw())
            .afterTime(0, manager.closeRightClaw())
            .waitSeconds(0.1)
            .lineToX(-52.75)

            // Back away and pack up
            .stopAndAdd(manager.raiseLiftAfterStackPickup())

            .afterDisp(3, manager.lowerLiftForDriving())
            .afterDisp(3, manager.zeroLift())
            .afterDisp(3, manager.positionTheClawToDriveWithPixels())
            .lineToX(-50)

            // Return to backdrop and angle drop
            .setReversed(true)
            .splineToSplineHeading(new Pose2d(-11, -12, Math.toRadians(0)), Math.toRadians(0))
            .afterDisp(30, manager.getLiftReadyToDropPixelFromLeft())
            .splineTo(new Vector2d(40, -12), Math.toRadians(0))
            .splineTo(new Vector2d(61, -20), Math.toRadians(-35))
            .afterTime(0, manager.openAutoClaw())
            .afterTime(0, manager.openLeftClaw())
            .afterTime(0, manager.openRightClaw())
            .waitSeconds(0.25)
            .build()
        );
    }

    // PARK
    // (PK)
    void park(ParkLocation parkLocation) {
        switch (parkLocation) {
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                // Back up and pack up
                                .lineToX(48)
                                .afterDisp(2, manager.getLiftReadyToDrive())
                                .setReversed(true)
                                .splineTo(new Vector2d(58, -14), Math.toRadians(0))
                                .build()));
                break;
            case Corner:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                // Back up and pack up
                                .lineToX(48)
                                .afterDisp(2, manager.getLiftReadyToDrive())
                                .setReversed(true)
                                .splineTo(new Vector2d(58, -64), Math.toRadians(0))
                                .build()));
                break;
            case None:
                manager.getLiftReadyToDrive();
                break;
            default:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                // Back up and pack up
                                .lineToX(50)
                                .afterDisp(1, manager.getLiftReadyToDrive())
                                .build()));
                break;
        }
    }
}