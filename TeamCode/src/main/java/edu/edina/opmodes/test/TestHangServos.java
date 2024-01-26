package edu.edina.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;

import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.SmartGamepad;

@TeleOp
//@Disabled
public class TestHangServos extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        RobotHardware hardware = new RobotHardware(hardwareMap);
        RobotConfiguration config = RobotConfiguration.getInstance();

        hardware.rightLiftServo.setPosition(config.startingRightLiftServoPosition);
        hardware.leftLiftServo.setPosition(config.startingLeftLiftServoPosition);
        waitForStart();

        while (opModeIsActive()) {
            pad1.update();

            if (pad1.y) {
                hardware.leftLiftServo.setPosition(config.leftLowDropOffServoPosition);
                hardware.rightLiftServo.setPosition(config.rightLowDropOffServoPosition);
            }

            if (pad1.a) {
                ((PwmControl)hardware.leftLiftServo).setPwmDisable();
                ((PwmControl)hardware.rightLiftServo).setPwmDisable();
            }

            telemetry.addData("Press the y button raise the lift", "");
            telemetry.addData("Press a to kill the PWM signal", "");
            telemetry.update();
        }
    }
}
