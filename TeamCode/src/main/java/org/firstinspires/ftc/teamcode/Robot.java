package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Vision;

public class Robot {

    public  Intake intake;
    public  Shooter shooter;
    public  Follower follower;
    public Vision vision;

    public Robot(HardwareMap hardwareMap) {
        follower = Constants.create(hardwareMap);
        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap);
        vision = new Vision(hardwareMap);
    }
}
