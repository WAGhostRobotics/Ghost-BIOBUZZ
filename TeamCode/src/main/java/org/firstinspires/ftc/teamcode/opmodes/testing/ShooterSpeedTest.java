package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot;

@Config
@TeleOp
public class ShooterSpeedTest extends OpMode {

    public static double power = 0;
    public static double hoodpos = 0.2;
    Robot shooterSpeedTest;

    @Override
    public void init() {
        shooterSpeedTest = new Robot(hardwareMap);
    }

    @Override
    public void loop() {
        shooterSpeedTest.shooter.setShooterPower(power);
        shooterSpeedTest.shooter.setHoodPos(hoodpos);
        shooterSpeedTest.intake.setIntakeSpeed(0.8);
        shooterSpeedTest.intake.setTransferSpeed(0.8);

        telemetry.addData("power: ", power);
        telemetry.addData("hood pos: ", hoodpos);
        telemetry.addData("X", shooterSpeedTest.follower.pose().x());
        telemetry.addData("Y", shooterSpeedTest.follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(shooterSpeedTest.follower.pose().heading()));

        telemetry.update();
    }
}
