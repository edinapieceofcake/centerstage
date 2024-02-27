package edu.edina.library.domain;

import com.qualcomm.robotcore.hardware.DcMotor;

import edu.edina.library.enums.HangerState;
import edu.edina.library.enums.LiftServoState;
import edu.edina.library.util.RobotHardware;
import edu.edina.library.util.RobotState;

public class RobotHanger {
    public static void setProperties(boolean toggleExtend, boolean toggleRetract,
                                     boolean hangServo, boolean latchServo, boolean resetLift,
                                     RobotHardware hardware) {
        RobotState state = RobotState.getInstance();

        if (toggleExtend) {
            state.hangerState = HangerState.Extending;
        } else if (toggleRetract) {
            state.hangerState = HangerState.Retracting;
        } else if (hardware.robotHangerMotor.getMode() == DcMotor.RunMode.RUN_USING_ENCODER){
            // only go to idle if we are running in manual mode
            state.hangerState = HangerState.Idle;
        }

        if (hangServo) {
            state.currentLiftServoState = LiftServoState.Low;
        } else if (latchServo) {
            state.currentLiftServoState = LiftServoState.Hang;
        }

        if (resetLift) {
            hardware.homeHangMotorAsync();
        }
    }
}
