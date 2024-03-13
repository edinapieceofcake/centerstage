package edu.edina.library.actions.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.util.ThreadPool;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.concurrent.ExecutorService;

import edu.edina.library.enums.DropOffOrientation;
import edu.edina.library.enums.DropOffState;
import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftServoState;
import edu.edina.library.enums.LiftSlideState;
import edu.edina.library.subsystems.Claw;
import edu.edina.library.subsystems.Lift;
import edu.edina.library.subsystems.RobotHanger;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotState;

public class DropOffPixelActionAsync implements Action {
    private boolean isBackstage = false;
    private Claw claw;
    private Lift lift;
    private RobotHanger robotHanger;

    public DropOffPixelActionAsync(Claw claw, Lift lift, RobotHanger robotHanger, boolean isBackstage) {
        this.claw = claw;
        this.lift = lift;
        this.robotHanger = robotHanger;
        this.isBackstage = isBackstage;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        RobotConfiguration config = RobotConfiguration.getInstance();
        RobotState state = RobotState.getInstance();
        ExecutorService dropOffPixelExecutor;

        Runnable dropOffPixelRunnable;
        dropOffPixelRunnable = () -> {
            dropOffPixel();
        };

        state.lastKnownLiftState = LiftDriveState.Drive;
        state.currentLiftDriveState = LiftDriveState.DropOff;
        state.currentLiftSlideState = LiftSlideState.Extending;
        state.dropOffOrientation = DropOffOrientation.Center;
        state.dropOffState = DropOffState.Start;
        state.currentLiftMotorDropOffPosition = config.liftDropOffPositionOne;
        state.currentLiftServoStateDropOffPosition = LiftServoState.Two;
        if (isBackstage) {
            state.currentLiftServoStateDropOffPosition = LiftServoState.One;
        }

        dropOffPixelExecutor = ThreadPool.newSingleThreadExecutor("drop off pixel");
        dropOffPixelExecutor.submit(dropOffPixelRunnable);

        return false;
    }

    private void dropOffPixel() {
        RobotConfiguration config = RobotConfiguration.getInstance();
        RobotState state = RobotState.getInstance();

        while (state.dropOffState != DropOffState.Finished) {
            lift.update();
            claw.update();
            robotHanger.update();
            Thread.yield();
        }

        state.lastKnownLiftState = LiftDriveState.DropOff;
    }
}