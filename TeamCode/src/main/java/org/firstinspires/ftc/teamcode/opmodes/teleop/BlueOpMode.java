package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.opmodes.BaseTeleop;
import org.firstinspires.ftc.teamcode.utils.FieldConstants;

@TeleOp(name = "BlueTeleop")
public class BlueOpMode extends BaseTeleop {

    @Override
    public void init() {
        alliance = FieldConstants.Alliance.BLUE;
        tagsA = new int[]{38, 39, 40, 41}; //this side starts up
        tagsB = new int[]{42, 43, 44, 45};

        super.init(); //use to run the rest of code in init
    }
}