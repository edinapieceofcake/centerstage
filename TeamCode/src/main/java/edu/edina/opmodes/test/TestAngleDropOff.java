package edu.edina.opmodes.test;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.PoCMecanumDrive;

import edu.edina.library.actions.roadrunner.ActionManager;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;
import edu.edina.library.util.SmartGamepad;

@TeleOp
@Disabled
public class TestAngleDropOff extends LinearOpMode  {
    @Override
    public void runOpMode() throws InterruptedException {
        RobotHardware hardware = new RobotHardware(hardwareMap);
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        ActionManager manager = new ActionManager(hardware);

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
                        manager.getLiftReadyToDropPixelFromLeft()
                ));
            }

            if (pad1.b) {
                Actions.runBlocking(new SequentialAction(
                        manager.getLiftReadyToDropPixelFromRight()
                ));
            }

            if (pad1.a) {
                Actions.runBlocking(new SequentialAction(
                        manager.getLiftReadyToDrive()
                ));
            }

            telemetry.addData("Press X to drop off from left", "");
            telemetry.addData("Press B to drop off from right", "");
            telemetry.addData("Press A to packup lift", "");
            RobotState.getInstance().telemetry(telemetry, hardware);
            telemetry.update();
        }
    }
}
