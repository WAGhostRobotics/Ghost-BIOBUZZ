package org.firstinspires.ftc.teamcode.core;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.components.Intake;
import org.firstinspires.ftc.teamcode.components.Shooter;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public class Charlie {

    public static Intake intake;
    public static Shooter shooter;
    public static Follower follower;


    public static void init(HardwareMap hardwareMap) {

        follower = Constants.create(hardwareMap);

        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap);
    }
}
