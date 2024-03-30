package edu.edina.library.util;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.DroneLauncherState;
import edu.edina.library.enums.DropOffOrientation;
import edu.edina.library.enums.DropOffState;
import edu.edina.library.enums.HangState;
import edu.edina.library.enums.HangerState;
import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftServoState;
import edu.edina.library.enums.LiftSlideState;
import edu.edina.library.enums.PickUpState;
import edu.edina.library.enums.TwistServoState;

public class RobotState {
    private static RobotState robotState = null;

    // lift properties
    public int currentTopMotorPosition;
    public int currentBottomMotorPosition;
    public int currentTopMotorTargetPosition;
    public int currentBottomMotorTargetPosition;
    public double currentLiftSlidePower;
    public LiftServoState currentLiftServoState;
    public LiftDriveState currentLiftDriveState;
    public LiftSlideState currentLiftSlideState;
    public LiftDriveState lastKnownLiftState;
    public DropOffState dropOffState;
    public DropOffOrientation dropOffOrientation;
    public PickUpState pickUpState;
    public HangState hangState;
    public HangerState hangerState;
    public int currentHangerPosition;
    public double currentTriggerStrength;
    public int currentLiftMotorDropOffPosition;
    public LiftServoState currentLiftServoStateDropOffPosition;
    public Deadline secondExtensionTimeout = new Deadline(1000, TimeUnit.MILLISECONDS);
    public Deadline hangLiftDelay = new Deadline(600, TimeUnit.MILLISECONDS);
    public boolean liftDPadChanged = false;

    // claw properties
    public TwistServoState twistServoState;
    public AngleClawState angleClawState;
    public double currentLiftAngle;
    public double currentLiftLength;
    public double currentLiftHeight;
    public ClawState leftClawState;
    public ClawState rightClawState;
    public ClawState autoClawState;
    public ClawState pusherState;

//    drone launcher properties
    public DroneLauncherState droneState;

//    mecanum drive properties
    public double leftStickX;
    public double leftStickY;
    public double rightStickX;

    public RobotState() {
        currentLiftSlidePower = 0.0;
        currentLiftDriveState = LiftDriveState.Manual;
        currentLiftServoState = LiftServoState.Start;
        twistServoState = TwistServoState.Pickup;
        angleClawState = AngleClawState.Drive;
        lastKnownLiftState = LiftDriveState.Drive;
        rightClawState = ClawState.Opened;
        leftClawState = ClawState.Opened;
        autoClawState = ClawState.Opened;
        pusherState = ClawState.Closed;
        dropOffOrientation = DropOffOrientation.Center;
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
        telemetry.addData("CurrentLiftMotorDropOffPosition", currentLiftMotorDropOffPosition);
        telemetry.addData("CurrentLiftServoStateDropOffPosition", currentLiftServoStateDropOffPosition);
        telemetry.addData("LiftDriveState", currentLiftDriveState);
        telemetry.addData("LastKnownLiftDriveState", lastKnownLiftState);
        telemetry.addData("PickUpState", pickUpState);
        telemetry.addData("DropOffState", dropOffState);
        telemetry.addData("currentTopMotorTargetPosition", currentTopMotorTargetPosition);
        telemetry.addData("Twist Servo State: ", twistServoState);
        telemetry.addData("AngleClawState", angleClawState);
        telemetry.addData("Current Lift Angle, Length, Height: ", "%f %f %f", currentLiftAngle, currentLiftLength, currentLiftHeight);
        telemetry.addData("Sin Current Lift Angle", Math.sin(Math.toRadians(currentLiftAngle)));

        if (hardware != null) {
            telemetry.addData("Left Front Power, Current", "%f %f", hardware.leftFront.getPower(), hardware.leftFront.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.addData("Right Front Power, Current", "%f %f", hardware.rightFront.getPower(), hardware.rightFront.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.addData("Left Rear Power, Current", "%f %f", hardware.leftBack.getPower(), hardware.leftBack.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.addData("Right Rear Power, Current", "%f %f", hardware.rightBack.getPower(), hardware.rightBack.getCurrent(CurrentUnit.MILLIAMPS));

            telemetry.addData("ImuInfo CHUB angle", hardware.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
            telemetry.addData("ImuInfo CHUB velocity", hardware.imu.getRobotAngularVelocity(AngleUnit.DEGREES).zRotationRate);
            telemetry.addData("ImuInfo External angle", hardware.externalImu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
            telemetry.addData("ImuInfo External velocity", hardware.externalImu.getRobotAngularVelocity(AngleUnit.DEGREES).zRotationRate);

//            Log.d("Imu CHUB, External angle, velocity", String.format("%.4f %.4f %.4f %.4f", hardware.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES),
//                    hardware.imu.getRobotAngularVelocity(AngleUnit.DEGREES).zRotationRate, hardware.externalImu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES),
//                    hardware.externalImu.getRobotAngularVelocity(AngleUnit.DEGREES).zRotationRate));

            telemetry.addData("Top Target", hardware.topLiftMotor.getTargetPosition());
            telemetry.addData("Bottom Target", hardware.bottomLiftMotor.getTargetPosition());

            telemetry.addData("Left Lift Servo Position", hardware.leftLiftServo.getPosition());
            telemetry.addData("Right Lift Servo Position", hardware.rightLiftServo.getPosition());

            telemetry.addData("Top Motor Mode", hardware.topLiftMotor.getMode());
            telemetry.addData("Bottom Motor Mode", hardware.bottomLiftMotor.getMode());
            telemetry.addData("Top Power, Current", "%f %f", hardware.topLiftMotor.getPower(), hardware.topLiftMotor.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.addData("Bottom Front Power, Current", "%f %f", hardware.bottomLiftMotor.getPower(), hardware.bottomLiftMotor.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.addData("Hang Motor Power, Current", "%f %f", hardware.robotHangerMotor.getPower(), hardware.robotHangerMotor.getCurrent(CurrentUnit.MILLIAMPS));

            telemetry.addData("Lift Switch", hardware.liftSwitch.getState());
            telemetry.addData("Hanger Motor Position", hardware.robotHangerMotor.getCurrentPosition());
            telemetry.addData("Beam break", hardware.beamBreak.getState());
        }
    }
}
