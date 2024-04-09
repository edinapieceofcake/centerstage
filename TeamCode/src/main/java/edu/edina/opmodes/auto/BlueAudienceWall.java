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
public class BlueAudienceWall extends BlueAudience {

    public static Vector2d firstPickupLeft = new Vector2d(-58, 38);
    public static Vector2d firstPickupCenter = new Vector2d(-57.5, 38);
    public static Vector2d firstPickupRight = new Vector2d(-57.5, 38);

    public static Vector2d secondPickupLeft = new Vector2d(-60, 37.5);
    public static Vector2d secondPickupCenter = new Vector2d(-60, 37);
    public static Vector2d secondPickupRight = new Vector2d(-60, 36.5);

    public static Vector2d firstAngleDropLeft = new Vector2d(52, 50);
    public static Vector2d firstAngleDropCenter = new Vector2d(52, 50);
    public static Vector2d firstAngleDropRight = new Vector2d(52, 50);

    public Vector2d firstPickup, secondPickup, firstAngleDrop;

    public static int EXTENDARM_FIRSTPICKUP = -200;
    public static int EXTENDARM_SECONDPICKUP = -100;

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

        // Drive to Stack Pick up 1st white
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Head to Stacks
                        .setReversed(true)
                        .splineTo(new Vector2d(-31, 54), Math.toRadians(45))
                        .setReversed(false)

                        .splineToSplineHeading(new Pose2d(-55, firstPickup.y, Math.toRadians(180)), Math.toRadians(180))

