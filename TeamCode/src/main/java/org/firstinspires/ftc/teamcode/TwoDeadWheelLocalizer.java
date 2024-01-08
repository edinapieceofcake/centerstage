package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.DualNum;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Time;
import com.acmerobotics.roadrunner.Twist2dDual;
import com.acmerobotics.roadrunner.Vector2dDual;
import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.FlightRecorder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.PositionVelocityPair;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Config
public final class TwoDeadWheelLocalizer implements Localizer {
    public static class Params {
        public double parYTicks = -756.6437132344282; // y position of the parallel encoder (in tick units)
        public double perpXTicks = -862.0096899816914; // x position of the perpendicular encoder (in tick units)
    }

    public static Params PARAMS = new Params();

    public final Encoder par, perp;
    public final IMU imu;
    public final IMU expansionImu;

    private int lastParPos, lastPerpPos;
    private Rotation2d lastHeading;

    private final double inPerTick;

    private double lastRawHeadingVel, headingVelOffset;

    public TwoDeadWheelLocalizer(DcMotorEx par, DcMotorEx perp, IMU imu, IMU expansionImu, double inPerTick) {
        this.par = new RawEncoder(par);
        this.perp = new RawEncoder(perp);
        this.imu = imu;
        this.expansionImu = expansionImu;

        lastParPos = this.par.getPositionAndVelocity().position;
        lastPerpPos = this.perp.getPositionAndVelocity().position;
        lastHeading = Rotation2d.exp(getRobotYawPitchRollAngles());

        this.inPerTick = inPerTick;

        FlightRecorder.write("TWO_DEAD_WHEEL_PARAMS", PARAMS);
    }

    public TwoDeadWheelLocalizer(HardwareMap hardwareMap, IMU imu, double inPerTick) {
        par = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "par")));
        perp = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "perp")));
        this.imu = imu;
        this.expansionImu = imu;

        lastParPos = par.getPositionAndVelocity().position;
        lastPerpPos = perp.getPositionAndVelocity().position;
        lastHeading = Rotation2d.exp(getRobotYawPitchRollAngles());

        this.inPerTick = inPerTick;

        FlightRecorder.write("TWO_DEAD_WHEEL_PARAMS", PARAMS);
    }

    // see https://github.com/FIRST-Tech-Challenge/FtcRobotController/issues/617
    private double getHeadingVelocity() {
        double rawHeadingVel = getRobotAngularVelocity();
        if (Math.abs(rawHeadingVel - lastRawHeadingVel) > Math.PI) {
            headingVelOffset -= Math.signum(rawHeadingVel) * 2 * Math.PI;
        }
        lastRawHeadingVel = rawHeadingVel;
        return headingVelOffset + rawHeadingVel;
    }

    public Twist2dDual<Time> update() {
        PositionVelocityPair parPosVel = par.getPositionAndVelocity();
        PositionVelocityPair perpPosVel = perp.getPositionAndVelocity();
        Rotation2d heading = Rotation2d.exp(getRobotYawPitchRollAngles());

        int parPosDelta = parPosVel.position - lastParPos;
        int perpPosDelta = perpPosVel.position - lastPerpPos;
        double headingDelta = heading.minus(lastHeading);

        double headingVel = getHeadingVelocity();

        Twist2dDual<Time> twist = new Twist2dDual<>(
                new Vector2dDual<>(
                        new DualNum<Time>(new double[] {
                                parPosDelta - PARAMS.parYTicks * headingDelta,
                                parPosVel.velocity - PARAMS.parYTicks * headingVel,
                        }).times(inPerTick),
                        new DualNum<Time>(new double[] {
                                perpPosDelta - PARAMS.perpXTicks * headingDelta,
                                perpPosVel.velocity - PARAMS.perpXTicks * headingVel,
                        }).times(inPerTick)
                ),
                new DualNum<>(new double[] {
                        headingDelta,
                        headingVel,
                })
        );

        lastParPos = parPosVel.position;
        lastPerpPos = perpPosVel.position;
        lastHeading = heading;

        return twist;
    }

    private double getRobotYawPitchRollAngles() {
        double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        if (heading == 0.0) {
            return expansionImu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        } else {
            return heading;
        }
    }

    private double getRobotAngularVelocity() {
        double velocity = imu.getRobotAngularVelocity(AngleUnit.RADIANS).zRotationRate;

        if (velocity == 0.0) {
            return expansionImu.getRobotAngularVelocity(AngleUnit.RADIANS).zRotationRate;
        } else {
            return velocity;
        }
    }
}
