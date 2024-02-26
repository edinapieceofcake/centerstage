package edu.edina.opmodes.test;

import android.util.Log;

import com.outoftheboxrobotics.photoncore.Photon;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ThreadPool;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import java.util.concurrent.ExecutorService;

import edu.edina.library.util.RobotHardware;

@Photon
@TeleOp
//@Disabled
public class TestImu extends LinearOpMode {
    RobotHardware hardware;
    IMU imu;
    IMU externalImu;
    IMU expansionImu;
    DcMotorEx par0;
    DcMotorEx perp;
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
        par0 = hardware.par0;
        perp = hardware.perp;

        waitForStart();

        pollImu = true;
        imuExecutor = ThreadPool.newSingleThreadExecutor("imu on separate thread");
        imuExecutor.submit(imuRunnable);

        imu.resetYaw();
        expansionImu.resetYaw();
        externalImu.resetYaw();
        while (opModeIsActive()) {
            idle();
//            YawPitchRollAngles imuAngles = imu.getRobotYawPitchRollAngles();
//            YawPitchRollAngles externalAngles = externalImu.getRobotYawPitchRollAngles();
//            YawPitchRollAngles expansionAngles = expansionImu.getRobotYawPitchRollAngles();
//
//            double imuValue = imuAngles.getYaw(AngleUnit.RADIANS);
//            double externalImuValue = externalAngles.getYaw(AngleUnit.RADIANS);
//            double expansionImuValue = expansionAngles.getYaw(AngleUnit.RADIANS);
//            double finalImuValue;
//            Log.d("IMU-MainThread Yaw", String.format("%d %f", imuAngles.getAcquisitionTime(), imuValue));
//            Log.d("IMUExternal-MainThread Yaw", String.format("%d %f", externalAngles.getAcquisitionTime(), externalImuValue));
//            Log.d("IMUExpansion-MainThread Yaw", String.format("%d %f", expansionAngles.getAcquisitionTime(), expansionImuValue));
//
//            telemetry.addData("IMU-MainThread Yaw","%d %f", imuAngles.getAcquisitionTime(), imuValue);
//            telemetry.addData("IMUExternal-MainThread Yaw","%d %f", externalAngles.getAcquisitionTime(), externalImuValue);
//            telemetry.addData("IMUExpansion-MainThread Yaw","%d %f", expansionAngles.getAcquisitionTime(), expansionImuValue);
//
//            if (imuAngles.getAcquisitionTime() != 0){
//                finalImuValue = imuValue;
//            } else if (externalAngles.getAcquisitionTime() != 0) {
//                finalImuValue = externalImuValue;
//            } else {
//                finalImuValue = expansionImuValue;
//            }
//
//            telemetry.addData("IMUFinal-MainThread Yaw","%f", finalImuValue);
//
//            telemetry.update();
        }

        pollImu = false;
    }

    private void multiThreadImu() {
        while (pollImu) {
            Log.d("IMU-SideThread Yaw", String.format("%f", externalImu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)));
            Log.d("IMU-Par0", String.format("%d", par0.getCurrentPosition()));
            Log.d("IMU-Perp", String.format("%d", perp.getCurrentPosition()));
//            Log.d("IMUExternal-MainThread Yaw", String.format("%f", externalImu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)));
//            Log.d("IMUExpansion-MainThread Yaw", String.format("%f", expansionImu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS)));
        }
    }
}
