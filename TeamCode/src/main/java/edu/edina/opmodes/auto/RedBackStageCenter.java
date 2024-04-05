package edu.edina.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.outoftheboxrobotics.photoncore.Photon;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
@Photon
@Config
public class RedBackStageCenter extends RedBackStage {
    public static double DRIVEINX_FIRSTPICKUP = -57.5;
    public static double DRIVEINY_FIRSTPICKUP = -11;
    public static double DRIVEINX_SECONDPICKUP = -56.5;
    public static double DRIVEINY_SECONDPICKUP = -13;
    public static int EXTENDARM_FIRSTPICKUP = -120;
    public static int EXTENDARM_SECONDPICKUP = -40;

    @Override
    protected void runPaths() {
        if (twoWhites) {
            // drive t
            // o stack - 1st trip
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            // Back up and pack up
                            .lineToX(50)
                            .afterDisp(0, manager.getLiftReadyToDrive())

                            // Drive to stacks - first trip
                            .setReversed(true)
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .splineToSplineHeading(new Pose2d(24, DRIVEINY_FIRSTPICKUP, Math.toRadians(180)), Math.toRadians(180))
                            .splineTo(new Vector2d(-44, DRIVEINY_FIRSTPICKUP), Math.toRadians(180))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                            // Prepare for grabbing - Trip 1
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                            .afterDisp(0, manager.runLiftToPosition(EXTENDARM_FIRSTPICKUP, true))
                            .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                            .lineToX(DRIVEINX_FIRSTPICKUP)
                            .build()
            );
        }

        if (fourWhites) {  // Make the middle trip
            if (dropOnBackDrop) {
                runFourWhitesMiddleTripBackDrop();
            }

            if (dropOnBackStage) {
                runFourWhitesMiddleTripBackStage();
            }
        }

