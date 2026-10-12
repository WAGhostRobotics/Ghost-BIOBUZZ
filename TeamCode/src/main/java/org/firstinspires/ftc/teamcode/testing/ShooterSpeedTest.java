package org.firstinspires.ftc.teamcode.testing;

import static org.firstinspires.ftc.teamcode.core.Charlie.follower;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.Shooter;
import org.firstinspires.ftc.teamcode.core.Charlie;

@Config
@TeleOp
public class ShooterSpeedTest extends OpMode {

    public static double power = 0;
    public static double hoodpos = 0.2;
    @Override
    public void init() {

        Charlie.init(hardwareMap);
    }

    @Override
    public void loop() {
        Charlie.shooter.setShooterPower(power);
        Charlie.shooter.setHoodPos(hoodpos);
        Charlie.intake.setIntakeSpeed(0.8);
        Charlie.intake.setTransferSpeed(0.8);

        telemetry.addData("power: ", power);
        telemetry.addData("hood pos: ", hoodpos);
        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));

        telemetry.update();
    }
}
