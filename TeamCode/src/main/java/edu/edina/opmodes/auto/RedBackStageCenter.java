package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

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
public class RedBackStageCenter extends LinearOpMode {
    protected RobotHardware hardware;
    protected ActionManager manager;
    protected PoCMecanumDrive drive;
    protected PoCHuskyLens poCHuskyLens;

    protected PropLocation propLocation = PropLocation.Center;
    private ParkLocation parkLocation = ParkLocation.Corner;

    private boolean useCamera = true;

    private boolean twoWhites = false;
    private boolean fourWhites = false;

    private long delayTime = 0;

    protected void initHardware() {
        ElapsedTime timer = new ElapsedTime();

        hardware = new RobotHardware(hardwareMap);
        manager = new ActionManager(hardware);

        timer.reset();
        while (hardware.navxMicro.isCalibrating())  {
            telemetry.addData("calibrating", "%s", Math.round(timer.seconds())%2==0 ? "|.." : "..|");
            telemetry.update();
            try {
                Thread.sleep(50);
            } catch (Exception ex) {}
        }
        telemetry.log().clear(); telemetry.log().add("Gyro Calibrated. Press Start.");
        telemetry.clear(); telemetry.update();


        drive = new PoCMecanumDrive(hardware.leftFront,
                hardware.leftBack, hardware.rightBack, hardware.rightFront,
                hardware.par0, hardware.perp,
                hardware.gyro, hardware.expansionImu, hardware.voltageSensor,
                hardware.beamBreak, getStartPose());

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
        return new Pose2d(14.5, -64, Math.toRadians(90));
    }

    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        ElapsedTime timer = new ElapsedTime();

        initHardware();

        // Turn on prop illumination
        hardware.lights.setPower(1);

        while (!isStarted()) {
            pad1.update();

            telemetry.addData("A for P, Y and park in corner", "");
            telemetry.addData("X for P, Y and park in center", "");
            telemetry.addData("Y for P, Y, 2Ws and park in center", "");
            telemetry.addData("B for P, Y, 4Ws and park in center", "");
            telemetry.addData("L-BUMPER to increase delay, R-BUMPER to decrease delay", "");
            telemetry.addData("L-TRIGGER to close claws, R-TRIGGER to open", "");
            telemetry.addData("left stick down manual rotate prop position", "");
            telemetry.addData("right stick down manual or auto camera", "");

            if (pad1.a) {
                parkLocation = ParkLocation.Corner;
            }

            if (pad1.x) {
                parkLocation = ParkLocation.Center;
            }

            if (pad1.y) {
                twoWhites = true;
                fourWhites = false;
                parkLocation = ParkLocation.None;
            }

            if (pad1.b) {
                twoWhites = true;
                fourWhites = true;
                parkLocation = ParkLocation.None;
            }

            // Delay - Max of 10000ms, Min of 0ms
            if (pad1.left_bumper) {
                delayTime += (delayTime > 9000) ? 0 : 1000;
            } else if (pad1.right_bumper) {
                delayTime -= (delayTime < 1000) ? 0 : 1000;
            }

            // If we have ANY delay, don't allow second trip
            fourWhites = (delayTime <=0 && fourWhites);

            // Close the claws
            if (gamepad1.left_trigger != 0) {
                Actions.runBlocking(new ParallelAction(
                        manager.closeRightClaw(),
                        manager.closeLeftClaw()
                ));
            }

            // Open the claws
            if (gamepad1.right_trigger != 0) {
                Actions.runBlocking(new ParallelAction(
                        manager.openRightClaw(),
                        manager.openLeftClaw()
                ));
            }

            // Select whether camera is live or not
            if (pad1.right_stick_button) {
                useCamera = !useCamera;
            }

            // Find Prop Location
            if (useCamera) {
                poCHuskyLens.update();
                propLocation = poCHuskyLens.getPropLocation();
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

            telemetry.addData("Make first trip", twoWhites);
            telemetry.addData("Make Second Trip", fourWhites);
            telemetry.addData("Current Park Location", parkLocation);
            telemetry.addData("Delay in seconds", delayTime / 1000);
            telemetry.addData("Location", propLocation);
            telemetry.addData("Use Camera", useCamera);
            telemetry.update();

        }

        // Turn off prop illumination
        hardware.lights.setPower(0);

        if (opModeIsActive()) {

            // Delay time
            if (delayTime > 0) {
                sleep(delayTime);
            }

            runPaths();
        }
    }

