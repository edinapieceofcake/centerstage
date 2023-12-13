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
public class RedAudienceWithBackStage extends RedBaseAutonomous {
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
                                .splineTo(new Vector2d(-37, -30), Math.toRadians(180))
                                .build()));
                break;
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(-35, -35), Math.toRadians(90))
                                .build()));
                break;
            case Right:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(-36, -36), Math.toRadians(0))
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
                                .splineTo(new Vector2d(-30, -60), Math.toRadians(0))
                                .splineTo(new Vector2d(0, -60), Math.toRadians(0))
                                .splineTo(new Vector2d(38, -60), Math.toRadians(0))
                                .splineTo(new Vector2d(40, -37), Math.toRadians(180))
                                .build())
                );
                break;
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(-30, -60), Math.toRadians(0))
                                .splineTo(new Vector2d(0, -60), Math.toRadians(0))
                                .splineTo(new Vector2d(38, -60), Math.toRadians(0))
                                .splineTo(new Vector2d(40, -38.5), Math.toRadians(180))
                                .build())
                );
                break;
            case Right:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(-30, -60), Math.toRadians(0))
                                .splineTo(new Vector2d(0, -60), Math.toRadians(0))
                                .splineTo(new Vector2d(38, -60), Math.toRadians(0))
                                .splineTo(new Vector2d(40, -49), Math.toRadians(180))
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
                                .splineTo(new Vector2d(60, -14), Math.toRadians(0))
                                .build()));
                break;
            case Corner:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(60, -64), Math.toRadians(0))
                                .build()));
                break;
            default:
                break;
        }
    }
   }
