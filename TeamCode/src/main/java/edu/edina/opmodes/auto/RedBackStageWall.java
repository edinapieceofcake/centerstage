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
@Config
@Photon
public class RedBackStageWall extends RedBackStage {
    public static double DRIVEINY_FIRSTPICKUP = -34;
    public static double DRIVEINY_SECONDPICKUP = -32;
    public static int EXTENDARM_FIRSTPICKUP = -170;
    public static int EXTENDARM_SECONDPICKUP = -20;
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
                            .splineToSplineHeading(new Pose2d(0, -58, Math.toRadians(-180)), Math.toRadians(180))
                            .splineToConstantHeading(new Vector2d(-38, -58), Math.toRadians(180))
                            .splineToConstantHeading(new Vector2d(-51, DRIVEINY_FIRSTPICKUP), Math.toRadians(180))

                            // Prepare for grabbing - Trip 1
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                            .afterDisp(0, manager.runLiftToPositionWithDuration(EXTENDARM_FIRSTPICKUP, 500, false))
                            .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
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

                        // Return to backdrop and angle drop
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, -58), Math.toRadians(0)), Math.toRadians(0))

                        .afterDisp(0, manager.lowerLiftForDriving())
                        .afterDisp(0, manager.zeroLift())
                        .afterDisp(0, manager.positionTheClawToDriveWithPixels())
                        .afterDisp(30, manager.getLiftReadyToDropPixelFromRight())

                        .splineToConstantHeading(new Vector2d(10, -58), Math.toRadians(0))
                        .splineTo(new Vector2d(40, -58), Math.toRadians(0))
                        .splineTo(new Vector2d(57, -52), Math.toRadians(20))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .afterTime(0.1, manager.openAutoClaw())
                        .afterTime(0.1, manager.openLeftClaw())
                        .waitSeconds(0.25)

                        // Head to Stacks VIA A-Row
                        .setReversed(true)
                        .afterDisp(0, manager.getLiftReadyToDrive())

                        // Head to Stacks VIA Wall
                        .splineToSplineHeading(new Pose2d(0, -58, Math.toRadians(-180)), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToConstantHeading(new Vector2d(-38, -58), Math.toRadians(180))
                        .splineToConstantHeading(new Vector2d(-50, DRIVEINY_SECONDPICKUP), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                        // Prepare for grabbing - Trip 2
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                        .afterDisp(0, manager.runLiftToPositionWithDuration(EXTENDARM_SECONDPICKUP, 500, false))
                        .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                        .lineToX(-55)
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

                        // Return to backstage and drop
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, -58), Math.toRadians(0)), Math.toRadians(0))

                        .afterDisp(0, manager.lowerLiftForDriving())
                        .afterDisp(0, manager.zeroLift())
                        .afterDisp(0, manager.positionTheClawToDriveWithPixels())

                        .splineToConstantHeading(new Vector2d(57, -58), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .afterTime(0.1, manager.openAutoClaw())
                        .afterTime(0.1, manager.openLeftClaw())
                        .waitSeconds(0.1)

                        // Head to Stacks VIA A-Row
                        .setReversed(true)

                        // Head to Stacks VIA Wall
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(0, -58, Math.toRadians(-180)), Math.toRadians(180))
                        .splineToConstantHeading(new Vector2d(-38, -58), Math.toRadians(180))
                        .splineToConstantHeading(new Vector2d(-50, DRIVEINY_SECONDPICKUP), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                        // Prepare for grabbing - Trip 2
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                        .afterDisp(0, manager.runLiftToPositionWithDuration(EXTENDARM_SECONDPICKUP, 500, false))
                        .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                        .lineToX(-55)
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

                        .stopAndAdd(manager.raiseLiftAfterStackPickup())

                        // Return to backdrop and angle drop
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, -58), Math.toRadians(0)), Math.toRadians(0))

                        .afterDisp(0, manager.lowerLiftForDriving())
                        .afterDisp(0, manager.zeroLift())
                        .afterDisp(0, manager.positionTheClawToDriveWithPixels())
                        .afterDisp(30, manager.getLiftReadyToDropPixelFromRight())

                        .splineToConstantHeading(new Vector2d(10, -58), Math.toRadians(0))
                        .splineTo(new Vector2d(40, -58), Math.toRadians(0))
                        .splineTo(new Vector2d(57, -50), Math.toRadians(20))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .afterTime(0.1, manager.openAutoClaw())
                        .afterTime(0.1, manager.openLeftClaw())
                        .waitSeconds(.25)
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

                        .stopAndAdd(manager.raiseLiftAfterStackPickup())

                        // Return to backstage and drop
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, -58), Math.toRadians(0)), Math.toRadians(0))

                        .afterDisp(0, manager.lowerLiftForDriving())
                        .afterDisp(0, manager.zeroLift())
                        .afterDisp(0, manager.positionTheClawToDriveWithPixels())

                        .splineToConstantHeading(new Vector2d(57, -58), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .afterTime(0.1, manager.openAutoClaw())
                        .afterTime(0.1, manager.openLeftClaw())
                        .waitSeconds(.1)
                        .lineToX(53)

                        .build()
        );
    }
}