package edu.edina.library.actions.roadrunner;

import com.acmerobotics.roadrunner.Action;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.TwistServoState;
import edu.edina.library.subsystems.Claw;
import edu.edina.library.subsystems.Lift;
import edu.edina.library.subsystems.RobotHanger;
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

    public Action openLeftClaw() {
        return new LeftClawAction(claw, ClawState.Opened);
    }

    public Action closeLeftClaw() {
        return new LeftClawAction(claw, ClawState.Closed);
    }

    public Action openRightClaw() {
        return new RightClawAction(claw, ClawState.Opened);
    }

    public Action closeRightClaw() {
        return new RightClawAction(claw, ClawState.Closed);
    }

    public Action openAutoClaw() {
        return new AutoClawAction(claw, ClawState.Opened); // replace with the right action
    }

    public Action closeAutoClaw() {
        return new AutoClawAction(claw, ClawState.Closed) ; // replace with the right action
    }

    public Action positionTheClawToDriveWithPixels() {
        return new AngleClawAction(claw, AngleClawState.Drive);
    }

    public Action positionTheClawToPickupPixels() {
        return new AngleClawAction(claw, AngleClawState.Pickup);
    }

    public Action twistClawForPickup() {
        return new TwistClawAction(claw, TwistServoState.Pickup);
    }

    public Action twistClawForDropOff() {
        return new TwistClawAction(claw, TwistServoState.DropOff);
    }

    public Action runLiftToPosition(int liftPosition) {
        return new RunLiftToPositionAction(hardware, liftPosition);
    }

    public Action zeroLift() {
        return new ZeroLiftAction(hardware);
    }

    public Action getLiftReadyToDropThePixelOnTheWall() {
        return new DropOffPixelAction(claw, lift, robotHanger);
    }

    public Action getLiftReadyToDrive() {
        return new RetractLiftAction(claw, lift, robotHanger);
    }
}
