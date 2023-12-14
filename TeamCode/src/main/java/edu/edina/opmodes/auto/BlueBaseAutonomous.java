package edu.edina.opmodes.auto;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import edu.edina.library.enums.Alliance;

public class BlueBaseAutonomous extends BaseAutonomous {
    @Override
    protected RevBlinkinLedDriver.BlinkinPattern getUnsuccessfulPropMatchColor() {
        return RevBlinkinLedDriver.BlinkinPattern.BLUE;
    }

    @Override
    protected RevBlinkinLedDriver.BlinkinPattern getSuccessfulPropMatchColor() {
        return RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_BLUE;
    }

    @Override
    protected Alliance getAlliance() {
        return Alliance.Blue;
    }
}
