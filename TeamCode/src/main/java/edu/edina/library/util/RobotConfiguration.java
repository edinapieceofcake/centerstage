package edu.edina.library.util;

public class RobotConfiguration {
    private static RobotConfiguration robotConfiguration = null;
    public double droneLauncherArmedPosition = 0.6;
    public double droneLauncherLaunchedPosition = 0;
    public double clawLeftOpenPosition = 0.92;
    public double clawLeftClosedPosition = 0.83;
    public double clawRightClosedPosition = 0.25;
    public double clawRightOpenPosition = 0.08;
    public double par0UpPosition = 1.0;
    public double par0DownPosition = 0.0;
    public double par1DownPosition = 0.0;
    public double par1UpPosition = 1.0;
    public double perpUpPosition = 0.0;
    public double perpDownPosition = 1.0;
    public double hangerRetractingPower = -0.5;
    public double hangerExtendingPower = 0.5;
    public double liftRetractingPower = -1;
    public double liftExtendingPower = 1;
    public double twistClawServoPickUpPosition = 0.96;
    public double twistClawServoDropOffPosition = 0.28;
    public double angleClawPickupPosition = 0.32;
    public double angleClawDrivePosition = 0.35;
    public double angleClawDropOffPosition = 1.0;
    public int liftDrivePosition = -50;
    public int liftPickupPosition = -20;
    public int liftLowDropOffPostion = -200;
    public int liftHighDropOffPosition = -550;
    public int minimumExtensionBeforeTwistingAtZeroLift = 360;
    public int minimumExtensionBeforeRaisingLift = 100;
    public double startingLiftServoPosition = 0.26;
    public double minimumHeightToTwistServo = 10;

    public static synchronized RobotConfiguration getInstance()
    {
        if (robotConfiguration == null) {
            robotConfiguration = new RobotConfiguration();
        }

        return robotConfiguration;
    }
}
