package edu.edina.library.actions.roadrunner;

import com.acmerobotics.roadrunner.Action;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.TwistServoState;
import edu.edina.library.subsystems.Claw;
import edu.edina.library.subsystems.Lift;
import edu.edina.library.subsystems.RobotHanger;
import edu.edina.library.util.Robot;
import edu.edina.library.util.RobotHardware;

public class ActionManager {
    private Claw claw;
    private Lift lift;
    private RobotHanger robotHanger;
    private RobotHardware hardware;

    public ActionManager(RobotHardware hardware) {
        claw = new Claw(hardware);
        lift = new Lift(hardware, false);
        robotHanger = new RobotHanger(hardware);
        this.hardware = hardware;
    }

    public void init() {
        claw.init();
        lift.init();
        robotHanger.init();
    }

    public void start() {
        claw.start();
        lift.start();
        robotHanger.start();
    }

    public Action getOpenLeftClawAction() {
        return new LeftClawAction(claw, ClawState.Opened);
    }

    public Action getCloseLeftClawAction() {
        return new LeftClawAction(claw, ClawState.Closed);
    }

    public Action getOpenRightClawAction() {
        return new RightClawAction(claw, ClawState.Opened);
    }

    public Action getCloseRightClawAction() {
        return new RightClawAction(claw, ClawState.Closed);
    }

    public Action getOpenAutoClawAction() {
        return new AutoClawAction(claw, ClawState.Opened);
    }

    public Action getCloseAutoClawAction() {
        return new AutoClawAction(claw, ClawState.Closed);
    }

    public Action getDriveAngleClawAction() {
        return new AngleClawAction(claw, AngleClawState.Drive);
    }

    public Action getPickupAngleClawAction() {
        return new AngleClawAction(claw, AngleClawState.Pickup);
    }

    public Action getPickupTwistClawAction() {
        return new TwistClawAction(claw, TwistServoState.Pickup);
    }

    public Action getDropOffTwistClawAction() {
        return new TwistClawAction(claw, TwistServoState.DropOff);
    }

    public Action getRunLiftToPositionAction(int liftPosition) {
        return new RunLiftToPositionAction(hardware, liftPosition);
    }

    public Action getZeroLiftAction() {
        return new ZeroLiftAction(hardware);
    }

    public Action getDropPixelAction() {
        return new DropOffPixelAction(claw, lift, robotHanger);
    }

    public Action getRetractLiftAction() {
        return new RetractLiftAction(claw, lift, robotHanger);
    }
}
