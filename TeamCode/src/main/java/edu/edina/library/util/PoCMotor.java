package edu.edina.library.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class PoCMotor {
    private DcMotorEx motor;
    private int currentTargetPosition;
    private double currentPower;
    private DcMotor.RunMode currentMode;
    private DcMotor.ZeroPowerBehavior currentZeroPowerBehavior;
    public PoCMotor(DcMotorEx motor) {
        this.motor = motor;
        currentTargetPosition = motor.getTargetPosition();
        currentPower = motor.getPower();
        currentMode = motor.getMode();
        currentZeroPowerBehavior = motor.getZeroPowerBehavior();
    }

    public void setPower(double power) {
        if (currentPower != power) {
            currentPower = power;
            motor.setPower(currentPower);
        }
    }

    public double getPower() {
        currentPower = motor.getPower();
        return currentPower;
    }

    public void setTargetPosition(int position) {
        if (currentTargetPosition != position) {
            currentTargetPosition = position;
            motor.setTargetPosition(currentTargetPosition);
        }
    }

    public int getTargetPosition() {
        currentTargetPosition = motor.getTargetPosition();
        return currentTargetPosition;
    }

    public void setMode(DcMotor.RunMode mode) {
        if (currentMode != mode) {
            currentMode = mode;
            motor.setMode(currentMode);
        }
    }

    public DcMotor.RunMode getMode() {
        currentMode = motor.getMode();
        return currentMode;
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPower) {
        if (currentZeroPowerBehavior != zeroPower) {
            currentZeroPowerBehavior = zeroPower;
            motor.setZeroPowerBehavior(currentZeroPowerBehavior);
        }
    }

    public DcMotor.ZeroPowerBehavior getZeroPowerBehavior() {
        currentZeroPowerBehavior = motor.getZeroPowerBehavior();
        return currentZeroPowerBehavior;
    }

    public int getCurrentPosition() {
        return motor.getCurrentPosition();
    }

    public boolean isBusy() {
        return motor.isBusy();
    }

    public Object getCurrent(CurrentUnit currentUnit) {
        return motor.getCurrent(currentUnit);
    }
}
