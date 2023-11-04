package edu.edina.library.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;

import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.ReelState;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class Reel extends Subsystem {
    Robot robot;
    public Reel(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {

    }

    @Override
    public void start() {

    }

    @Override
    public void update() {
        RobotState state = RobotState.getInstance();
        RobotHardware hardware = robot.RobotHardware;
        RobotConfiguration config = RobotConfiguration.getInstance();

        if ((state.currentLiftDriveState == LiftDriveState.Manual) ||
            (state.currentLiftDriveState == LiftDriveState.HighDropOff) ||
            (state.currentLiftDriveState == LiftDriveState.LowDropOff)){

            hardware.reelMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            switch (state.currentLiftSlideState) {
                case Extending:
                    hardware.reelMotor.setPower(0.1);
                    break;
                case Retracting:
                    hardware.reelMotor.setPower(-0.6);
                    break;
                case Idle:
                    hardware.reelMotor.setPower(0);
                    break;
            }
        } else {
            if (state.currentReelState == ReelState.Start) {
                hardware.reelMotor.setTargetPosition(hardware.reelMotor.getCurrentPosition() + config.reelDistanceForFirstRetraction);
                hardware.reelMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                hardware.reelMotor.setPower(0.5);
                state.currentReelState = ReelState.FirstRetraction;
            } else if (state.currentReelState == ReelState.FirstRetraction) {
                if (hardware.reelMotor.getCurrentPosition() < config.reelDistanceForFirstRetraction + 10)
                if (state.lastKnownLiftState == LiftDriveState.HighDropOff) {
                    hardware.reelMotor.setTargetPosition(hardware.reelMotor.getCurrentPosition() + config.reelDistanceForHighDrop);
                    state.currentReelState = ReelState.Drop;
                } else {
                    hardware.reelMotor.setTargetPosition(hardware.reelMotor.getCurrentPosition() + config.reelDistanceForLowDrop);
                    state.currentReelState = ReelState.Drop;
                }
            }
        }
    }
}