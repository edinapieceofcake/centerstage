package edu.edina.library.util;

public class RobotConfiguration {
    private static RobotConfiguration robotConfiguration = null;

    // droneservo positions
    public double droneLauncherArmedPosition = 0.6;
    public double droneLauncherLaunchedPosition = 0;

    // claw servo positions
    public double clawLeftOpenPosition = 0.92;
    public double clawLeftClosedPosition = 0.83;
    public double clawRightClosedPosition = 0.25;
    public double clawRightOpenPosition = 0.08;
    public double twistClawServoPickUpPosition = 0.96;
    public double twistClawServoDropOffPosition = 0.28;
    public double angleClawPickupPosition = 0.32;
    public double angleClawDrivePosition = 0.35;
    public double angleClawDropOffPosition = 1.0;

    // odo pod servo positions
    public double par0UpPosition = 1.0;
    public double par0DownPosition = 0.0;
    public double par1DownPosition = 0.0;
    public double par1UpPosition = 1.0;
    public double perpUpPosition = 0.0;
    public double perpDownPosition = 1.0;

    // lift servo positions
    public double startingLiftServoPosition = 0.25;
    public double lowDropOffServoPosition = .4;
    public double highDropOffServoPosition = .6;
    public double liftServoPositionAtBottomOfHubs = 0.29;

    // motor powers
    public double hangerRetractingPower = -0.5;
    public double hangerExtendingPower = 0.5;
    public double liftRetractingPower = -.25;
    public double liftExtendingPower = .25;

    // lift encoder positions
    public int liftDrivePosition = -50;
    public int liftPickupPosition = -20;
    public int liftFirstExtensionDropOffPosition = -120;
    public int liftLowDropOffPosition = -200;
    public int liftHighDropOffPosition = -550;
    public int minimumExtensionBeforeTwistingInTicks = -350;
    public int minimumExtensionBeforeRaisingLiftInTicks = -120;

    public double minimumHeightToTwistServoInInches = 9.0;

    public static synchronized RobotConfiguration getInstance()
    {
        if (robotConfiguration == null) {
            robotConfiguration = new RobotConfiguration();
        }

        return robotConfiguration;
    }
}
