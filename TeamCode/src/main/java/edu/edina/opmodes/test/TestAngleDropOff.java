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
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;
import edu.edina.library.util.SmartGamepad;

@TeleOp
//@Disabled
public class TestAngleDropOff extends LinearOpMode  {
    @Override
    public void runOpMode() throws InterruptedException {
        RobotHardware hardware = new RobotHardware(hardwareMap);
        RobotConfiguration config = RobotConfiguration.getInstance();
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        ActionManager manager = new ActionManager(hardware);
        boolean leftOpened = true;
        boolean rightOpened = true;

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

            if (pad1.dpad_left) {
                hardware.twistClawServo.setPosition(hardware.twistClawServo.getPosition() + .001);
            }

            if (pad1.dpad_right) {
                hardware.twistClawServo.setPosition(hardware.twistClawServo.getPosition() - .001);
            }

            if (pad1.dpad_up) {
                hardware.angleClawServo.setPosition(hardware.angleClawServo.getPosition() + .01);
            }

            if (pad1.dpad_down) {
                hardware.angleClawServo.setPosition(hardware.angleClawServo.getPosition() - .01);
            }

            if (pad1.right_bumper) {
                if (!rightOpened) {
                    hardware.rightClawServo.setPosition(config.clawRightOpenPosition);
                    hardware.autoClawServo.setPosition(config.autoClawServoOpenPosition);
                    rightOpened = true;
                } else {
                    hardware.rightClawServo.setPosition(config.clawRightClosedPosition);
                    hardware.autoClawServo.setPosition(config.autoClawServoClosePosition);
                    rightOpened = false;
                }
            }

            if (pad1.left_bumper) {
                if (!leftOpened) {
                    hardware.leftClawServo.setPosition(config.clawLeftOpenPosition);
                    leftOpened = true;
                } else {
                    hardware.leftClawServo.setPosition(config.clawLeftClosedPosition);
                    leftOpened = false;
                }
            }

            telemetry.addData("Bumpers control the claws", "");
            telemetry.addData("Press X to drop off from left", "");
            telemetry.addData("Press B to drop off from right", "");
            telemetry.addData("Press A to packup lift", "");
            telemetry.addData("Dpad left/right controls twist servo", "");
            telemetry.addData("Dpad up/down controls angle servo", "");
            telemetry.addData("Twist Claw Position: ", hardware.twistClawServo.getPosition());
            telemetry.addData("Angle Claw Position: ", hardware.angleClawServo.getPosition());

            telemetry.update();
        }
    }
}
