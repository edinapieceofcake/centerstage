package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

public class BlueBackstage extends LinearOpMode {
    protected org.firstinspires.ftc.teamcode.MecanumDrive drive;

    protected void initHardware() {
        Pose2d startPose = new Pose2d(8, -64, Math.toRadians(90));
        drive = new MecanumDrive(hardwareMap, startPose);
    }

    protected void runPaths() {
        Actions.runBlocking(
                drive.actionBuilder(drive.pose)
                    .splineTo(new Vector2d(48,-35), Math.toRadians(0))
                    .setReversed(true)
                    .splineTo(new Vector2d(12.5, -36), Math.toRadians(-90))
                    .splineTo(new Vector2d(60,-60), Math.toRadians(0))
                    .build());
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        if (opModeIsActive()) {
            runPaths();
        }
    }
}
