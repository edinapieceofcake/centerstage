package edu.edina.library.actions.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.util.ThreadPool;

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
    private LiftServoState liftServoState;
    private Claw claw;
    private Lift lift;
    private RobotHanger robotHanger;

    public DropOffPixelActionAsync(Claw claw, Lift lift, RobotHanger robotHanger, LiftServoState liftServoState) {
        this.claw = claw;
        this.lift = lift;
        this.robotHanger = robotHanger;
        this.liftServoState = liftServoState;
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

        switch (liftServoState) {
            case One:
                state.currentLiftServoStateDropOffPosition = LiftServoState.One;
                state.currentLiftMotorDropOffPosition = config.liftDropOffPositionOne;
                break;
            case Two:
                state.currentLiftServoStateDropOffPosition = LiftServoState.Two;
                state.currentLiftMotorDropOffPosition = config.liftDropOffPositionOne;
                break;
            case Three:
                state.currentLiftServoStateDropOffPosition = LiftServoState.Three;
                state.currentLiftMotorDropOffPosition = config.liftDropOffPositionTwo;
                break;
            case Four:
                state.currentLiftServoStateDropOffPosition = LiftServoState.Four;
                state.currentLiftMotorDropOffPosition = config.liftDropOffPositionTwo;
                break;
            case Five:
                state.currentLiftServoStateDropOffPosition = LiftServoState.Five;
                state.currentLiftMotorDropOffPosition = config.liftDropOffPositionThree;
                break;
            case Six:
                state.currentLiftServoStateDropOffPosition = LiftServoState.Six;
                state.currentLiftMotorDropOffPosition = config.liftDropOffPositionFour;
                break;
            case Seven:
                state.currentLiftServoStateDropOffPosition = LiftServoState.Seven;
                state.currentLiftMotorDropOffPosition = config.liftDropOffPositionFour;
                break;
            case Eight:
                state.currentLiftServoStateDropOffPosition = LiftServoState.Eight;
                state.currentLiftMotorDropOffPosition = config.liftDropOffPositionFive;
                break;
            case Nine:
                state.currentLiftServoStateDropOffPosition = LiftServoState.Nine;
                state.currentLiftMotorDropOffPosition = config.liftDropOffPositionFive;
                break;
        }

        dropOffPixelExecutor = ThreadPool.newSingleThreadExecutor("drop off pixel");
        dropOffPixelExecutor.submit(dropOffPixelRunnable);

        return false;
    }

    private void dropOffPixel() {
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