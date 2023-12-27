package edu.edina.opmodes.test;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ThreadPool;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.concurrent.ExecutorService;

import edu.edina.library.util.RobotHardware;

@TeleOp
public class TestImu extends LinearOpMode {
    RobotHardware hardware;
    IMU imu;
    private ExecutorService imuExecutor;
    boolean pollImu;
    private Runnable imuRunnable = () -> {
        while (!Thread.currentThread().isInterrupted()) {
            multiThreadImu();
        }
    };

    @Override
    public void runOpMode() throws InterruptedException {
        hardware = new RobotHardware(hardwareMap);
        imu = hardware.externalImu;

        waitForStart();

        pollImu = true;
        imuExecutor = ThreadPool.newSingleThreadExecutor("imu on separate thread");
        imuExecutor.submit(imuRunnable);

        while (opModeIsActive()) {
            idle();
//            Log.d("IMU-MainThread Yaw", String.format("%f", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)));
        }

        pollImu = false;
    }

    private void multiThreadImu() {
        while (pollImu) {
            Log.d("IMU-SideThread Yaw", String.format("%f", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)));
        }
    }
}
