package edu.edina.library.actions.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.ClawState;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;

public class AngleClawAction implements Action {
    private Deadline clawDelay = new Deadline(200, TimeUnit.MILLISECONDS);
    private boolean started = false;
    private AngleClawState angleClawState;
    private RobotHardware hardware;

    public AngleClawAction(RobotHardware hardware, AngleClawState clawState) {
        this.angleClawState = clawState;
        this.hardware = hardware;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        RobotConfiguration config = RobotConfiguration.getInstance();

        if (!started) {
            started = true;
            switch (angleClawState) {
                case Drive:
                    hardware.angleClawServo.setPosition(config.angleClawDrivePosition);
                    break;
                case Pickup:
                    hardware.angleClawServo.setPosition(config.angleClawPickupPosition);
                    break;
                case DropOff:
                    hardware.angleClawServo.setPosition(config.angleClawLowDropOffPosition);
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
