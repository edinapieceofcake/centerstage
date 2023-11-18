package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

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
                        .lineToX(20)
//                .setReversed(true)
//                .splineTo(new Vector2d(60, -60), Math.toRadians(0))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
//                .addEntity(myBot)
//                .addEntity(rightSpike)
//                .addEntity(centerSpike)
//                .addEntity(leftSpike)
//                .addEntity(corner)
//                .addEntity(middle)
                .addEntity(leftBotToPark)
                .start();
    }
}