package org.firstinspires.ftc.teamcode.testing;

import static org.firstinspires.ftc.teamcode.core.Charlie.intake;
import static org.firstinspires.ftc.teamcode.core.Charlie.shooter;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.Charlie;

@TeleOp
@Config
public class UndershooterTest extends OpMode {
    public static double P, I, D, F;
    public static double targetVel = 0, intakeSpeed, transferSpeed;

    Undershooter us;


    @Override
    public void init() {
        us = new Undershooter(hardwareMap);
    }

    @Override
    public void loop() {

        us.setTransferSpeed(transferSpeed);
        us.setIntakeSpeed(intakeSpeed);

        us.setPID(P,I,D,F);
        us.setTargetVel(targetVel);
        us.update();

        telemetry.addData("current velocity: ", us.getCurrentVelocity());
        telemetry.addData("target velocity: ", targetVel);
        telemetry.addData("error: ", targetVel- us.getCurrentVelocity());
        telemetry.addData("power: ", us.power);
        telemetry.update();
    }
}
