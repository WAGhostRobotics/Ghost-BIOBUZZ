package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Config
@TeleOp
public class Dashboard extends OpMode {

    public static int x = 0;

    @Override
    public void init() {

    }

    @Override
    public void loop() {
        telemetry.addData("variable:",x);
        telemetry.update();
    }
}
