package edu.edina.library.subsystems;

import android.util.Log;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.PoCMecanumDrive;

import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class MecanumDrive implements Subsystem {
    private RobotState state = RobotState.getInstance();
    private PoCMecanumDrive drive;
    private boolean started = false;
    private RobotHardware hardware;

    public MecanumDrive(RobotHardware hardware) {
        this.hardware = hardware;

        drive = new PoCMecanumDrive(hardware.leftFront,
                hardware.leftBack, hardware.rightBack, hardware.rightFront,
                hardware.par0, hardware.perp, hardware.externalImu, hardware.expansionImu,
                hardware.voltageSensor, hardware.beamBreak, new Pose2d(0, 0, 0));
    }

    @Override
    public void init() { }

    @Override
    public void start() {
        hardware.liftServosForTeleop();
        started = true;
        drive.startPoseThread();
    }

    @Override
    public void stop() {
        started = false;
        drive.stopPoseThread();
    }

    @Override
    public void update() {
        if (started) {
            drive.setDrivePowers(new PoseVelocity2d(
                    new Vector2d(
                            -state.leftStickY * 0.9,
                            -state.leftStickX * 0.9
                    ),
                    (-state.rightStickX / 1.5)
            ));


        //drive.updatePoseEstimate();

            drive.updatePoseEstimate();
            Log.d("ROBOTPOSE", String.format("%f %f %f", drive.pose.position.x, drive.pose.position.y, drive.pose.heading.real));
        }

    }

    public static double ScaleMotorCube(double joyStickPosition) {
        return (double) Math.pow(joyStickPosition, 3.0);
    }
}
