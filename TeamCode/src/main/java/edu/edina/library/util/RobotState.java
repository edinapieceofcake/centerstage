package edu.edina.library.util;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

public class RobotState {
    public double droneLauncherArmedPosition = 0.0;
    public double droneLauncherLaunchedPosition = 1.0;
    public double clawLeftOpenPosition = 1.0;
    public double clawLeftClosedPosition = 0.0;
    public double clawRightClosedPosition = 0.0;
    public double clawRightOpenPosition = 1.0;
    public double twistClawServoPickUpPosition = 0.0;
    public double twistClawServoDropOffPosition = 1.0;
    public double par0UpPosition = 1.0;
    public double par0DownPosition = 0.0;
    public double par1DownPosition = 0.0;
    public double par1UpPosition = 1.0;
    public double perpUpPosition = 0.0;
    public double perpDownPosition = 1.0;
    public double hangerRetractingPower = -0.5;
    public double hangerExtendingPower = -0.5;
    public double liftRetractingPower = -0.5;
    public double liftExtendingPower = -0.5;
    public double currentLiftPosition;
    public double currentLiftServoPosition;
    public List<AprilTagDetection> detections;

    public RobotState() {
    }

    public void telemetry(Telemetry telemetry) {
        telemetry.addData("# AprilTags Detected", detections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : detections) {
            if (detection.metadata != null) {
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }   // end for() loop
    }
}
