package edu.edina.opmodes.auto;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import edu.edina.library.enums.Alliance;

public class RedBaseAutonomous extends BaseAutonomous{
    @Override
    protected RevBlinkinLedDriver.BlinkinPattern getUnsuccessfulPropMatchColor() {
        return RevBlinkinLedDriver.BlinkinPattern.RED;
    }

    @Override
    protected RevBlinkinLedDriver.BlinkinPattern getSuccessfulPropMatchColor() {
        return RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_RED;
    }

    @Override
    protected Alliance getAlliance() {
        return Alliance.Red;
    }
}
