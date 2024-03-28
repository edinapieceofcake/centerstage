package edu.edina.library.actions.roadrunner;

import com.acmerobotics.roadrunner.Action;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.LiftServoState;
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
        return new AutoClawAction(claw, ClawState.Opened);
    }

    public Action closeAutoClaw() {
        return new AutoClawAction(claw, ClawState.Closed);
    }

    public Action openAllClaws() {return new AllClawsAction(claw, ClawState.Opened); }
    public Action closeAllClaws() {return new AllClawsAction(claw, ClawState.Closed); }

    public Action positionTheClawToDriveWithPixels() {
        return new AngleClawAction(claw, AngleClawState.Drive);
    }

    public Action positionTheClawToPickupPixels() {
        return new AngleClawAction(claw, AngleClawState.Pickup);
    }

    public Action positionTheClawToPickupPixelsFromStack() {
        return new AngleClawAction(claw, AngleClawState.Stack);
    }

    public Action positionTheClawToDropPixels() {
        return new AngleClawAction(claw, AngleClawState.CenterDropOff);
    }

    public Action twistClawForPickup() {
        return new TwistClawAction(claw, TwistServoState.Pickup);
    }

    public Action twistClawForDropOff() {
        return new TwistClawAction(claw, TwistServoState.CenterDropOff);
    }

    public Action runLiftToPositionWithDuration(int liftPosition, long duration) {
        return new RunLiftToPositionAction(hardware, liftPosition, duration);
    }

    public Action runLiftToPosition(int liftPosition) {
        return new RunLiftToPositionAction(hardware, liftPosition, 2000);
    }

    public Action zeroLift() {
        return new ZeroLiftAction(hardware);
    }

    public Action getLiftReadytoDropThePixelAtSpecificHeight(LiftServoState liftServoState) {
        return new DropOffPixelAction(claw, lift, robotHanger, liftServoState);
    }

    public Action getLiftReadyToDropThePixelLowOnTheWall() {
        return new DropOffPixelAction(claw, lift, robotHanger, LiftServoState.One);
    }

    public Action getLiftReadyToDropThePixelLowOnTheWallAsync() {
        return new DropOffPixelActionAsync(claw, lift, robotHanger, LiftServoState.One);
    }

    public Action getLiftReadyToDropThePixelHighOnTheWall() {
        return new DropOffPixelAction(claw, lift, robotHanger, LiftServoState.Two);
    }

    public Action getLiftReadyToDropPixelFromRight() {
        return new DropOffPixelRightSideAction(claw, lift, robotHanger, true);
    }

    public Action getLiftReadyToDropPixelFromLeft() {
        return new DropOffPixelLeftSideAction(claw, lift, robotHanger, true);
    }

    public Action getLiftReadyToDrive() {
        return new RetractLiftAction(claw, lift, robotHanger);
    }

    public Action raiseLiftAfterStackPickup() {
        return new LiftServoAction(hardware, LiftServoState.StackPickup);
    }

    public Action lowerLiftForDriving() {
        return new LiftServoAction(hardware, LiftServoState.Start);
    }
}
