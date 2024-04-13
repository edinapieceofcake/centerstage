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
public class BlueBackStageCenter extends BlueBackStage {

    public static Vector2d firstPickupLeft = new Vector2d(-63, 16);
    public static Vector2d firstPickupCenter = new Vector2d(-63, 15.75);
    public static Vector2d firstPickupRight = new Vector2d(-63, 15.75);

    public static Vector2d secondPickupLeft = new Vector2d(-67, 18.5);
    public static Vector2d secondPickupCenter = new Vector2d(-66, 18.5);
    public static Vector2d secondPickupRight = new Vector2d(-66, 19);

    public static Vector2d firstAngleDropLeft = new Vector2d(51, 21);
    public static Vector2d firstAngleDropCenter = new Vector2d(51, 21);
    public static Vector2d firstAngleDropRight = new Vector2d(51, 23);

    public static Vector2d secondAngleDropLeft = new Vector2d(50, 22);
    public static Vector2d secondAngleDropCenter = new Vector2d(50, 22);
    public static Vector2d secondAngleDropRight = new Vector2d(49, 24);

    public Vector2d firstPickup, secondPickup, firstAngleDrop, secondAngleDrop;

    public static int EXTENDARM_FIRSTPICKUP = -123;
    public static int EXTENDARM_SECONDPICKUP = -23;

    @Override
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
                            .splineToSplineHeading(new Pose2d(28, firstPickup.y-2, Math.toRadians(170)), Math.toRadians(180))
                            .afterDisp(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .splineToSplineHeading(new Pose2d(24, firstPickup.y-2, Math.toRadians(180)), Math.toRadians(180))
                            .splineTo(new Vector2d(-20, firstPickup.y-1), Math.toRadians(180))
                            .splineTo(new Vector2d(-48, firstPickup.y), Math.toRadians(180))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                            // Prepare for grabbing - Trip 1
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                            .afterDisp(0, manager.runLiftToPosition(EXTENDARM_FIRSTPICKUP, false))
                            .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                            .setReversed(false)
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

                            // Move in and grab pixels until beam break
                            .afterTime(0, manager.closeAutoClaw())
                            .afterTime(0, manager.closeLeftClaw())
                            .waitSeconds(0.1)

                            // Back away and pack up
                            .stopAndAdd(manager.raiseLiftAfterStackPickup())
                            .lineToX(-56.5)

                            .afterDisp(3, manager.lowerLiftForDriving())
                            .afterDisp(3, manager.zeroLift())
                            .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                            // Return to backdrop and angle drop
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(-19, 13, Math.toRadians(10)), Math.toRadians(0))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .splineToSplineHeading(new Pose2d(-15, 13, Math.toRadians(0)), Math.toRadians(0))
                            .splineTo(new Vector2d(30, 13), Math.toRadians(0))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                            .afterDisp(2, manager.getLiftReadyToDropPixelFromRight())
                            .splineTo(firstAngleDrop, Math.toRadians(35))
                            .stopAndAdd(manager.openLeftClaw(0))
                            .afterTime(0.5, manager.openAutoClaw(0))
                            .waitSeconds(0.6)

                            // Head to Stacks VIA C-Row
                            .setReversed(true)
                            .afterDisp(0, manager.getLiftReadyToDrive())
                            .splineToSplineHeading(new Pose2d(28, secondPickup.y-2, Math.toRadians(175)), Math.toRadians(180))
                            .afterDisp(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .splineToSplineHeading(new Pose2d(24, secondPickup.y-2, Math.toRadians(180)), Math.toRadians(180))

                            .splineTo(new Vector2d(-16, secondPickup.y-1), Math.toRadians(180))
                            .splineTo(new Vector2d(-44, secondPickup.y), Math.toRadians(180))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                            // Prepare for grabbing - Trip 2
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(175)))
                            .afterDisp(0, manager.runLiftToPosition(EXTENDARM_SECONDPICKUP, false))
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

                        .afterTime(0, manager.closeAutoClaw())
                        .afterTime(0, manager.closeLeftClaw())
                        .waitSeconds(0.1)

                        // Back away and pack up
                        .stopAndAdd(manager.raiseLiftAfterStackPickup())
                        .lineToX(-52.75)

                        .afterDisp(3, manager.lowerLiftForDriving())
                        .afterDisp(3, manager.zeroLift())
                        .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                        // Return to backdrop and angle drop
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(-19, 13, Math.toRadians(5)), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(-15, 13, Math.toRadians(0)), Math.toRadians(0))
                        .splineTo(new Vector2d(56, 13), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .afterTime(0, manager.openLeftClaw(0))
                        .afterTime(0, manager.openAutoClaw(0))
                        .waitSeconds(0.25)

                        // Head to Stacks VIA C-Row
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(28, secondPickup.y-2, Math.toRadians(175)), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(24, secondPickup.y-2, Math.toRadians(180)), Math.toRadians(180))

                        .splineTo(new Vector2d(-16, secondPickup.y-1), Math.toRadians(180))
                        .splineTo(new Vector2d(-44, secondPickup.y), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                        // Prepare for grabbing - Trip 2
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(175)))
                        .afterDisp(0, manager.runLiftToPosition(EXTENDARM_SECONDPICKUP, false))
                        .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                        .lineToX(secondPickup.x)
                        .build()
        );    }

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
                            .lineToX(-56.5)

                            .afterDisp(3, manager.lowerLiftForDriving())
                            .afterDisp(3, manager.zeroLift())
                            .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                            // Return to backdrop and angle drop
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(-19, 14, Math.toRadians(5)), Math.toRadians(0))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .splineToSplineHeading(new Pose2d(-15, 14, Math.toRadians(0)), Math.toRadians(0))
                            .splineTo(new Vector2d(30, 14), Math.toRadians(0))

                            .afterDisp(2, manager.getLiftReadyToDropPixelFromRight())
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                            .splineTo(secondAngleDrop, Math.toRadians(35))
                            .stopAndAdd(manager.openLeftClaw(0))
                            .afterTime(0.5, manager.openAutoClaw(0))
                            .build()
            );

            Actions.runBlocking(new SequentialAction(
                    drive.actionBuilder(drive.pose)
                            // Back up and pack up
                            .lineToX(48)
                            .afterDisp(1, manager.getLiftReadyToDrive())
                            .build())
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
                        .splineToSplineHeading(new Pose2d(-19, 14, Math.toRadians(5)), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(-15, 14, Math.toRadians(0)), Math.toRadians(0))
                        .splineTo(new Vector2d(54, 14), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .afterTime(0, manager.openAutoClaw(0))
                        .afterTime(0, manager.openLeftClaw(0))
                        .waitSeconds(0.25)
                        .lineToX(48)
                        .build()
        );
    }
}