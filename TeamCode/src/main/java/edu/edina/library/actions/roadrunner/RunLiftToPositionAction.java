package edu.edina.library.actions.roadrunner;

import android.util.Log;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;

import edu.edina.library.util.RobotHardware;

public class RunLiftToPositionAction implements Action {
    private boolean started = false;
    private int liftPosition;
    private RobotHardware hardware;
    private Deadline positionTimeout;
    private long duration;

    public RunLiftToPositionAction(RobotHardware hardware, int liftPosition, long duration) {
        this.hardware = hardware;
        this.liftPosition = liftPosition;
        this.duration = duration;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        if (!started) {
            started = true;

            hardware.topLiftMotor.setTargetPosition(liftPosition);
            hardware.bottomLiftMotor.setTargetPosition(liftPosition);
            hardware.topLiftMotor.setPower(1);
            hardware.bottomLiftMotor.setPower(1);
            positionTimeout = new Deadline(duration, TimeUnit.MILLISECONDS);
        } else {
            if (positionTimeout.hasExpired()) {
                Log.d("RunLiftToPositionAction: ", "Timeout expired");
                return false;
            }

            if ((hardware.topLiftMotor.getCurrentPosition() > (liftPosition - 10)) &&
                    (hardware.topLiftMotor.getCurrentPosition() < (liftPosition + 10))) {
                // reached end or we have timed out
                Log.d("RunLiftToPositionAction: ", "Distance expired");
                return false;
            }
        }

        return true;
    }
}
