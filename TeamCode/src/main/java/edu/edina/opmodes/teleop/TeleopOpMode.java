package edu.edina.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import edu.edina.library.util.Robot;
import edu.edina.library.util.SmartGamepad;

public class TeleopOpMode extends OpMode {
    protected Robot robot;
    protected SmartGamepad _gamepad1;
    protected SmartGamepad _gamepad2;

    @Override
    public void init() {
        _gamepad1 = new SmartGamepad(gamepad1);
        _gamepad2 = new SmartGamepad(gamepad2);
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

        _gamepad1.update();
        _gamepad2.update();

        robot.MecanumDrive.setProperties(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        robot.Claw.setProperties(_gamepad1.left_bumper, _gamepad1.right_bumper);

        robot.Lift.setProperties(gamepad1.left_trigger != 0, gamepad1.right_trigger != 0, _gamepad1.a,
                _gamepad1.x, _gamepad1.y, _gamepad1.b, gamepad1.dpad_up, gamepad1.dpad_down);

        robot.RobotHanger.setProperties(gamepad2.left_bumper, gamepad2.right_bumper);

        robot.DroneLauncher.setProperties(_gamepad2.y);

        robot.update();
        robot.telemetry();
    }

    @Override
    public  void stop() {
        robot.stop();
    }
}
