package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.edina.library.enums.PropLocation;

@Autonomous
//@Disabled
public class RedBackStageCenterInner extends RedBackStageInner {
    @Override
    protected void runPaths() {
        if (twoWhites) {
            // drive to stack - 1st trip
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            // Back up and pack up
                            .lineToX(50)
                            .afterDisp(0, manager.getLiftReadyToDrive())

                            // Drive to stacks - first trip
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(24, -10, Math.toRadians(180)), Math.toRadians(180))
                            .splineTo(new Vector2d(-44, -10), Math.toRadians(180))

                            // Prepare for grabbing - Trip 1
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                            .afterDisp(0, manager.runLiftToPosition(-123))
                            .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                            .lineToX(-53)
                            .build()
            );
        }

        if ((propLocation == PropLocation.Right || propLocation == PropLocation.Center) && dropOnBackDrop) {
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

                        .afterTime(0, manager.closeAutoClaw())
                        .afterTime(0, manager.closeLeftClaw())
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
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(-11, -12, Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(30, manager.getLiftReadyToDropPixelFromLeft())
                        .splineTo(new Vector2d(40, -12), Math.toRadians(0))
                        .splineTo(new Vector2d(59.5, -20), Math.toRadians(-35))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .waitSeconds(0.25)
                        .afterTime(0, manager.openAutoClaw())
                        .afterTime(0, manager.openLeftClaw())
                        .waitSeconds(0.25)

                        // Head to Stacks VIA C-Row
                        .setReversed(true)
                        .afterDisp(0, manager.getLiftReadyToDrive())
                        .splineToSplineHeading(new Pose2d(24, -11, Math.toRadians(180)), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineTo(new Vector2d(-44, -11), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                        // Prepare for grabbing - Trip 2
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(175)))
                        .afterDisp(0, manager.runLiftToPosition(-23))
                        .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                        .lineToX(-57)
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
                        .lineToX(-52.75)

                        // Back away and pack up
                        .stopAndAdd(manager.raiseLiftAfterStackPickup())

                        .afterDisp(3, manager.lowerLiftForDriving())
                        .afterDisp(3, manager.zeroLift())
                        .afterDisp(3, manager.positionTheClawToDriveWithPixels())
                        .lineToX(-50)

                        // Return to backstage and drop
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(-11, -12, Math.toRadians(0)), Math.toRadians(0))
                        .splineTo(new Vector2d(59.5, -12), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .afterTime(0, manager.openAutoClaw())
                        .afterTime(0, manager.openLeftClaw())
                        .waitSeconds(0.25)

                        // Head to Stacks VIA C-Row
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(24, -11, Math.toRadians(180)), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineTo(new Vector2d(-44, -11), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                        // Prepare for grabbing - Trip 2
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(175)))
                        .afterDisp(0, manager.runLiftToPosition(-23))
                        .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                        .lineToX(-57)
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

                        .stopAndAdd(manager.raiseLiftAfterStackPickup())
                        .lineToX(-53)
                        .afterDisp(3, manager.lowerLiftForDriving())
                        .afterDisp(3, manager.zeroLift())
                        .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                        // Return to backdrop and angle drop
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(-11, -12, Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(30, manager.getLiftReadyToDropPixelFromLeft())
                        .splineTo(new Vector2d(40, -12), Math.toRadians(0))
                        .splineTo(new Vector2d(59.5, -20), Math.toRadians(-35))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .waitSeconds(0.25)
                        .afterTime(0, manager.openAutoClaw())
                        .afterTime(0, manager.openLeftClaw())
                        .waitSeconds(0.25)
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

                        .stopAndAdd(manager.raiseLiftAfterStackPickup())
                        .lineToX(-53)
                        .afterDisp(3, manager.lowerLiftForDriving())
                        .afterDisp(3, manager.zeroLift())
                        .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                        // Return to backstage and drop
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(-11, -12, Math.toRadians(0)), Math.toRadians(0))
                        .splineTo(new Vector2d(59.5, -12), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .afterTime(0, manager.openAutoClaw())
                        .afterTime(0, manager.openLeftClaw())
                        .waitSeconds(0.25)
                        .lineToX(57)
                        .build()
        );
    }
}