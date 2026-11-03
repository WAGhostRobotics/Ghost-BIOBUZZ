package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.core.Charlie.alignRobot;
import static org.firstinspires.ftc.teamcode.core.Charlie.follower;
import static org.firstinspires.ftc.teamcode.core.Charlie.intake;
import static org.firstinspires.ftc.teamcode.core.Charlie.shooter;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.Charlie;

@TeleOp
@Config
public class Main extends OpMode {

    double hoodpos = 0.25;
    double targetVelocity = 1360;
    double P = 0.0055 , I=0.0006 , D=0, F=0.009;
    ToggleButtonReader headingReset, autoHeading;

    @Override
    public void init() {

        telemetry.addLine("init started");
        telemetry.update();

        Charlie.init(hardwareMap);

        shooter.closeGates();
        intake.setPolliUp();

        shooter.setHoodPos(hoodpos);

        intake.setPolliUp();

        headingReset = new ToggleButtonReader(new GamepadEx(gamepad1), GamepadKeys.Button.B);
        autoHeading = new ToggleButtonReader(new GamepadEx(gamepad1), GamepadKeys.Button.DPAD_LEFT);

        telemetry.addLine("init done");
        telemetry.update();

    }

    @Override
    public void loop() {
        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                follower.pose().heading()
        );

        ManualDrive.driveOrHold(follower, powers);


        // auto heading

        /**
        if(gamepad1.right_trigger > 0.02 && gamepad1.left_trigger > 0.02) {
            gamepad1.right_stick_x = 0;
            alignRobot.setTargetHive(follower.pose().y());
            alignRobot.setTargetHeading();
            alignRobot.head2Target();
        }


        // relocalise button
        if (headingReset.wasJustPressed()) {
            Pose cornerPose = new Pose(7.75, 8.21, Math.toRadians(90));
            follower.setPose(cornerPose);
        }

        headingReset.readValue();
         */

        follower.update();
        Pose robotPose = follower.pose(); // returns a Pose object

        // DRIVETRAIN STUFF IS OVER

        if(gamepad1.left_trigger > 0.02) {
            intake.setIntakeSpeed(0.8);
        } else {
            intake.setIntakeSpeed(0.3);
        }

        if (gamepad1.right_trigger > 0.2) {
            intake.setTransferSpeed(0.6);
        } else {
            intake.setTransferSpeed(0);
        }

        if(gamepad1.right_trigger > 0.02 && gamepad1.left_trigger > 0.02) {
            shooter.openGates();
        } else {
            shooter.closeGates();
        }

        if (gamepad1.left_bumper) {
            intake.setPolliDown();
        }
        else {
            intake.setPolliUp();
        }

        shooter.setPIDF(P,I,D,F);
        shooter.setTargetVelocity(targetVelocity);
        shooter.shooterUpdate();

        telemetry.addData("current velocity: ", shooter.getCurrentVelocity());
        telemetry.addData("target velocity: ", targetVelocity);
        telemetry.addData("error: ", targetVelocity- shooter.getCurrentVelocity());
        telemetry.addData("power: ", shooter.power);
        telemetry.addData("hood pos: ", hoodpos);
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        telemetry.update();
    }
}