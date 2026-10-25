package org.firstinspires.ftc.teamcode.utils.alignment;

import com.pedropathing.controllers.PIDController;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.pedropathing.utils.Angle;

import org.firstinspires.ftc.teamcode.utils.FieldConstants;

public class AlignPID {
    public static double kP = 0;
    public static double kI = 0;
    public static double kD = 0;
    public static double kS = 0.01; //static friction feedforward

    private final PIDController pid = new PIDController(kP, kI, kD);
    private Vector2D target;

    //    public static double offset = 0;
    public static double tolerance = 2; //degrees


    public AlignPID(Vector2D target){
        this.target = target;
    }

    public void setTarget(Vector2D target) {
        this.target = target;
    }

    public Vector2D target() {
        return target;
    }

    public DrivePowers aim(Follower f, DrivePowers translation){
        Pose p = f.pose();
        double dx = target.x() - p.x();
        double dy = target.y() - p.y();
        double targetHeading = Angle.normalize(Math.atan2(dy,dx));
        double error = Angle.normalizeSigned(targetHeading - p.heading());
        double angvel = f.velocity().omega;

        double turn = pid.calculate(0, error, angvel);
        if (Math.abs(error) > Math.toRadians(tolerance)){
            turn += kS * Math.signum(error);
        }
        return new DrivePowers(translation.forward(), translation.strafe(), turn);
    }

    public void reset(){
        pid.reset();
    }

    public DrivePowers autoAim(Follower f, DrivePowers translation, FieldConstants.Alliance alliance, boolean aimHiveA) {
        setTarget(FieldConstants.hive(alliance, aimHiveA)); //update
        return aim(f, translation); //calc pose
    }
}
