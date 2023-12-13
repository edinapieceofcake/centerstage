package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.ParkLocation;
import edu.edina.library.enums.PropLocation;
import edu.edina.library.util.RobotState;

@Autonomous
public class BlueAudienceWithBackStage extends BlueBaseAutonomous {
   @Override
    protected void runPaths(ParkLocation parkLocation) {
        RobotState state = RobotState.getInstance();

        // We want to detect if we don't have a block, but still need to default
        if (propLocation == PropLocation.None) {
            propLocation = PropLocation.Right;
        }

        // drop off purple pixel
        switch(propLocation) {
            case Left:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(-34, 31), Math.toRadians(0))
                                .build()));
                break;
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(-37, 33), Math.toRadians(270))
                                .build()));
                break;
            case Right:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(-49, 38), Math.toRadians(270))
                                .build()));
                break;
            default:
                break;
        }

        // Drop purple pixel
        state.leftClawState = ClawState.Opened;
        claw.update();
        sleep(500);
        state.leftClawState = ClawState.Closed;
        claw.update();

        // drop off yellow pixel
        switch (propLocation) {
            case Left:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(-30, 12), Math.toRadians(0))
                                .splineTo(new Vector2d(35, 12), Math.toRadians(0))
                                .splineTo(new Vector2d(40, 37), Math.toRadians(180))
                                .build())
                );
                break;
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(-55, 35), Math.toRadians(270))
                                .splineTo(new Vector2d(-45, 12), Math.toRadians(0))
                                .splineTo(new Vector2d(35, 12), Math.toRadians(0))
                                .splineTo(new Vector2d(40, 26), Math.toRadians(180))
                                .build())
                );
                break;
            case Right:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(-55, 35), Math.toRadians(270))
                                .splineTo(new Vector2d(-45, 12), Math.toRadians(0))
                                .splineTo(new Vector2d(35, 12), Math.toRadians(0))
                                .splineTo(new Vector2d(40, 19), Math.toRadians(180))
                                .build())
                );
                break;
            default:
                break;
        }

        // Straighten out Robot after travel
        Actions.runBlocking(new SequentialAction(
                drive.actionBuilder(drive.pose)
                        .turnTo(Math.toRadians(0))
                        .build()));

        // Extend and prepare lift
        extendLift(state);

        // Update Telemetry
        state.telemetry(telemetry, hardware);
        telemetry.update();

        // Move Forward to drop
        Actions.runBlocking(drive.actionBuilder(drive.pose).lineToX(52).build());

        // Open Claw
        state.rightClawState = ClawState.Opened;
        claw.update();
        sleep(500);

        // Back away from board
        Actions.runBlocking(drive.actionBuilder(drive.pose).lineToX(40).build());

        // Retract Lift
        retractLift(state);

        // where to park?
        switch (parkLocation) {
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(60, 14), Math.toRadians(0))
                                .build()));
                break;
            case Corner:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(60, 51), Math.toRadians(0))
                                .build()));
                break;
            default:
                break;
        }
    }
}
