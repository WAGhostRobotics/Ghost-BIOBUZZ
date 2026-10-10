package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.components.Intake;
import org.firstinspires.ftc.teamcode.components.Shooter;

@Config
@TeleOp
public class ShooterTest extends OpMode {

    Shooter shooter;
    Intake intake;
    public static double P=0, I=0, D=0, F=0;
    public static double hoodPos = 0.2;
    public static double targetVel = 0;


    @Override
    public void init() {
        shooter = new Shooter(hardwareMap);
        intake = new Intake (hardwareMap);
    }

    @Override
    public void loop() {

        intake.setIntakeSpeed(0.8);
        intake.setTransferSpeed(0.8);

        shooter.setHoodPos(hoodPos);

        shooter.setPIDF(P,I,D,F);
        shooter.setTargetVelocity(targetVel);
        shooter.shooterUpdate();

        telemetry.addData("current velocity: ", shooter.getCurrentVelocity());
        telemetry.addData("target velocity: ", targetVel);
        telemetry.addData("error: ", shooter.shooterError);
        telemetry.addData("power: ", shooter.power);
        telemetry.addData("hood pos: ", hoodPos);
        telemetry.update();
    }
}
