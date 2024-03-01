package edu.edina.library.actions.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.PwmControl;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

import edu.edina.library.enums.LiftServoState;
import edu.edina.library.subsystems.Lift;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;

public class LiftServoAction implements Action {
    private Deadline liftDelay = new Deadline(200, TimeUnit.MILLISECONDS);
    private RobotHardware hardware;
    private boolean started = false;
    private LiftServoState liftServoState;

    public LiftServoAction(RobotHardware hardware, LiftServoState liftServoState) {
        this.hardware = hardware;
        this.liftServoState = liftServoState;
    }
    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        RobotConfiguration config = RobotConfiguration.getInstance();

        if (!started) {
            started = true;

            switch (liftServoState) {
                case Start:
                    hardware.leftLiftServo.setPosition(config.startingLeftLiftServoPosition);
                    hardware.rightLiftServo.setPosition(config.startingRightLiftServoPosition);
                    break;
                case StackPickup:
                    hardware.leftLiftServo.setPosition(config.leftStackPickupServoPosition);
                    hardware.rightLiftServo.setPosition(config.rightStackPickupServoPosition);
                    break;
            }

            liftDelay.reset();
        } else {
            if (liftDelay.hasExpired()) {
                return false;
            }
        }

        return true;
    }
}
