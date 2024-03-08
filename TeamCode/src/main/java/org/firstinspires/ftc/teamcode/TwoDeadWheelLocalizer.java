package org.firstinspires.ftc.teamcode;

import android.util.Log;

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
import com.qualcomm.robotcore.util.ThreadPool;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import java.util.concurrent.ExecutorService;

@Config
public final class TwoDeadWheelLocalizer implements Localizer {
    public static class Params {
        public double parYTicks = -756.6437132344282; // y position of the parallel encoder (in tick units)
        public double perpXTicks = -862.0096899816914; // x position of the perpendicular encoder (in tick units)
    }

    public static Params PARAMS = new Params();

    public final Encoder par, perp;
    private final IMU primaryImu;
    private final IMU secondaryImu;

    private int lastParPos, lastPerpPos;
    private Rotation2d lastHeading;

    private final double inPerTick;

    private double lastRawHeadingVel, headingVelOffset;

    private boolean usePrimary = true;

    private ExecutorService updatePoseExecutor = null;

    private Runnable updatePoseRunnable = () -> {
        updatePose();
    };

    private volatile boolean updatingPose = false;

    private volatile Twist2dDual<Time> twist;

    public TwoDeadWheelLocalizer(DcMotorEx par, DcMotorEx perp, IMU primaryImu, IMU secondaryImu,
                                 double inPerTick) {
        this.par = new RawEncoder(par);
        this.perp = new RawEncoder(perp);
        this.primaryImu = primaryImu;
        this.secondaryImu = secondaryImu;

        lastParPos = this.par.getPositionAndVelocity().position;
        lastPerpPos = this.perp.getPositionAndVelocity().position;
        lastHeading = Rotation2d.exp(getRobotYawPitchRollAngles());

        this.inPerTick = inPerTick;

        FlightRecorder.write("TWO_DEAD_WHEEL_PARAMS", PARAMS);
    }

    public TwoDeadWheelLocalizer(HardwareMap hardwareMap, IMU primaryImu, double inPerTick) {
        par = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "par")));
        perp = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "perp")));
        this.primaryImu = primaryImu;
        this.secondaryImu = primaryImu;

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
        if (updatingPose) {
            return twist;
        } else {
            PositionVelocityPair parPosVel = par.getPositionAndVelocity();
            PositionVelocityPair perpPosVel = perp.getPositionAndVelocity();
            Rotation2d heading = Rotation2d.exp(getRobotYawPitchRollAngles());

            int parPosDelta = parPosVel.position - lastParPos;
            int perpPosDelta = perpPosVel.position - lastPerpPos;
            double headingDelta = heading.minus(lastHeading);

            double headingVel = getHeadingVelocity();

            Log.d("POSE-POSITION", String.format("%d %d %d %d, %f %f", parPosVel.position,
                    perpPosVel.position, parPosDelta, perpPosDelta, Math.toDegrees(heading.real),
                    Math.toDegrees(headingDelta)));

            Twist2dDual<Time> singleTwist = new Twist2dDual<>(
                    new Vector2dDual<>(
                            new DualNum<Time>(new double[]{
                                    parPosDelta - PARAMS.parYTicks * headingDelta,
                                    parPosVel.velocity - PARAMS.parYTicks * headingVel,
                            }).times(inPerTick),
                            new DualNum<Time>(new double[]{
                                    perpPosDelta - PARAMS.perpXTicks * headingDelta,
                                    perpPosVel.velocity - PARAMS.perpXTicks * headingVel,
                            }).times(inPerTick)
                    ),
                    new DualNum<>(new double[]{
                            headingDelta,
                            headingVel,
                    })
            );

            lastParPos = parPosVel.position;
            lastPerpPos = perpPosVel.position;
            lastHeading = heading;

            return singleTwist;
        }
    }

    private void updatePose() {
        while (updatingPose) {
            PositionVelocityPair parPosVel = par.getPositionAndVelocity();
            PositionVelocityPair perpPosVel = perp.getPositionAndVelocity();
            Rotation2d heading = Rotation2d.exp(getRobotYawPitchRollAngles());

            int parPosDelta = parPosVel.position - lastParPos;
            int perpPosDelta = perpPosVel.position - lastPerpPos;
            double headingDelta = heading.minus(lastHeading);

            double headingVel = getHeadingVelocity();

            Log.d("POSE-POSITION", String.format("%d %d %d %d, %f %f", parPosVel.position,
                    perpPosVel.position, parPosDelta, perpPosDelta, Math.toDegrees(heading.real),
                    Math.toDegrees(headingDelta)));

            twist = new Twist2dDual<>(
                    new Vector2dDual<>(
                            new DualNum<Time>(new double[]{
                                    parPosDelta - PARAMS.parYTicks * headingDelta,
                                    parPosVel.velocity - PARAMS.parYTicks * headingVel,
                            }).times(inPerTick),
                            new DualNum<Time>(new double[]{
                                    perpPosDelta - PARAMS.perpXTicks * headingDelta,
                                    perpPosVel.velocity - PARAMS.perpXTicks * headingVel,
                            }).times(inPerTick)
                    ),
                    new DualNum<>(new double[]{
                            headingDelta,
                            headingVel,
                    })
            );

            lastParPos = parPosVel.position;
            lastPerpPos = perpPosVel.position;
            lastHeading = heading;
        }
    }

    private double getRobotYawPitchRollAngles() {
        YawPitchRollAngles angles = null;

        if (usePrimary) {
            angles = primaryImu.getRobotYawPitchRollAngles();

            if (angles.getAcquisitionTime() == 0) {
                usePrimary = false;
                return secondaryImu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            } else {
                return angles.getYaw(AngleUnit.RADIANS);
            }
        } else {
            return secondaryImu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        }
    }

    private double getRobotAngularVelocity() {
        AngularVelocity velocities = null;

         if (usePrimary) {
             velocities = primaryImu.getRobotAngularVelocity(AngleUnit.RADIANS);

            if (velocities.acquisitionTime == 0) {
                usePrimary = false;
                return secondaryImu.getRobotAngularVelocity(AngleUnit.RADIANS).zRotationRate;
            } else {
                return velocities.zRotationRate;
            }
        } else {
            return secondaryImu.getRobotAngularVelocity(AngleUnit.RADIANS).zRotationRate;
        }
    }

    public void startPoseUpdate() {
        if (!updatingPose) {
            updatePoseExecutor = ThreadPool.newSingleThreadExecutor("pose update");
            updatePoseExecutor.submit(updatePoseRunnable);
            updatingPose = true;
        }
    }

    public void stopPoseUpdate() {
        if (updatePoseExecutor != null) {
            updatePoseExecutor.shutdown();
        }

        updatingPose = false;
    }
}
