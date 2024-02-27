package edu.edina.library.util;

import com.qualcomm.robotcore.hardware.ServoImplEx;

public class PoCServo {
    private ServoImplEx servo;
    private double currentPosition;
    public PoCServo (ServoImplEx servo) {
        this.servo = servo;
        currentPosition = servo.getPosition();
    }

    public void setPosition(double position) {
        if (currentPosition != position) {
            currentPosition = position;
            servo.setPosition(currentPosition);
        }
    }

    public double getPosition() {
        currentPosition = servo.getPosition();
        return currentPosition;
    }

    public Object getConnectionInfo() {
        return servo.getConnectionInfo();
    }
}
