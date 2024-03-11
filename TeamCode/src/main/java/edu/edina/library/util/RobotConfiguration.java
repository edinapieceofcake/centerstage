package edu.edina.library.util;

public class RobotConfiguration {
    private static RobotConfiguration robotConfiguration = null;

    // droneservo positions
    public final double droneLauncherArmedPosition = 0.7;
    public final double droneLauncherLaunchedPosition = 0.1;

    // claw servo positions
    public final double clawLeftOpenPosition = 0.55;
    public final double clawLeftOpenDropPosition = 0.65;
    public final double clawLeftClosedPosition = 0.75;
    public final double clawRightClosedPosition = 0.39;
    public final double clawRightOpenPosition = 0.54;
    public final double clawRightOpenDropPosition = 0.48;
    public final double autoClawServoOpenPosition = 0.70;
    public final double autoClawServoClosePosition = 1.0;
    public final double autoClawServoOpenDropPosition = .85;
    public final double twistClawServoPickUpPosition = 0.4983;
    public final double twistClawServoDropOffPosition = 0.621;

    public final double angleClawPickupPosition = .55;
    public final double angleClawStackPickupPosition = .60;
    public final double angleClawDrivePosition = .77;
    public final double angleClawLowDropOffPosition = .52;
    public final double angleClawMediumDropOffPosition = 0.52;
    public final double angleClawHighDropOffPosition = .4;

    public final double leftAutoLowAngleClawServoDropOffPosition = 0.55;
    public final double leftAutoMediumAngleClawServoDropOffPosition = 0.52;
    public final double leftAutoHighAngleClawServoDropOffPosition = 0.52;

    public final double leftTeleopLowAngleClawServoDropOffPosition = 0.57;
    public final double leftTeleopMediumAngleClawServoDropOffPosition = 0.52;
    public final double leftTeleopHighAngleClawServoDropOffPosition = 0.52;

    public final double rightLowAngleClawServoDropOffPosition = 0.619;
    public final double rightMediumAngleClawServoDropOffPosition = 0.469;
    public final double rightHighAngleClawServoDropOffPosition = 0.52;

    public final double leftAutoLowTwistClawServoDropOffPosition = 0.4277;
    public final double leftAutoMediumTwistClawServoDropOffPosition = 0.4277;
    public final double leftAutoHighTwistClawServoDropOffPosition = 0.4277;

    public final double leftTeleopLowTwistClawServoDropOffPosition = 0.66;
    public final double leftTeleopMediumTwistClawServoDropOffPosition = 0.67;
    public final double leftTeleopHighTwistClawServoDropOffPosition = 0.67;

    public final double rightLowTwistClawServoDropOffPosition = 0.57;
    public final double rightMediumTwistClawServoDropOffPosition = 0.5877;
    public final double rightHighTwistClawServoDropOffPosition = 0.5677;

    // odo pod servo positions
    public final double par0UpPosition = 0.01;
    public final double par0DownPosition = 0.5;
    public final double par1DownPosition = 0.39;
    public final double par1UpPosition = 0.99;
    public final double perpUpPosition = 0.01;
    public final double perpDownPosition = 0.39;

    // lift servo positions
    public final double startingLeftLiftServoPosition = 0.93;
    public final double startingRightLiftServoPosition = 0.13;
    public final double leftStackPickupServoPosition = 0.8;
    public final double rightStackPickupServoPosition = 0.29;
    public double leftLowDropOffServoPosition = .53;
    public double rightLowDropOffServoPosition = .51;
    public final double leftMediumDropOffServoPosition = .43;
    public final double rightMediumDropOffServoPosition = .58;
    public final double leftHighDropOffServoPosition = .33;
    public final double rightHighDropOffServoPosition = .68;
    public final double leftOne = .66;
    public final double leftTwo = 0.6233;
    public final double leftThree = 0.5866;
    public final double leftFour = 0.5499;
    public final double leftFive = 0.5132;
    public final double leftSix = 0.4765;
    public final double leftSeven = 0.4398;
    public final double leftEight = 0.4031;
    public final double leftNine = 0.3664;
    public final double leftTen = .33;

    public final double rightOne = 0.4;
    public final double rightTwo = 0.4311;
    public final double rightThree = 0.4622;
    public final double rightFour = 0.4933;
    public final double rightFive = 0.5244;
    public final double rightSix = 0.5555;
    public final double rightSeven = 0.5866;
    public final double rightEight = 0.6177;
    public final double rightNine = 0.6488;
    public final double rightTen = 0.68;

    // motor powers
    public final double hangerRetractingPower = -1;
    public final double hangerExtendingPower = 1;
    public final double liftRetractingPower = .75;
    public final double liftExtendingPower = 0.9;
    public final double slowLiftRetractingPower = .4;
    public final double superSlowLiftRetractingPower = .1;
    public final double liftRetractingStep = 100;
    public final double liftExtenstionStep = 150;

    // lift encoder positions
    public final int liftDrivePosition = 50;
    public int liftLowDropOffPosition = -600;
    public final int liftMediumDropOffPosition = -800;
    public final int liftHighDropOffPosition = -900;
    public final int liftTwistPosition = -200;
    public final int minimumExtensionBeforeTwistingInTicks = -350;
    public final int minimumExtensionBeforeRaisingLiftInTicks = -120;

    // robot hanger positions
    public final double leftLatchServoPosition = .62;
    public final double rightLatchServoPosition = .42;
    public final int hangMotorInitPosition = -230;
    public final int hangMotorStorePosition = 0;
    public final int hangMotorLowDropOffPosition = -640;
    public final int hangMotorMediumDropOffPosition = -730;
    public final int hangMotorHighDropOffPosition = -830;
    public final int hangMotorHangPosition = -2800;

    public final double minimumHeightToTwistServoInInches = 12.0;

    public static synchronized RobotConfiguration getInstance()
    {
        if (robotConfiguration == null) {
            robotConfiguration = new RobotConfiguration();
        }

        return robotConfiguration;
    }
}
