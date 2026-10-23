package org.firstinspires.ftc.teamcode.testing;

import static org.firstinspires.ftc.teamcode.core.Charlie.shooter;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Records the RPM-deficit table at ONE fixed distance (park the robot and leave it there).
 *
 * Workflow:
 *   1. Pick a deficit with dpad left/right. The flywheel spins at TARGET_VELOCITY * (1 - deficit%).
 *   2. Adjust the hood (dpad up/down coarse, bumpers fine).
 *   3. Press A as you fire a ball (snapshots the hood and the real velocity at that moment).
 *   4. Press Y if it scored, B if it missed.
 *   5. Sweep the hood and keep shooting until you've logged makes near BOTH edges of the
 *      scoring range for that deficit. Only press Y for clean makes: the tracked range is the
 *      lowest and highest hood that scored.
 *   6. Go to the next deficit. Do 0% first, since every offset is measured against it.
 *   X clears the data for the current deficit if you want to redo it.
 *
 * Telemetry prints ready-to-paste ".add(deficit, offset)" lines for HoodDeficitCompensator,
 * where offset = (center of scoring range at that deficit) - (center at 0%).
 * Write the numbers down (or screenshot) before stopping the OpMode.
 */
@TeleOp(name = "Shooter Compensator")
public class ShooterCompensator extends OpMode {

    static final double TARGET_VELOCITY = 0.8;
    static final double COARSE_STEP = 0.01;
    static final double FINE_STEP = 0.005;
    static final int MAX_DEFICIT_PCT = 20;
    static final double SETTLE_TOLERANCE_PCT = 1.5;

    private int deficitPct = 0;
    private double hood;

    // last fired ball
    private boolean pending = false;
    private int pendingDeficit;
    private double pendingHood;
    private double pendingVel;

    // {lowest scoring hood, highest scoring hood}
    private final TreeMap<Integer, double[]> makes = new TreeMap<>();

    @Override
    public void init() {
        hood = shooter.getHoodPos();
    }

    @Override
    public void loop() {
        if (gamepad1.dpadRightWasPressed()) deficitPct = Math.min(MAX_DEFICIT_PCT, deficitPct + 1);
        if (gamepad1.dpadLeftWasPressed()) deficitPct = Math.max(0, deficitPct - 1);

        if (gamepad1.dpadUpWasPressed()) hood += COARSE_STEP;
        if (gamepad1.dpadDownWasPressed()) hood -= COARSE_STEP;
        if (gamepad1.rightBumperWasPressed()) hood += FINE_STEP;
        if (gamepad1.leftBumperWasPressed()) hood -= FINE_STEP;
        hood = Range.clip(hood, 0.0, 1.0);

        applyTest(TARGET_VELOCITY, deficitPct, hood);

        double currentVel = shooter.getCurrentVelocity();
        double actualDeficit = 100.0 * (TARGET_VELOCITY - currentVel) / TARGET_VELOCITY;
        boolean settled = Math.abs(actualDeficit - deficitPct) <= SETTLE_TOLERANCE_PCT;

        if (gamepad1.aWasPressed()) { //press as firing
            pending = true;
            pendingDeficit = deficitPct;
            pendingHood = hood;
            pendingVel = currentVel;
        }
        if (gamepad1.yWasPressed() && pending) { // made it
            double[] range = makes.get(pendingDeficit);
            if (range == null) {
                makes.put(pendingDeficit, new double[]{pendingHood, pendingHood});
            } else {
                range[0] = Math.min(range[0], pendingHood);
                range[1] = Math.max(range[1], pendingHood);
            }
            pending = false;
        }
        if (gamepad1.bWasPressed()) { // missed
            pending = false;
        }
        if (gamepad1.xWasPressed()) { //redo deficit
            makes.remove(deficitPct);
        }

        // ---- telemetry ----
        telemetry.addData("target deficit", "%d%%", deficitPct);
        telemetry.addData("actual deficit", "%.1f%%  %s", actualDeficit, settled ? "SETTLED" : "settling...");
        telemetry.addData("hood", "%.3f", hood);
        if (pending) {
            telemetry.addLine(String.format(Locale.US,
                    "Shot logged at hood %.3f, vel %.3f (%d%%) -> Y = make, B = miss",
                    pendingHood, pendingVel, pendingDeficit));
        }

        telemetry.addLine("results");
        double[] zero = makes.get(0);
        for (Map.Entry<Integer, double[]> e : makes.entrySet()) {
            int d = e.getKey();
            double lo = e.getValue()[0];
            double hi = e.getValue()[1];
            double center = (lo + hi) / 2.0;
            telemetry.addLine(String.format(Locale.US,
                    "%d%%: hood %.3f-%.3f, center %.3f", d, lo, hi, center));
            if (zero != null) {
                double zeroCenter = (zero[0] + zero[1]) / 2.0;
                telemetry.addLine(String.format(Locale.US,
                        "   .add(%d, %.3f)", d, center - zeroCenter));
            }
        }
        if (zero == null && !makes.isEmpty()) {
            telemetry.addLine("No deficit first");
        }
        telemetry.update();
    }

    public void applyTest(double actualTargetVelocity, int percentDeficit, double angle) {
        double testedVelocity = actualTargetVelocity * (1.0 - percentDeficit / 100.0);
        shooter.setHoodPos(angle);
        shooter.setTargetVelocity(testedVelocity);
    }
}