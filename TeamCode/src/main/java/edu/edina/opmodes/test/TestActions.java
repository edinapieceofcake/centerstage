package edu.edina.opmodes.test;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import edu.edina.library.actions.roadrunner.ActionManager;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;
import edu.edina.library.util.SmartGamepad;

@TeleOp
@Disabled
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
                            manager.openLeftClaw(),
                            manager.openAutoClaw(),
                            manager.openRightClaw(),
                            manager.positionTheClawToPickupPixels()
                        )
                );
            }

            if (pad1.a) {
                Actions.runBlocking(new SequentialAction(
                            manager.closeLeftClaw(),
                            manager.closeRightClaw(),
                            manager.closeAutoClaw(),
                            manager.positionTheClawToDriveWithPixels()
                        )
                );
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
                        manager.getLiftReadyToDropThePixelLowOnTheWall(),
                        manager.openRightClaw(),
                        new SleepAction(.2),
                        manager.getLiftReadyToDrive()
                ));
            }

            if (pad1.dpad_down) {
                Actions.runBlocking(new SequentialAction(
                        manager.getLiftReadyToDrive()
                ));
            }

            telemetry.addData("Press left bumper to run lift to -600", "");
            telemetry.addData("Press right bumper to run lift home", "");
            telemetry.addData("Press x to lift and lower angle claw", "");
            telemetry.addData("Press y to load the pixels", "");
            telemetry.addData("Press a to lower claw for loading", "");
            telemetry.addData("Press b to twist and return twist claw. Make sure lift is out!!!", "");
            telemetry.addData("Dpad up to extend and get lift ready to drop", "");
            telemetry.addData("Dpad down to retract lift and get ready to drive", "");
            telemetry.addData("Dpad left to raise the lift a bit", "");
            telemetry.addData("Dpad right to lower the lift a bit", "");
            telemetry.addData("Top Motor Lift Position", hardware.topLiftMotor.getCurrentPosition());
            telemetry.update();
        }
    }
}
