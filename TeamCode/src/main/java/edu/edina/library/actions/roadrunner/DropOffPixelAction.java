package edu.edina.library.actions.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import edu.edina.library.enums.DropOffOrientation;
import edu.edina.library.enums.DropOffState;
import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftServoState;
import edu.edina.library.enums.LiftSlideState;
import edu.edina.library.subsystems.Claw;
import edu.edina.library.subsystems.Lift;
import edu.edina.library.subsystems.RobotHanger;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class DropOffPixelAction implements Action {
    private boolean started = false;
    private LiftServoState liftServoState;
    private Claw claw;
    private Lift lift;
    private RobotHanger robotHanger;

    public DropOffPixelAction(Claw claw, Lift lift, RobotHanger robotHanger, LiftServoState liftServoState) {
        this.claw = claw;
        this.lift = lift;
        this.robotHanger = robotHanger;
        this.liftServoState = liftServoState;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        RobotConfiguration config = RobotConfiguration.getInstance();
        RobotState state = RobotState.getInstance();

        if (!started) {
            started = true;

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
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionOne;
                    break;
                case Four:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Four;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionTwo;
                    break;
                case Five:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Five;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionTwo;
                    break;
                case Six:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Six;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionThree;
                    break;
                case Seven:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Seven;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionFour;
                    break;
                case Eight:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Eight;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionFour;
                    break;
                case Nine:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Nine;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionFive;
                    break;
                case Ten:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Ten;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionFive;
                    break;
            }
        }

        if (state.dropOffState != DropOffState.Finished) {
            lift.update();
            claw.update();
            robotHanger.update();
            return true;
        } else {
            state.lastKnownLiftState = LiftDriveState.DropOff;
            return false;
        }
    }
}
