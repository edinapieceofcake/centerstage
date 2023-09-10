package edu.edina.library.subsystems;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import edu.edina.library.util.RobotState;
import edu.edina.opmodes.teleop.Robot;

public class MecanumDrive extends Subsystem {
    private double leftStickX;
    private double leftStickY;
    private double rightStickX;
    private SampleMecanumDrive drive;
    private Robot robot;

    public MecanumDrive(HardwareMap map, Robot robot) {
        drive = new SampleMecanumDrive(map);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.robot = robot;
    }

    public void setDriveProperties(double leftStickX, double leftStickY, double rightStickX){
        this.leftStickX = leftStickX;
        this.leftStickY = leftStickY;
        this.rightStickX = rightStickX;
    }

    @Override
    public void init() { }

    @Override
    public void update() {
        // Pass in the rotated input + right stick value for rotation
        // Rotation is not part of the rotated input thus must be passed in separately
        drive.setWeightedDrivePower(
                new Pose2d(
                        // To change to Robot centric change leftSticks to ____
                        // To change to Robot centric change leftSticks to ____
                        -leftStickY,
                        -leftStickX,
                        -rightStickX
                )
        );

        // Update everything. Odometry. Etc.
        drive.update();
    }
}
