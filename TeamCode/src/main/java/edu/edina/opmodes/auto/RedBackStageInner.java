package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import edu.edina.library.enums.Alliance;
import edu.edina.library.enums.PropLocation;

public class RedBackStageInner extends BackStage {

    @Override
    protected Alliance getAlliance() {
        return Alliance.Red;
    }

    @Override
    protected Pose2d getStartPose() {
        return new Pose2d(14.5, -64, Math.toRadians(90));
    }

    @Override
    protected PropLocation getNonePropLocation() { return PropLocation.Left; }

    @Override
    protected void dropPurplePixel() {
        Vector2d propDropLocation;
        Pose2d backdropLocation;
        double propDropAngle = 90.0;

        // Determine location for the purple and yellow pixel
        switch(propLocation) {
            case Left:
                propDropLocation = new Vector2d(15.5, -42);
                propDropAngle = 135.0;
                backdropLocation = new Pose2d(42,-34, Math.toRadians(0));
                break;
            case Right:
                propDropLocation = new Vector2d(27, -43);
                propDropAngle =65.0;
                backdropLocation = new Pose2d(42,-43, Math.toRadians(0));
                break;
            case Center:
            default:
                propDropLocation = new Vector2d(16.5, -34.5);
                backdropLocation = new Pose2d(42,-38, Math.toRadians(0));
                break;
        }

        // Purple + Yellow
        if (propLocation == PropLocation.Right) {
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            // Go to spike and drop
                            .splineTo(propDropLocation, Math.toRadians(propDropAngle))
                            .endTrajectory()
                            .stopAndAdd(manager.openLeftClaw())

                            // Drive to backdrop and release
                            .setTangent((propLocation == PropLocation.Right) ? Math.toRadians(-180) : Math.toRadians(0))
                            .afterTime(0, manager.getLiftReadyToDropThePixelLowOnTheWall())
                            .splineToSplineHeading(backdropLocation, Math.toRadians(0))
                            .waitSeconds(.1)
                            .lineToX(53)
                            .stopAndAdd(manager.openRightClaw())
                            .build()
            );
        } else {
            Actions.runBlocking(
                    drive.actionBuilder(drive.pose)
                            // Go to spike and drop
                            .splineTo(propDropLocation, Math.toRadians(propDropAngle))
                            .endTrajectory()
                            .stopAndAdd(manager.openLeftClaw())

                            // Drive to backdrop and release
                            .setTangent((propLocation == PropLocation.Right) ? Math.toRadians(-180) : Math.toRadians(0))
                            .afterTime(0, manager.getLiftReadyToDropThePixelLowOnTheWall())
                            .splineToSplineHeading(backdropLocation, Math.toRadians(0))
                            .lineToX(53)
                            .stopAndAdd(manager.openRightClaw())
                            .build()
            );
        }
    }

    @Override
    protected void park() {
        // park
        switch (parkLocation) {
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                // Back up and pack up
                                .lineToX(48)
                                .afterDisp(2, manager.getLiftReadyToDrive())
                                .setReversed(true)
                                .splineTo(new Vector2d(58, -14), Math.toRadians(0))
                                .build()));
                break;
            case Corner:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                // Back up and pack up
                                .lineToX(48)
                                .afterDisp(2, manager.getLiftReadyToDrive())
                                .setReversed(true)
                                .splineTo(new Vector2d(58, -64), Math.toRadians(0))
                                .build()));
                break;
        }
    }
}
