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
//@Disabled
public class RedAudienceCenter extends RedAudience {

    public static double DRIVEINX_FIRSTPICKUP = -60;
    public static double DRIVEINY_FIRSTPICKUPLEFT = -12;
    public static double DRIVEINY_FIRSTPICKUPCENTER = -10;
    public static double DRIVEINY_FIRSTPICKUPRIGHT = -10.5;
    public static double DRIVEINX_SECONDPICKUP = -61.5;
    public static double DRIVEINY_SECONDPICKUP = -11;
    public static int EXTENDARM_FIRSTPICKUP = -180;
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

        switch(propLocation) {
            case Left:
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(-35, -12, Math.toRadians(180)), Math.toRadians(45))
                                // Prepare for grabbing - Trip 1
                                .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                                .afterDisp(0, manager.runLiftToPosition(EXTENDARM_FIRSTPICKUP))
                                .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                                .setReversed(false)
                                .splineToSplineHeading(new Pose2d(-55, DRIVEINY_FIRSTPICKUPLEFT, Math.toRadians(180)), Math.toRadians(180))
                                .lineToX(DRIVEINX_FIRSTPICKUP)
                                .build()
                );
                break;
            case Center:
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(-48, -18, Math.toRadians(180)), Math.toRadians(90))
                                .splineToSplineHeading(new Pose2d(-50, DRIVEINY_FIRSTPICKUPCENTER, Math.toRadians(180)), Math.toRadians(90))
                                .setReversed(false)
                                // Prepare for grabbing - Trip 1
                                .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                                .afterDisp(0, manager.runLiftToPosition(EXTENDARM_FIRSTPICKUP))
                                .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                                .lineToX(DRIVEINX_FIRSTPICKUP)
                                .build()
                );
                break;
            default:    // Left or unknown
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(-45, DRIVEINY_FIRSTPICKUPRIGHT, Math.toRadians(180)), Math.toRadians(90))
                                // Prepare for grabbing - Trip 1
                                .setReversed(false)
                                .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                                .afterDisp(0, manager.runLiftToPosition(EXTENDARM_FIRSTPICKUP))
                                .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                                .lineToX(DRIVEINX_FIRSTPICKUP)
                                .build()
                );
                break;
        }
    }

    protected void stackToYellow() {

        if (propLocation == PropLocation.Right) {
            // Run to backdrop.
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            // Back away and pack up
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))

                            // Move in and grab pixels until beam break
                            .afterTime(0, manager.closeLeftClaw())

                            // Back away a little and raise the lift
                            .lineToX(-56.5)
                            .stopAndAdd(manager.raiseLiftAfterStackPickup())
                            .waitSeconds(delayTime/1000) // Optional Wait

                            // Finish backing away and prepare to drive
                            .lineToX(-53)
                            .afterDisp(3, manager.lowerLiftForDriving())
                            .afterDisp(3, manager.zeroLift())
                            .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                            // Head to backdrop
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(new Vector2d(-35, -11), Math.toRadians(0)), Math.toRadians(0))
                            .afterDisp(25, manager.getLiftReadyToDropThePixelHighOnTheWall())
                            .splineToConstantHeading(new Vector2d(10, -11), Math.toRadians(0))
                            .splineToConstantHeading(backdropDropLocation, Math.toRadians(0))

                            // Release Yellow + White
                            .stopAndAdd(manager.openRightClaw())

                            .setReversed(true)
                            .splineToConstantHeading(backdropDropLocationSecond, Math.toRadians(0))
                            .setReversed(false)
                            .lineToX(50)

                            .stopAndAdd(manager.openLeftClaw())
                            .stopAndAdd(manager.openAutoClaw())
                            .waitSeconds(0.25)
                            .build()
            );
        } else {
            // Run to backdrop.
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            // Back away and pack up
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))

                            // Move in and grab pixels until beam break
                            .afterTime(0, manager.closeLeftClaw())

                            // Back away a little and raise the lift
                            .lineToX(-56.5)
                            .stopAndAdd(manager.raiseLiftAfterStackPickup())
                            .waitSeconds(delayTime / 1000) // Optional Wait

                            // Finish backing away and prepare to drive
                            .lineToX(-53)
                            .afterDisp(3, manager.lowerLiftForDriving())
                            .afterDisp(3, manager.zeroLift())
                            .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                            // Head to backdrop
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(new Vector2d(-35, -11), Math.toRadians(0)), Math.toRadians(0))
                            .afterDisp(25, manager.getLiftReadyToDropThePixelHighOnTheWall())
                            .splineToConstantHeading(new Vector2d(10, -11), Math.toRadians(0))
                            .splineToConstantHeading(backdropDropLocation, Math.toRadians(0))

                            // Release Yellow + White
                            .stopAndAdd(manager.openRightClaw())
                            .afterTime(0.25, manager.openLeftClaw())
                            .afterTime(0.25, manager.openAutoClaw())
                            .waitSeconds(0.25)
                            .build()
            );
        }
    }

    protected void stackToYellowToStack() {

        if (propLocation == PropLocation.Right) {
            // Run to backdrop.
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            // Disable beam break
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))

                            // Grab Pixels
                            .afterTime(0, manager.closeLeftClaw())

                            // Back away a little and raise the lift
                            .lineToX(-56.5)
                            .stopAndAdd(manager.raiseLiftAfterStackPickup())
                            .waitSeconds(delayTime / 1000)

                            // Finish backing away and prepare to drive
                            .lineToX(-53)
                            .afterDisp(3, manager.lowerLiftForDriving())
                            .afterDisp(3, manager.zeroLift())
                            .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                            // Return to backdrop
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(new Vector2d(-35, -11), Math.toRadians(0)), Math.toRadians(0))
                            .afterDisp(25, manager.getLiftReadyToDropThePixelHighOnTheWall())
                            .splineToConstantHeading(new Vector2d(10, -11), Math.toRadians(0))
                            .splineToConstantHeading(backdropDropLocation, Math.toRadians(0))

                            // Release Yellow + White
                            .stopAndAdd(manager.openRightClaw())
                            .setReversed(true)
                            .splineToConstantHeading(backdropDropLocationSecond, Math.toRadians(0))
                            .setReversed(false)
                            .lineToX(50)
                            .stopAndAdd(manager.openLeftClaw())
                            .stopAndAdd(manager.openAutoClaw())
                            .waitSeconds(0.25)

                            // Back up and pack up
                            .lineToX(50)
                            .afterTime(0.5, manager.getLiftReadyToDrive())

                            // Drive back to stacks
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(24, -10.5, Math.toRadians(180)), Math.toRadians(180))
                            .splineTo(new Vector2d(-48, DRIVEINY_SECONDPICKUP), Math.toRadians(180))

                            // Prepare for grabbing - Trip 2
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                            .afterDisp(0, manager.runLiftToPosition(EXTENDARM_SECONDPICKUP))
                            .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                            .lineToX(DRIVEINX_SECONDPICKUP)
                            .build()
            );
        } else {
            // Run to backdrop.
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            // Disable beam break
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))

                            // Grab Pixels
                            .afterTime(0, manager.closeLeftClaw())

                            // Back away a little and raise the lift
                            .lineToX(-56.5)
                            .stopAndAdd(manager.raiseLiftAfterStackPickup())
                            .waitSeconds(delayTime / 1000)

                            // Finish backing away and prepare to drive
                            .lineToX(-53)
                            .afterDisp(3, manager.lowerLiftForDriving())
                            .afterDisp(3, manager.zeroLift())
                            .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                            // Return to backdrop
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(new Vector2d(-35, -11), Math.toRadians(0)), Math.toRadians(0))
                            .afterDisp(25, manager.getLiftReadyToDropThePixelHighOnTheWall())
                            .splineToConstantHeading(new Vector2d(10, -11), Math.toRadians(0))
                            .splineToConstantHeading(backdropDropLocation, Math.toRadians(0))

                            // Release Yellow + White
                            .stopAndAdd(manager.openRightClaw())
                            .afterTime(0.2, manager.openLeftClaw())
                            .afterTime(0.2, manager.openAutoClaw())
                            .waitSeconds(0.25)

                            // Back up and pack up
                            .lineToX(50)
                            .afterTime(0.5, manager.getLiftReadyToDrive())

                            // Drive back to stacks
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(24, -10.5, Math.toRadians(180)), Math.toRadians(180))
                            .splineTo(new Vector2d(-48, DRIVEINY_SECONDPICKUP), Math.toRadians(180))

                            // Prepare for grabbing - Trip 2
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                            .afterDisp(0, manager.runLiftToPosition(EXTENDARM_SECONDPICKUP))
                            .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                            .lineToX(DRIVEINX_SECONDPICKUP)
                            .build()
            );
        }
    }

    protected void stackToAngleDrop () {
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
                        .splineToSplineHeading(new Pose2d(-11, -12, Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(30, manager.getLiftReadyToDropPixelFromLeft())
                        .splineTo(new Vector2d(35, -12), Math.toRadians(0))
                        .splineTo(new Vector2d(50, -23), Math.toRadians(-20))

                        // Release all pixels
                        .afterTime(0, manager.openLeftClaw())
                        .afterTime(0, manager.openRightClaw())
                        .afterTime(0.2, manager.openAutoClaw())
                        .waitSeconds(0.25)
                        .build()
        );

        Actions.runBlocking(new SequentialAction(
                drive.actionBuilder(drive.pose)
                        // Back up and pack up
                        .lineToX(48)
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
                        .splineToSplineHeading(new Pose2d(-11, -12, Math.toRadians(0)), Math.toRadians(0))
                        .splineTo(new Vector2d(55, -12), Math.toRadians(0))

                        // Release all pixels
                        .afterTime(0, manager.openLeftClaw())
                        .afterTime(0, manager.openRightClaw())
                        .afterTime(0.2, manager.openAutoClaw())
                        .waitSeconds(0.25)
                        .build()
        );

        Actions.runBlocking(new SequentialAction(
                drive.actionBuilder(drive.pose)
                        // Back up
                        .lineToX(58)
                        .build()));

    }
}