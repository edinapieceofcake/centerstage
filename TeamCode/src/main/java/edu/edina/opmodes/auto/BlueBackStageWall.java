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

    public static Vector2d firstPickupLeft = new Vector2d(-59, 38);
    public static Vector2d firstPickupCenter = new Vector2d(-59, 37.5);
    public static Vector2d firstPickupRight = new Vector2d(-58.5, 38);

    public static Vector2d secondPickupLeft = new Vector2d(-61, 36);
    public static Vector2d secondPickupCenter = new Vector2d(-61.5, 36);
    public static Vector2d secondPickupRight = new Vector2d(-61, 36.75);

    public static Vector2d firstAngleDropLeft = new Vector2d(52, 53);
    public static Vector2d firstAngleDropCenter = new Vector2d(52, 53);
    public static Vector2d firstAngleDropRight = new Vector2d(52, 53.5);

    public static Vector2d secondAngleDropLeft = new Vector2d(56, 49);
    public static Vector2d secondAngleDropCenter = new Vector2d(56.5, 49);
    public static Vector2d secondAngleDropRight = new Vector2d(55.5, 50);

    public Vector2d firstPickup, secondPickup, firstAngleDrop, secondAngleDrop;

    public static int EXTENDARM_FIRSTPICKUP = -123;
    public static int EXTENDARM_SECONDPICKUP = -23;

     protected void runPaths() {

         // drive to stack - 1st trip
         switch (propLocation) {
             case Left:
                 firstPickup = firstPickupLeft;
                 secondPickup = secondPickupLeft;
                 firstAngleDrop = firstAngleDropLeft;
                 secondAngleDrop = secondAngleDropLeft;
                 break;
             case Center:
                 firstPickup = firstPickupCenter;
                 secondPickup = secondPickupCenter;
                 firstAngleDrop = firstAngleDropCenter;
                 secondAngleDrop = secondAngleDropCenter;
                 break;
             case Right:
             default:
                 firstPickup = firstPickupRight;
                 secondPickup = secondPickupRight;
                 firstAngleDrop = firstAngleDropRight;
                 secondAngleDrop = secondAngleDropRight;
                 break;
         }

         if (twoWhites) {
             // drive to stack - 1st trip

             Actions.runBlocking(
                     drive.actionBuilder(drive.pose)
                             // Back up and pack up
                             .lineToX(43)
                             .afterDisp(0, manager.getLiftReadyToDrive())

                             // Drive to stacks - first trip
                             .setReversed(true)
                             .splineToSplineHeading(new Pose2d(10, 60, Math.toRadians(170)), Math.toRadians(180))
                             .afterDisp(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                             .splineToSplineHeading(new Pose2d(6, 60, Math.toRadians(180)), Math.toRadians(180))

                             .splineToConstantHeading(new Vector2d(-36, 60), Math.toRadians(180))
                             .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                             .splineToSplineHeading(new Pose2d(-50, firstPickup.y, Math.toRadians(180)), Math.toRadians(180))

                             // Prepare for grabbing - Trip 1
                             .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                             .afterDisp(0, manager.runLiftToPosition(EXTENDARM_FIRSTPICKUP, false))
                             .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                             .lineToX(firstPickup.x)
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
                            .splineToSplineHeading(new Pose2d(-35, 58, Math.toRadians(0)), Math.toRadians(0))

                            //.afterDisp(30, manager.getLiftReadyToDropPixelFromLeft())
                            .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(0))
                            .afterDisp(0, manager.getLiftReadyToDropPixelFromLeft())
                            .splineTo(new Vector2d(30, 58), Math.toRadians(0))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                            .splineTo(firstAngleDrop, Math.toRadians(-35))
                            .afterTime(0, manager.openLeftClaw(0))
                            .afterTime(0.3, manager.openAutoClaw(0))
                            .waitSeconds(0.4)

                            // Head to Stacks VIA A-Row
                            .setReversed(true)
                            .afterDisp(0, manager.getLiftReadyToDrive())

                            // Head to Stacks VIA Wall
                            .splineToSplineHeading(new Pose2d(24, 58, Math.toRadians(170)), Math.toRadians(180))
                            .afterDisp(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .splineToSplineHeading(new Pose2d(20, 58, Math.toRadians(180)), Math.toRadians(180))

                            .splineToConstantHeading(new Vector2d(-38, 58), Math.toRadians(180))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                            .splineToSplineHeading(new Pose2d(-50, secondPickup.y, Math.toRadians(180)), Math.toRadians(180))

                            // Prepare for grabbing - Trip 2
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                            .afterDisp(0, manager.runLiftToPosition(EXTENDARM_SECONDPICKUP, true))
                            .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                            .lineToX(secondPickup.x)
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
                         .splineToSplineHeading(new Pose2d(new Vector2d(-35, 57), Math.toRadians(0)), Math.toRadians(0))

                         .splineToConstantHeading(new Vector2d(10, 57), Math.toRadians(0))
                         .splineTo(new Vector2d(61, 57), Math.toRadians(0))
                         .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                         .afterTime(0.1, manager.openLeftClaw())
                         .afterTime(0.1, manager.openAutoClaw())
                         .waitSeconds(0.2)

                         // Head to Stacks VIA A-Row
                         .setReversed(true)

                         // Head to Stacks VIA Wall
                         .splineToSplineHeading(new Pose2d(4, 57, Math.toRadians(190)), Math.toRadians(180))
                         .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                         .splineToSplineHeading(new Pose2d(0, 57, Math.toRadians(180)), Math.toRadians(180))

                         .splineToConstantHeading(new Vector2d(-38, 57), Math.toRadians(180))
                         .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                         .splineToConstantHeading(new Vector2d(-50, secondPickup.y), Math.toRadians(180))

                         // Prepare for grabbing - Trip 2
                         .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                         .afterDisp(0, manager.runLiftToPosition(EXTENDARM_SECONDPICKUP, true))
                         .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                         .lineToX(secondPickup.x)
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
                            .splineToSplineHeading(new Pose2d(new Vector2d(-42, 58), Math.toRadians(0)), Math.toRadians(0))

                            .afterDisp(30, manager.getLiftReadyToDropPixelFromLeft())
                            .splineTo(new Vector2d(30, 58), Math.toRadians(0))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                            .splineTo(secondAngleDrop, Math.toRadians(-35))
                            .afterTime(0.1, manager.openLeftClaw(0))
                            .afterTime(0.3, manager.openAutoClaw(0))
                            .waitSeconds(0.4)
                            .lineToX(50)
                            .afterDisp(0, manager.getLiftReadyToDrive())
                            .build()
            );

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
                        .splineToSplineHeading(new Pose2d(new Vector2d(-37, 56), Math.toRadians(0)), Math.toRadians(0))
                        .splineToConstantHeading(new Vector2d(10, 56), Math.toRadians(0))
                        .splineTo(new Vector2d(60.5, 56), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .afterTime(0.1, manager.openLeftClaw(0))
                        .afterTime(0.1, manager.openAutoClaw(0))
                        .waitSeconds(0.1)
                        .lineToX(55)
                        .build()
        );
    }
}