package edu.edina.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import edu.edina.library.util.Robot;
import edu.edina.library.util.SmartGamepad;

public class TeleopOpMode extends OpMode {
    protected Robot robot;
    protected SmartGamepad driver1Gamepad;
    protected SmartGamepad driver2Gamepad;

    @Override
    public void init() {
        driver1Gamepad = new SmartGamepad(gamepad1);
        driver2Gamepad = new SmartGamepad(gamepad2);
    }

    @Override
    public void init_loop() {
        // hit after init is called and before play
        // great place to put vision code to detect where to go for autonomous
    }

    @Override
    public void start() {
        robot.start();
    }

    @Override
    public void loop() {

        driver1Gamepad.update();
        driver2Gamepad.update();

        robot.MecanumDrive.setProperties(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        robot.Claw.setProperties(driver1Gamepad.left_bumper, driver1Gamepad.right_bumper, driver1Gamepad.dpad_left, driver1Gamepad.dpad_right);

        robot.Lift.setProperties(gamepad1.right_trigger, gamepad1.left_trigger, driver1Gamepad.a,
                driver1Gamepad.x, driver1Gamepad.y, driver1Gamepad.b, gamepad1.dpad_up, gamepad1.dpad_down);

        robot.RobotHanger.setProperties(gamepad2.left_bumper, gamepad2.right_bumper);

        robot.DroneLauncher.setProperties(driver2Gamepad.y);

        robot.update();
        robot.telemetry();
    }

    @Override
    public  void stop() {
        robot.stop();
    }
}
