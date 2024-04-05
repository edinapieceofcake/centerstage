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
public class RedAudienceWall extends RedAudience {

    public static double DRIVEINX_FIRSTPICKUP = -60;
    public static double DRIVEINY_FIRSTPICKUP = -35.5;
    public static double DRIVEINX_SECONDPICKUP = -63;
    public static double DRIVEINY_SECONDPICKUP = -35.5;
    public static int EXTENDARM_FIRSTPICKUP = -205;
    public static int EXTENDARM_SECONDPICKUP = -100;

    @Override
    protected void runPaths() {

        if (yellowPixel) {  // P + Y + 1W path
            purpleToStack(); // B

            // If we are doing anything but stopping after purple
            if (makeSecondTrip) {  // P + Y + 3W path
                stackToYellowToStack(); // DPAD_UP/DOWN
                if (dropOnBackdrop) {
                    stackToAngleDrop(); // DPAD_UP
                } else if (dropOnBackstage) {
                    stackToBackStageDrop(); // DPAD_DOWN
                }
            } else {
                stackToYellow(); // C
            }
        }
    }

    protected void purpleToStack() {
        logPose();

        // Drive to Stack Pick up 1st white
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Head to Stacks
                        .setReversed(true)
                        .splineTo(new Vector2d(-33, -55), Math.toRadians(315))
                        .setReversed(false)

                        .splineToSplineHeading(new Pose2d(-44, DRIVEINY_FIRSTPICKUP, Math.toRadians(180)), Math.toRadians(180))

                        .lineToX(DRIVEINX_FIRSTPICKUP)
                        // Prepare for grabbing - Trip 1
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                        .afterDisp(0, manager.runLiftToPosition(EXTENDARM_FIRSTPICKUP, true))
                        .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())

