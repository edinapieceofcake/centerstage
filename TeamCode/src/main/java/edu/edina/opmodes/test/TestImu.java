package edu.edina.opmodes.test;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ThreadPool;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.concurrent.ExecutorService;

import edu.edina.library.util.RobotHardware;

@TeleOp
//@Disabled
public class TestImu extends LinearOpMode {
    RobotHardware hardware;
    IMU imu;
    IMU externalImu;
    IMU expansionImu;
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
        imu = hardware.imu;
        externalImu = hardware.externalImu;
        expansionImu = hardware.expansionImu;

        waitForStart();

        pollImu = true;
//        imuExecutor = ThreadPool.newSingleThreadExecutor("imu on separate thread");
//        imuExecutor.submit(imuRunnable);

        imu.resetYaw();
        expansionImu.resetYaw();
        externalImu.resetYaw();
        while (opModeIsActive()) {
            idle();
            double imuValue = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            double externalImuValue = externalImu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            double expansionImuValue = expansionImu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            double finalImuValue;
            Log.d("IMU-MainThread Yaw", String.format("%f", imuValue));
            Log.d("IMUExternal-MainThread Yaw", String.format("%f", externalImuValue));
            Log.d("IMUExpansion-MainThread Yaw", String.format("%f", expansionImuValue));

            telemetry.addData("IMU-MainThread Yaw","%f", imuValue);
            telemetry.addData("IMUExternal-MainThread Yaw","%f", externalImuValue);
            telemetry.addData("IMUExpansion-MainThread Yaw","%f", expansionImuValue);

            if (imuValue != 0.0){
                finalImuValue = imuValue;
            } else if (externalImuValue != 0.0) {
                finalImuValue = externalImuValue;
            } else {
                finalImuValue = expansionImuValue;
            }

            telemetry.addData("IMUFinal-MainThread Yaw","%f", finalImuValue);

            telemetry.update();
        }

        pollImu = false;
    }

    private void multiThreadImu() {
        while (pollImu) {
            Log.d("IMU-SideThread Yaw", String.format("%f", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)));
            Log.d("IMUExternal-MainThread Yaw", String.format("%f", externalImu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)));
            Log.d("IMUExpansion-MainThread Yaw", String.format("%f", expansionImu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)));
        }
    }
}
