package org.firstinspires.ftc.teamcode.components.alignment;

import static org.firstinspires.ftc.teamcode.components.alignment.FieldConstants.BLUE_HIVE_A;
import static org.firstinspires.ftc.teamcode.components.alignment.FieldConstants.BLUE_HIVE_B;
import static org.firstinspires.ftc.teamcode.components.alignment.FieldConstants.RED_HIVE_A;
import static org.firstinspires.ftc.teamcode.components.alignment.FieldConstants.RED_HIVE_B;
import static org.firstinspires.ftc.teamcode.core.Charlie.follower;

import com.pedropathing.math.Pose;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.core.Charlie;

/**
 * this class is used to align the chassis with the goal used.
 */
public class Align {

    Pose HIVE_A, HIVE_B, targetHive;

    double targetHeading, headingError;
    public Align(boolean blue, HardwareMap hwmap) {
        if (!blue) {
            HIVE_A = RED_HIVE_A;
            HIVE_B = RED_HIVE_B;
        } else {
            HIVE_A = BLUE_HIVE_A;
            HIVE_B = BLUE_HIVE_B;
        }
    }

    public void setTargetHive(double robot_y) {
        if(robot_y < 72.000) {
            targetHive = HIVE_A;
        } else {
            targetHive = HIVE_B;
        }
    }

    public void setTargetHeading() {
        targetHeading = Math.atan2(targetHive.y() - follower.pose().y(),
                                   targetHive.x() - follower.pose().x());   // radians
    }

    public double getTargetHeading() {
        return targetHeading;
    }

    public double getHeadingError() {
        return targetHeading - follower.pose().heading();
    }
}
