package org.firstinspires.ftc.teamcode.opmodes.auto.paths;

import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

public interface AutoPaths {
    Pose getStartPose();
    Path preload();
    Path flower();
    Path flowerBackup();
    Path flowerShoot();
    Path flower2();
    Path flower2shot();
    Path park();
}