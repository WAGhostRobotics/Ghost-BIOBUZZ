package org.firstinspires.ftc.teamcode.alignment;

import com.pedropathing.math.Vector2D;

public class FieldConstants {
    private FieldConstants() {};

    public enum Alliance {BLUE, RED};
    public static Vector2D BLUE_HIVE_A = Vector2D.cartesian(72-9.4,82);
    public static Vector2D BLUE_HIVE_B = Vector2D.cartesian(72+9.4,82);
    public static Vector2D RED_HIVE_A  = Vector2D.cartesian(72-9.4,62);
    public static Vector2D RED_HIVE_B  = Vector2D.cartesian(72+9.4,62);

    public static Vector2D hive(Alliance alliance, boolean hiveA) {
        if (alliance == Alliance.BLUE){
            return hiveA ? BLUE_HIVE_A : BLUE_HIVE_B;
        }
        return hiveA ? RED_HIVE_A : RED_HIVE_B;
    }

    public static double BLUE_DRIVER_FORWARD = Math.toRadians(90);
    public static double RED_DRIVER_FORWARD  = Math.toRadians(-90);
    public static double driverForward(Alliance alliance) {
        return alliance == Alliance.BLUE ? BLUE_DRIVER_FORWARD : RED_DRIVER_FORWARD;
    }


}
