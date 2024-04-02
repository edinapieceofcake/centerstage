package edu.edina.opmodes.teleop;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import edu.edina.library.domain.Claw;
import edu.edina.library.domain.DroneLauncher;
import edu.edina.library.domain.Lift;
import edu.edina.library.domain.MecanumDrive;
import edu.edina.library.domain.RobotHanger;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;
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

    // hit after init is called and before play
    // great place to put vision code to detect where to go for autonomous
    @Override
    public void init_loop() {
        telemetry.update();
    }

    @Override
    public void start() {

        robot.start();

        robot.RobotHardware.startCurrentMonitor();
    }

    @Override
    public void loop() {

        driver1Gamepad.update();
        driver2Gamepad.update();

        MecanumDrive.setProperties(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        Claw.setProperties(driver1Gamepad.left_bumper, driver1Gamepad.right_bumper, driver1Gamepad.dpad_left, driver1Gamepad.dpad_right, driver1Gamepad.b);

        Lift.setProperties(gamepad1.right_trigger, gamepad1.left_trigger, driver1Gamepad.a,
                driver1Gamepad.x, driver1Gamepad.y, driver2Gamepad.y,
                driver1Gamepad.left_stick_button, driver1Gamepad.right_stick_button);

        RobotHanger.setProperties(gamepad2.left_trigger != 0, gamepad2.right_trigger != 0,
                driver2Gamepad.dpad_up, driver2Gamepad.dpad_down, driver2Gamepad.left_bumper, robot.RobotHardware);

        DroneLauncher.setProperties(driver2Gamepad.a);

        robot.update();
        robot.telemetry();
    }

    @Override
    public  void stop() {

        robot.stop();

        robot.RobotHardware.stopCurrentMonitor();
    }
}
