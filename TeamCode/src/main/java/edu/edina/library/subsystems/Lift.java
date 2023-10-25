package edu.edina.library.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;

import edu.edina.library.enums.HangerState;
import edu.edina.library.enums.LiftState;
import edu.edina.library.util.Robot;

public class Lift extends Subsystem{
    private Robot robot;
    private final double maxLiftTicks = 600;
    private boolean liftMotorReset = false;

    public Lift(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {
        robot.RobotState.currentLiftState = LiftState.Idle;
    }

    @Override
    public void start() {}

    @Override
    public void update() {
        if (robot.Started) {
            switch (robot.RobotState.currentLiftState) {
                case Retracting:
                    robot.RobotHardware.topLiftMotor.setPower(robot.RobotConfiguration.liftRetractingPower);
                    robot.RobotHardware.bottomLiftMotor.setPower(robot.RobotConfiguration.liftRetractingPower);
                    break;
                case Extending:
                    robot.RobotHardware.topLiftMotor.setPower(robot.RobotConfiguration.liftExtendingPower);
                    robot.RobotHardware.bottomLiftMotor.setPower(robot.RobotConfiguration.liftExtendingPower);
                    break;
                case Idle:
                    robot.RobotHardware.topLiftMotor.setPower(0);
                    robot.RobotHardware.bottomLiftMotor.setPower(0);
                    break;
            }

            if (!robot.RobotHardware.liftSwitch.getState()) {
                if (!liftMotorReset) {
                    robot.RobotHardware.topLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    robot.RobotHardware.topLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.RobotHardware.topLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    robot.RobotHardware.bottomLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    robot.RobotHardware.bottomLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.RobotHardware.bottomLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    liftMotorReset = true;
                }
            } else {
                liftMotorReset = false;
            }

            robot.RobotState.currentTopMotorPosition = robot.RobotHardware.topLiftMotor.getCurrentPosition();
            robot.RobotState.currentBottomMotorPosition = robot.RobotHardware.bottomLiftMotor.getCurrentPosition();

            // only raise the lift after we move far enough out
            if (robot.RobotState.currentTopMotorPosition > robot.RobotConfiguration.minimumExtensionBeforeRaisingLift) {
                //robot.RobotState.currentLiftServoPosition = (1/maxLiftTicks) * robot.RobotState.currentTopMotorPosition + 0.26;
                robot.RobotHardware.leftLiftServo.setPosition(robot.RobotState.currentLiftServoPosition);
                robot.RobotHardware.rightLiftServo.setPosition(robot.RobotState.currentLiftServoPosition);
            }
        }
    }

    public void setProperties(boolean closeLift, boolean openLift) {
        if (openLift) {
            robot.RobotState.currentLiftState = LiftState.Extending;
        } else if (closeLift) {
            robot.RobotState.currentLiftState = LiftState.Retracting;
        } else {
            robot.RobotState.currentLiftState = LiftState.Idle;
        }
    }

    private double round(double originalValue) {
        return ((int)(originalValue * 100)) / 100.0;
    }
}
