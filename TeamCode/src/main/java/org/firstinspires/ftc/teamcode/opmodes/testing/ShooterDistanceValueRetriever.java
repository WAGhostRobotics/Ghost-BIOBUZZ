package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.Robot;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Collects the distance -> hood position table (full-speed single shots).
 *
 * Workflow at each spot on the field:
 *   1. Drive to a spot. The flywheel is already spinning at TARGET_VELOCITY.
 *   2. Adjust the hood (dpad up/down coarse, bumpers fine).
 *   3. Press A right as you fire a ball (snapshots distance, velocity, hood).
 *   4. Press Y if it scored, B if it missed.
 *   5. Repeat across hood positions until you've found the LOW and HIGH edges of the
 *      scoring range (record makes near both edges).
 *   6. Press X to commit the spot: stores (average distance, midpoint of lowest and
 *      highest scoring hood). Then move to the next spot.
 *
 * Other buttons:
 *   dpad_left : discard the makes recorded for the current spot
 *   BACK      : delete the last committed entry
 *
 * Output: telemetry shows ready-to-paste "hoodLUT.add(dist, hood);" lines, and every
 * committed entry is also saved to CSV_PATH so nothing is lost when the OpMode stops.
 */
@TeleOp(name = "Shooter Compensation")
public class ShooterDistanceValueRetriever extends OpMode {

    static final double TARGET_VELOCITY = 0.8;
    static final double GOAL_X = 70;                // TODO: Set goal pos
    static final double GOAL_Y = 70;
    static final double COARSE_STEP = 0.01;
    static final double FINE_STEP = 0.005;
    static final String CSV_PATH = "/sdcard/FIRST/hood_lut_log.csv";
    // --------------------------------------------

    GoBildaPinpointDriver odo;

    boolean pending = false;
    double pendingDist, pendingVel, pendingHood;

    final List<Double> spotDists = new ArrayList<>();
    final List<Double> spotHoods = new ArrayList<>();

    // {distance, hood}
    final List<double[]> entries = new ArrayList<>();

    Robot sdvr;

    @Override
    public void init() {
        sdvr = new Robot(hardwareMap);

        odo = hardwareMap.get(GoBildaPinpointDriver.class, "odo");
        orientIMU(-180, 30, 0);
        sdvr.shooter.setTargetVelocity(TARGET_VELOCITY); // spin up the whole time
    }

    @Override
    public void loop() {
        odo.update();

        double x = odo.getPosX(DistanceUnit.CM);
        double y = odo.getPosY(DistanceUnit.CM);
        double dist = Math.hypot(x - GOAL_X, y - GOAL_Y);

        double hood = sdvr.shooter.getHoodPos();
        if (gamepad1.dpadUpWasPressed()) hood += COARSE_STEP;
        if (gamepad1.dpadDownWasPressed()) hood -= COARSE_STEP;
        if (gamepad1.rightBumperWasPressed()) hood += FINE_STEP;
        if (gamepad1.leftBumperWasPressed()) hood -= FINE_STEP;
        hood = Math.max(0.0, Math.min(1.0, hood));
        sdvr.shooter.setHoodPos(hood);

        if (gamepad1.aWasPressed()) {
            pendingDist = dist;
            pendingVel = sdvr.shooter.getCurrentVelocity();
            pendingHood = hood;
            pending = true;
        }
        if (gamepad1.yWasPressed() && pending) { // made it
            spotDists.add(pendingDist);
            spotHoods.add(pendingHood);
            pending = false;
        }
        if (gamepad1.bWasPressed()) { // missed
            pending = false;
        }

        if (gamepad1.xWasPressed() && !spotHoods.isEmpty()) {
            double distSum = 0, lo = Double.MAX_VALUE, hi = -Double.MAX_VALUE;
            for (double d : spotDists) distSum += d;
            for (double h : spotHoods) {
                lo = Math.min(lo, h);
                hi = Math.max(hi, h);
            }
            entries.add(new double[]{distSum / spotDists.size(), (lo + hi) / 2.0});
            spotDists.clear();
            spotHoods.clear();
            writeCsv();
        }
        if (gamepad1.dpadLeftWasPressed()) {
            spotDists.clear();
            spotHoods.clear();
        }
        if (gamepad1.backWasPressed() && !entries.isEmpty()) {
            entries.remove(entries.size() - 1);
            writeCsv();
        }

        telemetry.addData("dist (cm)", "%.1f", dist);
        telemetry.addData("hood", "%.3f", hood);
        telemetry.addData("shooter vel", "%.3f  (target %.3f)", sdvr.shooter.getCurrentVelocity(), TARGET_VELOCITY);
        if (pending) {
            telemetry.addLine(String.format(Locale.US,
                    "Shot logged: d=%.1f vel=%.3f hood=%.3f -> Y = make, B = miss",
                    pendingDist, pendingVel, pendingHood));
        }
        if (!spotHoods.isEmpty()) {
            double lo = Double.MAX_VALUE, hi = -Double.MAX_VALUE;
            for (double h : spotHoods) {
                lo = Math.min(lo, h);
                hi = Math.max(hi, h);
            }
            telemetry.addLine(String.format(Locale.US,
                    "This spot: %d makes, hood %.3f-%.3f -> press X to commit",
                    spotHoods.size(), lo, hi));
        }
        telemetry.addLine("LUT table");
        for (double[] e : entries) {
            telemetry.addLine(String.format(Locale.US, "hoodLUT.add(%.1f, %.3f);", e[0], e[1]));
        }
        telemetry.update();
    }

    public void orientIMU(double startX, double startY, double startHeading) {
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.REVERSED);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setOffsets(-10, -1.0, DistanceUnit.CM);
        odo.setPosition(new Pose2D(DistanceUnit.CM, startX, startY, AngleUnit.DEGREES, startHeading));
    }

    private void writeCsv() {
        try (FileWriter w = new FileWriter(CSV_PATH, false)) {
            w.write("distanceCm,hoodPos\n");
            for (double[] e : entries) {
                w.write(String.format(Locale.US, "%.1f,%.3f\n", e[0], e[1]));
            }
        } catch (Exception ex) {
            // best effort; telemetry still shows the table
        }
    }
}