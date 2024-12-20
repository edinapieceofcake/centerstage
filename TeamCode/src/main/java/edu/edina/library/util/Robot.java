package edu.edina.library.util;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ThreadPool;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import edu.edina.library.subsystems.Claw;
import edu.edina.library.subsystems.DroneLauncher;
import edu.edina.library.subsystems.Lift;
import edu.edina.library.subsystems.MecanumDrive;
import edu.edina.library.subsystems.RobotHanger;
import edu.edina.library.subsystems.Subsystem;

public class Robot {
    private ExecutorService subsystemUpdateExecutor;
    private boolean started;
    private boolean runMultiThreaded;
    public RobotHardware RobotHardware;
    private List<Subsystem> subsystems = new ArrayList<>();
    private Telemetry telemetry;
    private Runnable subsystemUpdateRunnable = () -> {
        while (!Thread.currentThread().isInterrupted()) {
            internal_update();
        }
    };

    public Robot(Telemetry telemetry, HardwareMap map, boolean runMultiThreaded) {
        this.telemetry = telemetry;
        this.runMultiThreaded = runMultiThreaded;
        this.RobotHardware = new RobotHardware(map);

        subsystems.add(new Lift(this.RobotHardware, true));

        subsystems.add(new MecanumDrive(this.RobotHardware));

        subsystems.add(new Claw(this.RobotHardware));

        subsystems.add(new RobotHanger(this.RobotHardware));

        subsystems.add(new DroneLauncher(this.RobotHardware));

        if (this.runMultiThreaded) {
            // setup the thread executor
            subsystemUpdateExecutor = ThreadPool.newSingleThreadExecutor("subsystem update");
        }
    }

    public void init() {
        RobotHardware.liftServosForTeleop();

        for (Subsystem subsystem : subsystems) {
            subsystem.init();
        }
    }

    public void update() {
        // check to see if running update on different thread
        if (!runMultiThreaded) {
            // we are not so run it here
            internal_update();
        }
    }

    private void internal_update() {
        for (Subsystem subsystem : subsystems) {
            subsystem.update();
        }
    }

    public void start() {
        for (Subsystem subsystem : subsystems) {
            subsystem.start();
        }

        if (runMultiThreaded){
            if (!started) {
                subsystemUpdateExecutor.submit(subsystemUpdateRunnable);
            }
        }

        started = true;
    }

    public void stop() {
        if (runMultiThreaded){
            if (subsystemUpdateExecutor != null) {
                subsystemUpdateExecutor.shutdownNow();
                subsystemUpdateExecutor = null;
            }
        }

        for (Subsystem subsystem : subsystems) {
            subsystem.stop();
        }

        started = false;
    }

    public void telemetry() {
        RobotState.getInstance().telemetry(this.telemetry, this.RobotHardware);
        telemetry.update();
    }
}
