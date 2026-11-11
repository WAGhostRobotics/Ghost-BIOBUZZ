package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

@Config
@TeleOp
public class ShooterTest extends OpMode {

    public static double P, I, D, F;
    public static double hoodPos = 0.20;
    public static double targetVel = 0;
    public static double transferSpeed = 0;

    Shooter shooter;
    Intake intake;

    @Override
    public void init() {
        shooter = new Shooter(hardwareMap);
        intake = new Intake(hardwareMap);
        shooter.closeGates();
    }

    @Override
    public void loop() {
        boolean feeding = gamepad1.left_trigger_pressed;
        intake.setTransferSpeed(feeding ? transferSpeed : 0);
        if (feeding) shooter.openGates();
        else shooter.closeGates();
        intake.startIntake();

        shooter.setHoodPos(hoodPos);
        shooter.setPIDF(P, I, D, F);
        shooter.setTargetVelocity(targetVel);
        shooter.shooterUpdate();

        telemetry.addData("current velocity", shooter.getCurrentVelocity());
        telemetry.addData("target velocity", targetVel);
        telemetry.addData("error", shooter.shooterError);
        telemetry.addData("power", shooter.power);
        telemetry.addData("hood pos", hoodPos);
        telemetry.update();
    }
}