package edu.edina.library.actions.roadrunner;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class DropPixelAtBackBoard implements Action {
    RobotHardware hardware;
    RevBlinkinLedDriver.BlinkinPattern pattern;

    public DropPixelAtBackBoard(RobotHardware hardware) {
        this.hardware = hardware;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        RobotConfiguration configuration = RobotConfiguration.getInstance();
        RobotState state = RobotState.getInstance();

        return false;
    }

    public Action dropPixels() {
        // some stuff to do - right?  for now, just set LEDs white
        pattern = RevBlinkinLedDriver.BlinkinPattern.WHITE;
        hardware.blinkinLedDriver.setPattern(pattern);

        return new DropPixelAtBackBoard(hardware);
    }
}
