package edu.edina.opmodes.auto;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import edu.edina.library.enums.PropLocation;

@Autonomous
//@Disabled
public class RedAudienceCenter extends RedAudience {
    @Override
    protected void runPaths() {

        if (yellowPixel) {  // P + Y + 1W path
            purpleToStack(propLocation); // B

            // If we are doing anything but stopping after purple
            if (makeSecondTrip) {  // P + Y + 3W path
                stackToYellowToStack(); // DPAD_UP/DOWN
                if (dropOnBackdrop) {
                    stackToAngleDrop(); // DPAD_UP
                } else if (dropOnBackstage) {
                    stackToBackStageDrop(); // DPAD_DOWN
                }
            } else {
                stackToYellow(); // C
            }
        }
        
    }

    protected void purpleToStack(PropLocation propLocation) {}

    protected void stackToYellow() {}

    protected void stackToYellowToStack() {}

    protected void stackToAngleDrop () {}

    protected void stackToBackStageDrop() {}
}