package edu.edina.library.actions.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import edu.edina.library.enums.TwistServoState;
import edu.edina.library.subsystems.Claw;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class TwistClawAction extends ClawAction {
    TwistServoState twistServoState;

    public TwistClawAction(Claw claw, TwistServoState twistServoState, long duration) {
        super(claw, duration);

        this.twistServoState = twistServoState;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        RobotState.getInstance().twistServoState = twistServoState;

        return super.run(telemetryPacket);
    }
}
