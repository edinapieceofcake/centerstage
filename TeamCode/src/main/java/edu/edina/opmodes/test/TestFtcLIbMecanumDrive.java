package edu.edina.opmodes.test;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@TeleOp
public class TestFtcLIbMecanumDrive extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx leftFront = hardwareMap.get(DcMotorEx.class, "leftFront");
        DcMotorEx leftBack = hardwareMap.get(DcMotorEx.class, "leftBack");
        DcMotorEx rightBack = hardwareMap.get(DcMotorEx.class, "rightBack");
        DcMotorEx rightFront = hardwareMap.get(DcMotorEx.class, "rightFront");

        Motor fL = new Motor(hardwareMap, "leftFront");
        Motor fR = new Motor(hardwareMap, "rightFront");
        Motor bL = new Motor(hardwareMap, "leftBack");
        Motor bR = new Motor(hardwareMap, "rightBack");

        fL.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        fR.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        bL.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        bR.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        MecanumDrive drive = new MecanumDrive(fL, fR, bL, bR);
        GamepadEx driverOp = new GamepadEx(gamepad1);

        waitForStart();

        while (opModeIsActive()) {
            drive.driveRobotCentric(
                    driverOp.getLeftX(),
                    driverOp.getLeftY(),
                    driverOp.getRightY(),
                    true);

            telemetry.addData("Left Front Current", leftFront.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.addData("Left Back Current", leftBack.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.addData("Right Back Current", rightBack.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.addData("Right Front Current", rightFront.getCurrent(CurrentUnit.MILLIAMPS));
            telemetry.update();
        }
    }
}
