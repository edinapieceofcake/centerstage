package edu.edina.library.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;

import edu.edina.library.enums.HangerState;
import edu.edina.library.enums.LiftServoState;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class RobotHanger implements Subsystem {
    private Robot robot;
    private HangerState hangerState;

    public RobotHanger(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {
        hangerState = HangerState.Idle;
    }

    @Override
    public void start() {
        robot.RobotHardware.homeHangMotorAsync();
    }

    @Override
    public void update() {
        RobotState state = RobotState.getInstance();
        RobotConfiguration config = RobotConfiguration.getInstance();
        RobotHardware hardware = robot.RobotHardware;

        if (robot.Started) {
            if (!hardware.hangMotorHoming) {
                switch (hangerState) {
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
                        hardware.robotHangerMotor.setTargetPosition(config.hangMotorLowDropOffPosition);
                        hardware.robotHangerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        hardware.robotHangerMotor.setPower(config.hangerExtendingPower);
                        break;
                    case HighDrop:
                        hardware.robotHangerMotor.setTargetPosition(config.hangMotorHighDropOffPosition);
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
    public void setProperties(boolean toggleExtend, boolean toggleRetract,
                              boolean hangServo, boolean latchServo, boolean resetLift) {
        RobotState state = RobotState.getInstance();
        RobotHardware hardware = robot.RobotHardware;

        if (toggleExtend) {
            hangerState = HangerState.Extending;
        } else if (toggleRetract) {
            hangerState = HangerState.Retracting;
        } else {
            hangerState = HangerState.Idle;
        }

        if (hangServo) {
            state.currentLiftServoState = LiftServoState.Low;
        } else if (latchServo) {
            state.currentLiftServoState = LiftServoState.Hang;
        }

        if (resetLift) {
            hardware.homeHangMotorAsync();
        }
    }
}
