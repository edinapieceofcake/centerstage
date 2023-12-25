package edu.edina.library.actions.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

import edu.edina.library.util.RobotHardware;

public class ZeroLiftAction implements Action {
    private boolean started = false;
    private RobotHardware hardware;
    private Deadline zeroSwitchTimeout = new Deadline(2000, TimeUnit.MILLISECONDS);

    public ZeroLiftAction(RobotHardware hardware) {
        this.hardware = hardware;
    }

    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        if (!started) {
            started = true;

            hardware.topLiftMotor.setTargetPosition(10);
            hardware.bottomLiftMotor.setTargetPosition(10);
            hardware.topLiftMotor.setPower(1);
            hardware.bottomLiftMotor.setPower(1);
            zeroSwitchTimeout.reset();
        } else {
            if (hardware.topLiftMotor.getCurrentPosition() > -50) {
                // cut the power as we get closer
                hardware.topLiftMotor.setPower(.3);
                hardware.bottomLiftMotor.setPower(.3);
            }

            if (!hardware.liftSwitch.getState() || zeroSwitchTimeout.hasExpired()) {
                // hit zero switch or timed out
                hardware.topLiftMotor.setPower(0);
                hardware.bottomLiftMotor.setPower(0);
                return false;
            }
        }

        return true;
    }
}
