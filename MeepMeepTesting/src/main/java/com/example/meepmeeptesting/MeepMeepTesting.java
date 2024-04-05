package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.InstantAction;
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

        // blue maps

        RoadRunnerBotEntity blueAudience = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueAudience.runAction(blueAudience.getDrive().actionBuilder(new Pose2d(-31, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(-38, 35.5), Math.toRadians(270))
                .build());

        // blue audience center

        RoadRunnerBotEntity blueAudienceCenterParkCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueAudienceCenterParkCenter.runAction(blueAudienceCenterParkCenter.getDrive().actionBuilder(new Pose2d(-31, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(-38, 35.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-52, 39, Math.toRadians(180)), Math.toRadians(180))
                .lineToX(-55)
                .turnTo(Math.toRadians(270))
                .splineToSplineHeading(new Pose2d(new Vector2d(-30, 12), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(11, 12), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(50,35), Math.toRadians(0))
                .lineToX(51.5)
                .lineToX(44)
                .setReversed(true)
                .splineTo(new Vector2d(58, 14), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity blueAudienceCenterParkCorner = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueAudienceCenterParkCorner.runAction(blueAudienceCenterParkCorner.getDrive().actionBuilder(new Pose2d(-31, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(-38, 35.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-52, 39, Math.toRadians(180)), Math.toRadians(180))
                .lineToX(-55)
                .turnTo(Math.toRadians(270))
                .splineToSplineHeading(new Pose2d(new Vector2d(-30, 12), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(11, 12), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(50,35), Math.toRadians(0))
                .lineToX(51.5)
                .lineToX(44)
                .setReversed(true)
                .splineTo(new Vector2d(58, 64), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity blueAudienceCenterTwoWhitesOnBackDrop = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueAudienceCenterTwoWhitesOnBackDrop.runAction(blueAudienceCenterTwoWhitesOnBackDrop.getDrive().actionBuilder(new Pose2d(-31, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(-38, 35.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-52, 39, Math.toRadians(180)), Math.toRadians(180))
                .lineToX(-55)
                .turnTo(Math.toRadians(270))
                .splineToSplineHeading(new Pose2d(new Vector2d(-30, 12), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(11, 12), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(50,35), Math.toRadians(0))
                .lineToX(51.5)
                .lineToX(46)
                .splineToSplineHeading(new Pose2d(11, 12, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-48, 17), Math.toRadians(180))
                .lineToX(-58)
                .lineToX(-50)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-20, 12), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(10, 12), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(50,35), Math.toRadians(0))
                .lineToX(40)
                .build());

        RoadRunnerBotEntity blueAudienceCenterTwoWhitesOnBackStage = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueAudienceCenterTwoWhitesOnBackStage.runAction(blueAudienceCenterTwoWhitesOnBackStage.getDrive().actionBuilder(new Pose2d(-31, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(-38, 35.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-52, 39, Math.toRadians(180)), Math.toRadians(180))
                .lineToX(-55)
                .turnTo(Math.toRadians(270))
                .splineToSplineHeading(new Pose2d(new Vector2d(-30, 12), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(11, 12), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(50,35), Math.toRadians(0))
                .lineToX(51.5)
                .lineToX(46)
                .splineToSplineHeading(new Pose2d(11, 12, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-48, 17), Math.toRadians(180))
                .lineToX(-58)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, 12), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(54, 14), Math.toRadians(0))
                .lineToX(50)
                .build());

        // blue audience wall

        RoadRunnerBotEntity blueAudienceWallParkCorner = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueAudienceWallParkCorner.runAction(blueAudienceWallParkCorner.getDrive().actionBuilder(new Pose2d(-31, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(-38, 34.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-52, 39, Math.toRadians(180)), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-50)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-30, 59), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(0, 58), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(51.5,35), Math.toRadians(0))
                .lineToX(51.5)
                .lineToX(44)
                .setReversed(true)
                .splineTo(new Vector2d(58, 64), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity blueAudienceWallParkCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueAudienceWallParkCenter.runAction(blueAudienceWallParkCenter.getDrive().actionBuilder(new Pose2d(-31, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(-38, 34.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-52, 39, Math.toRadians(180)), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-50)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-30, 59), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(0, 58), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(51.5,35), Math.toRadians(0))
                .lineToX(51.5)
                .lineToX(44)
                .setReversed(true)
                .splineTo(new Vector2d(58, 14), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity blueAudienceWallBackdropCenterWithTwoWhites = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueAudienceWallBackdropCenterWithTwoWhites.runAction(blueAudienceWallBackdropCenterWithTwoWhites.getDrive().actionBuilder(new Pose2d(-31, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(-38, 34.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-52, 39, Math.toRadians(180)), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-50)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-30, 59), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(0, 58), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(51.5,35), Math.toRadians(0))
                .lineToX(51.5)
                .lineToX(44)
                .splineToSplineHeading(new Pose2d(0, 59, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30, 59), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, 37), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-30, 59), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(0, 59), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(51.5, 39), Math.toRadians(0))
                .lineToX(44)
                .build());

        RoadRunnerBotEntity blueAudienceWallParkCornerWithTwoWhites = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueAudienceWallParkCornerWithTwoWhites.runAction(blueAudienceWallParkCornerWithTwoWhites.getDrive().actionBuilder(new Pose2d(-31, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(-38, 34.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-52, 39, Math.toRadians(180)), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-50)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-30, 59), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(0, 58), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(51.5,35), Math.toRadians(0))
                .lineToX(51.5)
                .lineToX(44)
                .splineToSplineHeading(new Pose2d(0, 59, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30, 59), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, 37), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, 59), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(54, 64), Math.toRadians(0))
                .lineToX(50)
                .build());

        // blue backstage

        RoadRunnerBotEntity blueBackstageParkCorner = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueBackstageParkCorner.runAction(blueBackstageParkCorner.getDrive().actionBuilder(new Pose2d(17.5, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(13, 35.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineTo(new Vector2d(58, 64), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity blueBackstageParkCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueBackstageParkCenter.runAction(blueBackstageParkCenter.getDrive().actionBuilder(new Pose2d(17.5, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(13, 35.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineTo(new Vector2d(58, 14), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity blueBackstageCenterTwoWhitesInCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueBackstageCenterTwoWhitesInCenter.runAction(blueBackstageCenterTwoWhitesInCenter.getDrive().actionBuilder(new Pose2d(17.5, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(13, 35.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(20, 12, Math.toRadians(180)), Math.toRadians(180))
                .splineTo(new Vector2d(-48, 15), Math.toRadians(180))
                .lineToX(-59)
                .lineToX(-44)
                .splineToSplineHeading(new Pose2d(-11, 11, Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineTo(new Vector2d(40, 12), Math.toRadians(0))
                .splineTo(new Vector2d(50, 12), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity blueBackstageCenterFourWhitesInCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueBackstageCenterFourWhitesInCenter.runAction(blueBackstageCenterFourWhitesInCenter.getDrive().actionBuilder(new Pose2d(17.5, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(13, 35.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(20, 12, Math.toRadians(180)), Math.toRadians(180))
                .splineTo(new Vector2d(-48, 15), Math.toRadians(180))
                .lineToX(-59)
                .lineToX(-44)
                .splineToSplineHeading(new Pose2d(-11, 11, Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineTo(new Vector2d(40, 12), Math.toRadians(0))
                .splineTo(new Vector2d(50, 12), Math.toRadians(0))
                .lineToX(50)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(24, 11, Math.toRadians(180)), Math.toRadians(180))
                //.setReversed(false)
                .splineTo(new Vector2d(-48, 12.5), Math.toRadians(180))
                .lineToX(-59)
                .lineToX(-44)
                .splineToSplineHeading(new Pose2d(-12, 11, Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineTo(new Vector2d(40, 14), Math.toRadians(0))
                .splineTo(new Vector2d(50, 14), Math.toRadians(45))
                .build());

        RoadRunnerBotEntity blueBackstageCenterTwoWhitesOnBackdropParkInCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueBackstageCenterTwoWhitesOnBackdropParkInCenter.runAction(blueBackstageCenterTwoWhitesOnBackdropParkInCenter.getDrive().actionBuilder(new Pose2d(17.5, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(13, 35.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(20, 12, Math.toRadians(180)), Math.toRadians(180))
                .splineTo(new Vector2d(-48, 15), Math.toRadians(180))
                .lineToX(-59)
                .lineToX(-44)
                .splineToSplineHeading(new Pose2d(-11, 11, Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineToConstantHeading(new Vector2d(25,11), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(46.5,35), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineTo(new Vector2d(58, 14), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity blueBackstageCenterTwoWhitesOnBackdropParkInCorner = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueBackstageCenterTwoWhitesOnBackdropParkInCorner.runAction(blueBackstageCenterTwoWhitesOnBackdropParkInCorner.getDrive().actionBuilder(new Pose2d(17.5, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(13, 35.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(20, 12, Math.toRadians(180)), Math.toRadians(180))
                .splineTo(new Vector2d(-48, 15), Math.toRadians(180))
                .lineToX(-59)
                .lineToX(-44)
                .splineToSplineHeading(new Pose2d(-11, 11, Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineToConstantHeading(new Vector2d(25,11), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(46.5,35), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineTo(new Vector2d(58, 54), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity blueBackstageCenterFourWhitesOnBackdrop = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueBackstageCenterFourWhitesOnBackdrop.runAction(blueBackstageCenterFourWhitesOnBackdrop.getDrive().actionBuilder(new Pose2d(17.5, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(13, 35.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(20, 12, Math.toRadians(180)), Math.toRadians(180))
                .splineTo(new Vector2d(-48, 15), Math.toRadians(180))
                .lineToX(-59)
                .lineToX(-44)
                .splineToSplineHeading(new Pose2d(-11, 11, Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineToConstantHeading(new Vector2d(25,11), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(46.5,35), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(20, 12, Math.toRadians(180)), Math.toRadians(180))
                .splineTo(new Vector2d(-48, 15), Math.toRadians(180))
                .lineToX(-59)
                .lineToX(-44)
                .splineToSplineHeading(new Pose2d(-11, 11, Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineToConstantHeading(new Vector2d(25,11), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(46.5,35), Math.toRadians(0))
                .lineToX(44 )
                .build());

        RoadRunnerBotEntity blueBackstageWallTwoWhitesOnBackstage = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueBackstageWallTwoWhitesOnBackstage.runAction(blueBackstageWallTwoWhitesOnBackstage.getDrive().actionBuilder(new Pose2d(17.5, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(13, 35.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .splineToSplineHeading(new Pose2d(10, 59, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30, 59), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, 37), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, 59), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(54, 64), Math.toRadians(0))
                .lineToX(50)
                .build());

        RoadRunnerBotEntity blueBackstageWallFourWhitesOnBackstage = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueBackstageWallFourWhitesOnBackstage.runAction(blueBackstageWallFourWhitesOnBackstage.getDrive().actionBuilder(new Pose2d(17.5, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(13, 35.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .splineToSplineHeading(new Pose2d(10, 59, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30, 59), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, 37), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, 59), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(54, 64), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(10, 59, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30, 59), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, 37), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, 59), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(54, 64), Math.toRadians(0))
                .lineToX(50)
                .build());

        RoadRunnerBotEntity blueBackstageWallTwoWhitesOnBackdropParkInCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueBackstageWallTwoWhitesOnBackdropParkInCenter.runAction(blueBackstageWallTwoWhitesOnBackdropParkInCenter.getDrive().actionBuilder(new Pose2d(17.5, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(13, 35.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .splineToSplineHeading(new Pose2d(10, 59, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30, 59), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, 37), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, 59), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(15, 59), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(46.5, 35), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineTo(new Vector2d(58, 14), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity blueBackstageWallTwoWhitesOnBackdropParkInCorner = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueBackstageWallTwoWhitesOnBackdropParkInCorner.runAction(blueBackstageWallTwoWhitesOnBackdropParkInCorner.getDrive().actionBuilder(new Pose2d(17.5, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(13, 35.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .splineToSplineHeading(new Pose2d(10, 59, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30, 59), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, 37), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, 59), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(15, 59), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(46.5, 35), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineTo(new Vector2d(58, 64), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity blueBackstageWallFourWhitesOnBackdrop = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        blueBackstageWallFourWhitesOnBackdrop.runAction(blueBackstageWallFourWhitesOnBackdrop.getDrive().actionBuilder(new Pose2d(17.5, 64, Math.toRadians(270)))
                .splineTo(new Vector2d(13, 35.5), Math.toRadians(270))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .splineToSplineHeading(new Pose2d(10, 59, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30, 59), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, 37), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, 59), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(15, 59), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(46.5, 35), Math.toRadians(0))
                .lineToX(44 )
                .splineToSplineHeading(new Pose2d(10, 59, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30, 59), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, 37), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, 59), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(15, 59), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(46.5, 35), Math.toRadians(0))
                .lineToX(44 )
                .build());

        // red maps


        RoadRunnerBotEntity redAudience = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redAudience.runAction(redAudience.getDrive().actionBuilder(new Pose2d(-42, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(-38, -36), Math.toRadians(90))
                .build());

        // red audience center

        RoadRunnerBotEntity redAudienceCenterParkCorner = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redAudienceCenterParkCorner.runAction(redAudienceCenterParkCorner.getDrive().actionBuilder(new Pose2d(-42, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(-38, -36), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-35, -11, Math.toRadians(180)), Math.toRadians(90))
                .setReversed(true)
                .lineToX(-55)
                .lineToX(-48)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -11), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(10, -11), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(49,-38), Math.toRadians(0))
                .lineToX(44)
                .setReversed(true)
                .splineTo(new Vector2d(58, -64), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity redAudienceCenterParkCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redAudienceCenterParkCenter.runAction(redAudienceCenterParkCenter.getDrive().actionBuilder(new Pose2d(-42, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(-38, -36), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-35, -11, Math.toRadians(180)), Math.toRadians(90))
                .setReversed(true)
                .lineToX(-55)
                .lineToX(-48)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -11), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(10, -11), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(49,-38), Math.toRadians(0))
                .lineToX(44)
                .setReversed(true)
                .splineTo(new Vector2d(58, -14), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity redAudienceCenterTwoWhitesOnBackDrop = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redAudienceCenterTwoWhitesOnBackDrop.runAction(redAudienceCenterTwoWhitesOnBackDrop.getDrive().actionBuilder(new Pose2d(-42, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(-38, -36), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-35, -11, Math.toRadians(180)), Math.toRadians(90))
                .setReversed(true)
                .lineToX(-55)
                .lineToX(-48)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -11), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(10, -11), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(49,-38), Math.toRadians(0))
                .lineToX(44)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(10, -11.5, Math.toRadians(-180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, -11.5), Math.toRadians(180))
                .lineToX(-59.5)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -11), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(10, -11), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(49,-38), Math.toRadians(0))
                .lineToX(43)
                .build());

        RoadRunnerBotEntity redAudienceCenterTwoWhitesOnBackStage = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redAudienceCenterTwoWhitesOnBackStage.runAction(redAudienceCenterTwoWhitesOnBackStage.getDrive().actionBuilder(new Pose2d(-42, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(-38, -36), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-35, -11, Math.toRadians(180)), Math.toRadians(90))
                .setReversed(true)
                .lineToX(-55)
                .lineToX(-48)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -11), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(10, -11), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(49,-38), Math.toRadians(0))
                .lineToX(44)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(10, -11.5, Math.toRadians(-180)), Math.toRadians(180))
                .splineTo(new Vector2d(-52, -11.5), Math.toRadians(180))
                .lineToX(-59.5)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -11), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(54, -11), Math.toRadians(0))
                .lineToX(50)
                .build());


        // red audience wall

        RoadRunnerBotEntity redAudienceWallParkCorner = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redAudienceWallParkCorner.runAction(redAudienceWallParkCorner.getDrive().actionBuilder(new Pose2d(-42, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(-39, -31), Math.toRadians(135))
                .waitSeconds(0.5)
                .setReversed(true)
                .splineTo(new Vector2d(-33,-55), Math.toRadians(300))
                .setReversed(false)
                .splineToSplineHeading(new Pose2d(-48, -35.5, Math.toRadians(180)), Math.toRadians(180))
                .lineToX(-55.5)
                .waitSeconds(2)
                .lineToX(-54)

                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-30, -59), Math.toRadians(-5)), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(new Vector2d(-25, -59), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(10, -59), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(48, -35.5), Math.toRadians(0))
                .waitSeconds(2)

                // back to stacks
                .lineToX(47)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(6, -59, Math.toRadians(185)), Math.toRadians(180))
                .splineToSplineHeading(new Pose2d(0, -59, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30, -59), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-55,  -35.5), Math.toRadians(180))
                                .waitSeconds(2)

//                .lineToX(-48)
//                .setReversed(true)
//                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -60), Math.toRadians(0)), Math.toRadians(0))
//                .splineToConstantHeading(new Vector2d(10, -60), Math.toRadians(0))
//                .splineToConstantHeading(new Vector2d(49,-38), Math.toRadians(0))
//                .lineToX(44)
//                .setReversed(true)
//                .splineTo(new Vector2d(58, -64), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity redAudienceWallParkCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redAudienceWallParkCenter.runAction(redAudienceWallParkCenter.getDrive().actionBuilder(new Pose2d(-42, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(-38, -36), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-52, -36, Math.toRadians(180)), Math.toRadians(180))
                .lineToX(-55.5)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -60), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(10, -60), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(49,-38), Math.toRadians(0))
                .lineToX(44)
                .setReversed(true)
                .splineTo(new Vector2d(58, -14), Math.toRadians(0))
                .build());


        RoadRunnerBotEntity redAudienceWallBackdropCenterWithTwoWhites = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redAudienceWallBackdropCenterWithTwoWhites.runAction(redAudienceWallBackdropCenterWithTwoWhites.getDrive().actionBuilder(new Pose2d(-42, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(-38, -36), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-52, -36, Math.toRadians(180)), Math.toRadians(180))
                .lineToX(-55.5)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -60), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(10, -60), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(49,-38), Math.toRadians(0))
                .lineToX(44)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(0, -60, Math.toRadians(-180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-40, -58), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, -33), Math.toRadians(180))
                .lineToX(-60)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -60), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(10, -60), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(49,-38), Math.toRadians(0))
                .lineToX(42)
                .build());

        RoadRunnerBotEntity redAudienceWallParkCornerWithTwoWhites = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redAudienceWallParkCornerWithTwoWhites.runAction(redAudienceWallParkCornerWithTwoWhites.getDrive().actionBuilder(new Pose2d(-42, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(-38, -36), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(-52, -36, Math.toRadians(180)), Math.toRadians(180))
                .lineToX(-55.5)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -60), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(10, -60), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(49,-38), Math.toRadians(0))
                .lineToX(44)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(0, -60, Math.toRadians(-180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-40, -58), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, -33), Math.toRadians(180))
                .lineToX(-60)
                .lineToX(-48)
                .setReversed(true)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -60), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(54, -64), Math.toRadians(0))
                .lineToX(50)
                .build());

        // red backstage

        RoadRunnerBotEntity redBackstageParkCorner = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redBackstageParkCorner.runAction(redBackstageParkCorner.getDrive().actionBuilder(new Pose2d(14.5, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(16.5, -35.5), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(48, -38, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(53.5)
                .lineToX(50)
                .setReversed(true)
                .splineTo(new Vector2d(58, -64), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity redBackstageParkCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redBackstageParkCenter.runAction(redBackstageParkCenter.getDrive().actionBuilder(new Pose2d(14.5, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(16.5, -35.5), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(48, -38, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(53.5)
                .lineToX(50)
                .setReversed(true)
                .splineTo(new Vector2d(58, -14), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity redBackstageCenterTwoWhitesInCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redBackstageCenterTwoWhitesInCenter.runAction(redBackstageCenterTwoWhitesInCenter.getDrive().actionBuilder(new Pose2d(14.5, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(16.5, -35.5), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(48, -38, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(53.5)
                .lineToX(50)
                .turnTo(Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(-24, -11, Math.toRadians(180)), Math.toRadians(180))
                .splineTo(new Vector2d(-44,-11), Math.toRadians(180))
                .lineToX(-51.25)
                .lineToX(-44)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-11, -12), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(40, -13), Math.toRadians(0))
                .splineTo(new Vector2d(60, -14), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity redBackstageCenterFourWhitesInCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redBackstageCenterFourWhitesInCenter.runAction(redBackstageCenterFourWhitesInCenter.getDrive().actionBuilder(new Pose2d(14.5, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(16.5, -35.5), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(48, -38, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(53.5)
                .lineToX(50)
                .turnTo(Math.toRadians(90))
                .splineToSplineHeading(new Pose2d(-24, -11, Math.toRadians(180)), Math.toRadians(180))
                .splineTo(new Vector2d(-44,-11), Math.toRadians(180))
                .lineToX(-51.25)
                .lineToX(-44)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-11, -12), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(40, -13), Math.toRadians(0))
                .splineTo(new Vector2d(60, -14), Math.toRadians(0))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(24, -12.5, Math.toRadians(-180)), Math.toRadians(180))
                .setReversed(false)
                .splineTo(new Vector2d(-44, -12.5), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-48)
                .splineToSplineHeading(new Pose2d(-12, -14, Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineTo(new Vector2d(40, -14), Math.toRadians(0))
                .splineTo(new Vector2d(56, -17), Math.toRadians(-45))
                .build());

        RoadRunnerBotEntity redBackstageCenterTwoWhitesOnBackdropParkInCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redBackstageCenterTwoWhitesOnBackdropParkInCenter.runAction(redBackstageCenterTwoWhitesOnBackdropParkInCenter.getDrive().actionBuilder(new Pose2d(17.5, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(13, -35.5), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,-35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(20, -12, Math.toRadians(180)), Math.toRadians(180))
                .splineTo(new Vector2d(-48, -15), Math.toRadians(180))
                .lineToX(-59)
                .lineToX(-44)
                .splineToSplineHeading(new Pose2d(-11, -11, Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineToConstantHeading(new Vector2d(25,-11), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(46.5,-35), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineTo(new Vector2d(58, -14), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity redBackstageCenterTwoWhitesOnBackdropParkInCorner = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redBackstageCenterTwoWhitesOnBackdropParkInCorner.runAction(redBackstageCenterTwoWhitesOnBackdropParkInCorner.getDrive().actionBuilder(new Pose2d(17.5, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(13, -35.5), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,-35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(20, -12, Math.toRadians(180)), Math.toRadians(180))
                .splineTo(new Vector2d(-48, -15), Math.toRadians(180))
                .lineToX(-59)
                .lineToX(-44)
                .splineToSplineHeading(new Pose2d(-11, -11, Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineToConstantHeading(new Vector2d(25,-11), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(46.5,-35), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineTo(new Vector2d(58, -54), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity redBackstageCenterFourWhitesOnBackdrop = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redBackstageCenterFourWhitesOnBackdrop.runAction(redBackstageCenterFourWhitesOnBackdrop.getDrive().actionBuilder(new Pose2d(17.5, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(13, -35.5), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,-35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(20, -12, Math.toRadians(180)), Math.toRadians(180))
                .splineTo(new Vector2d(-48, -15), Math.toRadians(180))
                .lineToX(-59)
                .lineToX(-44)
                .splineToSplineHeading(new Pose2d(-11, -11, Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineToConstantHeading(new Vector2d(25,-11), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(46.5,-35), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(20, -12, Math.toRadians(180)), Math.toRadians(180))
                .splineTo(new Vector2d(-48, -15), Math.toRadians(180))
                .lineToX(-59)
                .lineToX(-44)
                .splineToSplineHeading(new Pose2d(-11, -11, Math.toRadians(0)), Math.toRadians(0))
                .setReversed(false)
                .splineToConstantHeading(new Vector2d(25,-11), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(46.5,-35), Math.toRadians(0))
                .lineToX(44 )
                .build());

        RoadRunnerBotEntity redBackstageWallTwoWhitesOnBackstage = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redBackstageWallTwoWhitesOnBackstage.runAction(redBackstageWallTwoWhitesOnBackstage.getDrive().actionBuilder(new Pose2d(17.5, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(13, -35.5), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,-35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .splineToSplineHeading(new Pose2d(10, -59, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30, -59), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, -37), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -59), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(54, -64), Math.toRadians(0))
                .lineToX(50)
                .build());

        RoadRunnerBotEntity redBackstageWallFourWhitesOnBackstage = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redBackstageWallFourWhitesOnBackstage.runAction(redBackstageWallFourWhitesOnBackstage.getDrive().actionBuilder(new Pose2d(17.5, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(13, -35.5), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,-35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .splineToSplineHeading(new Pose2d(10, -59, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30, -59), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, -37), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -59), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(54, -64), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(10, -59, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30, -59), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, -37), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -59), Math.toRadians(0)), Math.toRadians(0))
                .splineTo(new Vector2d(54, -64), Math.toRadians(0))
                .lineToX(50)
                .build());

        RoadRunnerBotEntity redBackstageWallTwoWhitesOnBackdropParkInCenter = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redBackstageWallTwoWhitesOnBackdropParkInCenter.runAction(redBackstageWallTwoWhitesOnBackdropParkInCenter.getDrive().actionBuilder(new Pose2d(17.5, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(13, -35.5), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,-35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .splineToSplineHeading(new Pose2d(10, -59, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30, -59), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, -37), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -59), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(15, -59), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(46.5, -35), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineTo(new Vector2d(58, -14), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity redBackstageWallTwoWhitesOnBackdropParkInCorner = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redBackstageWallTwoWhitesOnBackdropParkInCorner.runAction(redBackstageWallTwoWhitesOnBackdropParkInCorner.getDrive().actionBuilder(new Pose2d(17.5, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(13, -35.5), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,-35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .splineToSplineHeading(new Pose2d(10, -59, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30, -59), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, -37), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -59), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(15, -59), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(46.5, -35), Math.toRadians(0))
                .lineToX(44 )
                .setReversed(true)
                .splineTo(new Vector2d(58, -64), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity redBackstageWallFourWhitesOnBackdrop = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 40, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        redBackstageWallFourWhitesOnBackdrop.runAction(redBackstageWallFourWhitesOnBackdrop.getDrive().actionBuilder(new Pose2d(17.5, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(13, -35.5), Math.toRadians(90))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(46.5,-35, Math.toRadians(0)), Math.toRadians(0))
                .lineToX(44 )
                .splineToSplineHeading(new Pose2d(10, -59, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30, -59), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, -37), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -59), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(15, -59), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(46.5, -35), Math.toRadians(0))
                .lineToX(44 )
                .splineToSplineHeading(new Pose2d(10, -59, Math.toRadians(180)), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-30, -59), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-52, -37), Math.toRadians(180))
                .lineToX(-55)
                .lineToX(-48)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(new Vector2d(-35, -59), Math.toRadians(0)), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(15, -59), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(46.5, -35), Math.toRadians(0))
                .lineToX(44 )
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                // blue
//                .addEntity(blueAudience)
//                // blue audience center
//                .addEntity(blueAudienceCenterParkCenter)
//                .addEntity(blueAudienceCenterParkCorner)
//                .addEntity(blueAudienceCenterTwoWhitesOnBackStage)
//                .addEntity(blueAudienceCenterTwoWhitesOnBackDrop) // dpad up
//                // blue audience wall
//                .addEntity(blueAudienceWallParkCorner)
//                .addEntity(blueAudienceWallParkCenter)
//                .addEntity(blueAudienceWallBackdropCenterWithTwoWhites) // dpad up
//                .addEntity(blueAudienceWallParkCornerWithTwoWhites) // dpad down
//                // blue backstage
//                .addEntity(blueBackstageParkCenter)
//                .addEntity(blueBackstageParkCorner)
//                // blue backstage center
//                .addEntity(blueBackstageCenterTwoWhitesInCenter)
//                .addEntity(blueBackstageCenterFourWhitesInCenter)
//                .addEntity(blueBackstageCenterTwoWhitesOnBackdropParkInCenter) // new
//                .addEntity(blueBackstageCenterTwoWhitesOnBackdropParkInCorner) // new
//                .addEntity(blueBackstageCenterFourWhitesOnBackdrop) // new
//                // blue backstage wall
//                .addEntity(blueBackstageWallTwoWhitesOnBackstage) // new
//                .addEntity(blueBackstageWallFourWhitesOnBackstage) // new
//                .addEntity(blueBackstageWallTwoWhitesOnBackdropParkInCenter) // new
//                .addEntity(blueBackstageWallTwoWhitesOnBackdropParkInCorner) // new
//                .addEntity(blueBackstageWallFourWhitesOnBackdrop) // new
                // red
                //.addEntity(redAudience)
                // red audience center
                //.addEntity(redAudienceCenterParkCenter)
//                .addEntity(redAudienceCenterParkCorner)
//                .addEntity(redAudienceCenterTwoWhitesOnBackStage) // dpad down
//                .addEntity(redAudienceCenterTwoWhitesOnBackDrop) // dpad up
//                // red audience wall
                .addEntity(redAudienceWallParkCorner)
//                .addEntity(redAudienceWallParkCenter)
//                .addEntity(redAudienceWallParkCornerWithTwoWhites) // dpad down
//                .addEntity(redAudienceWallBackdropCenterWithTwoWhites) // dpad up
//                // red backstage
//                .addEntity(redBackstageParkCenter)
//                .addEntity(redBackstageParkCorner)
//                // red backstage center
//                .addEntity(redBackstageCenterTwoWhitesInCenter)
//                .addEntity(redBackstageCenterFourWhitesInCenter)
//                .addEntity(redBackstageCenterTwoWhitesOnBackdropParkInCenter) // new
//                .addEntity(redBackstageCenterTwoWhitesOnBackdropParkInCorner) // new
//                .addEntity(redBackstageCenterFourWhitesOnBackdrop) // new
//                // red backstage wall
//                .addEntity(redBackstageWallTwoWhitesOnBackstage) // new
//                .addEntity(redBackstageWallFourWhitesOnBackstage) // new
//                .addEntity(redBackstageWallTwoWhitesOnBackdropParkInCenter) // new
//                .addEntity(redBackstageWallTwoWhitesOnBackdropParkInCorner) // new
//                .addEntity(redBackstageWallFourWhitesOnBackdrop) // new
                .start();
    }
}