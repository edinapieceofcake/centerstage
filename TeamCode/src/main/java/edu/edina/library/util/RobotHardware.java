package edu.edina.library.util;

import com.acmerobotics.roadrunner.ftc.LynxFirmware;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import edu.edina.opmodes.teleop.Robot;

public class RobotHardware {

    public final DcMotorEx leftFront, leftBack, rightBack, rightFront;

    public final VoltageSensor voltageSensor;

    public final IMU imu;

    public final DcMotorEx par0, par1, perp;

    public final Servo par0Servo, par1Servo, perpServo;

    public final Servo leftClawServo, rightClawServo, twistClawServo, angleClawServo;

    public final Servo leftLiftServo, rightLiftServo;

    public final Servo droneLaunchServo;

    public final DcMotorEx robotHangerMotor;

    public final DcMotorEx topLiftMotor, bottomLiftMotor;

    public final WebcamName webcamName;

    public RobotHardware(HardwareMap hardwareMap, Robot robot) {
        LynxFirmware.throwIfModulesAreOutdated(hardwareMap);

        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");
        rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");

        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
        imu.initialize(parameters);

        voltageSensor = hardwareMap.voltageSensor.iterator().next();

        par0 = hardwareMap.get(DcMotorEx.class, "par0");
        par1 = hardwareMap.get(DcMotorEx.class, "par1");
        perp = hardwareMap.get(DcMotorEx.class, "perp");

        webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");

        par0Servo = hardwareMap.get(Servo.class, "par0Servo");
        par1Servo = hardwareMap.get(Servo.class, "par1Servo");
        perpServo = hardwareMap.get(Servo.class, "perpServo");

        leftClawServo = hardwareMap.get(Servo.class, "leftClawServo");
        rightClawServo = hardwareMap.get(Servo.class, "rightClawServo");
        twistClawServo = hardwareMap.get(Servo.class, "twistClawServo");
        angleClawServo = hardwareMap.get(Servo.class, "angleClawServo");

        leftLiftServo = hardwareMap.get(Servo.class, "leftLiftServo");
        rightLiftServo = hardwareMap.get(Servo.class, "rightLiftServo");

        droneLaunchServo = hardwareMap.get(Servo.class, "droneLaunchServo");

        robotHangerMotor = hardwareMap.get(DcMotorEx.class, "robotHangerMotor");

        topLiftMotor = hardwareMap.get(DcMotorEx.class, "topLiftMotor");
        bottomLiftMotor = hardwareMap.get(DcMotorEx.class, "bottomLiftMotor");
    }
}