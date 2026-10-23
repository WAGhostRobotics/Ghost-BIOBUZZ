package org.firstinspires.ftc.teamcode.opmodes.auto.blue;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.opmodes.BaseAuto;
import org.firstinspires.ftc.teamcode.utils.FieldConstants;

@Autonomous(name="BlueAuto - Side A")
public class BlueAutoSideA extends BaseAuto {

    @Override
    public void init() {
        alliance = FieldConstants.Alliance.BLUE;
        isSideA = true;

        upTags = new int[]{38, 39, 40, 41};
        downTags = new int[]{42, 43, 44, 45};

        super.init();  //use to run the rest of code in init
    }
}