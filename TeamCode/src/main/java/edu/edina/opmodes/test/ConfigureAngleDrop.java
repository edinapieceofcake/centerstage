package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.SmartGamepad;

@TeleOp
//@Disabled
public class ConfigureAngleDrop extends LinearOpMode {
    private final double maxLiftTicks = 2000;

    @Override
    public void runOpMode() throws InterruptedException {
        RobotHardware hardware = new RobotHardware(hardwareMap);
        boolean leftOpened = true;
        boolean rightOpened = true;
        boolean liftRaised = false;
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        RobotConfiguration config = RobotConfiguration.getInstance();

        DcMotorEx topLiftMotor = hardwareMap.get(DcMotorEx.class, "topLiftMotor");
        DcMotorEx bottomLiftMotor = hardwareMap.get(DcMotorEx.class, "bottomLiftMotor");

        ServoImplEx rightLiftServo = hardwareMap.get(ServoImplEx.class, "leftLiftServo");
        ServoImplEx leftLiftServo = hardwareMap.get(ServoImplEx.class, "rightLiftServo");

        ServoImplEx leftClawServo = hardwareMap.get(ServoImplEx.class, "leftClawServo");
        ServoImplEx rightClawServo = hardwareMap.get(ServoImplEx.class, "rightClawServo");
        ServoImplEx autoClawServo = hardwareMap.get(ServoImplEx.class, "autoClawServo");

        ServoImplEx twistClawServo = hardwareMap.get(ServoImplEx.class, "twistClawServo");
        ServoImplEx angleClawServo = hardwareMap.get(ServoImplEx.class, "angleClawServo");

        twistClawServo.setPosition(config.twistClawServoPickUpPosition);
        angleClawServo.setPosition(config.angleClawPickupPosition);

        leftClawServo.setPosition(config.clawLeftOpenPosition);
        rightClawServo.setPosition(config.clawRightOpenPosition);
        autoClawServo.setPosition(config.autoClawServoOpenPosition);

        topLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        topLiftMotor.setTargetPosition(0);
        topLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        topLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        topLiftMotor.setPower(0);

        bottomLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bottomLiftMotor.setTargetPosition(0);
        bottomLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bottomLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bottomLiftMotor.setPower(0);

        waitForStart();

        hardware.startCurrentMonitor();

        while (opModeIsActive()) {
            pad1.update();

            if (pad1.y) {
                // raise lift
                leftLiftServo.setPosition(config.leftLowDropOffServoPosition);
                rightLiftServo.setPosition(config.rightLowDropOffServoPosition);
                liftRaised = true;
            }

            if (pad1.a) {
                // lower lift
                leftLiftServo.setPosition(config.startingLeftLiftServoPosition);
                rightLiftServo.setPosition(config.startingRightLiftServoPosition);
                liftRaised = false;
            }

            if (pad1.x) {
                topLiftMotor.setTargetPosition(config.liftLowDropOffPosition);
                bottomLiftMotor.setTargetPosition(config.liftLowDropOffPosition);
                topLiftMotor.setPower(config.liftExtendingPower);
                bottomLiftMotor.setPower(config.liftExtendingPower);
            }

            if (gamepad1.right_trigger != 0) {
                // extend
                int currentPosition = topLiftMotor.getCurrentPosition();
                int newPosition = currentPosition - (int)(config.liftExtenstionStep * Math.abs(gamepad1.right_trigger));

                topLiftMotor.setTargetPosition(newPosition);
                bottomLiftMotor.setTargetPosition(newPosition);
                topLiftMotor.setPower(config.liftExtendingPower);
                bottomLiftMotor.setPower(config.liftExtendingPower);

            } else if (gamepad1.left_trigger != 0) {
                // intake
                if (liftRaised && (topLiftMotor.getCurrentPosition() > config.minimumExtensionBeforeRaisingLiftInTicks)) {
                    topLiftMotor.setTargetPosition(config.minimumExtensionBeforeRaisingLiftInTicks - 10);
                    bottomLiftMotor.setTargetPosition(config.minimumExtensionBeforeRaisingLiftInTicks - 10);
                    topLiftMotor.setPower(config.liftExtendingPower);
                    bottomLiftMotor.setPower(config.liftExtendingPower);
                } else {
                    int currentPosition = topLiftMotor.getCurrentPosition();
                    int newPosition = currentPosition + (int)(config.liftRetractingStep * Math.abs(gamepad1.left_trigger));

                    topLiftMotor.setTargetPosition(newPosition);
                    bottomLiftMotor.setTargetPosition(newPosition);
                    topLiftMotor.setPower(config.liftRetractingPower);
                    bottomLiftMotor.setPower(config.liftRetractingPower);
                }
            }

            if (pad1.right_bumper) {
                if (!rightOpened) {
                    rightClawServo.setPosition(config.clawRightOpenPosition);
                    autoClawServo.setPosition(config.autoClawServoOpenPosition);
                    rightOpened = true;
                } else {
                    rightClawServo.setPosition(config.clawRightClosedPosition);
                    autoClawServo.setPosition(config.autoClawServoClosePosition);
                    rightOpened = false;
                }
            }

            if (pad1.left_bumper) {
                if (!leftOpened) {
                    leftClawServo.setPosition(config.clawLeftOpenPosition);
                    leftOpened = true;
                } else {
                    leftClawServo.setPosition(config.clawLeftClosedPosition);
                    leftOpened = false;
                }
            }

            if (pad1.dpad_left) {
                // twist claw
                twistClawServo.setPosition(twistClawServo.getPosition() + .01);
            }

            if (pad1.dpad_right) {
                // twist claw
                twistClawServo.setPosition(twistClawServo.getPosition() - .01);
            }

            if (pad1.dpad_up) {
                // angle claw
                angleClawServo.setPosition(angleClawServo.getPosition() + .05);
            }

            if (pad1.dpad_down) {
                // angle claw
                angleClawServo.setPosition(angleClawServo.getPosition() - .05);
            }

            if (pad1.left_stick_button) {
                twistClawServo.setPosition(config.twistClawServoPickUpPosition);
                angleClawServo.setPosition(config.angleClawDrivePosition);
            }

            if (pad1.right_stick_button) {
                twistClawServo.setPosition(config.twistClawServoDropOffPosition);
                angleClawServo.setPosition(config.angleClawLowDropOffPosition);
            }

            if (gamepad1.left_stick_x != 0) {
                twistClawServo.setPosition(config.leftAutoLowTwistClawServoDropOffPosition);
                angleClawServo.setPosition(config.leftAutoLowAngleClawServoDropOffPosition);
            }

            if (gamepad1.right_stick_x != 0) {
                twistClawServo.setPosition(config.rightLowTwistClawServoDropOffPosition);
                angleClawServo.setPosition(config.rightLowAngleClawServoDropOffPosition);
            }

            telemetry.addData("Triggers control the lift motor", "");
            telemetry.addData("Bumpers control the claws", "");
            telemetry.addData("X to raise the lift to low position", "");
            telemetry.addData("Y to raise the lift", "");
            telemetry.addData("A to lower lift", "");
            telemetry.addData("B to raise the lift to medium position", "");
            telemetry.addData("Dpad up/down controls the angle servo", "");
            telemetry.addData("Dpad left/right controls twist servo", "");
            telemetry.addData("Press left stick to move claw to pickup position", "");
            telemetry.addData("Press right stick to move claw to drop off position", "");

            telemetry.addData("=======================================================", "");

            telemetry.addData("Top Lift Motor Current Position: ", topLiftMotor.getCurrentPosition());
            telemetry.addData("Bottom Lift Motor Current Position: ", bottomLiftMotor.getCurrentPosition());
            telemetry.addData("Twist Claw Position: ", twistClawServo.getPosition());
            telemetry.addData("Angle Claw Position: ", angleClawServo.getPosition());

            telemetry.update();
        }

        hardware.stopCurrentMonitor();
    }
}
