package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.Action;
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
public class RedBackstageDoubleStack extends LinearOpMode {
    protected RobotHardware hardware;
    protected ActionManager manager;
    protected MecanumDrive drive;
    protected RevBlinkinLedDriver.BlinkinPattern pattern;
    protected PoCHuskyLens poCHuskyLens;
    protected PropLocation propLocation;

    protected void initHardware() {
        hardware = new RobotHardware(hardwareMap);
        manager = new ActionManager(hardware);

        drive = new org.firstinspires.ftc.teamcode.MecanumDrive(hardware.leftFront,
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
        return new Pose2d(12.5, -64, Math.toRadians(90));
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

        // Drive to pixel drop
        Actions.runBlocking(
                new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(19.5, -35.5), Math.toRadians(90))
                                .build(),
                        manager.openLeftClaw()
                )
        );

        // Drive to backdrop
        Actions.runBlocking(new SequentialAction(
                drive.actionBuilder(drive.pose)
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(48,-33, Math.toRadians(0)), Math.toRadians(0))
                        .waitSeconds(0.5)
                        .build())
        );

        // drop pixel on wall
        Actions.runBlocking(
                new SequentialAction(
                    manager.getLiftReadyToDropThePixelOnTheWall(),
                    drive.actionBuilder(drive.pose)
                            .lineToX(59.5)
                            .build(),
                    manager.positionTheClawToDropPixels(),
                    manager.openRightClaw(), // add in auto claw
                    new SleepAction(.5)
            )
        );

        // back away and pack up
        Actions.runBlocking(
                new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .lineToX(52)
                                .build(),
                        new ParallelAction(
                            manager.positionTheClawToDriveWithPixels(),
                            manager.getLiftReadyToDrive()
                        )
                )
        );

        // drive to stack
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                // Head to Stacks VIA A-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(20, -12, Math.toRadians(-180)), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(-51, -11), Math.toRadians(180))
                .build());

        // Extend and pick up two pixels
        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                manager.runLiftToPosition(-160),
                                manager.positionTheClawToPickupPixels()
                        ),
                        new ParallelAction(
                                manager.closeAutoClaw(),
                                manager.closeLeftClaw()
                        ),
                        manager.raiseLiftAfterStackPickup()
                )
        );

        // drive to backstage
        Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            // Head to Stacks VIA A-Row
          //                  .setReversed(true)
                            .lineToX(-44)
                            .afterDisp(0,
                                    new ParallelAction(
                                            manager.lowerLiftForDriving(),
                                            manager.zeroLift(),
                                            manager.positionTheClawToDriveWithPixels()
                                    ))
                            //.setReversed(true)
                            .splineToSplineHeading(new Pose2d(-12, -12, Math.toRadians(0)), Math.toRadians(0))
                            .setReversed(false)
                            .splineTo(new Vector2d(40, -12), Math.toRadians(0))
                            .splineTo(new Vector2d(64,-14), Math.toRadians(0))
                            .build());

        // drop pixels backstage
        Actions.runBlocking(
                new SequentialAction(
                    //manager.runLiftToPosition(-900),
                    //manager.positionTheClawToPickupPixels(),
                    //new ParallelAction(
                            manager.openAutoClaw(),
                            manager.openLeftClaw()
                   // ),
                    //manager.zeroLift(),
                    //manager.positionTheClawToDriveWithPixels()
                )
        );

        // drive to stack
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Head to Stacks VIA A-Row
                        .lineToX(60)
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(24, -16, Math.toRadians(-180)), Math.toRadians(-180))
                        .setReversed(false)
                        .splineTo(new Vector2d(-50, -11.5), Math.toRadians(180))
                        .build());

        // Extend and pick up two pixels
        Actions.runBlocking(
                new SequentialAction(
                        new ParallelAction(
                                //manager.runLiftToPosition(-70),
                                manager.positionTheClawToPickupPixels()
                        ),
                        drive.actionBuilder(drive.pose)
                                .lineToX(-54)
                                .build(),
                        new ParallelAction(
                                manager.closeAutoClaw(),
                                manager.closeLeftClaw()
                        )
                        //manager.raiseLiftAfterStackPickup()
                )
        );


        // drive to backstage
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Head to Stacks VIA A-Row
                        //                  .setReversed(true)
                        .lineToX(-48)
                        .afterDisp(0,
                                new ParallelAction(
                                        manager.lowerLiftForDriving(),
                                        manager.zeroLift(),
                                        manager.positionTheClawToDriveWithPixels()
                                ))
                        //.setReversed(true)
                        .splineToSplineHeading(new Pose2d(-12, -12, Math.toRadians(0)), Math.toRadians(0))
                        .setReversed(false)
                        .splineTo(new Vector2d(40, -12), Math.toRadians(0))
                        .splineTo(new Vector2d(64,-12), Math.toRadians(-45))
                        .build());

        // drop pixels backstage
        Actions.runBlocking(
                new SequentialAction(
                        //manager.runLiftToPosition(-900),
                        //manager.positionTheClawToPickupPixels(),
                        //new ParallelAction(
                        manager.openAutoClaw(),
                        manager.openLeftClaw()
                        // ),
                        //manager.zeroLift(),
                        //manager.positionTheClawToDriveWithPixels()
                )
        );

        // Release
        // drive to stack
//        Actions.runBlocking(
//                drive.actionBuilder(drive.pose)
//                        // Head to Stacks VIA A-Row
//                        .setReversed(true)
//                        .splineToSplineHeading(new Pose2d(20, -12, Math.toRadians(-180)), Math.toRadians(180))
//                        .setReversed(false)
//                        .splineTo(new Vector2d(-48, -10), Math.toRadians(180))
//                        .build());

//        Actions.runBlocking(
//                new SequentialAction(
//                    new ParallelAction(
//                            manager.positionTheClawToPickupPixels(),
//                            manager.runLiftToPosition(-200)
//                    ),
//                    new ParallelAction(
//                            manager.closeAutoClaw(),
//                            manager.closeLeftClaw()
//                    ),
//                    new ParallelAction(
//                        manager.positionTheClawToDriveWithPixels(),
//                        manager.zeroLift()
//                    )
//                )
//        );



        /*
        // Return to Backboard
        Actions.runBlocking(drive.actionBuilder(drive.pose)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-12, -12), Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineTo(new Vector2d(48, -12), Math.toRadians(0))
                .build());
        */

        /*
//                .splineToSplineHeading(new Pose2d(48, -35, Math.toRadians(0)), Math.toRadians(0))
                .waitSeconds(0.5)

                // Head to Stacks VIA A-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(12, -12, Math.toRadians(-180)), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(-58, -12), Math.toRadians(180))
                .waitSeconds(0.5)

                // Return to Backboard
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-12, -12), Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineTo(new Vector2d(48, -12), Math.toRadians(0))
  //              .splineToSplineHeading(new Pose2d(48, -35, Math.toRadians(0)), Math.toRadians(0))
                .waitSeconds(0.5)
*/
                // Park
                //.setReversed(true)
                //.splineTo(new Vector2d(53, -60), Math.toRadians(0))
//                .build()));
    }
}
