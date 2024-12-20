package edu.edina.library.util;

import com.qualcomm.robotcore.hardware.ServoImplEx;

public class PoCServo {
    private ServoImplEx servo;
    private double currentPosition;
    public PoCServo (ServoImplEx servo) {
        this.servo = servo;
        currentPosition = -1;
    }

    public void setPosition(double position) {
        if (currentPosition != position) {
            currentPosition = position;
            servo.setPosition(currentPosition);
        }
    }

    public double getPosition() {
        return currentPosition;
    }

    public void setPwmDisable() {
        servo.setPwmDisable();
    }

    public String getConnectionInfo() {
        return servo.getConnectionInfo();
    }
}
