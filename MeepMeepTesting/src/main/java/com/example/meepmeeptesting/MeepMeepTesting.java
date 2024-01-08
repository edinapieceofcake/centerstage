package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity audienceCycleC = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        audienceCycleC.runAction(audienceCycleC.getDrive().actionBuilder(new Pose2d(-42, -64, Math.toRadians(90)))
                // Drop the pixel
                .splineTo(new Vector2d(-28, -30), Math.toRadians(90))

                // Back out and head to middle stack
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-56, -21, Math.toRadians(180)), Math.toRadians(180))
                .waitSeconds(0.5)

                // Return to Backboard VIA C-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-20, -10), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(10, -10), Math.toRadians(0))
                .splineTo(new Vector2d(48, -36), Math.toRadians(0))
                .waitSeconds(.5)

                // Return to Stacks VIA C-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(10, -10, Math.toRadians(180)), Math.toRadians(180))
                .splineToSplineHeading(new Pose2d(-20, -10, Math.toRadians(180)), Math.toRadians(180))
                .splineToSplineHeading(new Pose2d(-56, -21, Math.toRadians(180)), Math.toRadians(180))
                .waitSeconds(0.5)

                // Return to Backboard VIA C-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-20, -10), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(10, -10), Math.toRadians(0))
                .splineTo(new Vector2d(48, -36), Math.toRadians(0))
                .waitSeconds(.5)
                .build());

        RoadRunnerBotEntity audienceCycleCBlue = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        audienceCycleCBlue.runAction(audienceCycleCBlue.getDrive().actionBuilder(new Pose2d(-31, 64, Math.toRadians(180)))
                // Drop the pixel
                .splineTo(new Vector2d(-38, 34.5), Math.toRadians(270))

                // Back out and head to middle stack
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-58, 39, Math.toRadians(180)), Math.toRadians(180))
                .waitSeconds(0.5)

                // Return to Backboard VIA C-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-30, 12), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(10, 12), Math.toRadians(0))
                .splineTo(new Vector2d(48, 36), Math.toRadians(0))
                .waitSeconds(.5)

                // Return to Stacks VIA C-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(10, 12, Math.toRadians(180)), Math.toRadians(180))
                .splineToSplineHeading(new Pose2d(-20, 12, Math.toRadians(180)), Math.toRadians(180))
                .splineToSplineHeading(new Pose2d(-56, 15.5, Math.toRadians(180)), Math.toRadians(180))
                .waitSeconds(0.5)

                // Return to Backboard VIA C-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-20, 12), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(10, 12), Math.toRadians(0))
                .splineTo(new Vector2d(48, 36), Math.toRadians(0))
                .waitSeconds(.5)
                .build());

        RoadRunnerBotEntity audienceCycle = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        audienceCycle.runAction(audienceCycle.getDrive().actionBuilder(new Pose2d(-42, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(-38, -32), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-56, -37, Math.toRadians(180)), Math.toRadians(180))
                .waitSeconds(0.5)

                // Return to Backboard
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -58), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(0, -58), Math.toRadians(0))
                .splineTo(new Vector2d(48, -35), Math.toRadians(0))
                .waitSeconds(0.5)

                // Head to Stacks VIA A-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(0, -58, Math.toRadians(-180)), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(-30, -58), Math.toRadians(180))
                .splineTo(new Vector2d(-60,-36), Math.toRadians(180))
                .waitSeconds(0.5)
                .build());

        RoadRunnerBotEntity audienceRedRight = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        audienceRedRight.runAction(audienceRedRight.getDrive().actionBuilder(new Pose2d(-42, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(-30, -30), Math.toRadians(0))
                .setReversed(true)
                .splineTo(new Vector2d(-35, -10), Math.toRadians(0))
                .splineTo(new Vector2d(35, -10), Math.toRadians(0))
                .splineTo(new Vector2d(45, -40), Math.toRadians(180))
                .build());

        RoadRunnerBotEntity audienceRedRightWall = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        audienceRedRightWall.runAction(audienceRedRightWall.getDrive().actionBuilder(new Pose2d(-42, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(-30, -30), Math.toRadians(0))
                .setReversed(true)
                .splineTo(new Vector2d(-30, -55), Math.toRadians(0))
                .splineTo(new Vector2d(0, -55), Math.toRadians(0))
                .splineTo(new Vector2d(40, -55), Math.toRadians(0))
                .splineTo(new Vector2d(45, -40), Math.toRadians(180))
                .setReversed(true)
                .splineTo(new Vector2d(55, -64), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity audienceRedCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        audienceRedCenter.runAction(audienceRedCenter.getDrive().actionBuilder(new Pose2d(-42, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(-35, -30), Math.toRadians(90))
                .setReversed(true)
                .splineTo(new Vector2d(-55, -35), Math.toRadians(90))
                .splineTo(new Vector2d(-45, -10), Math.toRadians(0))
                .splineTo(new Vector2d(35, -10), Math.toRadians(0))
                .splineTo(new Vector2d(45, -40), Math.toRadians(180))
                .build());

        RoadRunnerBotEntity audienceRedCenterWall = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        audienceRedCenterWall.runAction(audienceRedCenterWall.getDrive().actionBuilder(new Pose2d(-42, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(-35, -33), Math.toRadians(90))
                .setReversed(true)
                .splineTo(new Vector2d(-30, -55), Math.toRadians(0))
                .splineTo(new Vector2d(0, -55), Math.toRadians(0))
                .splineTo(new Vector2d(28, -55), Math.toRadians(0))
                .splineTo(new Vector2d(30, -40), Math.toRadians(180))
                .setReversed(true)
                .splineTo(new Vector2d(54, -14), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity audienceRedLeft = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40,40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        audienceRedLeft.runAction(audienceRedLeft.getDrive().actionBuilder(new Pose2d(-42, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(-42, -30), Math.toRadians(180))
                .setReversed(true)
                .splineTo(new Vector2d(-30, -10), Math.toRadians(0))
                .splineTo(new Vector2d(30, -10), Math.toRadians(0))
                .splineTo(new Vector2d(40, -40), Math.toRadians(180))
                .build());

        RoadRunnerBotEntity audienceRedLeftWall = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40,40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        audienceRedLeftWall.runAction(audienceRedLeftWall.getDrive().actionBuilder(new Pose2d(-42, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(-42, -30), Math.toRadians(180))
                .setReversed(true)
                .splineTo(new Vector2d(-30, -55), Math.toRadians(0))
                .splineTo(new Vector2d(0, -55), Math.toRadians(0))
                .splineTo(new Vector2d(28, -55), Math.toRadians(0))
                .splineTo(new Vector2d(30, -40), Math.toRadians(180))
                .build());

        RoadRunnerBotEntity audienceBlueLeft = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        audienceBlueLeft.runAction(audienceBlueLeft.getDrive().actionBuilder(new Pose2d(-32, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(-30, 30), Math.toRadians(0))
                .setReversed(true)
                .splineTo(new Vector2d(-30, 10), Math.toRadians(0))
                .splineTo(new Vector2d(35, 10), Math.toRadians(0))
                .splineTo(new Vector2d(45, 40), Math.toRadians(180))
                .setReversed(true)
                .splineTo(new Vector2d(54, 60), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity audienceBlueLeftWall = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        audienceBlueLeftWall.runAction(audienceBlueLeftWall.getDrive().actionBuilder(new Pose2d(-32, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(-30, 30), Math.toRadians(0))
                .setReversed(true)
                .splineTo(new Vector2d(-30, 57), Math.toRadians(0))
                .splineTo(new Vector2d(35, 57), Math.toRadians(0))
                .splineTo(new Vector2d(45, 40), Math.toRadians(180))
                .build());

        RoadRunnerBotEntity audienceBlueCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        audienceBlueCenter.runAction(audienceBlueCenter.getDrive().actionBuilder(new Pose2d(-32, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(-35, 33), Math.toRadians(270))
                .setReversed(true)
                .splineTo(new Vector2d(-55, 35), Math.toRadians(270))
                .splineTo(new Vector2d(-45, 10), Math.toRadians(0))
                .splineTo(new Vector2d(35, 10), Math.toRadians(0))
                .splineTo(new Vector2d(45, 40), Math.toRadians(180))
                .setReversed(true)
                .splineTo(new Vector2d(54, 14), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity audienceBlueCenterWall = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        audienceBlueCenterWall.runAction(audienceBlueCenterWall.getDrive().actionBuilder(new Pose2d(-32, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(-35, 33), Math.toRadians(270))
                .setReversed(true)
                .splineTo(new Vector2d(-30, 57), Math.toRadians(0))
                .splineTo(new Vector2d(35, 57), Math.toRadians(0))
                .splineTo(new Vector2d(45, 40), Math.toRadians(180))
                .build());

        RoadRunnerBotEntity audienceBlueRight = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        audienceBlueRight.runAction(audienceBlueRight.getDrive().actionBuilder(new Pose2d(-32, 62.5, Math.toRadians(270)))
                .splineTo(new Vector2d(-50, 36), Math.toRadians(270))

//                .splineTo(new Vector2d(-40, 30), Math.toRadians(0))
                .setReversed(true)
                .splineTo(new Vector2d(-55, 35), Math.toRadians(270))
                .splineTo(new Vector2d(-45, 10), Math.toRadians(0))
                .splineTo(new Vector2d(35, 10), Math.toRadians(0))
                .splineTo(new Vector2d(45, 40), Math.toRadians(180))
                .setReversed(true)
                .splineTo(new Vector2d(54, 14), Math.toRadians(0))
                .build());
        RoadRunnerBotEntity audienceBlueRightWall = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        audienceBlueRightWall.runAction(audienceBlueRightWall.getDrive().actionBuilder(new Pose2d(-32, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(-40, 30), Math.toRadians(180))
                .setReversed(true)
                .splineTo(new Vector2d(-30, 57), Math.toRadians(0))
                .splineTo(new Vector2d(35, 57), Math.toRadians(0))
                .splineTo(new Vector2d(45, 40), Math.toRadians(180))
                .build());

        RoadRunnerBotEntity backstageBlueLeft = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        backstageBlueLeft.runAction(backstageBlueLeft.getDrive().actionBuilder(new Pose2d(8, 62.5, Math.toRadians(270)))
                .splineTo(new Vector2d(22, 34), Math.toRadians(270))
                .setReversed(true)
                .splineTo(new Vector2d(36,42), Math.toRadians(0))
                .turn(Math.toRadians(180))
                .build());

        RoadRunnerBotEntity backstageBlueCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        backstageBlueCenter.runAction(backstageBlueCenter.getDrive().actionBuilder(new Pose2d(8, 62.5, Math.toRadians(270)))
                .splineTo(new Vector2d(14, 29), Math.toRadians(270))
                .setReversed(true)
                .splineTo(new Vector2d(36,36), Math.toRadians(0))
                .turn(Math.toRadians(180))
                .build());

        RoadRunnerBotEntity backstageBlueRight = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        backstageBlueRight.runAction(backstageBlueRight.getDrive().actionBuilder(new Pose2d(8, 62.5, Math.toRadians(270)))
                .splineTo(new Vector2d(2, 34), Math.toRadians(180))
                .setReversed(true)
                .splineTo(new Vector2d(36,28), Math.toRadians(0))
                .turn(Math.toRadians(180))
                .build());

        RoadRunnerBotEntity backstageRedLeft = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        backstageRedLeft.runAction(backstageRedLeft.getDrive().actionBuilder(new Pose2d(9, -62.5, Math.toRadians(90)))
                .splineTo(new Vector2d(10, -34), Math.toRadians(180))
                .setReversed(true)
                .splineTo(new Vector2d(44,-30), Math.toRadians(0))
                .turn(Math.toRadians(180))
                .build());

        RoadRunnerBotEntity backstageRedCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        backstageRedCenter.runAction(backstageRedCenter.getDrive().actionBuilder(new Pose2d(9, -62.5, Math.toRadians(90)))
                .splineTo(new Vector2d(14, 29), Math.toRadians(270))
                .setReversed(true)
                .splineTo(new Vector2d(44,-38), Math.toRadians(0))
                .turn(Math.toRadians(180))
                .build());

        RoadRunnerBotEntity backstageRedRight = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        backstageRedRight.runAction(backstageRedRight.getDrive().actionBuilder(new Pose2d(9, -62.5, Math.toRadians(90)))
                .splineTo(new Vector2d(20, -34), Math.toRadians(90))
                .setReversed(true)
                .splineTo(new Vector2d(44,-45), Math.toRadians(0))
                .turn(Math.toRadians(180))
                .build());

        RoadRunnerBotEntity backstageDoublePickup = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(50, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        backstageDoublePickup.runAction(backstageDoublePickup.getDrive().actionBuilder(new Pose2d(12.5, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(48, -35), Math.toRadians(0))
                .setReversed(true)
                .splineTo(new Vector2d(20, -42), Math.toRadians(-135))
                .splineTo(new Vector2d(5, -58), Math.toRadians(180))
                .splineTo(new Vector2d(-25, -58), Math.toRadians(180))
                .splineTo(new Vector2d(-55, -35), Math.toRadians(0))
                .lineToX(-63)
                .lineToX(-55)
                .splineTo(new Vector2d(-40, -58), Math.toRadians(0))
                .splineTo(new Vector2d(35, -58), Math.toRadians(0))
                .splineTo(new Vector2d(48, -35), Math.toRadians(180))
                .splineTo(new Vector2d(20, -42), Math.toRadians(-135))
                .splineTo(new Vector2d(5, -58), Math.toRadians(180))
                .splineTo(new Vector2d(-25, -58), Math.toRadians(180))
                .splineTo(new Vector2d(-55, -35), Math.toRadians(0))
                .lineToX(-63)
                .lineToX(-55)
                .splineTo(new Vector2d(-40, -58), Math.toRadians(0))
                .splineTo(new Vector2d(35, -58), Math.toRadians(0))
                .splineTo(new Vector2d(48, -35), Math.toRadians(180))
                .build());

        RoadRunnerBotEntity backstageDoublePickupSpline = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(50, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        backstageDoublePickupSpline.runAction(backstageDoublePickupSpline.getDrive().actionBuilder(new Pose2d(12.5, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(14, -29), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(48, -35, Math.toRadians(0)), Math.toRadians(0))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(0, -58, Math.toRadians(-180)), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(-30, -58), Math.toRadians(180))
                .splineTo(new Vector2d(-60,-36), Math.toRadians(180))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -58), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(0, -58), Math.toRadians(0))
                .splineTo(new Vector2d(36, -58), Math.toRadians(0))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(0, -58, Math.toRadians(-180)), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(-30, -58), Math.toRadians(180))
                .splineTo(new Vector2d(-60,-36), Math.toRadians(180))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -58), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(0, -58), Math.toRadians(0))
                .splineTo(new Vector2d(36, -58), Math.toRadians(0))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(0, -58, Math.toRadians(-180)), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(-30, -58), Math.toRadians(180))
                .splineTo(new Vector2d(-60,-36), Math.toRadians(180))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -58), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(0, -58), Math.toRadians(0))
                .splineTo(new Vector2d(36, -58), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity backstageDoublePickupSplineCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        backstageDoublePickupSplineCenter.runAction(backstageDoublePickupSplineCenter.getDrive().actionBuilder(new Pose2d(12.5, -64, Math.toRadians(90)))
                        .splineTo(new Vector2d(14, -29), Math.toRadians(90))
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(48, -35, Math.toRadians(0)), Math.toRadians(0))
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(0, -12, Math.toRadians(-180)), Math.toRadians(180))
                        .setReversed(false)
                        .splineTo(new Vector2d(-30, -12), Math.toRadians(180))
                        .splineTo(new Vector2d(-60,-12), Math.toRadians(180))
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, -12), Math.toRadians(0)), Math.toRadians(0))
                        .splineTo(new Vector2d(0, -12), Math.toRadians(0))
                        .splineTo(new Vector2d(30, -12), Math.toRadians(0))
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(0, -12, Math.toRadians(-180)), Math.toRadians(180))
                        .setReversed(false)
                        .splineTo(new Vector2d(-30, -12), Math.toRadians(180))
                        .splineTo(new Vector2d(-60,-12), Math.toRadians(180))
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, -12), Math.toRadians(0)), Math.toRadians(0))
                        .splineTo(new Vector2d(0, -12), Math.toRadians(0))
                        .splineTo(new Vector2d(30, -12), Math.toRadians(0))
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(0, -12, Math.toRadians(-180)), Math.toRadians(180))
                        .setReversed(false)
                        .splineTo(new Vector2d(-30, -12), Math.toRadians(180))
                        .splineTo(new Vector2d(-60,-12), Math.toRadians(180))
                        .setReversed(true)
                        .splineToSplineHeading(new Pose2d(new Vector2d(-35, -12), Math.toRadians(0)), Math.toRadians(0))
                        .splineTo(new Vector2d(0, -12), Math.toRadians(0))
                        .splineTo(new Vector2d(30, -12), Math.toRadians(0))
                        .build());

        RoadRunnerBotEntity backstageDoublePickupSplineMTLeft = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(50, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        backstageDoublePickupSplineMTLeft.runAction(backstageDoublePickupSpline.getDrive().actionBuilder(new Pose2d(12.5, -64, Math.toRadians(90)))
                .splineToSplineHeading(new Pose2d(12.5, -18, Math.toRadians(270)), Math.toRadians(90))
                .waitSeconds(0.5)

                // Head to Backboard
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(48, -35, Math.toRadians(0)), Math.toRadians(0))
                .waitSeconds(0.5)

                // Head to Stacks VIA A-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(0, -58, Math.toRadians(-180)), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(-30, -58), Math.toRadians(180))
                .splineTo(new Vector2d(-60,-36), Math.toRadians(180))
                .waitSeconds(0.5)

                // Return to Backboard
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -58), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(0, -58), Math.toRadians(0))
                .splineTo(new Vector2d(48, -35), Math.toRadians(0))
                .waitSeconds(0.5)

                // Head to Stacks VIA A-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(0, -58, Math.toRadians(-180)), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(-30, -58), Math.toRadians(180))
                .splineTo(new Vector2d(-60,-36), Math.toRadians(180))
                .waitSeconds(0.5)

                // Return to Backboard
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -58), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(0, -58), Math.toRadians(0))
                .splineTo(new Vector2d(48, -35), Math.toRadians(0))
                .waitSeconds(0.5)

                // Park
                .setReversed(true)
                .splineTo(new Vector2d(53, -60), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity backstageDoublePickupSplineMTCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(50, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        backstageDoublePickupSplineMTCenter.runAction(backstageDoublePickupSplineMTCenter.getDrive().actionBuilder(new Pose2d(12.5, -64, Math.toRadians(90)))
                .splineToSplineHeading(new Pose2d(12.5, -18, Math.toRadians(270)), Math.toRadians(90))
                .waitSeconds(0.5)

                // Head to Backboard
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(48, -35, Math.toRadians(0)), Math.toRadians(0))
                .waitSeconds(0.5)

                // Head to Stacks VIA A-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(0, -58, Math.toRadians(-180)), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(-30, -58), Math.toRadians(180))
                .splineTo(new Vector2d(-60,-36), Math.toRadians(180))
                .waitSeconds(0.5)

                // Return to Backboard
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -58), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(0, -58), Math.toRadians(0))
                .splineTo(new Vector2d(48, -35), Math.toRadians(0))
                .waitSeconds(0.5)

                // Head to Stacks VIA A-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(0, -58, Math.toRadians(-180)), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(-30, -58), Math.toRadians(180))
                .splineTo(new Vector2d(-60,-36), Math.toRadians(180))
                .waitSeconds(0.5)

                // Return to Backboard
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -58), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(0, -58), Math.toRadians(0))
                .splineTo(new Vector2d(48, -35), Math.toRadians(0))
                .waitSeconds(0.5)

                // Park
                .setReversed(true)
                .splineTo(new Vector2d(53, -60), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity backstageDoublePickupSplineMTRight = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(50, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        backstageDoublePickupSplineMTRight.runAction(backstageDoublePickupSplineMTRight.getDrive().actionBuilder(new Pose2d(12.5, -64, Math.toRadians(90)))
                // Drop Purple
                .splineToSplineHeading(new Pose2d(24, -36, Math.toRadians(90)), Math.toRadians(90))
                .waitSeconds(0.5)

                // Head to Backboard
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(48, -35, Math.toRadians(0)), Math.toRadians(0))
                .waitSeconds(0.5)

                // Head to Stacks VIA A-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(0, -58, Math.toRadians(-180)), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(-30, -58), Math.toRadians(180))
                .splineTo(new Vector2d(-60,-36), Math.toRadians(180))
                .waitSeconds(0.5)

                // Return to Backboard
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -58), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(0, -58), Math.toRadians(0))
                .splineTo(new Vector2d(48, -35), Math.toRadians(0))
                .waitSeconds(0.5)

                // Head to Stacks VIA A-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(0, -58, Math.toRadians(-180)), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(-30, -58), Math.toRadians(180))
                .splineTo(new Vector2d(-60,-36), Math.toRadians(180))
                .waitSeconds(0.5)

                // Return to Backboard
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -58), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(0, -58), Math.toRadians(0))
                .splineTo(new Vector2d(48, -35), Math.toRadians(0))
                .waitSeconds(0.5)

                // Park
                .setReversed(true)
                .splineTo(new Vector2d(53, -60), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity audienceOnePickup = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(50, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        audienceOnePickup.runAction(audienceOnePickup.getDrive().actionBuilder(new Pose2d(-32, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(-30, 25), Math.toRadians(0))
                .splineTo(new Vector2d(-55, 12), Math.toRadians(180))
                .lineToX(-63)
                .setReversed(true)
                .splineTo(new Vector2d(35, 12), Math.toRadians(0))
                .splineTo(new Vector2d(45, 40), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(35, 12), Math.toRadians(180))
                .splineTo(new Vector2d(-63, 12), Math.toRadians(180))
                .setReversed(true)
                .splineTo(new Vector2d(35, 12), Math.toRadians(0))
                .splineTo(new Vector2d(45, 40), Math.toRadians(180))
                .setReversed(true)
                .splineTo(new Vector2d(54, 60), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity backstageCRowMTCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(50, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        backstageCRowMTCenter.runAction(backstageCRowMTCenter.getDrive().actionBuilder(new Pose2d(12.5, -64, Math.toRadians(90)))
                .splineToSplineHeading(new Pose2d(12.5, -20, Math.toRadians(270)), Math.toRadians(90))
                        .lineToY(-17)
                .waitSeconds(0.5)

                // Head to Backboard
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(48, -35, Math.toRadians(0)), Math.toRadians(0))
                .waitSeconds(0.5)

                // Head to Stacks VIA A-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(12, -12, Math.toRadians(-180)), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(-58, -12), Math.toRadians(180))
                .waitSeconds(0.5)

                // Return to Backboard
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-12, -12), Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineTo(new Vector2d(48, -12), Math.toRadians(0))
//                .splineToSplineHeading(new Pose2d(48, -35, Math.toRadians(0)), Math.toRadians(0))
                .waitSeconds(0.5)

                // Head to Stacks VIA A-Row
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(12, -12, Math.toRadians(-180)), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(-58, -12), Math.toRadians(180))
                .waitSeconds(0.5)

                // Return to Backboard
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-12, -12), Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineTo(new Vector2d(48, -12), Math.toRadians(0))
//                .splineToSplineHeading(new Pose2d(48, -35, Math.toRadians(0)), Math.toRadians(0))
                .waitSeconds(0.5)

                // Park
                //.setReversed(true)
                //.splineTo(new Vector2d(53, -60), Math.toRadians(0))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
  //              .addEntity(audienceCycle)
//                .addEntity(audienceCycleC)
                .addEntity(audienceCycleCBlue)
//                .addEntity(audienceBlueLeft)
//                .addEntity(audienceBlueLeftWall)
//                .addEntity(audienceBlueCenter)
//                .addEntity(audienceBlueCenterWall)
//                .addEntity(audienceBlueRight)
//                .addEntity(audienceBlueRightWall)
//                .addEntity(audienceRedLeft)
//                .addEntity(audienceRedLeftWall)
//                .addEntity(audienceRedCenterWall)
//                .addEntity(audienceRedRight)
//                .addEntity(audienceRedRightWall)
//                .addEntity(backstageBlueLeft)
//                .addEntity(backstageBlueCenter)
//                .addEntity(backstageBlueRight)
//                .addEntity(backstageRedLeft)
//                .addEntity(backstageRedCenter)
//                .addEntity(backstageRedRight)
//                .addEntity(backstageDoublePickup)
//                .addEntity(audienceOnePickup)
//                .addEntity(backstageDoublePickupSplineMTLeft)
//                .addEntity(backstageDoublePickupSplineMTCenter)
//                .addEntity(backstageDoublePickupSplineMTRight)
//                .addEntity(backstageDoublePickupSplineMTCenter)
//                .addEntity(backstageCRowMTCenter)
//                .addEntity(backstageDoublePickupSpline)
//                .addEntity(backstageDoublePickupMT)
//                .addEntity(backstageDoublePickupSpline)
//                .addEntity(backstageDoublePickupSplineCenter)
                .start();
    }
}