                        .build()
        );
    }

    protected void stackToYellow() {
        logPose();

        // Run to backdrop.
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Back away and pack up
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))

                        // Move in and grab pixels until beam break
                        .afterTime(0, manager.closeLeftClaw())

                        // Back away a little and raise the lift
                        .lineToX(-57.5)
                        .stopAndAdd(manager.raiseLiftAfterStackPickup())
                        .waitSeconds(delayTime / 1000) // Optional Wait

                        // Finish backing away and prepare to drive
                        .lineToX(-54)
                        .afterDisp(3, manager.lowerLiftForDriving())
                        .afterDisp(3, manager.zeroLift())
                        .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                        // Head to backdrop
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-40, -59), Math.toRadians(-10)), Math.toRadians(0))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, -59), Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(30, manager.getLiftReadyToDropThePixelHighOnTheWall())
                        .splineToConstantHeading(new Vector2d(10, -59), Math.toRadians(0))
                        .splineToConstantHeading(backdropDropLocationAW, Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                        // Release Yellow + White
                        .stopAndAdd(manager.openRightClaw(0))
                        .afterTime(0.25, manager.openLeftClaw(0))
                        .afterTime(0.25, manager.openAutoClaw(0))
                        .waitSeconds(0.25)
                        .lineToX(48)
                        .build()
        );

    }

    protected void stackToYellowToStack() {

        // Run to backdrop.
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Disable beam break
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))

                        // Grab Pixels
                        .afterTime(0, manager.closeLeftClaw())

                        // Back away a little and raise the lift
                        .lineToX(-57.5)
                        .stopAndAdd(manager.raiseLiftAfterStackPickup())
                        .waitSeconds(delayTime / 1000)

                        // Finish backing away and prepare to drive
                        .lineToX(-53)
                        .afterDisp(3, manager.lowerLiftForDriving())
                        .afterDisp(3, manager.zeroLift())
                        .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                        // Return to backdrop
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-40, -59), Math.toRadians(-10)), Math.toRadians(0))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, -59), Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(30, manager.getLiftReadyToDropThePixelHighOnTheWall())
                        .splineToConstantHeading(new Vector2d(10, -59), Math.toRadians(0))
                        .splineToConstantHeading(backdropDropLocationAW, Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                        // Release Yellow + White
                        .stopAndAdd(manager.openRightClaw(0))
                        .afterTime(0.25, manager.openLeftClaw(0))
                        .afterTime(0.25, manager.openAutoClaw(0))
                        .waitSeconds(0.25)

                        // Back up and pack up
                        .lineToX(47)
                        .afterDisp(0, manager.getLiftReadyToDrive())

                        // Head to Stacks VIA Wall
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(6, -59, Math.toRadians(190)), Math.toRadians(180))
                        .splineToSplineHeading(new Pose2d(0, -59, Math.toRadians(180)), Math.toRadians(180))
                        .splineToConstantHeading(new Vector2d(-38, -59), Math.toRadians(180))
                        .splineToConstantHeading(new Vector2d(-55,  DRIVEINY_SECONDPICKUP), Math.toRadians(180))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                        // Prepare for grabbing - Trip 2
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                        .afterDisp(0, manager.runLiftToPosition(EXTENDARM_SECONDPICKUP, true))
                        .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                        .lineToX(DRIVEINX_SECONDPICKUP)
                        .build()
        );
    }

    protected void stackToAngleDrop() {
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Turn off Beam Break
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))

                        // Grab Pixels
                        .afterTime(0, manager.closeAutoClaw())
                        .afterTime(0, manager.closeLeftClaw())
                        .afterTime(0, manager.closeRightClaw())

                        // Back away a little and raise the lift
                        .waitSeconds(0.1)
                        .stopAndAdd(manager.raiseLiftAfterStackPickup())

                        // Finish backing away and prepare to drive
                        .afterDisp(3, manager.lowerLiftForDriving())
                        .afterDisp(3, manager.zeroLift())
                        .afterDisp(3, manager.positionTheClawToDriveWithPixels())
                        .lineToX(-50)

                        // Return to backdrop and angle drop
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-40, -59), Math.toRadians(-10)), Math.toRadians(0))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, -59), Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(30, manager.getLiftReadyToDropPixelFromRight())
                        .splineToConstantHeading(new Vector2d(10, -59), Math.toRadians(0))
                        .splineTo(new Vector2d(30, -59), Math.toRadians(0))
                        .splineTo(new Vector2d(51, -54), Math.toRadians(35))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                        // Release all pixels
                        .afterTime(0.0, manager.openLeftClaw(0))
                        .afterTime(0.0, manager.openRightClaw(0))
                        .afterTime(0.2, manager.openAutoClaw(0))
                        .waitSeconds(.25)
                        .build()
        );

        Actions.runBlocking(new SequentialAction(
                drive.actionBuilder(drive.pose)
                        // Back up and pack up
                        .lineToX(47)
                        .afterDisp(1, manager.getLiftReadyToDrive())
                        .build()));
    }

    protected void stackToBackStageDrop() {
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Turn off Beam Break
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))

                        // Grab Pixels
                        .afterTime(0, manager.closeAutoClaw())
                        .afterTime(0, manager.closeLeftClaw())
                        .afterTime(0, manager.closeRightClaw())

                        // Back away a little and raise the lift
                        .waitSeconds(0.1)
                        .stopAndAdd(manager.raiseLiftAfterStackPickup())

                        // Finish backing away and prepare to drive
                        .afterDisp(3, manager.lowerLiftForDriving())
                        .afterDisp(3, manager.zeroLift())
                        .afterDisp(3, manager.positionTheClawToDriveWithPixels())
                        .lineToX(-50)

                        // Return to backdrop and angle drop
                        .setReversed(true)
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-40, -59), Math.toRadians(-10)), Math.toRadians(0))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, -59), Math.toRadians(0)), Math.toRadians(0))
                        .splineToConstantHeading(new Vector2d(10, -59), Math.toRadians(0))
                        .splineTo(new Vector2d(50, -59), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                        // Release all pixels
                        .afterTime(0, manager.openLeftClaw(0))
                        .afterTime(0, manager.openRightClaw(0))
                        .afterTime(0.2, manager.openAutoClaw(0))
                        .waitSeconds(0.25)
                        .build()
        );

        Actions.runBlocking(new SequentialAction(
                drive.actionBuilder(drive.pose)
                        // Back up
                        .lineToX(48)
                        .build()));
    }
}