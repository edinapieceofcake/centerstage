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
    private int pixelLengthInLiftTicks = -50;
    private boolean beamBreakDistanceSet = false;
    private boolean useBeamBreak = false;

    public RunLiftToPositionAction(RobotHardware hardware, int liftPosition, long duration, boolean useBeamBreak) {
        this.hardware = hardware;
        this.liftPosition = liftPosition;
        this.duration = duration;
        this.useBeamBreak = useBeamBreak;
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
            if (useBeamBreak) {
                if (!hardware.beamBreak.getState() && !beamBreakDistanceSet) {
                    // bream break hit
                    int currentLiftPosition = hardware.topLiftMotor.getCurrentPosition();
                    Log.d("RunLiftToPositionAction: ", String.format("Beambreak hit at %d", currentLiftPosition));

                    hardware.topLiftMotor.setPower(0);
                    hardware.bottomLiftMotor.setPower(0);
                    beamBreakDistanceSet = true;
                    return false;
                }
            }

            if (positionTimeout.hasExpired()) {
                Log.d("RunLiftToPositionAction: ", String.format("Timeout expired, length %d", duration));
                return false;
            }

            if ((hardware.topLiftMotor.getCurrentPosition() > (liftPosition - 10)) &&
                    (hardware.topLiftMotor.getCurrentPosition() < (liftPosition + 10))) {
                // reached end or we have timed out
                Log.d("RunLiftToPositionAction: ", String.format("Distance expired, timeout left %d", positionTimeout.timeRemaining(TimeUnit.MILLISECONDS)));
                hardware.topLiftMotor.setPower(0);
                hardware.bottomLiftMotor.setPower(0);
                return false;
            }
        }

        return true;
    }
}
