package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.core.Charlie.follower;
import static org.firstinspires.ftc.teamcode.core.Charlie.intake;
import static org.firstinspires.ftc.teamcode.core.Charlie.shooter;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.Charlie;

@TeleOp
@Config
public class Main extends OpMode {


    double hoodpos = 0.3;
    double shooterPw = 0.62;
    @Override
    public void init() {
        Charlie.init(hardwareMap);

        shooter.closeGates();
        intake.setPolliUp();

        shooter.setHoodPos(hoodpos);
    }

    @Override
    public void loop() {

        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_x,
                gamepad1.left_stick_y,
                -gamepad1.right_stick_x,
                follower.pose().heading()
        );

        ManualDrive.driveOrHold(follower, powers);
        follower.update();

        if(gamepad1.left_trigger > 0.02) {
            intake.setIntakeSpeed(0.8);
        } else {
            intake.setIntakeSpeed(0);
        }

        if (gamepad1.right_trigger > 0.2) {
            intake.setTransferSpeed(0.8);
        } else {
            intake.setTransferSpeed(0);
        }

        if(gamepad1.right_trigger > 0.02 && gamepad1.left_trigger > 0.02) {
            shooter.openGates();
        } else {
            shooter.closeGates();
        }

        shooter.setShooterPower(shooterPw);

    }
}
