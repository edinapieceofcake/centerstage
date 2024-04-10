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
    public static Vector2d firstPickupLeft = new Vector2d(-56.5, -12.5);
    public static Vector2d firstPickupCenter = new Vector2d(-56.5, -10.5);
    public static Vector2d firstPickupRight = new Vector2d(-56.5, -10.5);

    public static Vector2d secondPickupLeft = new Vector2d(-56.5, -13);
    public static Vector2d secondPickupCenter = new Vector2d(-56.5, -11);
    public static Vector2d secondPickupRight = new Vector2d(-56.5, -11);

    public static Vector2d firstAngleDropLeft = new Vector2d(57, -22);
    public static Vector2d firstAngleDropCenter = new Vector2d(57, -22);
    public static Vector2d firstAngleDropRight = new Vector2d(57, -22);

    public static Vector2d secondAngleDropLeft = new Vector2d(58, -22);
    public static Vector2d secondAngleDropCenter = new Vector2d(58, -25);
    public static Vector2d secondAngleDropRight = new Vector2d(58, -25);

    public Vector2d firstPickup, secondPickup, firstAngleDrop, secondAngleDrop;

    public static int EXTENDARM_FIRSTPICKUP = -180;
    public static int EXTENDARM_SECONDPICKUP = -40;

    @Override
    protected void runPaths() {

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
                            .splineToSplineHeading(new Pose2d(24, firstPickup.y, Math.toRadians(180)), Math.toRadians(180))
                            .splineTo(new Vector2d(-44, firstPickup.y), Math.toRadians(180))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                            // Prepare for grabbing - Trip 1
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                            .afterDisp(0, manager.runLiftToPosition(EXTENDARM_FIRSTPICKUP, true))
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
                        .splineToSplineHeading(new Pose2d(-11, secondPickup.y, Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(40, manager.getLiftReadyToDropPixelFromLeft())
                        .splineTo(new Vector2d(30, -13), Math.toRadians(0))
                        .splineTo(firstAngleDrop, Math.toRadians(-20))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .waitSeconds(0.25)
                        .afterTime(.1, manager.openAutoClaw(0))
                        .afterTime(0, manager.openLeftClaw(0))
                        .waitSeconds(0.25)

                        // Head to Stacks VIA C-Row
                        .setReversed(true)
                        .afterDisp(0, manager.getLiftReadyToDrive())
                        .splineToSplineHeading(new Pose2d(24, secondPickup.y, Math.toRadians(180)), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineTo(new Vector2d(-44, secondPickup.y), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                        // Prepare for grabbing - Trip 2
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(175)))
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
                        .splineToSplineHeading(new Pose2d(-11, secondPickup.y, Math.toRadians(0)), Math.toRadians(0))
                        .splineTo(new Vector2d(59.5, -12), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .afterTime(0, manager.openAutoClaw(0))
                        .afterTime(0, manager.openLeftClaw(0))
                        .waitSeconds(0.25)

                        // Head to Stacks VIA C-Row
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(24, secondPickup.y, Math.toRadians(180)), Math.toRadians(180))
                        .splineTo(new Vector2d(-44, secondPickup.y), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                        // Prepare for grabbing - Trip 2
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(175)))
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
                        .splineToSplineHeading(new Pose2d(-11, secondPickup.y, Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(40, manager.getLiftReadyToDropPixelFromLeft())
                        .splineTo(new Vector2d(30, -13), Math.toRadians(0))

                        .splineTo(secondAngleDrop, Math.toRadians(-20))
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
                        .splineToSplineHeading(new Pose2d(-11, secondPickup.y, Math.toRadians(0)), Math.toRadians(0))
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