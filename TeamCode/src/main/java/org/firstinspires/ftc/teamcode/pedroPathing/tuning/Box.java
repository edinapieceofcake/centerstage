package org.firstinspires.ftc.teamcode.pedroPathing.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;

/**
 * This is the StraightBackAndForth autonomous OpMode. It runs the robot in a specified distance
 * straight forward. On reaching the end of the forward Path, the robot runs the backward Path the
 * same distance back to the start. Rinse and repeat! This is good for testing a variety of Vectors,
 * like the drive Vector, the translational Vector, and the heading Vector. Remember to test your
 * tunings on CurvedBackAndForth as well, since tunings that work well for straight lines might
 * have issues going in curves.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @author Aaron Yang - 10158 Scott's Bots
 * @author Harrison Womack - 10158 Scott's Bots
 * @version 1.0, 3/12/2024
 */
@Config
@Autonomous (name = "Box", group = "Autonomous Pathing Tuning")
public class Box extends LinearOpMode {
    private Telemetry telemetryA;

    public static double DISTANCE = 24;
    public static double RADIUS = 4;

    private boolean forward = true;

    private Follower follower;

    private PathChain box;

    /**
     * This initializes the Follower and creates the forward and backward Paths. Additionally, this
     * initializes the FTC Dashboard telemetry.
     */
    @Override
    public void runOpMode() {
        follower = new Follower(hardwareMap);

        box = follower.pathBuilder()
                .addPath(new BezierLine(new Point(0,0, Point.CARTESIAN), new Point(DISTANCE,0, Point.CARTESIAN)))
                .addPath(new BezierCurve(new Point(DISTANCE,0, Point.CARTESIAN), new Point(DISTANCE+RADIUS,0+RADIUS, Point.CARTESIAN), new Point(DISTANCE+RADIUS, 0+RADIUS, Point.CARTESIAN)))
                .addPath(new BezierLine(new Point(DISTANCE+RADIUS,RADIUS, Point.CARTESIAN), new Point(DISTANCE+RADIUS, DISTANCE+RADIUS, Point.CARTESIAN)))
                //.addPath(new BezierCurve(new Point(DISTANCE,DISTANCE, Point.CARTESIAN), new Point(DISTANCE+RADIUS,DISTANCE-RADIUS, Point.CARTESIAN), new Point(DISTANCE+RADIUS, DISTANCE, Point.CARTESIAN)))
                //.addPath(new BezierLine(new Point(DISTANCE,DISTANCE, Point.CARTESIAN), new Point(0,DISTANCE, Point.CARTESIAN)))
                //.addPath(new BezierLine(new Point(0,DISTANCE, Point.CARTESIAN), new Point(0,0, Point.CARTESIAN)))
                .build();

        follower.followPath(box);

        telemetryA = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());
        telemetryA.addLine("This will run the robot in a straight line going " + DISTANCE
                            + " inches forward. The robot will go forward and backward continuously"
                            + " along the path. Make sure you have enough room.");
        telemetryA.update();

        waitForStart();

        telemetryA.addData("going forward", forward);
        follower.followPath(box);

        while(follower.isBusy()) {
            follower.update();
            follower.telemetryDebug(telemetryA);
        }

    }

}
