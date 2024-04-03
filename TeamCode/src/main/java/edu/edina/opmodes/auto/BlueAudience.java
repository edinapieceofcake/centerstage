package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import edu.edina.library.enums.Alliance;
import edu.edina.library.enums.PropLocation;

public class BlueAudience extends Audience {

    @Override
    protected Alliance getAlliance() {
            return Alliance.Blue;
        }

    @Override
    protected Pose2d getStartPose() { return new Pose2d(-31, 64, Math.toRadians(270)); }

    @Override
    protected PropLocation getNonePropLocation() { return PropLocation.Left; }

    @Override
    protected void dropPurplePixel() {
        Vector2d propDropLocation;
        double propAngle = 270;

        // Determine location for purple pixel
        switch(propLocation) {
            case Left:
                propDropLocation = new Vector2d(-31, 35);
                propAngle = 315;
                backdropDropLocation = new Vector2d(50,44.5);
                backdropDropLocationSecond = new Vector2d(48, 38);
                backdropDropLocationAW = new Vector2d(48,43.5);
                backdropDropLocationAW2 = new Vector2d(48, 33);
                break;
            case Right:
                propDropLocation = new Vector2d(-42, 33);
                propAngle = 225;
                backdropDropLocation = new Vector2d(50,32);
                backdropDropLocationAW = new Vector2d(48,27);
                backdropDropLocationSecond = new Vector2d(48, 38);
                break;
            case Center:
            default:
                propDropLocation = new Vector2d(-33, 34.5);  // default to Center if all goes bad
                propAngle = 270;
                backdropDropLocation = new Vector2d(50,38 ); // default to center if all goes bad
                backdropDropLocationAW = new Vector2d(48, 35.5);
                backdropDropLocationSecond = new Vector2d(48, 38);
                break;
        }

        // Run to drop PURPLE pixel
        Actions.runBlocking(
                new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(propDropLocation, Math.toRadians(propAngle))
                                .build(),
                        manager.openLeftClaw(),
                        manager.openAutoClaw()
                )
        );
    }

    @Override
    protected void park() {
        // park
        switch (parkLocation) {
            case Center:
                Actions.runBlocking(
                        new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(58, 14), Math.toRadians(0))
                                .build(),
                        manager.getLiftReadyToDrive()
                        ));
                break;
            case Corner:
                Actions.runBlocking(
                        new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(58, 60), Math.toRadians(0))
                                .build(),
                        manager.getLiftReadyToDrive()
                ));
                break;
            default:
                Actions.runBlocking(new SequentialAction(
                    manager.getLiftReadyToDrive()
                ));
                break;
        }
    }
}
