package edu.edina.library.util;

import com.qualcomm.robotcore.hardware.ServoImplEx;

public class PoCServo {
    private ServoImplEx servo;
    private double currentPosition;
    private boolean positionSetOnce = false;
    public PoCServo (ServoImplEx servo) {
        this.servo = servo;
        currentPosition = servo.getPosition();
    }

    public void setPosition(double position) {
        if (!positionSetOnce || (currentPosition != position)) {
            currentPosition = position;
            servo.setPosition(currentPosition);
            positionSetOnce = true;
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
