package edu.edina.opmodes.test;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import edu.edina.library.actions.roadrunner.ActionManager;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;
import edu.edina.library.util.SmartGamepad;

@TeleOp
public class TestActions extends LinearOpMode  {
    @Override
    public void runOpMode() throws InterruptedException {
        RobotHardware hardware = new RobotHardware(hardwareMap);
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        RobotState state = RobotState.getInstance();
        ActionManager manager = new ActionManager(hardware);

        // Start Position

        // use out version of the drive based off the hardware that we created above.

        manager.init();

        waitForStart();

        if (opModeIsActive()) {
            manager.start();
        }

        while (opModeIsActive()) {
            pad1.update();

            if (pad1.x) {
                Actions.runBlocking(new SequentialAction(
                        manager.positionTheClawToDriveWithPixels(),
                        new SleepAction(2),
                        manager.positionTheClawToPickupPixels(),
                        new SleepAction(2),
                        manager.positionTheClawToDriveWithPixels()
                ));
            }

            if (pad1.y) {
                Actions.runBlocking(new SequentialAction(
                        new ParallelAction(
                                manager.openLeftClaw(),
                                manager.openAutoClaw()
                        ), // add in auto claw
                        new SleepAction(2),
                        new ParallelAction(manager.closeLeftClaw()),  // add in auto claw
                        new SleepAction(2),
                        new ParallelAction(manager.openLeftClaw())  // add in auto claw
                        ));
            }

            if (pad1.a) {
                // add right claw
            }

            if (pad1.b) {
                Actions.runBlocking(new SequentialAction(
                        manager.twistClawForPickup(),
                        new SleepAction(2),
                        manager.twistClawForDropOff(),
                        new SleepAction(2),
                        manager.twistClawForPickup()
                ));
            }

            if (pad1.left_bumper) {
                Actions.runBlocking(new SequentialAction(
                manager.runLiftToPosition(-600)
                ));
            }

            if (pad1.right_bumper) {
                Actions.runBlocking(new SequentialAction(
                        manager.zeroLift()
                ));
            }

            if (pad1.dpad_up) {
                Actions.runBlocking(new SequentialAction(
                        manager.closeRightClaw(),
                        manager.getLiftReadyToDropThePixelOnTheWall(),
                        new SleepAction(.2),
                        manager.openRightClaw(),
                        new SleepAction(.5),
                        manager.getLiftReadyToDrive()
                ));
            }

            if (pad1.dpad_down) {
                Actions.runBlocking(new SequentialAction(
                        manager.getLiftReadyToDrive()
                ));
            }

            telemetry.addData("Press left bumper to run lift to -200", "");
            telemetry.addData("Press right bumper to run lift home", "");
            telemetry.addData("Press x to lift and lower angle claw", "");
            telemetry.addData("Press y to open and close the left claw", "");
            telemetry.addData("Press a to open and close the right claw", "");
            telemetry.addData("Press b to twist and return twist claw. Make sure lift is out!!!", "");
            telemetry.addData("Dpad up to extend and get lift ready to drop", "");
            telemetry.addData("Dpad down to retract lift and get ready to drive", "");
            state.telemetry(telemetry, hardware);
            telemetry.update();
        }
    }
}
