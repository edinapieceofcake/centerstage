package edu.edina.library.subsystems;

import edu.edina.library.util.ClosingState;
import edu.edina.library.util.LiftState;
import edu.edina.library.util.OpeningState;
import edu.edina.opmodes.teleop.Robot;

public class Lift extends Subsystem{
    private Robot robot;

    public Lift(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void init() {}

    @Override
    public void start() {

    }

    @Override
    public void update() {
        if (robot.Started) {
        }
    }

    public void setLiftProperties(boolean servoOneUp, boolean servoOneDown, boolean openLift, boolean closeLift) {
    }

    private double round(double originalValue) {
        return ((int)(originalValue * 100)) / 100.0;
    }
}
