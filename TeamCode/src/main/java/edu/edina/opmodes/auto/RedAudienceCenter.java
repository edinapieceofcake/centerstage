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
public class RedAudienceCenter extends RedAudience {

    public static Vector2d firstPickupLeft = new Vector2d(-60, -12);
    public static Vector2d firstPickupCenter = new Vector2d(-61.5, -10);
    public static Vector2d firstPickupRight = new Vector2d(-61, -10.5);

    public static Vector2d secondPickupLeft = new Vector2d(-61.5, -12);
    public static Vector2d secondPickupCenter = new Vector2d(-61.5, -12);
    public static Vector2d secondPickupRight = new Vector2d(-61.5, -12.5);

    public static Vector2d firstAngleDropLeft = new Vector2d(49, -23);
    public static Vector2d firstAngleDropCenter = new Vector2d(49, -19);
    public static Vector2d firstAngleDropRight = new Vector2d(49, -24);

    public Vector2d firstPickup, secondPickup, firstAngleDrop;

    public static int EXTENDARM_FIRSTPICKUP = -200;
    public static int EXTENDARM_SECONDPICKUP = -80;

    @Override
    protected void runPaths() {

        switch (propLocation) {
            case Left:
                firstPickup = firstPickupLeft;
                secondPickup = secondPickupLeft;
                firstAngleDrop = firstAngleDropLeft;
                break;
            case Center:
                firstPickup = firstPickupCenter;
                secondPickup = secondPickupCenter;
                firstAngleDrop = firstAngleDropCenter;
                break;
            case Right:
            default:
                firstPickup = firstPickupRight;
                secondPickup = secondPickupRight;
                firstAngleDrop = firstAngleDropRight;
                break;
        }

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
                                .afterDisp(0, manager.runLiftToPosition(EXTENDARM_FIRSTPICKUP, true))
                                .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                                .setReversed(false)
                                .splineToSplineHeading(new Pose2d(-55, firstPickup.y, Math.toRadians(180)), Math.toRadians(180))
                                .lineToX(firstPickup.x)
                                .build()
                );
                break;
            case Center:
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(-48, -18, Math.toRadians(180)), Math.toRadians(90))
                                .splineToSplineHeading(new Pose2d(-50, firstPickup.y, Math.toRadians(180)), Math.toRadians(90))
                                // Prepare for grabbing - Trip 1
                                .build()
                );

                Actions.runBlocking(new SequentialAction(
                        new InstantAction(() -> drive.turnBeamBreakOn(180)),
                        manager.runLiftToPosition(EXTENDARM_FIRSTPICKUP, true),
                        manager.positionTheClawToPickupPixelsFromStack()
                ));

                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                .setReversed(false)
                                .lineToX(firstPickup.x)
                                .build()
                );
                break;
            default:    // Right or unknown
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(-45, firstPickup.y, Math.toRadians(180)), Math.toRadians(90))
                                // Prepare for grabbing - Trip 1
                                .setReversed(false)
                                .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                                .afterDisp(0, manager.runLiftToPosition(EXTENDARM_FIRSTPICKUP, true))
                                .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                                .lineToX(firstPickup.x)
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
                            .lineToX(-57)
                            .stopAndAdd(manager.raiseLiftAfterStackPickup())
                            .waitSeconds(delayTime/1000) // Optional Wait

                            // Finish backing away and prepare to drive
                            .lineToX(-53)
                            .afterDisp(3, manager.lowerLiftForDriving())
                            .afterDisp(3, manager.zeroLift())
                            .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                            // Head to backdrop
                            .setReversed(true)
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .splineToSplineHeading(new Pose2d(new Vector2d(-40, -10), Math.toRadians(-10)), Math.toRadians(0))
                            .splineToSplineHeading(new Pose2d(new Vector2d(-35, -10), Math.toRadians(0)), Math.toRadians(0))
                            .afterDisp(25, manager.getLiftReadyToDropThePixelHighOnTheWall())
                            .splineToConstantHeading(new Vector2d(10, -10), Math.toRadians(0))
                            .splineToConstantHeading(backdropDropLocation, Math.toRadians(0))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                            // Release Yellow + White
                            .stopAndAdd(manager.openRightClaw(0))

                            .setReversed(true)
                            .splineToConstantHeading(backdropDropLocationSecond, Math.toRadians(0))
                            .setReversed(false)
                            .lineToX(50)

                            .stopAndAdd(manager.openLeftClaw(0))
                            .stopAndAdd(manager.openAutoClaw(0))
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
                            .lineToX(-57)
                            .stopAndAdd(manager.raiseLiftAfterStackPickup())
                            .waitSeconds(delayTime / 1000) // Optional Wait

                            // Finish backing away and prepare to drive
                            .lineToX(-53)
                            .afterDisp(3, manager.lowerLiftForDriving())
                            .afterDisp(3, manager.zeroLift())
                            .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                            // Head to backdrop
                            .setReversed(true)
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .splineToSplineHeading(new Pose2d(new Vector2d(-40, -10), Math.toRadians(-10)), Math.toRadians(0))
                            .splineToSplineHeading(new Pose2d(new Vector2d(-35, -10), Math.toRadians(0)), Math.toRadians(0))
                            .afterDisp(25, manager.getLiftReadyToDropThePixelHighOnTheWall())
                            .splineToConstantHeading(new Vector2d(10, -10), Math.toRadians(0))
                            .splineToConstantHeading(backdropDropLocation, Math.toRadians(0))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                            // Release Yellow + White
                            .stopAndAdd(manager.openRightClaw(0))
                            .afterTime(0.25, manager.openLeftClaw(0))
                            .afterTime(0.25, manager.openAutoClaw(0))
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
                            .lineToX(-57)
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
                            .splineToSplineHeading(new Pose2d(new Vector2d(-40, -10), Math.toRadians(-10)), Math.toRadians(0))
                            .splineToSplineHeading(new Pose2d(new Vector2d(-35, -10), Math.toRadians(0)), Math.toRadians(0))
                            .afterDisp(25, manager.getLiftReadyToDropThePixelHighOnTheWall())
                            .splineToConstantHeading(new Vector2d(15, -10), Math.toRadians(0))
                            .splineToConstantHeading(backdropDropLocation, Math.toRadians(0))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                            // Release Yellow + White
                            .stopAndAdd(manager.openRightClaw(0))
                            .setReversed(true)
                            .splineToConstantHeading(backdropDropLocationSecond, Math.toRadians(0))
                            .setReversed(false)
                            .lineToX(50)
                            .stopAndAdd(manager.openLeftClaw(0))
                            .stopAndAdd(manager.openAutoClaw(0))
                            .waitSeconds(0.25)

                            // Back up and pack up
                            .lineToX(50)
                            .afterTime(0.5, manager.getLiftReadyToDrive())

                            // Drive back to stacks
                            .setReversed(true)
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .splineToSplineHeading(new Pose2d(24, secondPickup.y, Math.toRadians(180)), Math.toRadians(180))
                            .splineTo(new Vector2d(-48, secondPickup.y), Math.toRadians(180))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                            // Prepare for grabbing - Trip 2
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                            .afterDisp(0, manager.runLiftToPosition(EXTENDARM_SECONDPICKUP, true))
                            .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                            .lineToX(secondPickup.x)
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
                            .lineToX(-57)
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
                            .splineToSplineHeading(new Pose2d(new Vector2d(-40, -10), Math.toRadians(-10)), Math.toRadians(0))
                            .splineToSplineHeading(new Pose2d(new Vector2d(-35, -10), Math.toRadians(0)), Math.toRadians(0))
                            .afterDisp(25, manager.getLiftReadyToDropThePixelHighOnTheWall())
                            .splineToConstantHeading(new Vector2d(15, -10), Math.toRadians(0))
                            .splineToConstantHeading(backdropDropLocation, Math.toRadians(0))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                            // Release Yellow + White
                            .stopAndAdd(manager.openRightClaw(0))
                            .afterTime(0.2, manager.openLeftClaw(0))
                            .afterTime(0.2, manager.openAutoClaw(0))
                            .waitSeconds(0.25)

                            // Back up and pack up
                            .lineToX(50)
                            .afterTime(0.5, manager.getLiftReadyToDrive())

                            // Drive back to stacks
                            .setReversed(true)
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .splineToSplineHeading(new Pose2d(24, secondPickup.y, Math.toRadians(180)), Math.toRadians(180))
                            .splineTo(new Vector2d(-48, secondPickup.y), Math.toRadians(180))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                            // Prepare for grabbing - Trip 2
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                            .afterDisp(0, manager.runLiftToPosition(EXTENDARM_SECONDPICKUP, true))
                            .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                            .lineToX(secondPickup.x)
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
                        .splineToSplineHeading(new Pose2d(new Vector2d(-40, -10), Math.toRadians(-10)), Math.toRadians(0))
                        .splineToSplineHeading(new Pose2d(-35, -10, Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(50, manager.getLiftReadyToDropPixelFromLeft())
                        .splineTo(new Vector2d(30, -13), Math.toRadians(0))

                        .splineTo(firstAngleDrop, Math.toRadians(-20))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                        // Release all pixels
                        .afterTime(0, manager.openLeftClaw(0))
                        .afterTime(0.2, manager.openAutoClaw(0))
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
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-40, -10), Math.toRadians(-10)), Math.toRadians(0))
                        .splineToSplineHeading(new Pose2d(-35, -10, Math.toRadians(0)), Math.toRadians(0))
                        .splineTo(new Vector2d(55, -10), Math.toRadians(0))
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
                        .lineToX(58)
                        .build()));

    }
}