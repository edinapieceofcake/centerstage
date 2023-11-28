package edu.edina.library.util;

import com.acmerobotics.roadrunner.ftc.LynxFirmware;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public class RobotHardware {

    public final DcMotorEx leftFront, leftBack, rightBack, rightFront;

    public final VoltageSensor voltageSensor;

    public final IMU imu;

    public final DcMotorEx par0, par1, perp;

    public final Servo par0Servo, par1Servo, perpServo;

    public final Servo leftClawServo, rightClawServo, twistClawServo, angleClawServo;

    public final ServoImplEx leftLiftServo, rightLiftServo;

    public final Servo droneLaunchServo;

    public final DcMotorEx robotHangerMotor;

    public final DcMotorEx topLiftMotor, bottomLiftMotor;

    public final HuskyLens huskyLens;

    public final DigitalChannel liftSwitch;
    public final RevBlinkinLedDriver blinkinLedDriver;

    public final DigitalChannel leftClawRed, leftClawGreen;
    public final DigitalChannel rightClawRed, rightClawGreen;

    public final DigitalChannel hangSwitch;


    public RobotHardware(HardwareMap hardwareMap) {
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
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        imu.initialize(parameters);

        voltageSensor = hardwareMap.voltageSensor.iterator().next();

        par0 = hardwareMap.get(DcMotorEx.class, "rightFront");
        par1 = hardwareMap.get(DcMotorEx.class, "rightBack");
        perp = hardwareMap.get(DcMotorEx.class, "leftFront");

        par0Servo = hardwareMap.get(Servo.class, "par0Servo");
        par1Servo = hardwareMap.get(Servo.class, "par1Servo");
        perpServo = hardwareMap.get(Servo.class, "perpServo");

        leftClawServo = hardwareMap.get(Servo.class, "leftClawServo");
        rightClawServo = hardwareMap.get(Servo.class, "rightClawServo");
        twistClawServo = hardwareMap.get(Servo.class, "twistClawServo");
        angleClawServo = hardwareMap.get(Servo.class, "angleClawServo");

        leftLiftServo = hardwareMap.get(ServoImplEx.class, "rightLiftServo");
        rightLiftServo = hardwareMap.get(ServoImplEx.class, "leftLiftServo");

        droneLaunchServo = hardwareMap.get(Servo.class, "droneLaunchServo");

        robotHangerMotor = hardwareMap.get(DcMotorEx.class, "robotHangerMotor");
        robotHangerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robotHangerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHangerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        topLiftMotor = hardwareMap.get(DcMotorEx.class, "topLiftMotor");
        bottomLiftMotor = hardwareMap.get(DcMotorEx.class, "bottomLiftMotor");

        huskyLens = hardwareMap.get(HuskyLens.class, "huskylens");

        liftSwitch = hardwareMap.get(DigitalChannel.class, "liftSwitch");
        liftSwitch.setMode(DigitalChannel.Mode.INPUT);

        topLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        topLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        topLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        bottomLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bottomLiftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bottomLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");

        leftClawRed = hardwareMap.get(DigitalChannel.class, "leftClawRed");
        leftClawGreen = hardwareMap.get(DigitalChannel.class, "leftClawGreen");
        leftClawRed.setMode(DigitalChannel.Mode.OUTPUT);
        leftClawGreen.setMode(DigitalChannel.Mode.OUTPUT);

        rightClawRed = hardwareMap.get(DigitalChannel.class, "rightClawRed");
        rightClawGreen = hardwareMap.get(DigitalChannel.class, "rightClawGreen");
        rightClawRed.setMode(DigitalChannel.Mode.OUTPUT);
        rightClawGreen.setMode(DigitalChannel.Mode.OUTPUT);

        hangSwitch = hardwareMap.get(DigitalChannel.class, "hangSwitch");
        hangSwitch.setMode(DigitalChannel.Mode.INPUT);
    }

    public void liftServosForTeleop() {
        RobotConfiguration config = RobotConfiguration.getInstance();

        par0Servo.setPosition(config.par0UpPosition);
        par1Servo.setPosition(config.par1UpPosition);
        perpServo.setPosition(config.perpUpPosition);
    }

    public void dropServosForAutonomous() {
        RobotConfiguration config = RobotConfiguration.getInstance();

        par0Servo.setPosition(config.par0DownPosition);
        par1Servo.setPosition(config.par1DownPosition);
        perpServo.setPosition(config.perpDownPosition);
    }

    public void homeHangMotor(Telemetry telemetry) {
        robotHangerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHangerMotor.setPower(.1);

        while (!hangSwitch.getState()) {
            telemetry.addData("Hang Switch", hangSwitch.getState());
            telemetry.addData("Mode", robotHangerMotor.getMode());
            telemetry.addData("Target Position", robotHangerMotor.getTargetPosition());
            telemetry.addData("Current Position", robotHangerMotor.getCurrentPosition());
            telemetry.update();
            Thread.yield();
        }

        robotHangerMotor.setPower(0);
        robotHangerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robotHangerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHangerMotor.setTargetPosition(RobotConfiguration.getInstance().hangMotorInitPosition);
        robotHangerMotor.setPower(.1);

        while (robotHangerMotor.isBusy()) {
            telemetry.addData("Mode", robotHangerMotor.getMode());
            telemetry.addData("Target Position", robotHangerMotor.getTargetPosition());
            telemetry.addData("Current Position", robotHangerMotor.getCurrentPosition());
            telemetry.update();
            Thread.yield();
        }
//
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException ex) {
//
//        }

        robotHangerMotor.setPower(0);
//        robotHangerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
