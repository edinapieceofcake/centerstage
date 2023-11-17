package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous
public class BlueBackstage extends LinearOpMode {
    protected MecanumDrive drive;
    RevBlinkinLedDriver blinkinLedDriver;
    RevBlinkinLedDriver.BlinkinPattern pattern;

    protected void initHardware() {
        Pose2d startPose = new Pose2d(8, -64, Math.toRadians(90));
        drive = new MecanumDrive(hardwareMap, startPose);
        blinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
        pattern = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_BLUE;
        blinkinLedDriver.setPattern(pattern);
    }

    protected void runPaths() {
        Actions.runBlocking(
               drive.actionBuilder(drive.pose)
                    .splineTo(new Vector2d(48,35), Math.toRadians(0))
                       .waitSeconds(1)
                    .setReversed(true)
                    .splineTo(new Vector2d(12.5, 36), Math.toRadians(-90))
                       .waitSeconds(1)
                       .setReversed(true)
                    .splineTo(new Vector2d(60,60), Math.toRadians(0))
                    .build());
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();

        waitForStart();

        pattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
        blinkinLedDriver.setPattern(pattern);

        if (opModeIsActive()) {
            runPaths();
        }

        pattern = RevBlinkinLedDriver.BlinkinPattern.BLACK;
        blinkinLedDriver.setPattern(pattern);
    }
}
