package edu.edina.library.util;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.DropOffState;
import edu.edina.library.enums.HangState;
import edu.edina.library.enums.HangerState;
import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftServoRange;
import edu.edina.library.enums.LiftServoState;
import edu.edina.library.enums.LiftSlideState;
import edu.edina.library.enums.PickUpState;
import edu.edina.library.enums.TwistServoState;

public class RobotState {
    private static RobotState robotState = null;

    // lift properties
    public int currentTopMotorPosition;
    public int currentBottomMotorPosition;
    public double currentLiftSlidePower;
    public LiftServoState currentLiftServoState;
    public LiftDriveState currentLiftDriveState;
    public LiftSlideState currentLiftSlideState;
    public LiftDriveState lastKnownLiftState;
    public int liftTargetPosition = 0;
    public DropOffState dropOffState;
    public PickUpState pickUpState;
    public HangState hangState;
    public LiftServoRange liftServoRange;

    // claw properties
    public TwistServoState twistServoState;
    public AngleClawState angleClawState;
    public double currentLiftAngle;
    public double currentLiftLength;
    public double currentLiftHeight;
    public ClawState leftClawState;
    public ClawState rightClawState;

    public RobotState() {
        currentLiftSlidePower = 0.0;
        currentLiftDriveState = LiftDriveState.Manual;
        currentLiftServoState = LiftServoState.Start;
        twistServoState = TwistServoState.Pickup;
        angleClawState = AngleClawState.Drive;
        lastKnownLiftState = LiftDriveState.Drive;
        rightClawState = ClawState.Opened;
        leftClawState = ClawState.Opened;
        liftServoRange = LiftServoRange.Low;
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
        telemetry.addData("DropOffState", dropOffState);
        telemetry.addData("liftTargetPosition", liftTargetPosition);
        telemetry.addData("Twist Servo State: ", twistServoState);
        telemetry.addData("AngleClawState", angleClawState);
//        telemetry.addData("Current Lift Angle, Length, Height: ", "%f %f %f", currentLiftAngle, currentLiftLength, currentLiftHeight);
//        telemetry.addData("Sin Current Lift Angle", Math.sin(Math.toRadians(currentLiftAngle)));

        if (hardware != null) {
//            telemetry.addData("Left Front Power, Current", "%f %f", hardware.leftFront.getPower(), hardware.leftFront.getCurrent(CurrentUnit.MILLIAMPS));
//            telemetry.addData("Right Front Power, Current", "%f %f", hardware.rightFront.getPower(), hardware.leftFront.getCurrent(CurrentUnit.MILLIAMPS));
//            telemetry.addData("Left Rear Power, Current", "%f %f", hardware.leftBack.getPower(), hardware.leftFront.getCurrent(CurrentUnit.MILLIAMPS));
//            telemetry.addData("Right Rear Power, Current", "%f %f", hardware.rightBack.getPower(), hardware.leftFront.getCurrent(CurrentUnit.MILLIAMPS));

            telemetry.addData("Top Target", hardware.topLiftMotor.getTargetPosition());
            telemetry.addData("Bottom Target", hardware.bottomLiftMotor.getTargetPosition());

            telemetry.addData("Left Lift Servo Position", hardware.leftLiftServo.getPosition());
            telemetry.addData("Right Lift Servo Position", hardware.rightLiftServo.getPosition());

            telemetry.addData("Top Motor Mode", hardware.topLiftMotor.getMode());
            telemetry.addData("Bottom Motor Mode", hardware.bottomLiftMotor.getMode());
            telemetry.addData("Top Power, Current", "%f %f", hardware.topLiftMotor.getPower(), hardware.topLiftMotor.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.addData("Bottom Front Power, Current", "%f %f", hardware.bottomLiftMotor.getPower(), hardware.bottomLiftMotor.getCurrent(CurrentUnit.MILLIAMPS));

            telemetry.addData("Lift Switch", hardware.liftSwitch.getState());
            telemetry.addData("Hanger Motor Position", hardware.robotHangerMotor.getCurrentPosition());
        }
    }
}
