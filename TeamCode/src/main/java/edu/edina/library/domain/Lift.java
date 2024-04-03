package edu.edina.library.domain;

import static edu.edina.library.enums.LiftDriveState.Hang;
import static edu.edina.library.enums.LiftDriveState.DropOff;
import static edu.edina.library.enums.LiftDriveState.Manual;

import android.util.Log;

import edu.edina.library.enums.AngleClawState;
import edu.edina.library.enums.DropOffState;
import edu.edina.library.enums.HangState;
import edu.edina.library.enums.LiftDriveState;
import edu.edina.library.enums.LiftServoState;
import edu.edina.library.enums.LiftSlideState;
import edu.edina.library.enums.PickUpState;
import edu.edina.library.util.RobotConfiguration;
import edu.edina.library.util.RobotState;

public class Lift {

    public static void setProperties(double rightTrigger, double leftTrigger, boolean a, boolean x, boolean y, boolean gm2y,
                              boolean rightStick, boolean leftStick, boolean dpadDown) {
        RobotState state = RobotState.getInstance();
        RobotConfiguration config = RobotConfiguration.getInstance();

        if (leftTrigger != 0) {
            state.currentLiftDriveState = Manual;
            state.currentLiftSlideState = LiftSlideState.Retracting;
            state.currentTriggerStrength = Math.abs(leftTrigger);
        } else if (rightTrigger != 0) {
            state.currentLiftDriveState = Manual;
            state.currentLiftSlideState = LiftSlideState.Extending;
            state.currentTriggerStrength = Math.abs(rightTrigger);
        } else {
            state.currentLiftSlideState = LiftSlideState.Idle;
        }

        if (a) {
            // Pickup
            if (state.lastKnownLiftState == LiftDriveState.Drive || state.lastKnownLiftState == LiftDriveState.Pickup) {
                if (state.dropOffState == DropOffState.Finished) {
                    state.angleClawState = AngleClawState.Pickup;
                    state.lastKnownLiftState = LiftDriveState.Pickup;
                    state.pickUpState = PickUpState.Finished;
                    state.currentLiftSlideState = LiftSlideState.Idle;
                    state.currentLiftDriveState = Manual;
                } else {
                    state.dropOffState = DropOffState.Finished;
                    state.pickUpState = PickUpState.Start;
                    state.lastKnownLiftState = state.currentLiftDriveState;
                    state.currentLiftDriveState = LiftDriveState.Pickup;
                    state.currentLiftSlideState = LiftSlideState.Retracting;
                }
            } else {
                state.pickUpState = PickUpState.Start;
                state.currentLiftDriveState = LiftDriveState.Pickup;
                state.currentLiftSlideState = LiftSlideState.Retracting;
            }
        }

        if (x) {
            // drive
            if (state.lastKnownLiftState == LiftDriveState.Drive  || state.lastKnownLiftState == LiftDriveState.Pickup) {
                if (state.dropOffState == DropOffState.Finished) {
                    state.angleClawState = AngleClawState.Drive;
                    state.lastKnownLiftState = LiftDriveState.Drive;
                    state.pickUpState = PickUpState.Finished;
                    state.currentLiftSlideState = LiftSlideState.Idle;
                    state.currentLiftDriveState = Manual;
                } else {
                    state.dropOffState = DropOffState.Finished;
                    state.pickUpState = PickUpState.Start;
                    state.lastKnownLiftState = state.currentLiftDriveState;
                    state.currentLiftDriveState = LiftDriveState.Drive;
                    state.currentLiftSlideState = LiftSlideState.Retracting;
                }
            } else {
                state.pickUpState = PickUpState.Start;
                state.currentLiftDriveState = LiftDriveState.Drive;
                state.currentLiftSlideState = LiftSlideState.Retracting;
            }
        }

        if (y) {
            // dropoff
            Log.d("IAMHERE", String.format("YS P:%s, L:%s, C:%s, D:%s, M:%d, T:%d", state.pickUpState, state.lastKnownLiftState,
                    state.currentLiftSlideState, state.currentLiftDriveState, state.currentTopMotorPosition, config.liftTwistPosition));
            if (state.lastKnownLiftState == DropOff) {
                if (state.pickUpState == PickUpState.Finished) {
                    state.currentLiftDriveState = Manual;
                } else {
                    state.lastKnownLiftState = state.currentLiftDriveState;
                    state.currentLiftDriveState = LiftDriveState.DropOff;
                    if (state.currentLiftSlideState == LiftSlideState.Retracting || state.currentLiftSlideState == LiftSlideState.Idle) {
                        switch (state.pickUpState) {
                            case FirstRetraction:
                                state.dropOffState = DropOffState.SecondExtension;
                                state.currentTopMotorTargetPosition = state.currentLiftMotorDropOffPosition;
                                state.currentBottomMotorTargetPosition = state.currentLiftMotorDropOffPosition;
                                state.currentLiftSlidePower = config.liftExtendingPower;
                                state.secondExtensionTimeout.reset();
                                break;
                            case DropArm:
                                state.dropOffState = DropOffState.LiftArm;
                                break;
                            default:
                                state.dropOffState = DropOffState.Start;
                                break;
                        }
                    } else {
                        Log.d("IAMHERE", "Y Pressed");
                    }
                    state.currentLiftSlideState = LiftSlideState.Extending;
                    state.pickUpState = PickUpState.Finished;
                }
            } else {
                state.currentLiftDriveState = LiftDriveState.DropOff;
                state.dropOffState = DropOffState.Start;

                state.currentLiftSlideState = LiftSlideState.Extending;
            }
            Log.d("IAMHERE", String.format("YF D:%s, L:%s, C:%s, D:%s, M:%d, T:%d", state.dropOffState, state.lastKnownLiftState,
                    state.currentLiftSlideState, state.currentLiftDriveState, state.currentTopMotorPosition, config.liftTwistPosition));
        }

//        if (b) {
//            // high
//            Log.d("IAMHERE", String.format("BS P:%s, L:%s, C:%s, D:%s, M:%d, T:%d", state.pickUpState, state.lastKnownLiftState,
//                    state.currentLiftSlideState, state.currentLiftDriveState, state.currentTopMotorPosition, config.liftTwistPosition));
//            if (state.lastKnownLiftState == LiftDriveState.HighDropOff) {
//                if (state.pickUpState == PickUpState.Finished) {
//                    state.currentLiftDriveState = Manual;
//                } else {
//                    state.lastKnownLiftState = state.currentLiftDriveState;
//                    state.currentLiftDriveState = LiftDriveState.HighDropOff;
//                    if (state.currentLiftSlideState == LiftSlideState.Retracting || state.currentLiftSlideState == LiftSlideState.Idle) {
//                        switch (state.pickUpState) {
//                            case FirstRetraction:
//                                state.currentTopMotorTargetPosition = state.currentHighDropOffPostiion;
//                                state.currentBottomMotorTargetPosition = state.currentHighDropOffPostiion;
//                                state.currentLiftSlidePower = config.liftExtendingPower;
//                                state.dropOffState = DropOffState.SecondExtension;
//                                state.secondExtensionTimeout.reset();
//                                break;
//                            case DropArm:
//                                state.dropOffState = DropOffState.LiftArm;
//                                break;
//                            default:
//                                state.dropOffState = DropOffState.Start;
//                                break;
//                        }
//                    } else {
//                        Log.d("IAMHERE", "B Pressed");
//                    }
//                    state.currentLiftSlideState = LiftSlideState.Extending;
//                    state.pickUpState = PickUpState.Finished;
//                }
//            } else {
//                state.currentLiftDriveState = LiftDriveState.HighDropOff;
//                if (state.lastKnownLiftState == LiftDriveState.LowDropOff) {
//                    state.dropOffState = DropOffState.FirstExtension;
//                } else if (state.dropOffState != DropOffState.Start) {
//                    state.dropOffState = DropOffState.Start;
//                }
//                state.currentLiftSlideState = LiftSlideState.Extending;
//            }
//            Log.d("IAMHERE", String.format("BF D:%s, L:%s, C:%s, D:%s, M:%d, T:%d", state.dropOffState, state.lastKnownLiftState,
//                    state.currentLiftSlideState, state.currentLiftDriveState, state.currentTopMotorPosition, config.liftTwistPosition));
//        }

        if (gm2y) {
            if (state.currentLiftDriveState != Hang) {
                state.currentLiftDriveState = Hang;
                state.hangState = HangState.Start;
                state.currentLiftSlideState = LiftSlideState.Extending;
            }
        }

        if (rightStick) {
            switch (state.currentLiftServoStateDropOffPosition) {
                case One:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Two;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionOne;
                    state.liftDPadChanged = true;
                    break;
                case Two:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Three;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionTwo;
                    state.liftDPadChanged = true;
                    break;
                case Three:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Four;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionTwo;
                    state.liftDPadChanged = true;
                    break;
                case Four:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Five;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionThree;
                    state.liftDPadChanged = true;
                    break;
                case Five:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Six;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionFour;
                    state.liftDPadChanged = true;
                    break;
                case Six:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Seven;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionFour;
                    state.liftDPadChanged = true;
                    break;
                case Seven:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Eight;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionFive;
                    state.liftDPadChanged = true;
                    break;
                case Eight:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Nine;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionFive;
                    state.liftDPadChanged = true;
                    break;

            }
        }

        if (leftStick) {
            switch (state.currentLiftServoStateDropOffPosition) {
                case Two:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.One;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionOne;
                    state.liftDPadChanged = true;
                    break;
                case Three:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Two;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionOne;
                    state.liftDPadChanged = true;
                    break;
                case Four:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Three;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionTwo;
                    state.liftDPadChanged = true;
                    break;
                case Five:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Four;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionTwo;
                    state.liftDPadChanged = true;
                    break;
                case Six:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Five;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionThree;
                    state.liftDPadChanged = true;
                    break;
                case Seven:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Six;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionFour;
                    state.liftDPadChanged = true;
                    break;
                case Eight:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Seven;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionFour;
                    state.liftDPadChanged = true;
                    break;
                case Nine:
                    state.currentLiftServoStateDropOffPosition = LiftServoState.Eight;
                    state.currentLiftMotorDropOffPosition = config.liftDropOffPositionFive;
                    state.liftDPadChanged = true;
                    break;
            }
            if(dpadDown) {
                state.currentLiftServoStateDropOffPosition = LiftServoState.One;
                state.currentLiftMotorDropOffPosition = config.liftDropOffPositionOne;
                state.liftDPadChanged = true;
            }
        }
    }
}
