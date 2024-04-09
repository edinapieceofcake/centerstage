package edu.edina.library.actions.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import edu.edina.library.enums.ClawState;
import edu.edina.library.subsystems.Claw;
import edu.edina.library.util.RobotState;

public class RightClawAction extends ClawAction {
    private ClawState clawState;

    public RightClawAction(Claw claw, ClawState clawState, long duration) {
        super(claw, duration);

        this.clawState = clawState;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        RobotState.getInstance().rightClawState = clawState;

        return super.run(telemetryPacket);
    }
}
