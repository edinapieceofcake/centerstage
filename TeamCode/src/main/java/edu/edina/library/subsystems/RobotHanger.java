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
    public void start() {}

    @Override
    public void update() {
        RobotState state = RobotState.getInstance();
        RobotConfiguration config = RobotConfiguration.getInstance();
        RobotHardware hardware = robot.RobotHardware;

        if (robot.Started) {
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
            }


        }
    }
    public void setProperties(boolean toggleExtend, boolean toggleRetract,
                              boolean hangServo, boolean latchServo) {
        RobotState state = RobotState.getInstance();
        RobotConfiguration config = RobotConfiguration.getInstance();
        RobotHardware hardware = robot.RobotHardware;

        if (toggleExtend) {
            hangerState = HangerState.Extending;
        } else if (toggleRetract) {
            hangerState = HangerState.Retracting;
        } else {
            hangerState = HangerState.Idle;
        }

        if (hangServo) {
            state.currentLeftLiftServoPosition = config.leftLowDropOffServoPosition;
            state.currentRightLiftServoPosition = config.rightLowDropOffServoPosition;
            state.currentLiftServoState = LiftServoState.Medium;
        } else if (latchServo) {
            state.currentLeftLiftServoPosition = config.startingLeftLiftServoPosition;
            state.currentRightLiftServoPosition = config.startingRightLiftServoPosition;
            state.currentLiftServoState = LiftServoState.Start;
        }
    }
}
