package edu.edina.library.actions.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import edu.edina.library.enums.ClawState;
import edu.edina.library.subsystems.Claw;
import edu.edina.library.util.RobotState;

public class LeftClawAction extends ClawAction {
    private ClawState clawState;

    public LeftClawAction(Claw claw, ClawState clawState) {
        super(claw);

        this.clawState = clawState;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        RobotState.getInstance().leftClawState = clawState;

        return super.run(telemetryPacket);
    }
}
