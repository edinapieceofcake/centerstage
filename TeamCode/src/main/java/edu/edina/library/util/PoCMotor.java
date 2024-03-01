package edu.edina.library.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class PoCMotor {
    private DcMotorEx motor;
    private int currentTargetPosition;
    private double currentPower;
    private DcMotor.RunMode currentMode;
    private DcMotor.ZeroPowerBehavior currentZeroPowerBehavior;
    private DcMotorSimple.Direction currentDirection;
    // need this to make sure that even if the current and new are the same, it gets through for
    // the first time or you get the exception about changing modes without doing the setTarget
    private boolean targetSetOnce = false;

    public PoCMotor(DcMotorEx motor) {
        this.motor = motor;
        currentTargetPosition = motor.getTargetPosition();
        currentPower = motor.getPower();
        currentMode = motor.getMode();
        currentZeroPowerBehavior = motor.getZeroPowerBehavior();
        currentDirection = motor.getDirection();
    }

    public void setPower(double power) {
        if (currentPower != power) {
            currentPower = power;
            motor.setPower(currentPower);
        }
    }

    public double getPower() {
        return currentPower;
    }

    public void setTargetPosition(int position) {
        if (!targetSetOnce || (currentTargetPosition != position)) {
            currentTargetPosition = position;
            motor.setTargetPosition(currentTargetPosition);
            targetSetOnce = true;
        }
    }

    public int getTargetPosition() {
        return currentTargetPosition;
    }

    public void setMode(DcMotor.RunMode mode) {
        if (currentMode != mode) {
            currentMode = mode;
            motor.setMode(currentMode);
        }
    }

    public DcMotor.RunMode getMode() {
        return currentMode;
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPower) {
        if (currentZeroPowerBehavior != zeroPower) {
            currentZeroPowerBehavior = zeroPower;
            motor.setZeroPowerBehavior(currentZeroPowerBehavior);
        }
    }

    public DcMotor.ZeroPowerBehavior getZeroPowerBehavior() {
        return currentZeroPowerBehavior;
    }

    public int getCurrentPosition() {
        return motor.getCurrentPosition();
    }

    public void setDirection(DcMotorSimple.Direction direction) {
        if (currentDirection != direction) {
            currentDirection = direction;
            motor.setDirection(currentDirection);
        }
    }

    public DcMotorSimple.Direction getDirection() {
        return currentDirection;
    }

    public boolean isBusy() {
        return motor.isBusy();
    }

    public double getCurrent(CurrentUnit currentUnit) {
        return motor.getCurrent(currentUnit);
    }
}
