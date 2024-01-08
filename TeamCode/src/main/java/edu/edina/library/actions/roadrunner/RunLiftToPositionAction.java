package edu.edina.library.actions.roadrunner;

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
    private Deadline positionTimeout = new Deadline(2000, TimeUnit.MILLISECONDS);

    public RunLiftToPositionAction(RobotHardware hardware, int liftPosition) {
        this.hardware = hardware;
        this.liftPosition = liftPosition;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        if (!started) {
            started = true;

            hardware.topLiftMotor.setTargetPosition(liftPosition);
            hardware.bottomLiftMotor.setTargetPosition(liftPosition);
            hardware.topLiftMotor.setPower(1);
            hardware.bottomLiftMotor.setPower(1);
            positionTimeout.reset();
        } else {
            if (((hardware.topLiftMotor.getCurrentPosition() > (liftPosition - 10)) &&
                    (hardware.topLiftMotor.getCurrentPosition() < (liftPosition + 10))) || positionTimeout.hasExpired()) {
                // reached end or we have timed out
                return false;
            }
        }

        return true;
    }
}
