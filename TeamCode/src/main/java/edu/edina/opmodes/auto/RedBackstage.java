package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.hardware.PwmControl;

import org.firstinspires.ftc.teamcode.MecanumDrive;

import edu.edina.library.enums.Alliance;
import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.DropOffState;
import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftSlideState;
import edu.edina.library.enums.ParkLocation;
import edu.edina.library.enums.PickUpState;
import edu.edina.library.enums.PropLocation;
import edu.edina.library.subsystems.Claw;
import edu.edina.library.subsystems.Lift;
import edu.edina.library.util.PoCHuskyLens;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;
import edu.edina.library.util.SmartGamepad;

@Autonomous
public class RedBackstage extends BaseAutonomous {

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
                                .splineTo(new Vector2d(4, -32), Math.toRadians(180))
                                .build()));
                break;
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(9, -35), Math.toRadians(90))
                                .build()));
                break;
            case Right:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .splineTo(new Vector2d(26, -32), Math.toRadians(180))
                                .build()));
                break;
            default:
                break;
        }

        // Drop Purple Pixel
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
                                .splineTo(new Vector2d(37,-28), Math.toRadians(0))
                                .build())
                );
                break;
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(37,-36), Math.toRadians(0))
                                .build())
                );
                break;
            case Right:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                                .setReversed(true)
                                .splineTo(new Vector2d(37,-44), Math.toRadians(0))
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

        // Move forward to drop
        Actions.runBlocking(drive.actionBuilder(drive.pose).lineToX(47).build());

        // Open Claw
        state.rightClawState = ClawState.Opened;
        claw.update();
        sleep(1000);

        // Back away from board
        Actions.runBlocking(drive.actionBuilder(drive.pose).lineToX(37).build());

        // Retract Lift
        retractLift(state);

        // where to park?
        switch (parkLocation) {
            case Center:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                            .setReversed(true)
                            .splineTo(new Vector2d(53, -14), Math.toRadians(0))
                            .build()));
                break;
            case Corner:
                Actions.runBlocking(new SequentialAction(
                        drive.actionBuilder(drive.pose)
                            .setReversed(true)
                            .splineTo(new Vector2d(53, -60), Math.toRadians(0))
                            .build()));
                break;
            default:
                break;
        }
    }

    @Override
    protected Alliance getAlliance() {
        return Alliance.Red;
    }
}
