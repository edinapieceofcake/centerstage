package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
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

public class RedBackStage extends LinearOpMode {
    private RobotHardware hardware;
    private PoCHuskyLens poCHuskyLens;
    private PropLocation propLocation = PropLocation.Center;
    private ParkLocation parkLocation = ParkLocation.Corner;
    private Vector2d propDropLocation;
    private Pose2d backdropLocation;
    private double propDropAngle = 90.0;
    private boolean useCamera = true;
    private long delayTime = 0;

    protected ActionManager manager;
    protected PoCMecanumDrive drive;
    protected boolean twoWhites = false;
    protected boolean fourWhites = false;

    private void initHardware() {
        hardware = new RobotHardware(hardwareMap);
        manager = new ActionManager(hardware);

        drive = new PoCMecanumDrive(hardware.leftFront,
                hardware.leftBack, hardware.rightBack, hardware.rightFront,
                hardware.par0, hardware.par1, hardware.perp,
                hardware.externalImu, hardware.expansionImu, hardware.voltageSensor,
                hardware.beamBreak, getStartPose());

        // HuskyLens Init
        poCHuskyLens = new PoCHuskyLens(hardware.huskyLens, telemetry, getAlliance());
        poCHuskyLens.init();

        hardware.dropServosForAutonomous();
        hardware.droneLaunchServo.setPosition(RobotConfiguration.getInstance().droneLauncherArmedPosition);

        manager.init();
        manager.start();
    }

    private Alliance getAlliance() {
        return Alliance.Red;
    }

    private Pose2d getStartPose() {
        return new Pose2d(14.5, -64, Math.toRadians(90));
    }

    protected void runPaths() {}

    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);

        initHardware();

        Actions.runBlocking(manager.positionTheClawToPickupPixels());

        // Turn on prop illumination
        hardware.lights.setPower(1);

        while (!isStarted()) {
            pad1.update();

            telemetry.addData("A for P, Y and park in corner", "");
            telemetry.addData("X for P, Y and park in center", "");
            telemetry.addData("Y for P, Y, 2Ws and park in front", "");
            telemetry.addData("B for P, Y, 4Ws and park in front", "");
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
                Actions.runBlocking(
                        new SequentialAction(
                            new ParallelAction(
                                    manager.closeRightClaw(),
                                    manager.closeLeftClaw()
                            ),
                        manager.positionTheClawToDriveWithPixels())
                );
            }

            // Open the claws
            if (gamepad1.right_trigger != 0) {
                Actions.runBlocking(new ParallelAction(
                        manager.openRightClaw(),
                        manager.openLeftClaw(),
                        manager.positionTheClawToPickupPixels()
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

            dropPurplePixel();

            runPaths();

            park();
        }
    }

    private void dropPurplePixel() {

        // Determine location for the purple and yellow pixel
        switch(propLocation) {
            case Left:
                propDropLocation = new Vector2d(15.5, -42);
                propDropAngle = 135.0;
                backdropLocation = new Pose2d(48,-32, Math.toRadians(0));
                break;
            case Center:
                propDropLocation = new Vector2d(16.5, -34.5);
                backdropLocation = new Pose2d(48,-38, Math.toRadians(0));
                break;
            case Right:
                propDropLocation = new Vector2d(27, -43);
                propDropAngle =65.0;
                backdropLocation = new Pose2d(48,-45, Math.toRadians(0));
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
                        .setTangent((propLocation== PropLocation.Right) ? Math.toRadians(-180) : Math.toRadians(0))
                        .afterTime(0, manager.getLiftReadyToDropThePixelLowOnTheWall())
                        .splineToSplineHeading(backdropLocation, Math.toRadians(0))
                        .lineToX(56.5)
                        .stopAndAdd(manager.openRightClaw())
                        .build()
        );
    }

    private void park() {
        // park
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
