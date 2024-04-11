package edu.edina.opmodes.test;

import android.util.Log;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.PoCMecanumDrive;

import edu.edina.library.actions.roadrunner.ActionManager;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.SmartGamepad;

@TeleOp
@Disabled
public class TestDebugDriveBuilding extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        RobotHardware hardware = new RobotHardware(hardwareMap);
        ActionManager manager = new ActionManager(hardware);
        PoCMecanumDrive drive = new org.firstinspires.ftc.teamcode.PoCMecanumDrive(hardware.leftFront,
                hardware.leftBack, hardware.rightBack, hardware.rightFront,
                hardware.par0, hardware.perp, hardware.externalImu,
                hardware.expansionImu, hardware.voltageSensor, hardware.beamBreak,
                new Pose2d(12.5, -64, Math.toRadians(90)));


        waitForStart();

        Action driveAction = drive.actionBuilder(drive.pose)
                .afterDisp(0, new ParallelAction(
                        manager.runLiftToPosition(-180, true),
                        manager.positionTheClawToPickupPixelsFromStack())
                )
                .lineToY(-46)
                .build();

        Log.d("ACTION-BUILDER", driveAction.toString());
    }
}
