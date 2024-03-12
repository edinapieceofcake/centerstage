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
public class RedAudienceWall extends RedAudience {

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

        switch (propLocation) {
            case Left:
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks
                                .setReversed(true)
                                .turnTo(Math.toRadians(180))
                                .lineToX(-52)
                                .build()
                );
                break;
            default:
                // Drive to Stack Pick up 1st white
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(-52, -36, Math.toRadians(180)), Math.toRadians(180))
                                .build()
                );
                break;
        }
    }

    protected void stackToYellow() {
        // Run to backdrop.
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Back away and pack up
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOff()))

                        // Move in and grab pixels until beam break
                        .afterTime(0, manager.closeAutoClaw())
                        .afterTime(0, manager.closeLeftClaw())
                        .afterTime(0, manager.closeRightClaw())

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
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, -56), Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(30, manager.getLiftReadyToDropPixelFromRight())
                        .splineToConstantHeading(new Vector2d(10, -56), Math.toRadians(0))
                        .splineToConstantHeading(backdropDropLocation, Math.toRadians(0))

                        // Release Yellow + White
                        .stopAndAdd(manager.openRightClaw())
                        .afterTime(0.25, manager.openLeftClaw())
                        .waitSeconds(0.25)
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
                        .afterTime(0, manager.closeAutoClaw())
                        .afterTime(0, manager.closeLeftClaw())
                        .afterTime(0, manager.closeRightClaw())

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
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, -56), Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(30, manager.getLiftReadyToDropPixelFromRight())
                        .splineToConstantHeading(new Vector2d(10, -56), Math.toRadians(0))
                        .splineToConstantHeading(backdropDropLocation, Math.toRadians(0))

                        // Release Yellow + White
                        .stopAndAdd(manager.openRightClaw())
                        .afterTime(0.25, manager.openLeftClaw())
                        .waitSeconds(0.25)

                        // Back up and pack up
                        .lineToX(50)
                        .afterDisp(0, manager.getLiftReadyToDrive())

                        // Head to Stacks VIA Wall
                        .splineToSplineHeading(new Pose2d(0, -54, Math.toRadians(-180)), Math.toRadians(180))
                        .splineToConstantHeading(new Vector2d(-38, -56), Math.toRadians(180))
                        .splineToConstantHeading(new Vector2d(-50, -31), Math.toRadians(180))

                        // Prepare for grabbing - Trip 2
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                        .afterDisp(0, manager.runLiftToPosition(-23))
                        .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                        .lineToX(-57)
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

                        // Return to backdrop and angle drop
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, -56), Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(30, manager.getLiftReadyToDropPixelFromRight())
                        .splineToConstantHeading(new Vector2d(10, -56), Math.toRadians(0))
                        .splineTo(new Vector2d(40, -56), Math.toRadians(0))
                        .splineTo(new Vector2d(59.5, -53), Math.toRadians(35))

                        // Release all pixels
                        .afterTime(0.1, manager.openAutoClaw())
                        .afterTime(0.1, manager.openLeftClaw())
                        .afterTime(0.1, manager.openRightClaw())
                        .waitSeconds(.25)

                        .build()
        );
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

                        // Return to backdrop and angle drop
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, -56), Math.toRadians(0)), Math.toRadians(0))
                        .afterDisp(30, manager.getLiftReadyToDropPixelFromRight())
                        .splineToConstantHeading(new Vector2d(10, -56), Math.toRadians(0))
                        .splineTo(new Vector2d(40, -56), Math.toRadians(0))
                        .splineTo(new Vector2d(59.5, -53), Math.toRadians(35))

                        // Release all pixels
                        .afterTime(0.1, manager.openAutoClaw())
                        .afterTime(0.1, manager.openLeftClaw())
                        .afterTime(0.1, manager.openRightClaw())
                        .waitSeconds(.25)

                        .build()
        );
    }
}