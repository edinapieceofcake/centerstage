package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

import edu.edina.library.enums.Alliance;
import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.DropOffState;
import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftSlideState;
import edu.edina.library.enums.ParkLocation;
import edu.edina.library.enums.PickUpState;
import edu.edina.library.enums.PropLocation;
import edu.edina.library.subsystems.Claw;
import edu.edina.library.subsystems.Lift;
import edu.edina.library.util.PoCHuskyLens;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;
import edu.edina.library.util.SmartGamepad;

public class BaseAutonomous extends LinearOpMode {
    protected RobotHardware hardware;
    protected Claw claw;
    protected Lift lift;
    protected MecanumDrive drive;
    protected RevBlinkinLedDriver.BlinkinPattern pattern;
    protected PoCHuskyLens poCHuskyLens;
    protected PropLocation propLocation;

    protected Alliance getAlliance() {
        return Alliance.Red;
    }

    protected RevBlinkinLedDriver.BlinkinPattern getUnsuccessfulPropMatchColor() {
        return RevBlinkinLedDriver.BlinkinPattern.GREEN;
    }

    protected RevBlinkinLedDriver.BlinkinPattern getSuccessfulPropMatchColor() {
        return RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_GRAY;
    }

    protected Pose2d getStartPose() {
        return new Pose2d(2, -62.5, Math.toRadians(90));
    }

    protected void initHardware() {
        // test hardware construction and use in an empty action
        hardware = new RobotHardware(hardwareMap);

        // Start Position

        // use out version of the drive based off the hardware that we created above.
        drive = new org.firstinspires.ftc.teamcode.MecanumDrive(hardware.leftFront,
                hardware.leftBack, hardware.rightBack, hardware.rightFront,
                hardware.par0, hardware.par1, hardware.perp,
                hardware.imu, hardware.voltageSensor, getStartPose());

        // uncomment this and comment out the above if it doesn't work right
        //drive = new MecanumDrive(hardwareMap, startPose);

        // Heartbeat Red to signify Red alliance
        pattern = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_GRAY;
        hardware.blinkinLedDriver.setPattern(pattern);

        PropLocation lastLocation = PropLocation.Idle;

        // HuskyLens Init
        poCHuskyLens = new PoCHuskyLens(hardware.huskyLens, telemetry, getAlliance());
        poCHuskyLens.init();

        claw = new Claw(hardware);
        lift = new Lift(hardware, false);

        // Initialize Odo Wheels, Drone Launcher, and Hanger
        hardware.dropServosForAutonomous();
        hardware.droneLaunchServo.setPosition(RobotConfiguration.getInstance().droneLauncherArmedPosition);
        hardware.homeHangMotor(telemetry);
    }

    protected void extendLift (RobotState state) {
        state.lastKnownLiftState = LiftDriveState.Drive;
        state.currentLiftDriveState = LiftDriveState.LowDropOff;
        state.currentLiftSlideState = LiftSlideState.Extending;
        state.dropOffState = DropOffState.Start;
        RobotConfiguration.getInstance().liftLowDropOffPosition = -475;

        // Update lift until done  TODO: Make this Parallel with drive code
        while (state.dropOffState != DropOffState.Finished) {
            lift.update();
            claw.update();
            idle();
        }
    }

    protected void retractLift (RobotState state) {
        // Retract lift
        state.pickUpState = PickUpState.Start;
        state.lastKnownLiftState = LiftDriveState.LowDropOff;
        state.currentLiftDriveState = LiftDriveState.Drive;
        state.currentLiftSlideState = LiftSlideState.Retracting;

        while (state.pickUpState != PickUpState.Finished) {
            lift.update();
            claw.update();
            idle();
        }

        state.lastKnownLiftState = LiftDriveState.Drive;
        RobotConfiguration.getInstance().liftLowDropOffPosition = -600;
    }

    protected void runPaths(ParkLocation parkLocation) {}

    @Override
    public void runOpMode() throws InterruptedException {
        RobotState state = RobotState.getInstance();
        ParkLocation parkLocation = ParkLocation.Corner;
        SmartGamepad pad1 = new SmartGamepad(gamepad1);
        long delayTime = 0;
        initHardware();

        // Initialize claw and lift
        claw.init();
        claw.start();
        lift.init();
        lift.start();

        state.leftClawState = ClawState.Closed;
        state.rightClawState = ClawState.Closed;
        claw.update();

        hardware.lights.setPower(1);

        while (!isStarted()) {
            pad1.update();

            telemetry.addData("Press A for corner, Y for center park", "");
            if (pad1.a) {
                parkLocation = ParkLocation.Corner;
            } else if (pad1.y) {
                parkLocation = ParkLocation.Center;
            }

            telemetry.addData("Current Park Location", parkLocation);
            telemetry.addData("Press left bumper to increase delay, right number to decrease delay.", "");
            if (pad1.left_bumper) {
                delayTime += 1000;
            } else if (pad1.right_bumper) {
                delayTime -= 1000;
            }

            telemetry.addData("Delay in seconds", delayTime / 1000);

            poCHuskyLens.update();

            // Find Prop Location
            propLocation = poCHuskyLens.getPropLocation();

            telemetry.addData("Location", propLocation);
            telemetry.update();

            // Show solid pattern if block seen, otherwise heartbeat
            if (propLocation != PropLocation.None) {
                pattern = getSuccessfulPropMatchColor();
            } else {
                pattern = getUnsuccessfulPropMatchColor();
            }

            hardware.blinkinLedDriver.setPattern(pattern);
        }

        hardware.lights.setPower(0);

        if (opModeIsActive()) {
            // Signal GREEN for successful run
            pattern = RevBlinkinLedDriver.BlinkinPattern.GREEN;
            hardware.blinkinLedDriver.setPattern(pattern);
            if (delayTime > 0) {
                sleep(delayTime);
            }

            runPaths(parkLocation);

            pattern = RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_WHITE;
            hardware.blinkinLedDriver.setPattern(pattern);
        }
    }
}
