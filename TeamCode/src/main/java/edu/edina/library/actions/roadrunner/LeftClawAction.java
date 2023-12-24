package edu.edina.library.actions.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

import edu.edina.library.enums.ClawState;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;

public class LeftClawAction implements Action {
    private Deadline clawDelay = new Deadline(200, TimeUnit.MILLISECONDS);
    private boolean started = false;
    private ClawState clawState;
    private RobotHardware hardware;

    public LeftClawAction(RobotHardware hardware, ClawState clawState) {
        this.clawState = clawState;
        this.hardware = hardware;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        RobotConfiguration config = RobotConfiguration.getInstance();

        if (!started) {
            started = true;

            switch (clawState) {
                case Opened:
                    hardware.leftClawServo.setPosition(config.clawLeftOpenPosition);
                    hardware.leftClawRed.setState(false);
                    hardware.leftClawGreen.setState(true);
                    break;
                case Closed:
                    hardware.leftClawServo.setPosition(config.clawLeftClosedPosition);
                    hardware.leftClawRed.setState(true);
                    hardware.leftClawGreen.setState(false);
                    break;
            }

            clawDelay.reset();
        } else {
            if (clawDelay.hasExpired()) {
                return false;
            }
        }

        return true;
    }
}
