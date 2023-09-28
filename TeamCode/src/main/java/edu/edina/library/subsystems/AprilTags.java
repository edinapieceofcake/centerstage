package edu.edina.library.subsystems;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import edu.edina.library.util.Robot;

public class AprilTags extends Subsystem {
    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera

    /**
     * The variable to store our instance of the AprilTag processor.
     */
    private AprilTagProcessor aprilTag;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;

    private Robot robot;

    public AprilTagProcessor getAprilTag() {
        return aprilTag;
    }

    public AprilTags(Robot robot){
        // Create the AprilTag processor the easy way.
        aprilTag = AprilTagProcessor.easyCreateWithDefaults();
        this.robot = robot;
        // Create the vision portal the easy way.
        if (USE_WEBCAM) {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                    robot.RobotHardware.webcamName, aprilTag);
        } else {
            visionPortal = VisionPortal.easyCreateWithDefaults(
                    BuiltinCameraDirection.BACK, aprilTag);
        }   

    }

    public void init(){}

    @Override
    public void start() {

    }

    public void update(){
        this.robot.RobotState.detections = aprilTag.getDetections();
    }
}
