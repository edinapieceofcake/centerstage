package edu.edina.opmodes.test;

import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import edu.edina.library.actions.roadrunner.ActionManager;
import edu.edina.library.enums.LiftServoState;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
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
        LiftServoState liftServoState = LiftServoState.One;
        boolean leftOpened = true;
        boolean rightOpened = true;
        boolean liftRaised = false;

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
                liftRaised = true;
            }

            if (pad1.y) {
                Actions.runBlocking(new SequentialAction(
                        manager.getLiftReadytoDropThePixelAtSpecificHeight(liftServoState)
                ));
                liftRaised = true;
            }

            if (pad1.b) {
                Actions.runBlocking(new SequentialAction(
                        manager.getLiftReadyToDropPixelFromRight()
                ));
                liftRaised = true;
            }

            if (pad1.a) {
                Actions.runBlocking(new SequentialAction(
                        manager.getLiftReadyToDrive()
                ));
                liftRaised = false;
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

            if (gamepad1.right_trigger != 0) {
                // extend
                int currentPosition = hardware.topLiftMotor.getCurrentPosition();
                int newPosition = currentPosition - (int)(config.liftExtenstionStep * Math.abs(gamepad1.right_trigger));

                hardware.topLiftMotor.setTargetPosition(newPosition);
                hardware.bottomLiftMotor.setTargetPosition(newPosition);
                hardware.topLiftMotor.setPower(config.liftExtendingPower);
                hardware.bottomLiftMotor.setPower(config.liftExtendingPower);

            } else if (gamepad1.left_trigger != 0) {
                // intake
                if (liftRaised && (hardware.topLiftMotor.getCurrentPosition() > config.minimumExtensionBeforeRaisingLiftInTicks)) {
                    hardware.topLiftMotor.setTargetPosition(config.minimumExtensionBeforeRaisingLiftInTicks - 10);
                    hardware.bottomLiftMotor.setTargetPosition(config.minimumExtensionBeforeRaisingLiftInTicks - 10);
                    hardware.topLiftMotor.setPower(config.liftExtendingPower);
                    hardware.bottomLiftMotor.setPower(config.liftExtendingPower);
                } else {
                    int currentPosition = hardware.topLiftMotor.getCurrentPosition();
                    int newPosition = currentPosition + (int)(config.liftRetractingStep * Math.abs(gamepad1.left_trigger));

                    hardware.topLiftMotor.setTargetPosition(newPosition);
                    hardware.bottomLiftMotor.setTargetPosition(newPosition);
                    hardware.topLiftMotor.setPower(config.liftRetractingPower);
                    hardware.bottomLiftMotor.setPower(config.liftRetractingPower);
                }
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

            if (pad1.left_stick_button) {
                switch (liftServoState) {
                    case One:
                        liftServoState = LiftServoState.Two;
                        break;
                    case Two:
                        liftServoState = LiftServoState.Three;
                        break;
                    case Three:
                        liftServoState = LiftServoState.Four;
                        break;
                    case Four:
                        liftServoState = LiftServoState.Five;
                        break;
                    case Five:
                        liftServoState = LiftServoState.Six;
                        break;
                    case Six:
                        liftServoState = LiftServoState.Seven;
                        break;
                    case Seven:
                        liftServoState = LiftServoState.Eight;
                        break;
                    case Eight:
                        liftServoState = LiftServoState.Nine;
                        break;
                }
            }

            if (pad1.right_stick_button) {
                switch (liftServoState) {
                    case Two:
                        liftServoState = LiftServoState.One;
                        break;
                    case Three:
                        liftServoState = LiftServoState.Two;
                        break;
                    case Four:
                        liftServoState = LiftServoState.Three;
                        break;
                    case Five:
                        liftServoState = LiftServoState.Four;
                        break;
                    case Six:
                        liftServoState = LiftServoState.Five;
                        break;
                    case Seven:
                        liftServoState = LiftServoState.Six;
                        break;
                    case Eight:
                        liftServoState = LiftServoState.Seven;
                        break;
                    case Nine:
                        liftServoState = LiftServoState.Eight;
                        break;
                }
            }

            telemetry.addData("Triggers control the lift motor", "");
            telemetry.addData("Bumpers control the claws", "");
            telemetry.addData("Press X to drop off from left", "");
            telemetry.addData("Press Y to drop off from center", "");
            telemetry.addData("Press B to drop off from right", "");
            telemetry.addData("Press A to packup lift", "");
            telemetry.addData("Dpad left/right controls twist servo", "");
            telemetry.addData("Dpad up/down controls angle servo", "");
            telemetry.addData("Press left/right joysticks to raise/lower lift", "");
            telemetry.addData("Robot Angle", hardware.externalImu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
            telemetry.addData("Twist Claw Position: ", hardware.twistClawServo.getPosition());
            telemetry.addData("Angle Claw Position: ", hardware.angleClawServo.getPosition());
            telemetry.addData("Lift servo state", liftServoState);

            telemetry.update();
        }
    }
}
