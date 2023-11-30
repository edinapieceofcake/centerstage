package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
/*
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(12.5, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(48, -35), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity rightSpike = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        rightSpike.runAction(rightSpike.getDrive().actionBuilder(new Pose2d(48, -35, Math.toRadians(0)))
                .setReversed(true)
                .splineTo(new Vector2d(12.5, -36), Math.toRadians(-135))
                .build());

        RoadRunnerBotEntity centerSpike = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        centerSpike.runAction(centerSpike.getDrive().actionBuilder(new Pose2d(48, -35, Math.toRadians(0)))
                .setReversed(true)
                .splineTo(new Vector2d(12.5, -36), Math.toRadians(-90))
                .build());

        RoadRunnerBotEntity leftSpike = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        leftSpike.runAction(leftSpike.getDrive().actionBuilder(new Pose2d(48, -35, Math.toRadians(0)))
                .setReversed(true)
                .splineTo(new Vector2d(12.5, -36), Math.toRadians(-45))
                .build());

        RoadRunnerBotEntity corner = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        corner.runAction(corner.getDrive().actionBuilder(new Pose2d(12.5, -36, Math.toRadians(90)))
                .setReversed(true)
                .splineTo(new Vector2d(60, -60), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity middle = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        middle.runAction(middle.getDrive().actionBuilder(new Pose2d(12.5, -36, Math.toRadians(90)))
                .setReversed(true)
                .splineTo(new Vector2d(60, -12), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity centerBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        centerBot.runAction(centerBot.getDrive().actionBuilder(new Pose2d(12.5, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(48, -35), Math.toRadians(0))
                //.waitSeconds(3)
                .setReversed(true)
                .splineTo(new Vector2d(12.5, -36), Math.toRadians(-90))
                //.waitSeconds(3)
                .setReversed(true)
                .splineTo(new Vector2d(60, -60), Math.toRadians(0))
                .build());

        // center bot steps
        RoadRunnerBotEntity centerBotToDropBack = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        centerBotToDropBack.runAction(centerBotToDropBack.getDrive().actionBuilder(new Pose2d(8, -64, Math.toRadians(90)))
                .splineTo(new Vector2d(48, -35), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity centerBotToPixel = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        centerBotToPixel.runAction(centerBotToPixel.getDrive().actionBuilder(new Pose2d(48, -35, Math.toRadians(0)))
                .setReversed(true)
                .splineTo(new Vector2d(12.5, -36), Math.toRadians(-90))
                .build());

        RoadRunnerBotEntity centerBotToPark = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        centerBotToPark.runAction(centerBotToPark.getDrive().actionBuilder(new Pose2d(12.5, -36, Math.toRadians(90)))
                .setReversed(true)
                .splineTo(new Vector2d(60, -60), Math.toRadians(0))
                .build());

        // right bot steps
        RoadRunnerBotEntity rightBotToPixel = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        rightBotToPixel.runAction(rightBotToPixel.getDrive().actionBuilder(new Pose2d(48, -35, Math.toRadians(0)))
                .setReversed(true)
                .splineTo(new Vector2d(30, -36), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity rightBotToPark = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        rightBotToPark.runAction(rightBotToPark.getDrive().actionBuilder(new Pose2d(30, -36, Math.toRadians(180)))
                .setReversed(true)
                .splineTo(new Vector2d(60, -60), Math.toRadians(0))
                .build());

        // left bot steps
        RoadRunnerBotEntity leftBotToPixel = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        leftBotToPixel.runAction(leftBotToPixel.getDrive().actionBuilder(new Pose2d(48, -35, Math.toRadians(0)))
                .setReversed(true)
                .splineTo(new Vector2d(10, -36), Math.toRadians(0))
                .build());

        RoadRunnerBotEntity leftBotToPark = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(12, 12)
                .build();

        leftBotToPark.runAction(rightBotToPark.getDrive().actionBuilder(new Pose2d(10, -36, Math.toRadians(180)))
                .setReversed(true)
                .splineTo(new Vector2d(60, -60), Math.toRadians(0))
                .build());
*/
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
                .splineTo(new Vector2d(16, -25), Math.toRadians(90))
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

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
//                .addEntity(audienceBlueLeft)
//                .addEntity(audienceBlueLeftWall)
                .addEntity(audienceBlueCenter)
//                .addEntity(audienceBlueCenterWall)
                .addEntity(audienceBlueRight)
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
                .start();
    }
}