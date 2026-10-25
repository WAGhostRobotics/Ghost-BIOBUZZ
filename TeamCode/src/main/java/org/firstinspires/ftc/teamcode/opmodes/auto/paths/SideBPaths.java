package org.firstinspires.ftc.teamcode.opmodes.auto.paths;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

public class SideBPaths implements AutoPaths{
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(32.3357, 135.4817, 90);
    private final Pose preload = poseFactory.of(41.4872, 14.4899, 56);
    private final Pose preloadControl1 = poseFactory.of(13.8923, 65.4654, 0);
    private final Pose flowerStart = poseFactory.of(41.4872, 14.4899, 56);
    private final Pose flower = poseFactory.of(47.1795, 132.4782, 90);
    private final Pose flowerControl1 = poseFactory.of(12.8734, 78.8413, 0);
    private final Pose flowerBackupStart = poseFactory.of(47.1795, 132.4782, 90);
    private final Pose flowerBackup = poseFactory.of(43.4954, 123.2726, 381);
    private final Pose flowerBackupControl1 = poseFactory.of(37.7525, 130.7406, 0);
    private final Pose flowerShootStart = poseFactory.of(43.4954, 123.2726, 381);
    private final Pose flowerShoot = poseFactory.of(41.5397, 14.2071, 56);
    private final Pose flowerShootControl1 = poseFactory.of(1.2689, 74.4446, 0);
    private final Pose flower2Start = poseFactory.of(41.5397, 14.2071, 56);
    private final Pose flower2 = poseFactory.of(11.6483, 46.9769, 180);
    private final Pose flower2Control1 = poseFactory.of(31.1505, 49.6751, 0);
    private final Pose flower2shotStart = poseFactory.of(11.6483, 46.9769, 180);
    private final Pose flower2shot = poseFactory.of(41.2991, 14.1889, 56);
    private final Pose flower2shotControl1 = poseFactory.of(24.2286, 30.6448, 0);
    private final Pose parkStart = poseFactory.of(41.2991, 14.1889, 56);
    private final Pose park = poseFactory.of(15.4861, 90.8797, 0);
    private final Pose parkControl1 = poseFactory.of(9.9137, 62.5359, 0);

    @Override
    public Pose getStartPose() {
        return start;
    }

    public Path preload() {
        return Paths.curve(start, preloadControl1, preload).linear(start, preload);
    }
    @Override
    public Path flower() {
        return Paths.curve(flowerStart, flowerControl1, flower).linear(flowerStart, flower);
    }
    @Override
    public Path flowerBackup() {
        return Paths.curve(flowerBackupStart, flowerBackupControl1, flowerBackup).linear(flowerBackupStart, flowerBackup);
    }
    @Override
    public Path flowerShoot() {
        return Paths.curve(flowerShootStart, flowerShootControl1, flowerShoot).linear(flowerShootStart, flowerShoot);
    }
    @Override
    public Path flower2() {
        return Paths.curve(flower2Start, flower2Control1, flower2).linear(flower2Start, flower2);
    }
    @Override
    public Path flower2shot() {
        return Paths.curve(flower2shotStart, flower2shotControl1, flower2shot).linear(flower2shotStart, flower2shot);
    }
    @Override
    public Path park() {
        return Paths.curve(parkStart, parkControl1, park).linear(parkStart, park);
    }
}
