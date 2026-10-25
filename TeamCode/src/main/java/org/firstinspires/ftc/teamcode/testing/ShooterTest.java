package org.firstinspires.ftc.teamcode.testing;

import static org.firstinspires.ftc.teamcode.core.Charlie.intake;
import static org.firstinspires.ftc.teamcode.core.Charlie.shooter;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.Intake;
import org.firstinspires.ftc.teamcode.components.Shooter;
import org.firstinspires.ftc.teamcode.core.Charlie;

@Config
@TeleOp
public class ShooterTest extends OpMode {
    Follower follower;
    public static double P, I, D, F;
    public static double hoodPos = 0.20;
    public static double targetVel = 0;

    public static double transferSpeed;


    @Override
    public void init() {

        Charlie.init(hardwareMap);
        shooter.closeGates();
    }

    @Override
    public void loop() {

        if(gamepad1.left_trigger_pressed){
            intake.setTransferSpeed(transferSpeed);
            shooter.openGates();
        }
        else {
            intake.setTransferSpeed(0);
            shooter.closeGates();
        }

        intake.setIntakeSpeed(0.8);


        shooter.setHoodPos(hoodPos);

        shooter.setPIDF(P,I,D,F);
        shooter.setTargetVelocity(targetVel);
        shooter.shooterUpdate();

        telemetry.addData("current velocity: ", shooter.getCurrentVelocity());
        telemetry.addData("target velocity: ", targetVel);
        telemetry.addData("error: ", targetVel- shooter.getCurrentVelocity());
        telemetry.addData("power: ", shooter.power);
        telemetry.addData("hood pos: ", hoodPos);
        telemetry.update();
    }
}
