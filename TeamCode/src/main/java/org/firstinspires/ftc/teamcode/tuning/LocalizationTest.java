package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Twist2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.TankDrive;
@Config

public class LocalizationTest extends LinearOpMode {
    @Override

    public void runOpMode() throws InterruptedException {
        if (TuningOpModes.DRIVE_CLASS.equals(MecanumDrive.class)) {
            MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
            DcMotorEx leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
            DcMotorEx leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
            DcMotorEx rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");
            DcMotorEx rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");

            waitForStart();

            while (opModeIsActive()) {
                drive.setDrivePowers(new PoseVelocity2d(
                        new Vector2d(
                                -gamepad1.left_stick_y,
                                -gamepad1.left_stick_x
                        ),
                        -gamepad1.right_stick_x
                ));

                drive.updatePoseEstimate();

                telemetry.addData("x", drive.pose.position.x);
                telemetry.addData("y", drive.pose.position.y);
                telemetry.addData("heading", drive.pose.heading);
                telemetry.addData("Left Front Current", leftFront.getCurrent(CurrentUnit.MILLIAMPS));
                telemetry.addData("Left Back Current", leftBack.getCurrent(CurrentUnit.MILLIAMPS));
                telemetry.addData("Right Back Current", rightBack.getCurrent(CurrentUnit.MILLIAMPS));
                telemetry.addData("Right Front Current", rightFront.getCurrent(CurrentUnit.MILLIAMPS));
                telemetry.update();
            }
        } else if (TuningOpModes.DRIVE_CLASS.equals(TankDrive.class)) {
            TankDrive drive = new TankDrive(hardwareMap, new Pose2d(0, 0, 0));

            waitForStart();

            while (opModeIsActive()) {
                drive.setDrivePowers(new PoseVelocity2d(
                        new Vector2d(
                                -gamepad1.left_stick_y,
                                0.0
                        ),
                        -gamepad1.right_stick_x
                ));

                drive.updatePoseEstimate();

                telemetry.addData("x", drive.pose.position.x);
                telemetry.addData("y", drive.pose.position.y);
                telemetry.addData("heading", drive.pose.heading);
                telemetry.update();
            }
        } else {
            throw new AssertionError();
        }
    }
}