        if (twoWhites || fourWhites) {  // Drop the last pixels of the run
            if (dropOnBackDrop) {
                runLastTwoOrFourWhitesBackDrop();
            }

            if (dropOnBackStage) {
                runLastTwoOrFourWhitesBackStage();
            }
        }
    }

    private void runFourWhitesMiddleTripBackDrop() {
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Move in and grab pixels until beam break
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))

                        .afterTime(0, manager.closeAutoClaw())
                        .afterTime(0, manager.closeLeftClaw())
                        .waitSeconds(0.1)

                        // Back away and pack up
                        .stopAndAdd(manager.raiseLiftAfterStackPickup())
                        .lineToX(-45)
                        .afterDisp(3, manager.lowerLiftForDriving())
                        .afterDisp(3, manager.zeroLift())
                        .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                        // Return to backdrop and angle drop
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-40, -10), Math.toRadians(-10)), Math.toRadians(0))
                        .splineToSplineHeading(new Pose2d(-11, DRIVEINY_SECONDPICKUP, Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(40, manager.getLiftReadyToDropPixelFromLeft())
                        .splineTo(new Vector2d(30, -13), Math.toRadians(0))
                        .splineTo(new Vector2d(57, -22), Math.toRadians(-20))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .waitSeconds(0.25)
                        .afterTime(.1, manager.openAutoClaw(0))
                        .afterTime(0, manager.openLeftClaw(0))
                        .waitSeconds(0.25)

                        // Head to Stacks VIA C-Row
                        .setReversed(true)
                        .afterDisp(0, manager.getLiftReadyToDrive())
                        .splineToSplineHeading(new Pose2d(24, DRIVEINY_SECONDPICKUP, Math.toRadians(180)), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineTo(new Vector2d(-44, DRIVEINY_SECONDPICKUP), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                        // Prepare for grabbing - Trip 2
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(175)))
                        .afterDisp(0, manager.runLiftToPosition(EXTENDARM_SECONDPICKUP, true))
                        .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                        .lineToX(DRIVEINX_SECONDPICKUP)
                        .build()
        );
    }

    private void runFourWhitesMiddleTripBackStage() {
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Move in and grab pixels until beam break
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))

                        .afterTime(0, manager.closeAutoClaw())
                        .afterTime(0, manager.closeLeftClaw())
                        .waitSeconds(0.1)

                        // Back away and pack up
                        .stopAndAdd(manager.raiseLiftAfterStackPickup())
                        .lineToX(-45)
                        .afterDisp(3, manager.lowerLiftForDriving())
                        .afterDisp(3, manager.zeroLift())
                        .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                        // Return to backstage and drop
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-40, -10), Math.toRadians(-10)), Math.toRadians(0))
                        .splineToSplineHeading(new Pose2d(-11, DRIVEINY_SECONDPICKUP, Math.toRadians(0)), Math.toRadians(0))
                        .splineTo(new Vector2d(59.5, -12), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .afterTime(0, manager.openAutoClaw(0))
                        .afterTime(0, manager.openLeftClaw(0))
                        .waitSeconds(0.25)

                        // Head to Stacks VIA C-Row
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(24, DRIVEINY_SECONDPICKUP, Math.toRadians(180)), Math.toRadians(180))
                        .splineTo(new Vector2d(-44, DRIVEINY_SECONDPICKUP), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                        // Prepare for grabbing - Trip 2
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(175)))
                        .afterDisp(0, manager.runLiftToPosition(EXTENDARM_SECONDPICKUP, true))
                        .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                        .lineToX(DRIVEINX_SECONDPICKUP)
                        .build()
        );
    }

    private void runLastTwoOrFourWhitesBackDrop() {
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Back away and pack up
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))

                        // Move in and grab pixels until beam break
                        .afterTime(0, manager.closeAutoClaw())
                        .afterTime(0, manager.closeLeftClaw())
                        .waitSeconds(0.1)

                        // Back away and pack up
                        .stopAndAdd(manager.raiseLiftAfterStackPickup())
                        .lineToX(-45)
                        .afterDisp(3, manager.lowerLiftForDriving())
                        .afterDisp(3, manager.zeroLift())
                        .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                        // Return to backdrop and angle drop
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-40, -10), Math.toRadians(-10)), Math.toRadians(0))
                        .splineToSplineHeading(new Pose2d(-11, DRIVEINY_SECONDPICKUP, Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(40, manager.getLiftReadyToDropPixelFromLeft())
                        .splineTo(new Vector2d(30, -13), Math.toRadians(0))
                        .splineTo(new Vector2d(58, -22), Math.toRadians(-20))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .waitSeconds(0.25)
                        .afterTime(.1, manager.openAutoClaw(0))
                        .afterTime(0, manager.openLeftClaw(0))
                        .waitSeconds(0.25)
                        .build()
        );

        Actions.runBlocking(new SequentialAction(
                manager.getLiftReadyToDrive()
        ));
    }

    private void runLastTwoOrFourWhitesBackStage() {
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Back away and pack up
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))

                        // Move in and grab pixels until beam break
                        .afterTime(0, manager.closeAutoClaw())
                        .afterTime(0, manager.closeLeftClaw())
                        .waitSeconds(0.1)

                        // Back away and pack up
                        .stopAndAdd(manager.raiseLiftAfterStackPickup())
                        .lineToX(-45)
                        .afterDisp(3, manager.lowerLiftForDriving())
                        .afterDisp(3, manager.zeroLift())
                        .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                        // Return to backstage and drop
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-40, -10), Math.toRadians(-10)), Math.toRadians(0))
                        .splineToSplineHeading(new Pose2d(-11, DRIVEINY_SECONDPICKUP, Math.toRadians(0)), Math.toRadians(0))
                        .splineTo(new Vector2d(59.5, -12), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .afterTime(0, manager.openAutoClaw(0))
                        .afterTime(0, manager.openLeftClaw(0))
                        .waitSeconds(0.25)
                        .lineToX(57)
                        .build()
        );
    }
}