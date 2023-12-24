package edu.edina.library.actions.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.TwistServoState;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;

public class TwistClawAction implements Action {
    private Deadline clawDelay = new Deadline(200, TimeUnit.MILLISECONDS);
    private boolean started = false;
    private TwistServoState twistServoState;
    private RobotHardware hardware;

    public TwistClawAction(RobotHardware hardware, TwistServoState twistServoState) {
        this.hardware = hardware;
        this.twistServoState = twistServoState;
    }
    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        RobotConfiguration config = RobotConfiguration.getInstance();

        if (!started) {
            started = true;

            switch (twistServoState) {
                case Pickup:
                    hardware.twistClawServo.setPosition(config.twistClawServoPickUpPosition);
                    break;
                case DropOff:
                    hardware.twistClawServo.setPosition(config.twistClawServoDropOffPosition);
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
