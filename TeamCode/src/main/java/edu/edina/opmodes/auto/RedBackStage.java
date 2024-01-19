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
public class RedBackStage extends LinearOpMode {
    protected RobotHardware hardware;
    protected ActionManager manager;
    protected PoCMecanumDrive drive;
    protected RevBlinkinLedDriver.BlinkinPattern pattern;
    protected PoCHuskyLens poCHuskyLens;

    protected PropLocation propLocation = PropLocation.Center;
    private ParkLocation parkLocation = ParkLocation.Corner;

    private boolean useCamera = true;

    private boolean twoWhites = false;
    private boolean fourWhites = false;

    private long delayTime = 0;

    protected void initHardware() {
        hardware = new RobotHardware(hardwareMap);
        manager = new ActionManager(hardware);

        drive = new PoCMecanumDrive(hardware.leftFront,
                hardware.leftBack, hardware.rightBack, hardware.rightFront,
                hardware.par0, hardware.par1, hardware.perp,
                hardware.externalImu, hardware.expansionImu, hardware.voltageSensor,
                hardware.beamBreak, getStartPose());

        // Heartbeat Red to signify Red alliance
        pattern = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_GRAY;
        hardware.blinkinLedDriver.setPattern(pattern);

        PropLocation lastLocation = PropLocation.None;

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
        return new Pose2d(14.5, -64, Math.toRadians(90));
    }

    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);

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
            //telemetry.addData("left stick down manual rotate prop position", "");
            //telemetry.addData("right stick down manual or auto camera", "");

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
            fourWhites = (delayTime > 0) ? false : fourWhites;

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
                if (useCamera) {
                    useCamera = false;
                } else {
                    useCamera = true;
                }
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

            // Show solid pattern if block seen, otherwise heartbeat
            if (propLocation != PropLocation.None) {
                pattern = getSuccessfulPropMatchColor();
            } else {
                pattern = getUnsuccessfulPropMatchColor();
            }

            hardware.blinkinLedDriver.setPattern(pattern);
        }

        // Turn off prop illumination
        hardware.lights.setPower(0);

        if (opModeIsActive()) {
            // Signal GREEN for successful run
            pattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
            hardware.blinkinLedDriver.setPattern(pattern);

            // Delay time
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
        Pose2d backdropLocation;
        double propDropAngle = 90.0;

        // Comment out when actually using camera!!
        //propLocation = PropLocation.Left;

        // Determine location for the purple and yellow pixel
        switch(propLocation) {
            case Left:
                propDropLocation = new Vector2d(15.5, -43);
                propDropAngle = 135.0;
                backdropLocation = new Pose2d(48,-32, Math.toRadians(0));
                break;
            case Center:
                propDropLocation = new Vector2d(16.5, -35.5);
                backdropLocation = new Pose2d(48,-38, Math.toRadians(0));
                break;
            case Right:
                propDropLocation = new Vector2d(31.5, -43);
                backdropLocation = new Pose2d(48,-47, Math.toRadians(0));
                break;
            default:
                propDropLocation = new Vector2d(16.5, -35.5);  // default to Center if all goes bad
                backdropLocation = new Pose2d(48,-38, Math.toRadians(0)); // default to center if all goes bad
                break;
        }

        // Execute drive to prop spot and drop
        Actions.runBlocking(
                new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(propDropLocation, Math.toRadians(propDropAngle))
                                .build(),
                        manager.openLeftClaw()
                )
        );

        // Drive to backdrop
        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                drive.actionBuilder(drive.pose)
                                        .setReversed(true)
                                        .waitSeconds(.5)
                                        .splineToSplineHeading(backdropLocation, Math.toRadians(0))
                                        .lineToX(53.5)
                                        .build(),
                                manager.getLiftReadyToDropThePixelLowOnTheWall()
                        ),
                        manager.openRightClaw()
                )
        );

        // back away and pack up
        Actions.runBlocking(
                new ParallelAction(
                        drive.actionBuilder(drive.pose)
                                .lineToX(50)
                                .turnTo(Math.toRadians(90))
                                .build(),
                        new SequentialAction(
                            new SleepAction(0.5),
                            manager.getLiftReadyToDrive()
                        )
                )
        );

        if (twoWhites) {
            // drive to stack - 1st trip
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            // Head to Stacks VIA C-Row
                            .splineToSplineHeading(new Pose2d(24, -11, Math.toRadians(180)), Math.toRadians(180))
                            .splineTo(new Vector2d(-44, -11), Math.toRadians(180))
                            .build());

            drive.turnBeamBreakOn();

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
                                    manager.closeLeftClaw(),
                                    manager.closeRightClaw()
                            ),
                            manager.raiseLiftAfterStackPickup()
                    )
            );

            drive.turnBeamBreakOff();

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
                                    .splineToSplineHeading(new Pose2d(-11, -12, Math.toRadians(0)), Math.toRadians(0))
                                    .setReversed(false)
                                    .splineTo(new Vector2d(40, -13), Math.toRadians(0))
                                    .splineTo(new Vector2d(60, -14), Math.toRadians(0))
                                    .build(),
                            new ParallelAction(
                                    manager.openAutoClaw(),
                                    manager.openLeftClaw(),
                                    manager.openRightClaw()
                            )
                    )
            );
        }

        if (fourWhites) {
            // drive to stack - 2nd trip
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            // Head to Stacks VIA C-Row
                            .lineToX(60)
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(24, -12.5, Math.toRadians(-180)), Math.toRadians(-180))
                            .setReversed(false)
                            .splineTo(new Vector2d(-44, -12.5), Math.toRadians(180))
                            .build());

            // Extend and pick up two pixels
            drive.turnBeamBreakOn();

            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    manager.runLiftToPosition(-35),
                                    manager.positionTheClawToPickupPixels()
                            ),
                            drive.actionBuilder(drive.pose)
                                    .lineToX(-55)
                                    .build(),
                            new ParallelAction(
                                    manager.closeAutoClaw(),
                                    manager.closeLeftClaw(),
                                    manager.closeRightClaw()
                            ),
                            manager.raiseLiftAfterStackPickup()
                    )
            );

            drive.turnBeamBreakOff();

            // drive to backstage - 2nd trip
            Actions.runBlocking(
                    new SequentialAction(
                            drive.actionBuilder(drive.pose)
                                    // Head to Stacks VIA A-Row
                                    .lineToX(-48)
                                    .afterDisp(0,
                                            new ParallelAction(
                                                    manager.lowerLiftForDriving(),
                                                    manager.zeroLift(),
                                                    manager.positionTheClawToDriveWithPixels()
                                            ))
                                    .splineToSplineHeading(new Pose2d(-12, -14, Math.toRadians(0)), Math.toRadians(0))
                                    .setReversed(false)
                                    .splineTo(new Vector2d(40, -14), Math.toRadians(0))
                                    .splineTo(new Vector2d(56, -17), Math.toRadians(-45))
                                    .build(),
                            new ParallelAction(
                                    manager.openAutoClaw(),
                                    manager.openLeftClaw(),
                                    manager.openRightClaw()
                            )
                    )
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