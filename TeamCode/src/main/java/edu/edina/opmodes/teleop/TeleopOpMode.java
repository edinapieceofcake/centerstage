package edu.edina.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import edu.edina.library.util.Smartgamepad;

public class TeleopOpMode extends OpMode {
    protected Robot robot;
    protected Smartgamepad _gamepad1;
    protected Smartgamepad _gamepad2;

    @Override
    public void init() {
        _gamepad1 = new Smartgamepad(gamepad1);
        _gamepad2 = new Smartgamepad(gamepad2);
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

        robot.Lift.setLiftProperties(_gamepad1.dpad_up, _gamepad1.dpad_down, _gamepad1.a, _gamepad1.b);

        robot.MecanumDrive.setDriveProperties(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x);

        robot.update();
        robot.telemetry();
    }

    @Override
    public  void stop() {
        robot.stop();
    }
}
