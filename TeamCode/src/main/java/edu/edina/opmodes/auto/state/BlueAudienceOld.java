package edu.edina.opmodes.auto.state;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import edu.edina.library.enums.Alliance;
import edu.edina.library.enums.PropLocation;
import edu.edina.opmodes.auto.Audience;

public class BlueAudienceOld extends Audience {
    protected Vector2d backdropDropLocation;
    @Override
    protected Alliance getAlliance() {
            return Alliance.Blue;
        }

    @Override
    protected Pose2d getStartPose() {
        return new Pose2d(-31, 64, Math.toRadians(270));
    }

    @Override
    protected PropLocation getNonePropLocation() { return PropLocation.Left; }

    @Override
    protected void dropPurplePixel() {
        Vector2d propDropLocation;
        double propAngle = 270;

        // Determine location for purple pixel
        switch(propLocation) {
            case Left:
                propDropLocation = new Vector2d(-33, 35);
                propAngle = 315.0;
                backdropDropLocation = new Vector2d(50,42.5);
                break;
            case Right:
                propDropLocation = new Vector2d(-41, 40);
                propAngle = 225.0;
                backdropDropLocation = new Vector2d(50,29);
                break;
            case Center:
            default:
                propDropLocation = new Vector2d(-33, 34.5);  // default to Center if all goes bad
                propAngle = 270.0;
                backdropDropLocation = new Vector2d(50,34 ); // default to center if all goes bad
                break;
        }

        // Run to drop PURPLE pixel
        Actions.runBlocking(
                new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(propDropLocation, Math.toRadians(propAngle))
                                .build(),
                        manager.openLeftClaw()
                )
        );
    }

    @Override
    protected void park() {
        // park
        switch (parkLocation) {
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(58, 14), Math.toRadians(0))
                                .build()));
                break;
            case Corner:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(58, 60), Math.toRadians(0))
                                .build()));
                break;
            default:
                break;
        }
    }
}
