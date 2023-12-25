package edu.edina.opmodes.test;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.checkerframework.checker.units.qual.A;

import edu.edina.library.actions.roadrunner.ActionManager;
import edu.edina.library.actions.roadrunner.AngleClawAction;
import edu.edina.library.actions.roadrunner.AutoClawAction;
import edu.edina.library.actions.roadrunner.DropOffPixelAction;
import edu.edina.library.actions.roadrunner.LeftClawAction;
import edu.edina.library.actions.roadrunner.RetractLiftAction;
import edu.edina.library.actions.roadrunner.RightClawAction;
import edu.edina.library.actions.roadrunner.RunLiftToPositionAction;
import edu.edina.library.actions.roadrunner.TwistClawAction;
import edu.edina.library.actions.roadrunner.ZeroLiftAction;
import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.TwistServoState;
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
                        manager.getDriveAngleClawAction(),
                        new SleepAction(2),
                        manager.getPickupAngleClawAction(),
                        new SleepAction(2),
                        manager.getDriveAngleClawAction()
                ));
            }

            if (pad1.y) {
                Actions.runBlocking(new SequentialAction(
                        new ParallelAction(manager.getOpenLeftClawAction(), manager.getOpenAutoClawAction()),
                        new SleepAction(2),
                        new ParallelAction(manager.getCloseLeftClawAction(), manager.getCloseAutoClawAction()),
                        new SleepAction(2),
                        new ParallelAction(manager.getOpenLeftClawAction(), manager.getOpenAutoClawAction())
                        ));
            }

            if (pad1.a) {
                Actions.runBlocking(new SequentialAction(
                        manager.getOpenRightClawAction(),
                        new SleepAction(2),
                        manager.getCloseRightClawAction(),
                        new SleepAction(2),
                        manager.getOpenRightClawAction()
                ));
            }

            if (pad1.b) {
                Actions.runBlocking(new SequentialAction(
                        manager.getPickupTwistClawAction(),
                        new SleepAction(2),
                        manager.getDropOffTwistClawAction(),
                        new SleepAction(2),
                        manager.getPickupTwistClawAction()
                ));
            }

            if (pad1.left_bumper) {
                Actions.runBlocking(new SequentialAction(
                manager.getRunLiftToPositionAction(-600)
                ));
            }

            if (pad1.right_bumper) {
                Actions.runBlocking(new SequentialAction(
                        manager.getZeroLiftAction()
                ));
            }

            if (pad1.dpad_up) {
                Actions.runBlocking(new SequentialAction(
                        manager.getCloseRightClawAction(),
                        manager.getDropPixelAction(),
                        manager.getOpenRightClawAction()
                ));
            }

            if (pad1.dpad_down) {
                Actions.runBlocking(new SequentialAction(
                        manager.getRetractLiftAction()
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
