package org.firstinspires.ftc.teamcode.alignment;

import com.pedropathing.math.Vector2D;

public class FieldConstants {
    private FieldConstants() {};

    public enum Alliance {BLUE, RED};
    public static Vector2D BLUE_HIVE_A = Vector2D.cartesian(84.27,58.24);
    public static Vector2D BLUE_HIVE_B = Vector2D.cartesian(84.27,79.85);
    public static Vector2D RED_HIVE_A  = Vector2D.cartesian(58.68,59.69);
    public static Vector2D RED_HIVE_B  = Vector2D.cartesian(57.95,85.29);

    public static Vector2D hive(Alliance alliance, boolean hiveA) {
        if (alliance == Alliance.BLUE){
            return hiveA ? BLUE_HIVE_A : BLUE_HIVE_B;
        }
        return hiveA ? RED_HIVE_A : RED_HIVE_B;
    }

    // whichever of the alliance's two points is nearer to (x, y)
    public static Vector2D closestHive(Alliance alliance, double x, double y) {
        Vector2D a = hive(alliance, true);
        Vector2D b = hive(alliance, false);
        return Math.hypot(a.x() - x, a.y() - y) <= Math.hypot(b.x() - x, b.y() - y) ? a : b;
    }

    public static double BLUE_DRIVER_FORWARD = Math.toRadians(180);
    public static double RED_DRIVER_FORWARD  = Math.toRadians(0);
    public static double driverForward(Alliance alliance) {
        return alliance == Alliance.BLUE ? BLUE_DRIVER_FORWARD : RED_DRIVER_FORWARD;
    }


}
