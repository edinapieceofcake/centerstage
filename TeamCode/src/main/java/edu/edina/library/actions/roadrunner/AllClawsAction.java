package edu.edina.library.actions.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import edu.edina.library.enums.ClawState;
import edu.edina.library.subsystems.Claw;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class AllClawsAction extends ClawAction {
    private ClawState clawState;

    public AllClawsAction(Claw claw, ClawState clawState, long duration) {
        super(claw, duration);

        this.clawState = clawState;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        RobotState.getInstance().leftClawState = clawState;
        RobotState.getInstance().rightClawState = clawState;
        RobotState.getInstance().autoClawState = clawState;

        return super.run(telemetryPacket);
    }
}
