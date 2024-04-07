package edu.edina.library.domain;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.ClawState;
import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftServoState;
import edu.edina.library.enums.TwistServoState;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotState;

public class Claw {
    public static void setProperties(boolean toggleLeftClaw, boolean toggleRightClaw, boolean leftAngleDrop, boolean rightAngleDrop, boolean b) {
        RobotState state = RobotState.getInstance();
        RobotConfiguration config = RobotConfiguration.getInstance();

//        if ((state.currentLiftDriveState == LiftDriveState.Manual) && (state.currentLiftServoState == LiftServoState.Start)) {
//            if (state.currentLiftHeight > config.minimumHeightToTwistServoInInches) {
//                state.twistServoState = TwistServoState.CenterDropOff;
//            } else {
//                state.twistServoState = TwistServoState.Pickup;
//            }
//        }

        if (toggleLeftClaw) {
            if (state.leftClawState == ClawState.Opened) {
                state.leftClawState = ClawState.Closed;
            } else {
                state.leftClawState = ClawState.Opened;
            }
        }

        if (toggleRightClaw) {
            if (state.rightClawState == ClawState.Opened) {
                state.rightClawState = ClawState.Closed;
            } else {
                state.rightClawState = ClawState.Opened;
            }
        }

        if (toggleRightClaw) {
            if (state.autoClawState == ClawState.Opened) {
                state.autoClawState = ClawState.Closed;
            } else {
                state.autoClawState = ClawState.Opened;
            }
        }

        if (leftAngleDrop) {
            if (state.twistServoState == TwistServoState.CenterDropOff) {
                state.twistServoState = TwistServoState.LeftDropOff;
                state.angleClawState = AngleClawState.LeftDropOff;
            } else if (state.twistServoState == TwistServoState.RightDropOff) {
                state.twistServoState = TwistServoState.CenterDropOff;
                state.angleClawState = AngleClawState.CenterDropOff;
            }
        }

        if (rightAngleDrop) {
            if (state.twistServoState == TwistServoState.CenterDropOff) {
                state.twistServoState = TwistServoState.RightDropOff;
                state.angleClawState = AngleClawState.RightDropOff;
            } else if (state.twistServoState == TwistServoState.LeftDropOff) {
                state.twistServoState = TwistServoState.CenterDropOff;
                state.angleClawState = AngleClawState.CenterDropOff;
            }
        }

        if (b) {
            if (state.pusherState == ClawState.Opened) {
                state.pusherState = ClawState.Closed;
                if (state.angleClawState == AngleClawState.Pusher) {
                    state.angleClawState = AngleClawState.CenterDropOff;
                }

            } else if (state.twistServoState == TwistServoState.CenterDropOff) {
                state.pusherState = ClawState.Opened;
                state.angleClawState = AngleClawState.Pusher;
            }
        }
    }
}
