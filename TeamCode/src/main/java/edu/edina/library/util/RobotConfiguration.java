package edu.edina.library.util;

public class RobotConfiguration {
    private static RobotConfiguration robotConfiguration = null;

    // droneservo positions
    public double droneLauncherArmedPosition = 0.7;
    public double droneLauncherLaunchedPosition = 0.1;

    // claw servo positions
    public double clawLeftOpenPosition = 0.92;
    public double clawLeftClosedPosition = 0.75;
    public double clawRightClosedPosition = 0.25;
    public double clawRightOpenPosition = 0.08;
    public double twistClawServoPickUpPosition = 0.96;
    public double twistClawServoDropOffPosition = 0.28;
    public double angleClawPickupPosition = 0.37;
    public double angleClawDrivePosition = 0.23;
    public double angleClawLowDropOffPosition = .77;
    public double angleClawHighDropOffPosition = .85;

    // odo pod servo positions
    public double par0UpPosition = 1.0;
    public double par0DownPosition = 0.0;
    public double par1DownPosition = 0.0;
    public double par1UpPosition = 1.0;
    public double perpUpPosition = 0.0;
    public double perpDownPosition = 1.0;

    // lift servo positions
    public double startingLeftLiftServoPosition = 0.96;
    public double startingRightLiftServoPosition = 0.1;
    public double leftLowDropOffServoPosition = .53;
    public double rightLowDropOffServoPosition = .51;
    public double leftHighDropOffServoPosition = .33;
    public double rightHighDropOffServoPosition = .68;
    public double liftServoPositionAtBottomOfHubs = 0.29;

    // motor powers
    public double hangerRetractingPower = -0.5;
    public double hangerExtendingPower = 0.5;
    public double liftRetractingPower = -.75;
    public double liftExtendingPower = 1;

    // lift encoder positions
    public int liftDrivePosition = 10;
    public int liftPickupPosition = 0;
    public int liftFirstExtensionDropOffPosition = -120;
    public int liftLowDropOffPosition = -600;
    public int liftHighDropOffPosition = -800;
    public int liftTwistPosition = -300;
    public int minimumExtensionBeforeTwistingInTicks = -350;
    public int minimumExtensionBeforeRaisingLiftInTicks = -120;

    public double minimumHeightToTwistServoInInches = 12.0;

    public static synchronized RobotConfiguration getInstance()
    {
        if (robotConfiguration == null) {
            robotConfiguration = new RobotConfiguration();
        }

        return robotConfiguration;
    }
}
