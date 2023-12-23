package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.ParkLocation;
import edu.edina.library.enums.PropLocation;
import edu.edina.library.util.RobotState;

@Autonomous
public class RedBackstageDoubleStack extends RedBaseAutonomous {

    @Override
    protected Pose2d getStartPose() {
        return new Pose2d(12.5, -64, Math.toRadians(90)));
    }

    @Override
    protected void runPaths(ParkLocation parkLocation) {
        RobotState state = RobotState.getInstance();

        // We want to detect if we don't have a block, but still need to default
        if (propLocation == PropLocation.None) {
            propLocation = PropLocation.Right;
        }

        Actions.runBlocking(new SequentialAction(
                drive.actionBuilder(drive.pose)
                .splineToSplineHeading(new Pose2d(12.5, -17, Math.toRadians(270)), Math.toRadians(90))
                .waitSeconds(0.5)

                // Head to Backboard
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(48, -35, Math.toRadians(0)), Math.toRadians(0))
                .waitSeconds(0.5)

                // Head to Stacks VIA A-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(12, -12, Math.toRadians(-180)), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(-58, -12), Math.toRadians(180))
                .waitSeconds(0.5)

                // Return to Backboard
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-12, -12), Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineTo(new Vector2d(24, -12), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(48, -35, Math.toRadians(0)), Math.toRadians(0))
                .waitSeconds(0.5)

                // Head to Stacks VIA A-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(12, -12, Math.toRadians(-180)), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(-58, -12), Math.toRadians(180))
                .waitSeconds(0.5)

                // Return to Backboard
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-12, -12), Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineTo(new Vector2d(24, -12), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(48, -35, Math.toRadians(0)), Math.toRadians(0))
                .waitSeconds(0.5)

                // Park
                //.setReversed(true)
                //.splineTo(new Vector2d(53, -60), Math.toRadians(0))
                .build()));
    }
}
