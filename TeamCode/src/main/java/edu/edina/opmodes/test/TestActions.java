package edu.edina.opmodes.test;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import edu.edina.library.actions.roadrunner.AngleClawAction;
import edu.edina.library.actions.roadrunner.AutoClawAction;
import edu.edina.library.actions.roadrunner.LeftClawAction;
import edu.edina.library.actions.roadrunner.RightClawAction;
import edu.edina.library.actions.roadrunner.RunLiftToPositionAction;
import edu.edina.library.actions.roadrunner.TwistClawAction;
import edu.edina.library.actions.roadrunner.ZeroLiftAction;
import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.TwistServoState;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.SmartGamepad;

@TeleOp
public class TestActions extends LinearOpMode  {
    @Override
    public void runOpMode() throws InterruptedException {
        RobotHardware hardware = new RobotHardware(hardwareMap);
        SmartGamepad pad1 = new SmartGamepad(gamepad1);

        // Start Position

        // use out version of the drive based off the hardware that we created above.

        waitForStart();

        while (opModeIsActive()) {
            pad1.update();

            if (pad1.x) {
                Actions.runBlocking(new SequentialAction(
                        new AngleClawAction(hardware, AngleClawState.Drive),
                        new SleepAction(2),
                        new AngleClawAction(hardware, AngleClawState.Pickup),
                        new SleepAction(2),
                        new AngleClawAction(hardware, AngleClawState.Drive)
                ));
            }

            if (pad1.y) {
                Actions.runBlocking(new SequentialAction(
                        new ParallelAction(new LeftClawAction(hardware, ClawState.Opened), new AutoClawAction(hardware, ClawState.Opened)),
                        new SleepAction(2),
                        new ParallelAction(new LeftClawAction(hardware, ClawState.Closed), new AutoClawAction(hardware, ClawState.Opened)),
                        new SleepAction(2),
                        new ParallelAction(new LeftClawAction(hardware, ClawState.Opened), new AutoClawAction(hardware, ClawState.Opened))
                        ));
            }

            if (pad1.a) {
                Actions.runBlocking(new SequentialAction(
                        new RightClawAction(hardware, ClawState.Opened),
                        new SleepAction(2),
                        new RightClawAction(hardware, ClawState.Closed),
                        new SleepAction(2),
                        new RightClawAction(hardware, ClawState.Opened)
                ));
            }

            if (pad1.b) {
                Actions.runBlocking(new SequentialAction(
                        new TwistClawAction(hardware, TwistServoState.Pickup),
                        new SleepAction(2),
                        new TwistClawAction(hardware, TwistServoState.DropOff),
                        new SleepAction(2),
                        new TwistClawAction(hardware, TwistServoState.Pickup)
                ));
            }

            if (pad1.left_bumper) {
                Actions.runBlocking(new SequentialAction(
                        new RunLiftToPositionAction(hardware, -600)
                ));
            }

            if (pad1.right_bumper) {
                Actions.runBlocking(new SequentialAction(
                        new ZeroLiftAction(hardware)
                ));
            }

            telemetry.addData("Press left bumper to run lift to -200", "");
            telemetry.addData("Press right bumper to run lift home", "");
            telemetry.addData("Press x to lift and lower angle claw", "");
            telemetry.addData("Press y to open and close the left claw", "");
            telemetry.addData("Press a to open and close the right claw", "");
            telemetry.addData("Press b to twist and return twist claw. Make sure lift is out!!!", "");
            telemetry.update();
        }
    }
}
