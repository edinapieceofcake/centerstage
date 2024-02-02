package edu.edina.library.actions.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import edu.edina.library.enums.DropOffOrientation;
import edu.edina.library.enums.DropOffState;
import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftSlideState;
import edu.edina.library.subsystems.Claw;
import edu.edina.library.subsystems.Lift;
import edu.edina.library.subsystems.RobotHanger;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class DropOffPixelAction implements Action {
    private boolean started = false;
    private boolean isBackstage = false;
    private Claw claw;
    private Lift lift;
    private RobotHanger robotHanger;

    public DropOffPixelAction(Claw claw, Lift lift, RobotHanger robotHanger, boolean isBackstage) {
        this.claw = claw;
        this.lift = lift;
        this.robotHanger = robotHanger;
        this.isBackstage = isBackstage;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        RobotConfiguration config = RobotConfiguration.getInstance();
        RobotState state = RobotState.getInstance();

        if (!started) {
            started = true;

            state.lastKnownLiftState = LiftDriveState.Drive;
            state.currentLiftDriveState = LiftDriveState.LowDropOff;
            state.currentLiftSlideState = LiftSlideState.Extending;
            state.dropOffOrientation = DropOffOrientation.Center;
            state.dropOffState = DropOffState.Start;
            if (isBackstage) {
                config.leftLowDropOffServoPosition = .70;
                config.rightLowDropOffServoPosition = .36;
            }

            config.liftLowDropOffPosition = -600;
        }

        if (state.dropOffState != DropOffState.Finished) {
            lift.update();
            claw.update();
            robotHanger.update();
            return true;
        } else {
            state.lastKnownLiftState = LiftDriveState.LowDropOff;
            if (isBackstage) {
                config.leftLowDropOffServoPosition = .53;
                config.rightLowDropOffServoPosition = .51;
            }

            return false;
        }
    }
}
