package edu.edina.library.util;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.DropOffState;
import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftServoState;
import edu.edina.library.enums.LiftSlideState;
import edu.edina.library.enums.PickUpState;
import edu.edina.library.enums.ReelState;
import edu.edina.library.enums.TwistServoState;

public class RobotState {
    private static RobotState robotState = null;

    // lift properties
    public int currentTopMotorPosition;
    public int currentBottomMotorPosition;
    public double currentLiftSlidePower;
    public LiftServoState currentLiftServoState;
    public double currentLeftLiftServoPosition;
    public double currentRightLiftServoPosition;
    public LiftDriveState currentLiftDriveState;
    public LiftSlideState currentLiftSlideState;
    public LiftDriveState lastKnownLiftState;
    public int liftTargetPosition = 0;
    public DropOffState dropOffState;
    public PickUpState pickUpState;

    // claw properties
    public TwistServoState twistServoState;
    public AngleClawState angleClawState;
    public double currentLiftAngle;
    public double currentLiftLength;
    public double currentLiftHeight;

    // Reel Properties
    public ReelState currentReelState;
    public int reelTargetPosition;

    public RobotState() {
        currentLiftSlidePower = 0.0;
        currentLiftDriveState = LiftDriveState.Manual;
        currentLiftServoState = LiftServoState.Start;
        twistServoState = TwistServoState.Pickup;
        angleClawState = AngleClawState.Drive;
        lastKnownLiftState = LiftDriveState.Drive;
    }

    public static synchronized RobotState getInstance()
    {
        if (robotState == null) {
            robotState = new RobotState();
        }

        return robotState;
    }

    public void telemetry(Telemetry telemetry, RobotHardware hardware) {
        telemetry.addData("Top Motor LiftPosition", currentTopMotorPosition);
        telemetry.addData("Bottom Motor LiftPosition", currentBottomMotorPosition);
        telemetry.addData("LiftSlidePower", currentLiftSlidePower);
        telemetry.addData("LiftServoState", currentLiftServoState);
        telemetry.addData("LiftDriveState", currentLiftDriveState);
        telemetry.addData("LastKnownLiftDriveState", lastKnownLiftState);
        telemetry.addData("PickUpState", pickUpState);
        telemetry.addData("Current Left Lift Servo Position", currentLeftLiftServoPosition);
        telemetry.addData("Current Right Servo Position", currentRightLiftServoPosition);
        telemetry.addData("liftTargetPosition", liftTargetPosition);
        telemetry.addData("Twist Servo State: ", twistServoState);
        telemetry.addData("AngleClawState", angleClawState);
        telemetry.addData("Current Lift Angle, Length, Height: ", "%f %f %f", currentLiftAngle, currentLiftLength, currentLiftHeight);
        telemetry.addData("Sin Current Lift Angle", Math.sin(Math.toRadians(currentLiftAngle)));

        if (hardware != null) {
            telemetry.addData("Left Front Power, Current", "%f %f", hardware.leftFront.getPower(), hardware.leftFront.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.addData("Right Front Power, Current", "%f %f", hardware.rightFront.getPower(), hardware.leftFront.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.addData("Left Rear Power, Current", "%f %f", hardware.leftBack.getPower(), hardware.leftFront.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.addData("Right Rear Power, Current", "%f %f", hardware.rightBack.getPower(), hardware.leftFront.getCurrent(CurrentUnit.MILLIAMPS));

            telemetry.addData("Top Target", hardware.topLiftMotor.getTargetPosition());
            telemetry.addData("Bottom Target", hardware.bottomLiftMotor.getTargetPosition());

            telemetry.addData("Left Lift Servo Position", hardware.leftLiftServo.getPosition());
            telemetry.addData("Right Lift Servo Position", hardware.rightLiftServo.getPosition());
        }
    }
}
