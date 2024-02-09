package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import edu.edina.library.enums.PropLocation;

@Autonomous
//@Disabled
public class BlueAudienceCenter extends BlueAudience {
    @Override
    protected void runPaths() {
        // If we want to drop Yellow..
        if (yellowPixel) {
            if (propLocation == PropLocation.Left) {
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(-35, 14, Math.toRadians(180)), Math.toRadians(90))
                                .build()
                );
            } else if (propLocation == PropLocation.Center) {
                // Drive to Stack Pick up 1st white
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(-52, 14, Math.toRadians(180)), Math.toRadians(90))
                                .build()
                );
            } else {
                // Drive to Stack Pick up 1st white
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(-35, 14, Math.toRadians(180)), Math.toRadians(-45))
                                .build()
                );
            }

            // Prepare lift, grab pixel, and raise lift
            drive.turnBeamBreakOn(150);

            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    manager.runLiftToPosition(-133),
                                    manager.positionTheClawToPickupPixels()
                            ),
                            drive.actionBuilder(drive.pose)
                                    // Head to Stacks
                                    .lineToX(-57)
                                    .build(),
                            manager.closeLeftClaw(),
                            new SleepAction(.2),
                            manager.raiseLiftAfterStackPickup()
                    )
            );

            drive.turnBeamBreakOff();

            // Check to see if delay is set and determine which routine to run
            if (delayTime > 0) {  // Yes - delay is set
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Turn, then pack up and drive to backdrop
                                //.turnTo(Math.toRadians(270))
                                .lineToX(-50)
                                .afterDisp(0,
                                        new ParallelAction(
                                                manager.lowerLiftForDriving(),
                                                manager.zeroLift(),
                                                manager.positionTheClawToDriveWithPixels()
                                        )
                                )
                                .splineToSplineHeading(new Pose2d(new Vector2d(-30, 12), Math.toRadians(0)), Math.toRadians(0))
                                .splineToConstantHeading(new Vector2d(11, 12), Math.toRadians(0))
                                .waitSeconds(delayTime/1000)  // Wait at midfield
                                .afterDisp(0, manager.getLiftReadyToDropThePixelHighOnTheWall())
                                .splineToConstantHeading(backdropDropLocation, Math.toRadians(0))
                                .lineToX(51.5)
                                .afterDisp(0, new SequentialAction(
                                        manager.openRightClaw(),
                                        new SleepAction(0.25),
                                        manager.openLeftClaw()
                                ))
                                .build()
                );
            } else {  // No delay - operate as normal
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Turn, then pack up and drive to backdrop
                                .lineToX(-50)
                                .afterDisp(0,
                                        new ParallelAction(
                                                manager.lowerLiftForDriving(),
                                                manager.zeroLift(),
                                                manager.positionTheClawToDriveWithPixels()
                                        )
                                )
                                .splineToSplineHeading(new Pose2d(new Vector2d(-30, 12), Math.toRadians(0)), Math.toRadians(0))
                                .splineToConstantHeading(new Vector2d(11, 12), Math.toRadians(0))
                                .afterDisp(0, manager.getLiftReadyToDropThePixelHighOnTheWall())
                                .splineToConstantHeading(backdropDropLocation, Math.toRadians(0))
                                .lineToX(51.5)
                                .afterDisp(0, new SequentialAction(
                                        manager.openRightClaw(),
                                        new SleepAction(0.25),
                                        manager.openLeftClaw()
                                ))
                                .build()
                );
            }

            // If we're done making stack trips
            if (!makeSecondTrip) {
                // back away and pack up
                Actions.runBlocking(
                        new ParallelAction(
                                drive.actionBuilder(drive.pose)
                                        .lineToX(44)
                                        .build(),
                                new SequentialAction(
                                        new SleepAction(.2),
                                        manager.getLiftReadyToDrive()
                                )
                        )
                );
            }
        }

        // If we are making a second trip to the stacks
        if (makeSecondTrip) {
            // go get other white pixels
            Actions.runBlocking(
                    new ParallelAction(
                            new SequentialAction(
                                    new SleepAction(.2),
                                    manager.getLiftReadyToDrive()
                            ),
                            drive.actionBuilder(drive.pose)
                                    .lineToX(46)
                                    .splineToSplineHeading(new Pose2d(11, 12, Math.toRadians(180)), Math.toRadians(180))
                                    .splineToConstantHeading(new Vector2d(-48, 17), Math.toRadians(180))
                                    .build()
                    )
            );

            // Reach out, grab pixels, close the claws
            drive.turnBeamBreakOn(150);

            Actions.runBlocking(
                    new SequentialAction(
                            new ParallelAction(
                                    manager.positionTheClawToPickupPixels(),
                                    manager.runLiftToPosition(-180)
                            ),
                            drive.actionBuilder(drive.pose)
                                    // Head to Stacks
                                    .lineToX(-59)
                                    .build(),
                            new ParallelAction(
                                    manager.closeLeftClaw(),
                                    manager.closeAutoClaw(),
                                    manager.closeRightClaw()
                            ),
                            new SleepAction(.2)
                    )
            );

            drive.turnBeamBreakOff();

            // If we're going to drop on the background
            if (dropOnBackdrop) {
                // drive to backstage - 2nd trip
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks VIA A-Row
                                .lineToX(-50)
                                .afterDisp(0,
                                        new ParallelAction(
                                                manager.zeroLift(),
                                                manager.positionTheClawToDriveWithPixels()
                                        ))
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(new Vector2d(-20, 12), Math.toRadians(0)), Math.toRadians(0))
                                .splineToConstantHeading(new Vector2d(10, 12), Math.toRadians(0))
                                .afterDisp(0, manager.getLiftReadyToDropThePixelHighOnTheWall())
                                .splineToConstantHeading(secondBackdropDropLocation, Math.toRadians(0))
                                .afterDisp(0, new SequentialAction(
                                        manager.openAutoClaw(),
                                        manager.openLeftClaw(),
                                        manager.openRightClaw()
                                ))
                                .build());

                // back away and pack up
                Actions.runBlocking(
                        new SequentialAction(
                                drive.actionBuilder(drive.pose)
                                        .lineToX(40)
                                        .build(),
                                manager.getLiftReadyToDrive()
                        )
                );
            }

            // If we are dropping on Backstage
            if (dropOnBackstage) {
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                // Head to Stacks VIA A-Row
                                .lineToX(-48)
                                .afterDisp(0,
                                        new ParallelAction(
                                                manager.lowerLiftForDriving(),
                                                manager.zeroLift(),
                                                manager.positionTheClawToDriveWithPixels()
                                        ))
                                .setReversed(true)
                                .splineToSplineHeading(new Pose2d(new Vector2d(-35, 12), Math.toRadians(0)), Math.toRadians(0))
                                .splineToConstantHeading(new Vector2d(54, 14), Math.toRadians(0))
                                .afterDisp(0, new SequentialAction(
                                        manager.openAutoClaw(),
                                        manager.openLeftClaw(),
                                        manager.openRightClaw()
                                ))
                                .lineToX(50)
                                .build());

            }
        }
    }
}