package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.core.Charlie.follower;
import static org.firstinspires.ftc.teamcode.core.Charlie.intake;
import static org.firstinspires.ftc.teamcode.core.Charlie.shooter;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.Charlie;

@TeleOp
@Config
public class RedTeleOp extends OpMode {

    double hoodpos = 0.23;
    double targetVelocity = 1280;
    double P = 0.0055 , I=0.0006 , D=0, F=0.009;

    boolean polliDown = false;

    ToggleButtonReader polliButton, headingReset;
    @Override
    public void init() {
        Charlie.init(hardwareMap);

        shooter.closeGates();
        intake.setPolliUp();

        shooter.setHoodPos(hoodpos);

        intake.setPolliUp();

        polliButton = new ToggleButtonReader(new GamepadEx(gamepad1), GamepadKeys.Button.A);
        headingReset = new ToggleButtonReader(new GamepadEx(gamepad1), GamepadKeys.Button.B);

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
            intake.setIntakeSpeed(0.3);
        }

        if (gamepad1.right_trigger > 0.2) {
            intake.setTransferSpeed(0.47);
        } else {
            intake.setTransferSpeed(0);
        }

        if(gamepad1.right_trigger > 0.02 && gamepad1.left_trigger > 0.02) {
            shooter.openGates();
        } else {
            shooter.closeGates();
        }

        if (polliButton.wasJustPressed()) {
            if(polliDown) {
                intake.setPolliUp();
            }
            else {
                intake.setPolliDown();
            }

            polliDown = !polliDown;
        }

        polliButton.readValue();

        shooter.setPIDF(P,I,D,F);
        shooter.setTargetVelocity(targetVelocity);
        shooter.shooterUpdate();

        telemetry.addData("current velocity: ", shooter.getCurrentVelocity());
        telemetry.addData("target velocity: ", targetVelocity);
        telemetry.addData("error: ", targetVelocity- shooter.getCurrentVelocity());
        telemetry.addData("power: ", shooter.power);
        telemetry.addData("hood pos: ", hoodpos);
        telemetry.update();

    }
}