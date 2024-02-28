package edu.edina.library.util;

public class RobotConfiguration {
    private static RobotConfiguration robotConfiguration = null;

    // droneservo positions
    public double droneLauncherArmedPosition = 0.7;
    public double droneLauncherLaunchedPosition = 0.1;

    // claw servo positions
    public double clawLeftOpenPosition = 0.55;
    public double clawLeftOpenDropPosition = 0.65;
    public double clawLeftClosedPosition = 0.75;
    public double clawRightClosedPosition = 0.39;
    public double clawRightOpenPosition = 0.54;
    public double clawRightOpenDropPosition = 0.45;
    public double autoClawServoOpenPosition = 0.70;
    public double autoClawServoClosePosition = 1.0;
    public double autoClawServoOpenDropPosition = .85;
    public double twistClawServoPickUpPosition = 0.4983;
    public double twistClawServoDropOffPosition = 0.621;

    public double angleClawPickupPosition = .55;
    public double angleClawStackPickupPosition = .60;
    public double angleClawDrivePosition = .77;
    public double angleClawLowDropOffPosition = .52;
    public double angleClawMediumDropOffPosition = 0.52;
    public double angleClawHighDropOffPosition = .4;

    public double leftAutoLowAngleClawServoDropOffPosition = 0.55;
    public double leftAutoMediumAngleClawServoDropOffPosition = 0.52;
    public double leftAutoHighAngleClawServoDropOffPosition = 0.52;

    public double leftTeleopLowAngleClawServoDropOffPosition = 0.57;
    public double leftTeleopMediumAngleClawServoDropOffPosition = 0.52;
    public double leftTeleopHighAngleClawServoDropOffPosition = 0.52;

    public double rightLowAngleClawServoDropOffPosition = 0.619;
    public double rightMediumAngleClawServoDropOffPosition = 0.469;
    public double rightHighAngleClawServoDropOffPosition = 0.52;

    public double leftAutoLowTwistClawServoDropOffPosition = 0.4277;
    public double leftAutoMediumTwistClawServoDropOffPosition = 0.4277;
    public double leftAutoHighTwistClawServoDropOffPosition = 0.4277;

    public double leftTeleopLowTwistClawServoDropOffPosition = 0.66;
    public double leftTeleopMediumTwistClawServoDropOffPosition = 0.67;
    public double leftTeleopHighTwistClawServoDropOffPosition = 0.67;

    public double rightLowTwistClawServoDropOffPosition = 0.57;
    public double rightMediumTwistClawServoDropOffPosition = 0.5877;
    public double rightHighTwistClawServoDropOffPosition = 0.5677;

    // odo pod servo positions
    public double par0UpPosition = 0.01;
    public double par0DownPosition = 0.5;
    public double par1DownPosition = 0.39;
    public double par1UpPosition = 0.99;
    public double perpUpPosition = 0.01;
    public double perpDownPosition = 0.39;

    // lift servo positions
    public double startingLeftLiftServoPosition = 0.93;
    public double startingRightLiftServoPosition = 0.13;
    public double leftStackPickupServoPosition = 0.8;
    public double rightStackPickupServoPosition = 0.29;
    public double leftLowDropOffServoPosition = .53;
    public double rightLowDropOffServoPosition = .51;
    public double leftMediumDropOffServoPosition = .43;
    public double rightMediumDropOffServoPosition = .58;
    public double leftHighDropOffServoPosition = .33;
    public double rightHighDropOffServoPosition = .68;

    // motor powers
    public double hangerRetractingPower = -1;
    public double hangerExtendingPower = 1;
    public double liftRetractingPower = .75;
    public double liftExtendingPower = 0.9;
    public double slowLiftRetractingPower = .4;
    public double superSlowLiftRetractingPower = .1;
    public double liftRetractingStep = 100;
    public double liftExtenstionStep = 150;

    // lift encoder positions
    public int liftDrivePosition = 50;
    public int liftLowDropOffPosition = -600;
    public int liftMediumDropOffPosition = -800;
    public int liftHighDropOffPosition = -900;
    public int liftTwistPosition = -200;
    public int minimumExtensionBeforeTwistingInTicks = -350;
    public int minimumExtensionBeforeRaisingLiftInTicks = -120;

    // robot hanger positions
    public double leftLatchServoPosition = .62;
    public double rightLatchServoPosition = .42;
    public int hangMotorInitPosition = -230;
    public int hangMotorStorePosition = 0;
    public int hangMotorLowDropOffPosition = -640;
    public int hangMotorMediumDropOffPosition = -730;
    public int hangMotorHighDropOffPosition = -830;
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
