package edu.edina.library.actions.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.TwistServoState;
import edu.edina.library.subsystems.Claw;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class ClawAction implements Action {
    private Deadline clawDelay;
    private boolean started = false;
    private Claw claw;

    public ClawAction(Claw claw, long duration) {
        this.claw = claw;
        clawDelay = new Deadline(duration, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        if (!started) {
            started = true;

            claw.update();

            clawDelay.reset();
        } else {
            claw.update();
            if (clawDelay.hasExpired()) {
                return false;
            }
        }

        return true;
    }
}
