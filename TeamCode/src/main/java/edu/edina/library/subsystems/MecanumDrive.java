package edu.edina.library.subsystems;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import edu.edina.library.util.Robot;

public class MecanumDrive extends Subsystem {
    private double leftStickX;
    private double leftStickY;
    private double rightStickX;
    private org.firstinspires.ftc.teamcode.MecanumDrive drive;
    private Robot robot;

    public MecanumDrive(Robot robot) {
        drive = new org.firstinspires.ftc.teamcode.MecanumDrive(robot.RobotHardware.leftFront,
                robot.RobotHardware.leftBack, robot.RobotHardware.rightBack, robot.RobotHardware.rightFront,
                robot.RobotHardware.par0, robot.RobotHardware.par1, robot.RobotHardware.perp,
                robot.RobotHardware.imu, robot.RobotHardware.voltageSensor, new Pose2d(0, 0, 0));

        this.robot = robot;
    }

    public void setDriveProperties(double leftStickX, double leftStickY, double rightStickX){
        this.leftStickX = ScaleMotorCube(leftStickX);
        this.leftStickY = ScaleMotorCube(leftStickY);
        this.rightStickX = ScaleMotorCube(rightStickX);
    }

    @Override
    public void init() { }

    @Override
    public void start() {
        robot.RobotHardware.par0Servo.setPosition(robot.RobotState.par0UpPosition);
        robot.RobotHardware.par1Servo.setPosition(robot.RobotState.par1UpPosition);
        robot.RobotHardware.perpServo.setPosition(robot.RobotState.perpUpPosition);
    }

    @Override
    public void update() {
        drive.setDrivePowers(new PoseVelocity2d(
                new Vector2d(
                        -leftStickY,
                        -leftStickX
                ),
                -rightStickX
        ));

        drive.updatePoseEstimate();
    }

    public static double ScaleMotorCube(double joyStickPosition) {
        return (double) Math.pow(joyStickPosition, 3.0);
    }
}
