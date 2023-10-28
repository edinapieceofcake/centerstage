package edu.edina.library.util;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftServoState;
import edu.edina.library.enums.LiftSlideState;
import edu.edina.library.enums.TwistServoState;

public class RobotState {
    public int currentTopMotorPosition;
    public int currentBottomMotorPosition;
    public double currentLiftSlidePower;
    public LiftServoState currentLiftServoState;
    public double currentLiftServoPosition;
    public LiftDriveState currentLiftDriveState;
    public int liftTargetPosition = 0;
    public TwistServoState twistServoState;

    public RobotState() {
        currentLiftSlidePower = 0.0;
        currentLiftDriveState = LiftDriveState.Manual;
        currentLiftServoState = LiftServoState.Idle;
        twistServoState = TwistServoState.Pickup;
    }

    public void telemetry(Telemetry telemetry, RobotHardware hardware) {
        telemetry.addData("Top Motor LiftPosition", currentTopMotorPosition);
        telemetry.addData("Bottom Motor LiftPosition", currentBottomMotorPosition);
        telemetry.addData("LiftSlidePower", currentLiftSlidePower);
        telemetry.addData("LiftServoState", currentLiftServoState);
        telemetry.addData("LiftDriveState", currentLiftDriveState);
        telemetry.addData("Current Lift Servo Position", currentLiftServoPosition);
        telemetry.addData("liftTargetPosition", liftTargetPosition);
        telemetry.addData("Twist Servo State: ", twistServoState);

        if (hardware != null) {
            telemetry.addData("Left Front Power, Current", "%f %f", hardware.leftFront.getPower(), hardware.leftFront.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.addData("Right Front Power, Current", "%f %f", hardware.rightFront.getPower(), hardware.leftFront.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.addData("Left Rear Power, Current", "%f %f", hardware.leftBack.getPower(), hardware.leftFront.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.addData("Right Rear Power, Current", "%f %f", hardware.rightBack.getPower(), hardware.leftFront.getCurrent(CurrentUnit.MILLIAMPS));

            telemetry.addData("Top Target", hardware.topLiftMotor.getTargetPosition());
            telemetry.addData("Bottom Target", hardware.bottomLiftMotor.getTargetPosition());
        }
    }
}
