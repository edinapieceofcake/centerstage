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
@Photon
@Config
public class RedBackStageCenter extends RedBackStage {
    public static double DRIVEINX_FIRSTPICKUP = -57.5;
    public static double DRIVEINY_FIRSTPICKUPCENTER = -12;
    public static double DRIVEINY_FIRSTPICKUPLEFT = -11.5;
    public static double DRIVEINX_SECONDPICKUP = -56.5;
    public static double DRIVEINY_SECONDPICKUPCENTER = -12;
    public static double DRIVEINY_SECONDPICKUPLEFT = -12;
    public static int EXTENDARM_FIRSTPICKUP = -120;
    public static int EXTENDARM_SECONDPICKUP = -40;

    private double DRIVEINFIRST_Y;
    private double DRIVEINSECOND_Y;
    @Override
    protected void runPaths() {
        DRIVEINFIRST_Y = DRIVEINY_FIRSTPICKUPLEFT;
        DRIVEINSECOND_Y = DRIVEINY_SECONDPICKUPLEFT;

        if (propLocation == PropLocation.Center) {
            DRIVEINFIRST_Y = DRIVEINY_FIRSTPICKUPCENTER;
            DRIVEINSECOND_Y = DRIVEINY_SECONDPICKUPCENTER;
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
                            .splineToSplineHeading(new Pose2d(24, DRIVEINFIRST_Y, Math.toRadians(180)), Math.toRadians(180))
                            .splineTo(new Vector2d(-44, DRIVEINFIRST_Y), Math.toRadians(180))

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
                        .splineToSplineHeading(new Pose2d(-11, DRIVEINSECOND_Y, Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(40, manager.getLiftReadyToDropPixelFromLeft())
                        .splineTo(new Vector2d(30, -13), Math.toRadians(0))
                        .splineTo(new Vector2d(55, -22), Math.toRadians(-20))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .waitSeconds(0.25)
                        .afterTime(0, manager.openAutoClaw())
                        .afterTime(0, manager.openLeftClaw())
                        .waitSeconds(0.25)

                        // Head to Stacks VIA C-Row
                        .setReversed(true)
                        .afterDisp(0, manager.getLiftReadyToDrive())
                        .splineToSplineHeading(new Pose2d(24, DRIVEINSECOND_Y, Math.toRadians(180)), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineTo(new Vector2d(-44, DRIVEINSECOND_Y), Math.toRadians(180))
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
                        .splineToSplineHeading(new Pose2d(-11, DRIVEINSECOND_Y, Math.toRadians(0)), Math.toRadians(0))
                        .splineTo(new Vector2d(59.5, -12), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .afterTime(0, manager.openAutoClaw())
                        .afterTime(0, manager.openLeftClaw())
                        .waitSeconds(0.25)

                        // Head to Stacks VIA C-Row
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(24, DRIVEINSECOND_Y, Math.toRadians(180)), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineTo(new Vector2d(-44, DRIVEINSECOND_Y), Math.toRadians(180))
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
                        .splineToSplineHeading(new Pose2d(-11, DRIVEINSECOND_Y, Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(40, manager.getLiftReadyToDropPixelFromLeft())
                        .splineTo(new Vector2d(30, -13), Math.toRadians(0))
                        .splineTo(new Vector2d(54.5, -22), Math.toRadians(-20))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                        .waitSeconds(0.25)
                        .afterTime(0, manager.openAutoClaw())
                        .afterTime(0, manager.openLeftClaw())
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
                        .splineToSplineHeading(new Pose2d(-11, DRIVEINSECOND_Y, Math.toRadians(0)), Math.toRadians(0))
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