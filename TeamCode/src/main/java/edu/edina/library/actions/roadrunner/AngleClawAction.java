package edu.edina.library.actions.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.subsystems.Claw;
import edu.edina.library.util.RobotState;

public class AngleClawAction extends ClawAction {
    private AngleClawState clawState;
    public AngleClawAction(Claw claw, AngleClawState clawState) {
        super(claw);

        this.clawState = clawState;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        RobotState.getInstance().angleClawState = clawState;

        return super.run(telemetryPacket);
    }
}
