package edu.edina.library.util;

import android.util.Log;

import com.acmerobotics.roadrunner.ftc.LynxFirmware;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ThreadPool;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class RobotHardware {

    public final DcMotorEx leftFront, leftBack, rightBack, rightFront;

    public final VoltageSensor voltageSensor;

    public final IMU imu;
    public final IMU externalImu;
    public final IMU expansionImu;

    public final DcMotorEx par0, par1, perp;

    public final DcMotorEx lights;

    public final Servo par0Servo, par1Servo, perpServo;

    public final ServoImplEx leftClawServo, rightClawServo, twistClawServo, autoClawServo, angleClawServo;

    public final ServoImplEx leftLiftServo, rightLiftServo;

    public final Servo droneLaunchServo;

    public final DcMotorEx robotHangerMotor;

    public final DcMotorEx topLiftMotor, bottomLiftMotor;

    public final HuskyLens huskyLens;

    public final DigitalChannel liftSwitch;
    public final RevBlinkinLedDriver blinkinLedDriver;

    public final DigitalChannel leftClawRed, leftClawGreen;
    public final DigitalChannel rightClawRed, rightClawGreen;

    public final DigitalChannel beamBreak;
    public final DigitalChannel hangSwitch;

    private ExecutorService homeHangMotorExecutor;

    private Runnable homeHangMotorRunnable = () -> {
        homeHangMotor(null);
    };

    public boolean hangMotorHoming = false;

    private ExecutorService monitorCurrentExecutor;

    private Runnable monitorCurrentRunnable = () -> {
        monitorCurrent();
    };

    public boolean monitoringCurrent = false;

    public List<LynxModule> lynxModules;

    public RobotHardware(HardwareMap hardwareMap) {
        LynxFirmware.throwIfModulesAreOutdated(hardwareMap);

        lynxModules = hardwareMap.getAll(LynxModule.class);

        for (LynxModule module : lynxModules) {
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

        expansionImu = hardwareMap.get(IMU.class, "imu1");
        IMU.Parameters expansionParameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP));
        expansionImu.initialize(expansionParameters);

        externalImu = hardwareMap.get(IMU.class, "externalImu");
        IMU.Parameters externalImuParameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
//                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
//                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD));
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));
//                RevHubOrientationOnRobot.UsbFacingDirection.LEFT));
        externalImu.initialize(externalImuParameters);

        voltageSensor = hardwareMap.voltageSensor.iterator().next();

        par0 = hardwareMap.get(DcMotorEx.class, "rightFront");
        par1 = hardwareMap.get(DcMotorEx.class, "rightBack");
        perp = hardwareMap.get(DcMotorEx.class, "leftFront");

        par0Servo = hardwareMap.get(Servo.class, "par0Servo");
        par1Servo = hardwareMap.get(Servo.class, "par1Servo");
        perpServo = hardwareMap.get(Servo.class, "perpServo");

        leftClawServo = hardwareMap.get(ServoImplEx.class, "leftClawServo");
        rightClawServo = hardwareMap.get(ServoImplEx.class, "rightClawServo");
        twistClawServo = hardwareMap.get(ServoImplEx.class, "twistClawServo");
        angleClawServo = hardwareMap.get(ServoImplEx.class, "angleClawServo");
        autoClawServo = hardwareMap.get(ServoImplEx.class, "autoClawServo");

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
        topLiftMotor.setTargetPosition(0);
        topLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        topLiftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        bottomLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bottomLiftMotor.setTargetPosition(0);
        bottomLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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

        lights = hardwareMap.get(DcMotorEx.class, "lights");

        beamBreak = hardwareMap.get(DigitalChannel.class, "beamBreak");
        beamBreak.setMode(DigitalChannel.Mode.INPUT);
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
        hangMotorHoming = true;
        robotHangerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHangerMotor.setPower(.75);

        while (!hangSwitch.getState()) {
            if (telemetry != null) {
                telemetry.addData("Hang Switch", hangSwitch.getState());
                telemetry.addData("Mode", robotHangerMotor.getMode());
                telemetry.addData("Target Position", robotHangerMotor.getTargetPosition());
                telemetry.addData("Current Position", robotHangerMotor.getCurrentPosition());
                telemetry.update();
            }

            Thread.yield();
        }

        robotHangerMotor.setPower(0);
        robotHangerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robotHangerMotor.setTargetPosition(RobotConfiguration.getInstance().hangMotorInitPosition);
        robotHangerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHangerMotor.setPower(.75);

        while (robotHangerMotor.isBusy()) {
            if (telemetry != null) {
                telemetry.addData("Hang Switch", hangSwitch.getState());
                telemetry.addData("Mode", robotHangerMotor.getMode());
                telemetry.addData("Target Position", robotHangerMotor.getTargetPosition());
                telemetry.addData("Current Position", robotHangerMotor.getCurrentPosition());
                telemetry.update();
            }

            Thread.yield();
        }

        robotHangerMotor.setPower(0);
        robotHangerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hangMotorHoming = false;
    }

    public void monitorCurrent() {
        while (monitoringCurrent) {
            try {
                Thread.sleep(100);
            } catch (Exception x) {
            }
            Log.d("CURRENT_MONITOR", String.format("%f %f %f", getCurrent(), getLiftCurrent(), getDriveCurrent()));
            if(getLiftCurrent() > 16000){
                throw new OpModeManagerImpl.ForceStopException();
            }
        }
    }

    public void homeHangMotorAsync() {
        if (!hangMotorHoming) {
            homeHangMotorExecutor = ThreadPool.newSingleThreadExecutor("home hang motor");
            homeHangMotorExecutor.submit(homeHangMotorRunnable);
            hangMotorHoming = true;
        }
    }

    public void startCurrentMonitor() {
        if (!monitoringCurrent) {
            monitorCurrentExecutor = ThreadPool.newSingleThreadExecutor("monitor current");
            monitorCurrentExecutor.submit(monitorCurrentRunnable);
            monitoringCurrent = true;
        }
    }

    public void stopCurrentMonitor() {
        monitoringCurrent = false;
    }

    public double getCurrent() {
        double totalCurrent = 0;

        for (LynxModule module : lynxModules) {
            totalCurrent += module.getCurrent(CurrentUnit.MILLIAMPS);
        }

        return totalCurrent;
    }

    public double getLiftCurrent() {
        return topLiftMotor.getCurrent(CurrentUnit.MILLIAMPS) + bottomLiftMotor.getCurrent(CurrentUnit.MILLIAMPS);
    }

    public double getDriveCurrent() {
        return  leftFront.getCurrent(CurrentUnit.MILLIAMPS) + leftBack.getCurrent(CurrentUnit.MILLIAMPS)
                + rightFront.getCurrent(CurrentUnit.MILLIAMPS) + rightBack.getCurrent(CurrentUnit.MILLIAMPS);
    }
}
