package edu.edina.opmodes.auto.hold;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import edu.edina.library.enums.PropLocation;

@Autonomous
@Disabled
public class RedAudienceCenterNew extends RedAudienceNew {
    // PATH TO GO FROM PURPLE DROP TO STACK
    // (B)
    @Override
    protected void purpleToStack(PropLocation propLocation) {

        if (propLocation == PropLocation.Left) {
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            // Head to Stacks
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(-35, -11, Math.toRadians(180)), Math.toRadians(45))
                            // Prepare for grabbing - Trip 1
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                            .afterDisp(0, manager.runLiftToPosition(-123, true))
                            .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                            .setReversed(false)
                            .splineToSplineHeading(new Pose2d(-50, -11, Math.toRadians(180)), Math.toRadians(180))
                            .lineToX(-53)
                            .build()
            );
        } else if (propLocation == PropLocation.Center) {
            // Drive to Stack Pick up 1st white
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            // Head to Stacks
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(-48, -18, Math.toRadians(180)), Math.toRadians(90))
                            .splineToSplineHeading(new Pose2d(-48, -11, Math.toRadians(180)), Math.toRadians(90))
                            .setReversed(false)
                            // Prepare for grabbing - Trip 1
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                            .afterDisp(0, manager.runLiftToPosition(-123, true))
                            .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                            .lineToX(-53)
                            .build()
            );
        } else {
            // Drive to Stack Pick up 1st white
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            // Head to Stacks
                            .setReversed(true)
                            .splineToSplineHeading(new Pose2d(-48, -11, Math.toRadians(180)), Math.toRadians(90))
                            // Prepare for grabbing - Trip 1
                            .setReversed(false)
                            .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                            .afterDisp(0, manager.runLiftToPosition(-123, true))
                            .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                            .lineToX(-56)
                            .build()
            );
        }
    }

    // PATH TO GO FROM STACK TO BACKDROP TO DROP YELLOW
    // (C)
    @Override
    protected void stackToYellow(Vector2d backdropLocation) {
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
                        .splineToConstantHeading(backdropLocation, Math.toRadians(0))

                        // Release Yellow + White
                        .stopAndAdd(manager.openRightClaw())
                        .afterTime(0.25, manager.openLeftClaw())
                        .waitSeconds(0.25)
                        .build()
        );
    }

    // STACK TO YELLOW TO STACK
    // (D)
    @Override
    protected void stackToYellowToStack(Vector2d backdropLocation) {
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
                        .waitSeconds(delayTime/1000)

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
                        .splineToConstantHeading(backdropLocation, Math.toRadians(0))

                        // Release Yellow + White
                        .stopAndAdd(manager.openRightClaw())
                        .afterTime(0.25, manager.openLeftClaw())
                        .waitSeconds(0.25)

                        // Back up and pack up
                        .lineToX(50)
                        .afterDisp(0, manager.getLiftReadyToDrive())

                        // Drive back to stacks
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(24, -10.5, Math.toRadians(180)), Math.toRadians(180))
                        .splineTo(new Vector2d(-44, -10.5), Math.toRadians(180))

                        // Prepare for grabbing - Trip 2
                        .afterTime(0, new InstantAction(() -> drive.turnBeamBreakOn(150)))
                        .afterDisp(0, manager.runLiftToPosition(-123, true))
                        .afterDisp(0, manager.positionTheClawToPickupPixelsFromStack())
                        .lineToX(-53)
                        .build()
            );
    }

    // STACK TO ANGLE DROP
    // (DPAD_UP)
    @Override
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
                .splineToSplineHeading(new Pose2d(-11, -12, Math.toRadians(0)), Math.toRadians(0))
                .afterDisp(30, manager.getLiftReadyToDropPixelFromLeft())
                .splineTo(new Vector2d(40, -12), Math.toRadians(0))
                .splineTo(new Vector2d(61, -20), Math.toRadians(-35))

                // Release all pixels
                .afterTime(0, manager.openAutoClaw())
                .afterTime(0, manager.openLeftClaw())
                .afterTime(0, manager.openRightClaw())
                .waitSeconds(0.25)
                .build()
        );

        Actions.runBlocking(new SequentialAction(
                drive.actionBuilder(drive.pose)
                        // Back up and pack up
                        .lineToX(58)
                        .afterDisp(1, manager.getLiftReadyToDrive())
                        .build()));
    }

    // STACK TO ANGLE DROP
    // (DPAD_UP)
    @Override
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
                        .splineTo(new Vector2d(61, -12), Math.toRadians(0))

                        // Release all pixels
                        .afterTime(0, manager.openAutoClaw())
                        .afterTime(0, manager.openLeftClaw())
                        .afterTime(0, manager.openRightClaw())
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