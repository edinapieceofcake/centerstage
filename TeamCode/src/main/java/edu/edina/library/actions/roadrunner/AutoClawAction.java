package edu.edina.library.actions.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import edu.edina.library.enums.ClawState;
import edu.edina.library.subsystems.Claw;
import edu.edina.library.util.RobotState;

public class AutoClawAction extends ClawAction {
    private ClawState clawState;

    public AutoClawAction(Claw claw, ClawState clawState, long duration) {
        super(claw, duration);
        this.clawState = clawState;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        RobotState.getInstance().autoClawState = clawState;
        return super.run(telemetryPacket);
    }
}
