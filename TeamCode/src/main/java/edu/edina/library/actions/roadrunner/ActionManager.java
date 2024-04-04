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
        return openLeftClaw(200);
    }
    public Action openLeftClaw(long duration) { return new LeftClawAction(claw, ClawState.Opened, duration); }

    public Action closeLeftClaw() { return closeLeftClaw(200); }
    public Action closeLeftClaw(long duration) { return new LeftClawAction(claw, ClawState.Closed, duration); }

    public Action openRightClaw() { return openRightClaw(200); }

    public Action openRightClaw(long duration) { return new RightClawAction(claw, ClawState.Opened, duration); }

    public Action closeRightClaw() { return closeRightClaw(200); }

    public Action closeRightClaw(long duration) { return new RightClawAction(claw, ClawState.Closed, duration); }

    public Action openAutoClaw() {
        return openAutoClaw(200);
    }

    public Action openAutoClaw(long duration) { return new AutoClawAction(claw, ClawState.Opened, duration); }

    public Action closeAutoClaw() { return closeAutoClaw(200); }

    public Action closeAutoClaw(long duration) { return new AutoClawAction(claw, ClawState.Closed, duration); }

    public Action positionTheClawToDriveWithPixels(long duration) {
        return new AngleClawAction(claw, AngleClawState.Drive, duration);
    }

    public Action positionTheClawToDriveWithPixels() {
        return positionTheClawToDriveWithPixels(200);
    }

    public Action positionTheClawToPickupPixels(long duration) {
        return new AngleClawAction(claw, AngleClawState.Pickup, duration);
    }

    public Action positionTheClawToPickupPixels() {
        return positionTheClawToPickupPixels(200);
    }

    public Action positionTheClawToPickupPixelsFromStack(long duration) {
        return new AngleClawAction(claw, AngleClawState.Stack, duration);
    }

    public Action positionTheClawToPickupPixelsFromStack() {
        return positionTheClawToPickupPixelsFromStack(200);
    }

    public Action positionTheClawToDropPixels(long duration) {
        return new AngleClawAction(claw, AngleClawState.CenterDropOff, duration);
    }

    public Action positionTheClawToDropPixels() {
        return positionTheClawToDropPixels(200);
    }

    public Action twistClawForPickup(long duration) {
        return new TwistClawAction(claw, TwistServoState.Pickup, duration);
    }

    public Action twistClawForPickup() {
        return twistClawForPickup(200);
    }

    public Action twistClawForDropOff(long duration) {
        return new TwistClawAction(claw, TwistServoState.CenterDropOff, duration);
    }

    public Action twistClawForDropOff() {
        return twistClawForDropOff(200);
    }

    public Action runLiftToPositionWithDuration(int liftPosition, long duration, boolean useBeamBreak) {
        return new RunLiftToPositionAction(hardware, liftPosition, duration, useBeamBreak);
    }

    public Action runLiftToPosition(int liftPosition, boolean useBeamBreak) {
        return runLiftToPositionWithDuration(liftPosition, 750, useBeamBreak);
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
