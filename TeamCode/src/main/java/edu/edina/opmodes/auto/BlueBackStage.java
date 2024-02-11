package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import edu.edina.library.enums.Alliance;
import edu.edina.library.enums.PropLocation;


public class BlueBackStage extends BackStage {

    @Override
    protected Alliance getAlliance() {
        return Alliance.Blue;
    }

    @Override
    protected Pose2d getStartPose() { return new Pose2d(17.5, 64, Math.toRadians(270)); }

    @Override
    protected PropLocation getNonePropLocation() { return PropLocation.Left; }

    @Override
    protected void dropPurplePixel() {
        Vector2d propDropLocation;
        Pose2d backdropLocation;
        double propDropAngle = -90;

        // Determine location for the purple and yellow pixel
        switch(propLocation) {
            case Left:
                propDropLocation = new Vector2d(16.5, 42);
                propDropAngle = -65;
                backdropLocation = new Pose2d(42.5,41.5, Math.toRadians(0));
                break;
            case Right:
                propDropLocation = new Vector2d(7.5, 42);
                propDropAngle = 225;
                backdropLocation = new Pose2d(42.5,26.5, Math.toRadians(0));
                break;
            case Center:
            default:
                propDropLocation = new Vector2d(15, 35);  // default to Center if all goes bad
                backdropLocation = new Pose2d(42.5,34, Math.toRadians(0)); // default to center if all goes bad
                break;
        }

        // Purple + Yellow
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                        // Go to spike and drop
                        .splineTo(propDropLocation, Math.toRadians(propDropAngle))
                        .endTrajectory()
                        .stopAndAdd(manager.openLeftClaw())

                        // Drive to backdrop and release
                        .setTangent((propLocation==PropLocation.Left) ? Math.toRadians(115) : Math.toRadians(0))
                        .afterTime(0, manager.getLiftReadyToDropThePixelLowOnTheWall())
                        .waitSeconds(.1)
                        .splineToSplineHeading(backdropLocation, Math.toRadians(0))
                        .lineToX(45.5)
                        .stopAndAdd(manager.openRightClaw())
                        .waitSeconds(.25)
                        .build()
        );
    }

    @Override
    protected void park() {
        // park
        switch (parkLocation) {
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                // Back up and pack up
                                .lineToX(43)
                                .afterDisp(2, manager.getLiftReadyToDrive())
                                .setReversed(true)
                                .splineTo(new Vector2d(58, 10), Math.toRadians(0))
                                .build()));
                break;
            case Corner:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                // Back up and pack up
                                .lineToX(43)
                                .afterDisp(2, manager.getLiftReadyToDrive())
                                .setReversed(true)
                                .splineTo(new Vector2d(58, 64), Math.toRadians(0))
                                .build()));
                break;

        }
    }
}
