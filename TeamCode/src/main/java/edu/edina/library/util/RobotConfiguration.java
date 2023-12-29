package edu.edina.library.util;

public class RobotConfiguration {
    private static RobotConfiguration robotConfiguration = null;

    // droneservo positions
    public double droneLauncherArmedPosition = 0.7;
    public double droneLauncherLaunchedPosition = 0.1;

    // claw servo positions
    public double clawLeftOpenPosition = 0.55;// 0.92;
    public double clawLeftClosedPosition = 0.73;//0.75;
    public double clawRightClosedPosition = 0.2;//0.25;
    public double clawRightOpenPosition = 0.45;//0.08;
    public double autoClawServoOpenPosition = 0.70;
    public double autoClawServoClosePosition = 1.0;
    public double twistClawServoPickUpPosition = 0.87;//0.97;
    public double twistClawServoDropOffPosition = 0.98;//0.28;
    public double angleClawPickupPosition = .52; //.57
    public double angleClawDrivePosition = .77;
    public double angleClawLowDropOffPosition = .23;
    public double angleClawHighDropOffPosition = .15;

    // odo pod servo positions
    public double par0UpPosition = 0.0;
    public double par0DownPosition = 0.5;
    public double par1DownPosition = 0.39;
    public double par1UpPosition = 1.0;
    public double perpUpPosition = 0.0;
    public double perpDownPosition = 0.39;

    // lift servo positions
    public double startingLeftLiftServoPosition = 0.96;
    public double startingRightLiftServoPosition = 0.13;
    public double leftStackPickupServoPosition = 0.8;
    public double rightStackPickupServoPosition = 0.29;
    public double leftLowDropOffServoPosition = .53;
    public double rightLowDropOffServoPosition = .51;
    public double leftMediumDropOffServoPosition = .43;
    public double rightMediumDropOffServoPosition = .58;
    public double leftHighDropOffServoPosition = .33;
    public double rightHighDropOffServoPosition = .68;
    public double liftServoPositionAtBottomOfHubs = 0.29;

    // motor powers
    public double hangerRetractingPower = -1;
    public double hangerExtendingPower = 1;
    public double liftRetractingPower = .75;
    public double liftExtendingPower = 1;
    public double slowLiftRetractingPower = .3;
    public double superSlowLiftRetractingPower = .1;
    public double liftRetractingStep = 100;
    public double liftExtenstionStep = 150;

    // lift encoder positions
    public int liftDrivePosition = 50;
    public int liftLowDropOffPosition = -600;
    public int liftMediumDropOffPosition = -600;
    public int liftAutonomousDropOffPosition = -500;
    public int liftHighDropOffPosition = -800;
    public int liftTwistPosition = -300;
    public int minimumExtensionBeforeTwistingInTicks = -350;
    public int minimumExtensionBeforeRaisingLiftInTicks = -120;

    // robot hanger positions
    public double leftHangServoPosition = .53;
    public double rightHangServoPosition = .51;
    public int hangMotorInitPosition = -260;
    public int hangMotorStorePosition = 0;
    public int hangMotorLowDropOffPosition = -850;
    public int hangMotorHighDropOffPosition = -1100;
    public int hangMotorHangPosition = -2800;

    public double minimumHeightToTwistServoInInches = 12.0;

    public static synchronized RobotConfiguration getInstance()
    {
        if (robotConfiguration == null) {
            robotConfiguration = new RobotConfiguration();
        }

        return robotConfiguration;
    }
}
