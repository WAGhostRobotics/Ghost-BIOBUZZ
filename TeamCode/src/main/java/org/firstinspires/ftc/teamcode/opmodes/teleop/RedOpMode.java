package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.opmodes.BaseTeleop;
import org.firstinspires.ftc.teamcode.utils.FieldConstants;

@TeleOp(name = "RedTeleop")
public class RedOpMode extends BaseTeleop {

    @Override
    public void init() {
        alliance = FieldConstants.Alliance.RED;
        tagsA = new int[]{34, 35, 36, 37}; //this side starts up
        tagsB = new int[]{30, 31, 32, 33};

        super.init(); //use to run the rest of code in init
    }
}