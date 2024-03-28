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
                             .splineToSplineHeading(new Pose2d(0, 60, Math.toRadians(-180)), Math.toRadians(180))
                             .afterDisp(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                             .splineToConstantHeading(new Vector2d(-38, 60), Math.toRadians(180))
                             .splineToConstantHeading(new Vector2d(-50, 38), Math.toRadians(180))
                             .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                             // Prepare for grabbing - Trip 1
                             .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                             .afterDisp(0, manager.runLiftToPosition(-123))
                             .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                             .lineToX(-59)
                             .build()
             );
         }

         if ((propLocation == PropLocation.Left || propLocation == PropLocation.Center) && dropOnBackDrop) {
             dropOnBackDrop = false;
             dropOnBackStage = true;
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
                            .splineToSplineHeading(new Pose2d(new Vector2d(-35, 58), Math.toRadians(0)), Math.toRadians(0))
                            .afterDisp(0, manager.getLiftReadyToDropPixelFromLeft())
                            .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(0))
                            .splineTo(new Vector2d(30, 58), Math.toRadians(0))
                            .splineTo(new Vector2d(51.5, 52), Math.toRadians(-35))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                            .afterTime(0.1, manager.openLeftClaw())
                            .afterTime(0.1, manager.openAutoClaw())
                            .waitSeconds(0.25)

                            // Head to Stacks VIA A-Row
                            .setReversed(true)
                            .afterDisp(0, manager.getLiftReadyToDrive())

                            // Head to Stacks VIA Wall
                            .splineToSplineHeading(new Pose2d(0, 58, Math.toRadians(-180)), Math.toRadians(180))
                            .afterDisp(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .splineToConstantHeading(new Vector2d(-38, 58), Math.toRadians(180))
                            .splineToConstantHeading(new Vector2d(-50, 38), Math.toRadians(180))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                            // Prepare for grabbing - Trip 2
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                            .afterDisp(0, manager.runLiftToPosition(-23))
                            .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                            .lineToX(-61)
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
                         .splineToSplineHeading(new Pose2d(new Vector2d(-35, 58), Math.toRadians(0)), Math.toRadians(0))
                         .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(0))
                         .splineTo(new Vector2d(59, 58), Math.toRadians(0))
                         .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                         .afterTime(0.1, manager.openLeftClaw())
                         .afterTime(0.1, manager.openAutoClaw())
                         .waitSeconds(0.25)

                         // Head to Stacks VIA A-Row
                         .setReversed(true)

                         // Head to Stacks VIA Wall
                         .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                         .splineToSplineHeading(new Pose2d(0, 58, Math.toRadians(-180)), Math.toRadians(180))
                         .splineToConstantHeading(new Vector2d(-38, 58), Math.toRadians(180))
                         .splineToConstantHeading(new Vector2d(-50, 38), Math.toRadians(180))
                         .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                         // Prepare for grabbing - Trip 2
                         .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                         .afterDisp(0, manager.runLiftToPosition(-23))
                         .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                         .lineToX(-62)
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
                            .splineToSplineHeading(new Pose2d(new Vector2d(-35, 58), Math.toRadians(0)), Math.toRadians(0))
                            .afterDisp(5, manager.getLiftReadyToDropPixelFromLeft())
                            .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(0))
                            .splineTo(new Vector2d(30, 58), Math.toRadians(0))
                            .splineTo(new Vector2d(54, 53), Math.toRadians(-35))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                            .afterTime(0.1, manager.openLeftClaw())
                            .afterTime(0.1, manager.openAutoClaw())
                            .waitSeconds(.25)

                            .build()
            );

         Actions.runBlocking(new SequentialAction(
                 drive.actionBuilder(drive.pose)
                         // Back up and pack up
                         .lineToX(46)
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
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, 58), Math.toRadians(0)), Math.toRadians(0))
                        .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(0))
                        .splineTo(new Vector2d(59.5, 58), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .afterTime(0.1, manager.openLeftClaw())
                        .afterTime(0.1, manager.openAutoClaw())
                        .waitSeconds(.25)
                        .lineToX(55)
                        .build()
        );
    }
}