package edu.edina.opmodes.test;

import android.util.Log;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.PoCMecanumDrive;

import edu.edina.library.actions.roadrunner.ActionManager;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;
import edu.edina.library.util.SmartGamepad;

@TeleOp
@Disabled
public class TestStackPickup extends LinearOpMode  {
    @Override
    public void runOpMode() throws InterruptedException {
        RobotHardware hardware = new RobotHardware(hardwareMap);
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        RobotState state = RobotState.getInstance();
        ActionManager manager = new ActionManager(hardware);
        boolean toggleBeamBreak = true;
        // Start Position
        PoCMecanumDrive drive = new org.firstinspires.ftc.teamcode.PoCMecanumDrive(hardware.leftFront,
                hardware.leftBack, hardware.rightBack, hardware.rightFront,
                hardware.par0, hardware.perp, hardware.externalImu,
                hardware.expansionImu, hardware.voltageSensor, hardware.beamBreak,
                new Pose2d(12.5, -64, Math.toRadians(90)));

        // use out version of the drive based off the hardware that we created above.

        manager.init();

        hardware.dropServosForAutonomous();

        waitForStart();

        if (opModeIsActive()) {
            manager.start();
        }

        while (opModeIsActive()) {
            pad1.update();

            if (pad1.y) {
                Actions.runBlocking(new SequentialAction(
                        manager.positionTheClawToDriveWithPixels()
                ));
            }

            if (pad1.a) {
                Actions.runBlocking(new SequentialAction(
                        manager.positionTheClawToPickupPixels()
                ));
            }

            if (pad1.left_bumper) {
                Actions.runBlocking(new ParallelAction(
                        manager.closeLeftClaw(), manager.closeAutoClaw()
                ));
            }

            if (pad1.right_bumper) {
                Actions.runBlocking(new ParallelAction(
                        manager.openLeftClaw(), manager.openAutoClaw()
                ));
            }

            if(pad1.left_stick_button){
                if(toggleBeamBreak){
                    toggleBeamBreak = false;
                }
                else{
                    toggleBeamBreak = true;
                }
            }

            if (pad1.dpad_left) {
                Actions.runBlocking(
                        new ParallelAction(
                                manager.lowerLiftForDriving(),
                                manager.zeroLift(),
                                manager.positionTheClawToDriveWithPixels()
                        )
                );
            }

            if (pad1.dpad_up) {
                // start at one tile out
                if(toggleBeamBreak){
                    drive.turnBeamBreakOn();
                }

                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                .afterDisp(0, new ParallelAction(
                                        manager.runLiftToPosition(-180),
                                        manager.positionTheClawToPickupPixelsFromStack())
                                )
                                .lineToY(-46)
                                .build()
                );

                if(toggleBeamBreak){
                    drive.turnBeamBreakOff();
                }

                Log.d("RunLiftToPositionAction: ", String.format("Drive pose %f, %f, %f", drive.pose.position.x, drive.pose.position.y, drive.pose.heading.real));

                double newLineToY = drive.pose.position.y - 1.0;
                Actions.runBlocking(
                        drive.actionBuilder(drive.pose)
                                .afterTime(0, new ParallelAction(manager.closeLeftClaw(), manager.closeAutoClaw()))
                                .lineToY(newLineToY)
                                .stopAndAdd(manager.raiseLiftAfterStackPickup())
                                .lineToY(-64)
                                .afterDisp(3, new ParallelAction(
                                        manager.lowerLiftForDriving(),
                                        manager.zeroLift(),
                                        manager.positionTheClawToDriveWithPixels())
                                )
                                .build()
                );
            }

            if (pad1.dpad_right) {
                Actions.runBlocking(
                        new ParallelAction(
                                manager.runLiftToPosition(-180),
                                manager.positionTheClawToPickupPixelsFromStack()
                        )
                );
            }

            if (pad1.dpad_down) {
                Actions.runBlocking(
                        new SequentialAction(
                                manager.runLiftToPosition(-900),
                                manager.positionTheClawToPickupPixels(),
                                new ParallelAction(
                                        manager.openAutoClaw(),
                                        manager.openLeftClaw()
                                ),
                                manager.zeroLift(),
                                manager.positionTheClawToDriveWithPixels()
                        )
                );
            }

            telemetry.addData("Press left bumper to close left claw", "");
            telemetry.addData("Press right bumper to close right claw", "");
            telemetry.addData("Press y to raise angle claw", "");
            telemetry.addData("Press a to lower angle claw", "");
            telemetry.addData("Press dpad left to retract and store lift", "");
            telemetry.addData("Press dpad up to drive forward, extend lift, close, retract and drive back.", "");
            telemetry.addData("Press Dpad right top right to run lift out for testing", "");
            telemetry.addData("Press dpad down to extend lift to -900, and drop pixels.", "");
            telemetry.addData("Press left joystick to toggle beam break for drive", "");
            telemetry.addData("Top Motor Lift Position", hardware.topLiftMotor.getCurrentPosition());
            telemetry.addData("Zero Switch", hardware.liftSwitch.getState());
            telemetry.addData("Top motor power", hardware.topLiftMotor.getPower());
            telemetry.addData("Toggle Beam Break", toggleBeamBreak);
            telemetry.addData("Beam Break:", hardware.beamBreak.getState());
            telemetry.addData("Pose", drive.pose);
            telemetry.update();
        }
    }
}