    protected void runPaths() {

        Vector2d propDropLocation;
        Pose2d backdropLocation;
        double propDropAngle = 90.0;

        // Determine location for the purple and yellow pixel
        switch(propLocation) {
            case Left:
                propDropLocation = new Vector2d(15.5, -42);
                propDropAngle = 135.0;
                backdropLocation = new Pose2d(48,-32, Math.toRadians(0));
                break;
            case Center:
                propDropLocation = new Vector2d(16.5, -35.5);
                backdropLocation = new Pose2d(48,-38, Math.toRadians(0));
                break;
            case Right:
                propDropLocation = new Vector2d(27, -43);
                propDropAngle =65.0;
                backdropLocation = new Pose2d(48.5,-45, Math.toRadians(0));
                break;
            default:
                propDropLocation = new Vector2d(16.5, -35.5);  // default to Center if all goes bad
                backdropLocation = new Pose2d(48,-38, Math.toRadians(0)); // default to center if all goes bad
                break;
        }

        // Purple + Yellow
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Go to spike and drop
                        .splineTo(propDropLocation, Math.toRadians(propDropAngle))
                        .endTrajectory()
                        .stopAndAdd(manager.openLeftClaw())

                        // Drive to backdrop and release
                        .setTangent((propLocation==PropLocation.Right) ? Math.toRadians(-30) : Math.toRadians(0))
                        .afterDisp(2, manager.getLiftReadyToDropThePixelLowOnTheWall())
                        .splineToSplineHeading(backdropLocation, Math.toRadians(0))
                        .lineToX(53.5)
                        .stopAndAdd(manager.openRightClaw())

                        // Back up and pack up
                        .lineToX(50)
                        .afterDisp(2, manager.getLiftReadyToDrive())
                        .build()
        );

        if (twoWhites) {
            // drive to stack - 1st trip
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)

                            // Drive to stacks - first trip
                            .splineToSplineHeading(new Pose2d(24, -11, Math.toRadians(180)), Math.toRadians(180))
                            .splineTo(new Vector2d(-44, -11), Math.toRadians(180))

                            // Prepare for grabbing - Trip 1
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn()))
                            .afterDisp(0, manager.runLiftToPosition(-143))
                            .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                            .lineToX(-51.25)

                            // Move in and grab pixels until beam break
                            .afterTime(0, manager.closeAutoClaw())
                            .afterTime(0, manager.closeLeftClaw())
                            .afterTime(0, manager.closeRightClaw())
                            .waitSeconds(0.2)
                            .lineToX(-51)
                            .build()
            );
        }

        if (fourWhites) {  // Make the middle trip
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)

                            // Back away and pack up
                            .stopAndAdd(manager.raiseLiftAfterStackPickup())
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))
                            .lineToX(-44)
                            .afterDisp(3, manager.lowerLiftForDriving())
                            .afterDisp(3, manager.zeroLift())
                            .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                            // Return to backdrop and angle drop
                            .splineToSplineHeading(new Pose2d(-11, -12, Math.toRadians(0)), Math.toRadians(0))
                            .setReversed(false)
                            .afterDisp(30, manager.getLiftReadyToDropPixelFromLeft())
                            .splineTo(new Vector2d(40, -12), Math.toRadians(0))
                            .splineTo(new Vector2d(52, -20), Math.toRadians(-25))
                            .afterTime(0, manager.openAutoClaw())
                            .afterTime(0, manager.openLeftClaw())
                            .afterTime(0, manager.openRightClaw())

                            // Pause, then pack up
                            .waitSeconds(0.5)
                            // TODO - Back away??!
                            .stopAndAdd(manager.getLiftReadyToDrive())

                            // Head to Stacks VIA C-Row
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(24, -11, Math.toRadians(180)), Math.toRadians(180))
                            .splineTo(new Vector2d(-44, -11), Math.toRadians(180))

                            // Prepare for grabbing - Trip 2
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn()))
                            .afterDisp(0, manager.runLiftToPosition(-143))
                            .afterDisp(0, manager.positionTheClawToPickupPixels())
                            .lineToX(-51.25)

                            // Move in and grab pixels until beam break
                            .afterTime(0, manager.closeAutoClaw())
                            .afterTime(0, manager.closeLeftClaw())
                            .afterTime(0, manager.closeRightClaw())
                            .waitSeconds(0.2)
                            .lineToX(-51)
                            .build()
            );
        }

        if (twoWhites || fourWhites) {  // Drop the last pixels of the run
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            // Back away and pack up
                            .stopAndAdd(manager.raiseLiftAfterStackPickup())
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))
                            .lineToX(-44)
                            .afterDisp(3, manager.lowerLiftForDriving())
                            .afterDisp(3, manager.zeroLift())
                            .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                            // Return to backdrop and angle drop
                            .splineToSplineHeading(new Pose2d(-11, -12, Math.toRadians(0)), Math.toRadians(0))
                            .setReversed(false)
                            .afterDisp(30, manager.getLiftReadyToDropPixelFromLeft())
                            .splineTo(new Vector2d(40, -12), Math.toRadians(0))
                            .splineTo(new Vector2d(52, -20), Math.toRadians(-25))
                            .afterTime(0, manager.openAutoClaw())
                            .afterTime(0, manager.openLeftClaw())
                            .afterTime(0, manager.openRightClaw())

                            // Pause, then pack up
                            .waitSeconds(0.5)
                            // TODO - Back away??!
                            .stopAndAdd(manager.getLiftReadyToDrive())
                            .build()
            );
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