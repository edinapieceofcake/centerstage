package edu.edina.library.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;

import edu.edina.library.enums.HangerState;
import edu.edina.library.enums.LiftServoRange;
import edu.edina.library.enums.LiftServoState;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class RobotHanger implements Subsystem {
    private RobotHardware hardware;
    private boolean started = false;

    public RobotHanger(RobotHardware hardware) {
        this.hardware = hardware;
    }

    public RobotHanger(Robot robot) {
        this.hardware = robot.RobotHardware;
    }

    @Override
    public void init() {
        RobotState.getInstance().hangerState = HangerState.Idle;
    }

    @Override
    public void start() {
        hardware.homeHangMotorAsync();
        started = true;
    }

    @Override
    public void update() {
        RobotState state = RobotState.getInstance();
        RobotConfiguration config = RobotConfiguration.getInstance();

        if (started) {
            state.currentHangerPosition = hardware.robotHangerMotor.getCurrentPosition();

            if (!hardware.hangMotorHoming) {
                switch (state.hangerState) {
                    case Retracting:
                        hardware.robotHangerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        hardware.robotHangerMotor.setPower(RobotConfiguration.getInstance().hangerRetractingPower);
                        break;
                    case Extending:
                        hardware.robotHangerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        hardware.robotHangerMotor.setPower(RobotConfiguration.getInstance().hangerExtendingPower);
                        break;
                    case Idle:
                        if (hardware.robotHangerMotor.getMode() == DcMotor.RunMode.RUN_USING_ENCODER) {
                            hardware.robotHangerMotor.setPower(0);
                        }
                        break;
                    case Hang:
                        hardware.robotHangerMotor.setTargetPosition(config.hangMotorHangPosition);
                        hardware.robotHangerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        hardware.robotHangerMotor.setPower(config.hangerExtendingPower);
                        break;
                    case LowDrop:
                        if (state.liftServoRange == LiftServoRange.Low) {
                            hardware.robotHangerMotor.setTargetPosition(config.hangMotorLowDropOffPosition);
                        } else {
                            hardware.robotHangerMotor.setTargetPosition(config.hangMotorMediumDropOffPosition);
                        }

                        hardware.robotHangerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        hardware.robotHangerMotor.setPower(config.hangerExtendingPower);
                        break;
                    case HighDrop:
                        if (state.liftServoRange == LiftServoRange.Low) {
                            hardware.robotHangerMotor.setTargetPosition(config.hangMotorMediumDropOffPosition);
                        } else {
                            hardware.robotHangerMotor.setTargetPosition(config.hangMotorHighDropOffPosition);
                        }

                        hardware.robotHangerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        hardware.robotHangerMotor.setPower(config.hangerExtendingPower);
                        break;
                    case Store:
                        hardware.robotHangerMotor.setTargetPosition(config.hangMotorStorePosition);
                        hardware.robotHangerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        hardware.robotHangerMotor.setPower(config.hangerExtendingPower);
                        break;
                }
            }
        }
    }
}
