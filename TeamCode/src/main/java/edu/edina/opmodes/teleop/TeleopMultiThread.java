package edu.edina.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import edu.edina.library.util.Robot;

@TeleOp(name = "TeleopMultiThread", group = "teleop")
@Disabled
public class TeleopMultiThread extends TeleopOpMode {
    @Override
    public void init() {
        // this line calls the init method in the TeleopOpMode to setup the game pads
        // it saves us from having to enter it twice and fix problems in only one spot.
        super.init();

        // this is here for simplicity, but we could "refactor" or change all this to be
        // less code as the only difference between the multi thread and single thread is
        // the third boolean parameter
        robot = new Robot(telemetry, hardwareMap, true);
        robot.init();
    }
}
