package edu.edina.opmodes.test;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.PoCMecanumDrive;

import edu.edina.library.actions.roadrunner.ActionManager;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;
import edu.edina.library.util.SmartGamepad;

@TeleOp
//@Disabled
public class TestAutoDrop extends LinearOpMode  {
    @Override
    public void runOpMode() throws InterruptedException {
        RobotHardware hardware = new RobotHardware(hardwareMap);
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        RobotState state = RobotState.getInstance();
        ActionManager manager = new ActionManager(hardware);
        boolean toggleBeamBreak = true;
        // Start Position
        PoCMecanumDrive drive = new PoCMecanumDrive(hardware.leftFront,
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
            Actions.runBlocking(new SequentialAction(
                manager.positionTheClawToPickupPixelsFromStack()
            ));
        }

        while (opModeIsActive()) {
            pad1.update();

            if (pad1.y) {
                Actions.runBlocking(new SequentialAction(
                    manager.runLiftToPosition(-300),
                    manager.positionTheClawToPickupPixelsFromStack(),
                    new InstantAction(
                            () -> hardware.leftClawServo.setPosition(RobotConfiguration.getInstance().clawLeftOpenDropPosition))
                ));
            }

            if (pad1.a) {
                Actions.runBlocking(new SequentialAction(
                        manager.positionTheClawToPickupPixelsFromStack(),
                        manager.zeroLift()
                ));
            }

            if (pad1.x) {
                Actions.runBlocking(new SequentialAction(
                    manager.closeAllClaws()
                ));
            }

            if (pad1.b) {
                Actions.runBlocking(new SequentialAction(
                    manager.openAllClaws()
                ));
            }

            telemetry.addLine("Press Y to Run Lift Out");
            telemetry.addLine("Press A Run Lift To Zero");
            telemetry.addLine("Press X to Close Claws");
            telemetry.addLine("Press B to Open Claws");
            telemetry.update();
        }
    }
}
