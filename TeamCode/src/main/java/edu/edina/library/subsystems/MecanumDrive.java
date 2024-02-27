package edu.edina.library.subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.PoCMecanumDrive;

import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;

public class MecanumDrive implements Subsystem {
    private double leftStickX;
    private double leftStickY;
    private double rightStickX;

    private PoCMecanumDrive drive;
    private Robot robot;

    public MecanumDrive(Robot robot) {
        RobotHardware hardware = robot.RobotHardware;

        drive = new PoCMecanumDrive(hardware.leftFront,
                hardware.leftBack, hardware.rightBack, hardware.rightFront,
                hardware.par0, hardware.perp, hardware.externalImu, hardware.expansionImu,
                hardware.voltageSensor, hardware.beamBreak, new Pose2d(0, 0, 0));

        this.robot = robot;
    }

    public void setProperties(double leftStickX, double leftStickY, double rightStickX){
        this.leftStickX = ScaleMotorCube(leftStickX);
        this.leftStickY = ScaleMotorCube(leftStickY);
        this.rightStickX = ScaleMotorCube(rightStickX);
    }

    @Override
    public void init() { }

    @Override
    public void start() {
        robot.RobotHardware.liftServosForTeleop();
    }

    @Override
    public void update() {
        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -leftStickY * 0.9,
                        -leftStickX * 0.9
                ),
                (-rightStickX/1.5)
        ));

        //drive.updatePoseEstimate();
    }

    public static double ScaleMotorCube(double joyStickPosition) {
        return (double) Math.pow(joyStickPosition, 3.0);
    }
}
