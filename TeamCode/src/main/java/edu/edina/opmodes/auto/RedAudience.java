package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.edina.library.enums.Alliance;
import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.PropLocation;
import edu.edina.library.util.RobotState;

@Autonomous
public class RedAudience extends BaseAutonomous {
    protected void runPaths() {
        RobotState state = RobotState.getInstance();

        // We want to detect if we don't have a block, but still need to default
        if (propLocation == PropLocation.None) {
            propLocation = PropLocation.Right;
        }

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
                                .splineTo(new Vector2d(-36, -38), Math.toRadians(0))
                                .build()));
                break;
            default:
                break;
        }

        // place purple on the ground
        state.leftClawState = ClawState.Opened;
        claw.update();
        sleep(500);
    }

    @Override
    protected RevBlinkinLedDriver.BlinkinPattern getUnsuccessfulPropMatchColor() {
        return RevBlinkinLedDriver.BlinkinPattern.RED;
    }

    @Override
    protected RevBlinkinLedDriver.BlinkinPattern getSuccessfulPropMatchColor() {
        return RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_RED;
    }

    @Override
    protected Alliance getAlliance() {
        return Alliance.Red;
    }
}
