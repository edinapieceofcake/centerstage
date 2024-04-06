package edu.edina.opmodes.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.outoftheboxrobotics.photoncore.Photon;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.edina.library.enums.PropLocation;

@Autonomous
@Config
@Photon
public class BlueBackStageWall extends BlueBackStage {
    public static double DRIVEINX_FIRSTPICKUP = -58;
    public static double DRIVEINY_FIRSTPICKUP = 38;
    public static double DRIVEINX_SECONDPICKUP = -61;
    public static double DRIVEINX_THIRDPICKUP = -62;
    public static double DRIVEINY_SECONDPICKUP = 34;
    public static double DRIVEINY_COLUMN1 = 58;
    public static double DRIVEINY2_COLUMN1 = 54;
    public static int EXTENDARM_FIRSTPICKUP = -123;
    public static int EXTENDARM_SECONDPICKUP = -23;
     protected void runPaths() {
         if (twoWhites) {
             // drive to stack - 1st trip
             Actions.runBlocking(
                     drive.actionBuilder(drive.pose)
                             // Back up and pack up
                             .lineToX(43)
                             .afterDisp(0, manager.getLiftReadyToDrive())

                             // Drive to stacks - first trip
                             .setReversed(true)
                             .splineToSplineHeading(new Pose2d(6, 60, Math.toRadians(190)), Math.toRadians(180))
                             .splineToSplineHeading(new Pose2d(0, 60, Math.toRadians(-180)), Math.toRadians(180))
                             .afterDisp(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                             .splineToConstantHeading(new Vector2d(-38, 60), Math.toRadians(180))
                             .splineToConstantHeading(new Vector2d(-50, DRIVEINY_FIRSTPICKUP), Math.toRadians(180))
                             .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                             // Prepare for grabbing - Trip 1
                             .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                             .afterDisp(0, manager.runLiftToPosition(EXTENDARM_FIRSTPICKUP, false))
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

                            .afterTime(0, manager.closeLeftClaw())
                            .afterTime(0, manager.closeAutoClaw())
                            .waitSeconds(0.1)

                            // Back away and pack up
                            .stopAndAdd(manager.raiseLiftAfterStackPickup())
                            .lineToX(-56.5)

                            .afterDisp(3, manager.lowerLiftForDriving())
                            .afterDisp(3, manager.zeroLift())
                            .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                            // Return to backdrop and angle drop
                            .setReversed(true)
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .splineToSplineHeading(new Pose2d(new Vector2d(-40, DRIVEINY_COLUMN1), Math.toRadians(-10)), Math.toRadians(0))
                            .splineToSplineHeading(new Pose2d(new Vector2d(-35, DRIVEINY_COLUMN1), Math.toRadians(0)), Math.toRadians(0))

                            .afterDisp(30, manager.getLiftReadyToDropPixelFromLeft())
                            .splineToConstantHeading(new Vector2d(10, DRIVEINY_COLUMN1), Math.toRadians(0))
                            .splineTo(new Vector2d(30, DRIVEINY_COLUMN1), Math.toRadians(0))

                            .splineTo(new Vector2d(54, 53), Math.toRadians(-35))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                            .afterTime(0.1, manager.openLeftClaw(0))
                            .afterTime(0.2, manager.openAutoClaw(0))
                            .waitSeconds(0.25)

                            // Head to Stacks VIA A-Row
                            .setReversed(true)
                            .afterDisp(0, manager.getLiftReadyToDrive())

                            // Head to Stacks VIA Wall
                            .splineToSplineHeading(new Pose2d(6, DRIVEINY2_COLUMN1, Math.toRadians(190)), Math.toRadians(180))
                            .splineToSplineHeading(new Pose2d(0, DRIVEINY2_COLUMN1, Math.toRadians(180)), Math.toRadians(180))

                            .afterDisp(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .splineToConstantHeading(new Vector2d(-38, DRIVEINY2_COLUMN1), Math.toRadians(180))
                            .splineToConstantHeading(new Vector2d(-50, DRIVEINY_SECONDPICKUP), Math.toRadians(180))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                            // Prepare for grabbing - Trip 2
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
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

                         .afterTime(0, manager.closeLeftClaw())
                         .afterTime(0, manager.closeAutoClaw())
                         .waitSeconds(0.1)

                         // Back away and pack up
                         .stopAndAdd(manager.raiseLiftAfterStackPickup())
                         .lineToX(-56.5)

                         .afterDisp(3, manager.lowerLiftForDriving())
                         .afterDisp(3, manager.zeroLift())
                         .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                         // Return to backstage and drop
                         .setReversed(true)
                         .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                         .splineToSplineHeading(new Pose2d(new Vector2d(-40, DRIVEINY_COLUMN1), Math.toRadians(-10)), Math.toRadians(0))
                         .splineToSplineHeading(new Pose2d(new Vector2d(-35, DRIVEINY_COLUMN1), Math.toRadians(0)), Math.toRadians(0))

                         .splineToConstantHeading(new Vector2d(10, DRIVEINY_COLUMN1), Math.toRadians(0))
                         .splineTo(new Vector2d(59, DRIVEINY_COLUMN1), Math.toRadians(0))
                         .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                         .afterTime(0.1, manager.openLeftClaw())
                         .afterTime(0.1, manager.openAutoClaw())
                         .waitSeconds(0.25)

                         // Head to Stacks VIA A-Row
                         .setReversed(true)

                         // Head to Stacks VIA Wall
                         .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                         .splineToSplineHeading(new Pose2d(6, DRIVEINY_COLUMN1, Math.toRadians(190)), Math.toRadians(180))
                         .splineToSplineHeading(new Pose2d(0, DRIVEINY_COLUMN1, Math.toRadians(-180)), Math.toRadians(180))

                         .splineToConstantHeading(new Vector2d(-38, DRIVEINY_COLUMN1), Math.toRadians(180))
                         .splineToConstantHeading(new Vector2d(-50, DRIVEINY_SECONDPICKUP), Math.toRadians(180))
                         .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                         // Prepare for grabbing - Trip 2
                         .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                         .afterDisp(0, manager.runLiftToPosition(EXTENDARM_SECONDPICKUP, true))
                         .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                         .lineToX(DRIVEINX_THIRDPICKUP)
                         .build()
         );
     }

     private void runLastTwoOrFourWhitesBackDrop() {
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            // Back away and pack up
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))

                            // Move in and grab pixels until beam break
                            .afterTime(0, manager.closeLeftClaw())
                            .afterTime(0, manager.closeAutoClaw())
                            .waitSeconds(0.1)

                            .stopAndAdd(manager.raiseLiftAfterStackPickup())
                            .lineToX(-56.5)

                            .afterDisp(3, manager.lowerLiftForDriving())
                            .afterDisp(3, manager.zeroLift())
                            .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                            // Return to backdrop and angle drop
                            .setReversed(true)
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .splineToSplineHeading(new Pose2d(new Vector2d(-42, DRIVEINY2_COLUMN1), Math.toRadians(-10)), Math.toRadians(0))
                            .splineToSplineHeading(new Pose2d(new Vector2d(-37, DRIVEINY2_COLUMN1), Math.toRadians(0)), Math.toRadians(0))

                            .afterDisp(30, manager.getLiftReadyToDropPixelFromLeft())
                            .splineToConstantHeading(new Vector2d(10, DRIVEINY2_COLUMN1), Math.toRadians(0))
                            .splineTo(new Vector2d(30, DRIVEINY2_COLUMN1), Math.toRadians(0))

                            .splineTo(new Vector2d(58, 50), Math.toRadians(-35))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                            .afterTime(0.1, manager.openLeftClaw(0))
                            .afterTime(0.2, manager.openAutoClaw())
                            .waitSeconds(.25)

                            .build()
            );

         Actions.runBlocking(new SequentialAction(
                 drive.actionBuilder(drive.pose)
                         // Back up and pack up
                         .lineToX(50)
                         .afterDisp(1, manager.getLiftReadyToDrive())
                         .build()));
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

                        .stopAndAdd(manager.raiseLiftAfterStackPickup())
                        .lineToX(-56.5)

                        .afterDisp(3, manager.lowerLiftForDriving())
                        .afterDisp(3, manager.zeroLift())
                        .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                        // Return to backstage and drop
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-42, DRIVEINY2_COLUMN1), Math.toRadians(-10)), Math.toRadians(0))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-37, DRIVEINY2_COLUMN1), Math.toRadians(0)), Math.toRadians(0))
                        .splineToConstantHeading(new Vector2d(10, DRIVEINY2_COLUMN1), Math.toRadians(0))
                        .splineTo(new Vector2d(59.5, DRIVEINY2_COLUMN1), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .afterTime(0.1, manager.openLeftClaw(0))
                        .afterTime(0.1, manager.openAutoClaw(0))
                        .waitSeconds(.25)
                        .lineToX(55)
                        .build()
        );
    }
}