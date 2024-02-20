package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
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

public class BackStage extends LinearOpMode {
    private RobotHardware hardware;
    private PoCHuskyLens poCHuskyLens;
    private boolean useCamera = true;
    private long delayTime = 0;

    protected ParkLocation parkLocation = ParkLocation.Corner;
    protected PropLocation propLocation = PropLocation.Center;
    protected ActionManager manager;
    protected PoCMecanumDrive drive;
    protected boolean twoWhites = false;
    protected boolean fourWhites = false;
    protected boolean dropOnBackDrop = false;
    protected boolean dropOnBackStage = false;

    protected Alliance getAlliance() {
        return Alliance.Red;
    }

    protected Pose2d getStartPose() {
        return new Pose2d(14.5, -64, Math.toRadians(90));
    }

    protected PropLocation getNonePropLocation() { return PropLocation.None; }

    protected void runPaths() {}

    protected void dropPurplePixel() {}

    protected void park() {}

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
            telemetry.addData("Y for P, Y, 2Ws on backdrop and park in front", "");
            telemetry.addData("B for P, Y, 4Ws on backdrop and park in front", "");
            telemetry.addData("DPAD_UP for P, Y, 2Ws on backstage and park in front", "");
            telemetry.addData("DPAD_DN for P, Y, 4Ws on backstage and park in front", "");
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
                dropOnBackDrop = true;
                dropOnBackStage = false;
            }

            if (pad1.b) {
                twoWhites = true;
                fourWhites = true;
                parkLocation = ParkLocation.None;
                dropOnBackDrop = true;
                dropOnBackStage = false;
            }

            if (pad1.dpad_up) {
                twoWhites = true;
                fourWhites = false;
                parkLocation = ParkLocation.None;
                dropOnBackDrop = false;
                dropOnBackStage = true;
            }

            if (pad1.dpad_down) {
                twoWhites = true;
                fourWhites = true;
                parkLocation = ParkLocation.None;
                dropOnBackDrop = false;
                dropOnBackStage = true;
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
                useCamera = !useCamera;
            }

            // Find Prop Location
            if (useCamera) {
                poCHuskyLens.update();
                propLocation = poCHuskyLens.getPropLocation();
                if (propLocation == PropLocation.None) {
                    propLocation = getNonePropLocation();
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

            telemetry.addData("Make first trip", twoWhites);
            telemetry.addData("Make Second Trip", fourWhites);
            telemetry.addData("Drop on backdrop", dropOnBackDrop);
            telemetry.addData("Drop on back stage", dropOnBackStage);
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

            hardware.startCurrentMonitor();

            dropPurplePixel();

            if (twoWhites || fourWhites) {
                runPaths();
            } else {
                park();
            }

            hardware.stopCurrentMonitor();
        }
    }
}