                        .lineToX(firstPickup.x)
                        // Prepare for grabbing - Trip 1
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                        .afterDisp(0, manager.runLiftToPosition(EXTENDARM_FIRSTPICKUP, false))
                        .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())

                        .build()
        );
    }

    protected void stackToYellow() {
        // Run to backdrop.

        if (propLocation == PropLocation.Right) {
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
                            .lineToX(-54)
                            .afterDisp(3, manager.lowerLiftForDriving())
                            .afterDisp(3, manager.zeroLift())
                            .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                            // Head to backdrop
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(new Vector2d(-28, 58), Math.toRadians(350)), Math.toRadians(0))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .afterDisp(30, manager.getLiftReadyToDropThePixelHighOnTheWall())
                            .splineToSplineHeading(new Pose2d(new Vector2d(-24, 58), Math.toRadians(0)), Math.toRadians(0))

                            .splineToConstantHeading(new Vector2d(25, 58), Math.toRadians(0))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                            .splineToConstantHeading(backdropDropLocationAW, Math.toRadians(0))

                            // Release Yellow + White
                            .stopAndAdd(manager.openRightClaw(0))

                            .setReversed(true)
                            .splineToConstantHeading(backdropDropLocationSecond, Math.toRadians(0))
                            .setReversed(false)
                            .lineToX(50)

                            .afterTime(0.25, manager.openLeftClaw(0))
                            .afterTime(0.25, manager.openAutoClaw(0))
                            .waitSeconds(0.25)
                            .build()
            );
        } else {
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
                            .lineToX(-54)
                            .afterDisp(3, manager.lowerLiftForDriving())
                            .afterDisp(3, manager.zeroLift())
                            .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                            // Head to backdrop
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(new Vector2d(-28, 58), Math.toRadians(350)), Math.toRadians(0))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .afterDisp(30, manager.getLiftReadyToDropThePixelHighOnTheWall())
                            .splineToSplineHeading(new Pose2d(new Vector2d(-24, 58), Math.toRadians(0)), Math.toRadians(0))

                            .splineToConstantHeading(new Vector2d(25, 58), Math.toRadians(0))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                            .splineToConstantHeading(backdropDropLocationAW, Math.toRadians(0))

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
                            .waitSeconds(0.25)

                            // Back away a little and raise the lift
                            .lineToX(-56.5)
                            .stopAndAdd(manager.raiseLiftAfterStackPickup())
                            .waitSeconds(delayTime / 1000)

                            // Finish backing away and prepae to drive
                            .lineToX(-53)
                            .afterDisp(3, manager.lowerLiftForDriving())
                            .afterDisp(3, manager.zeroLift())
                            .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                            // Return to backdrop
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(new Vector2d(-28, 58), Math.toRadians(350)), Math.toRadians(0))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .afterDisp(30, manager.getLiftReadyToDropThePixelHighOnTheWall())
                            .splineToSplineHeading(new Pose2d(new Vector2d(-24, 58), Math.toRadians(0)), Math.toRadians(0))
                            .splineToConstantHeading(new Vector2d(25, 58), Math.toRadians(0))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                            .splineToConstantHeading(backdropDropLocationAW, Math.toRadians(0))

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
                            .lineToX(48)
                            .afterDisp(0, manager.getLiftReadyToDrive())

                            // Head to Stacks VIA Wall
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(0, 58, Math.toRadians(-180)), Math.toRadians(180))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .splineToConstantHeading(new Vector2d(-38, 58), Math.toRadians(180))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                            .splineToConstantHeading(new Vector2d(-50, secondPickup.y), Math.toRadians(180))

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
                            .waitSeconds(0.25)

                            // Back away a little and raise the lift
                            .lineToX(-56.5)
                            .stopAndAdd(manager.raiseLiftAfterStackPickup())
                            .waitSeconds(delayTime / 1000)

                            // Finish backing away and prepae to drive
                            .lineToX(-53)
                            .afterDisp(3, manager.lowerLiftForDriving())
                            .afterDisp(3, manager.zeroLift())
                            .afterDisp(3, manager.positionTheClawToDriveWithPixels())

                            // Return to backdrop
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(new Vector2d(-28, 58), Math.toRadians(350)), Math.toRadians(0))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .afterDisp(30, manager.getLiftReadyToDropThePixelHighOnTheWall())
                            .splineToSplineHeading(new Pose2d(new Vector2d(-24, 58), Math.toRadians(0)), Math.toRadians(0))
                            .splineToConstantHeading(new Vector2d(25, 58), Math.toRadians(0))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))
                            .splineToConstantHeading(backdropDropLocationAW, Math.toRadians(0))

                            // Release Yellow + White
                            .stopAndAdd(manager.openRightClaw(0))
                            .stopAndAdd(manager.openLeftClaw(0))
                            .stopAndAdd(manager.openAutoClaw(0))
                            .waitSeconds(0.25)

                            // Back up and pack up
                            .lineToX(48)
                            .afterDisp(0, manager.getLiftReadyToDrive())

                            // Head to Stacks VIA Wall
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(0, 58, Math.toRadians(-180)), Math.toRadians(180))
                            .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                            .splineToConstantHeading(new Vector2d(-38, 58), Math.toRadians(180))
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
                        .splineToSplineHeading(new Pose2d(new Vector2d(-28, 58), Math.toRadians(350)), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .afterDisp(30, manager.getLiftReadyToDropPixelFromLeft())
                        .splineToSplineHeading(new Pose2d(new Vector2d(-24, 58), Math.toRadians(0)), Math.toRadians(0))
                        .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(0))
                        .splineTo(new Vector2d(30, 58), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOff()))

                        .splineTo(firstAngleDrop, Math.toRadians(-20))

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
                        .splineToSplineHeading(new Pose2d(new Vector2d(-28, 58), Math.toRadians(350)), Math.toRadians(0))
                        .afterTime(0, new InstantAction(() -> drive.turnErrorPoseStopOn()))
                        .splineToSplineHeading(new Pose2d(new Vector2d(-24, 58), Math.toRadians(0)), Math.toRadians(0))
                        .splineToConstantHeading(new Vector2d(10, 58), Math.toRadians(0))
                        .splineTo(new Vector2d(50, 58), Math.toRadians(0))
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