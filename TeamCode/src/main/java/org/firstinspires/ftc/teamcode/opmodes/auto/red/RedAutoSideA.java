package org.firstinspires.ftc.teamcode.opmodes.auto.red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.opmodes.BaseAuto;
import org.firstinspires.ftc.teamcode.utils.FieldConstants;

@Autonomous(name="RedAuto - Side A")
public class RedAutoSideA extends BaseAuto {

    @Override
    public void init() {
        alliance = FieldConstants.Alliance.RED;
        isSideA = true;

        upTags = new int[]{34, 35, 36, 37};
        downTags = new int[]{30, 31, 32, 33};

        super.init(); //use to run the rest of code in init
    }
}