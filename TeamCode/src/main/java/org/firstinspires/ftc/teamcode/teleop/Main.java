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
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.alignment.AlignPID;
import org.firstinspires.ftc.teamcode.alignment.FieldConstants;
import org.firstinspires.ftc.teamcode.core.Charlie;

@TeleOp
@Config
public class Main extends OpMode {

    double hoodpos = 0.25;

    public static boolean blue = false;
    double targetVelocity = 1370;
    double P = 0.0055 , I=0.0006 , D=0, F=0.009;

    boolean polliDown = false;

    ToggleButtonReader polliButton, headingReset, autoHeading;

    public static Pose startPose;

    AlignPID align;
    FieldConstants.Alliance alliance;
    boolean aimHiveA = true, wasShooting = false;

    @Override
    public void init() {
        Charlie.init(hardwareMap);

        shooter.closeGates();
        intake.setPolliUp();

        shooter.setHoodPos(hoodpos);

        intake.setPolliUp();

        polliButton = new ToggleButtonReader(new GamepadEx(gamepad1), GamepadKeys.Button.A);
        headingReset = new ToggleButtonReader(new GamepadEx(gamepad1), GamepadKeys.Button.B);
        autoHeading = new ToggleButtonReader(new GamepadEx(gamepad1), GamepadKeys.Button.DPAD_LEFT);

        alliance = blue ? FieldConstants.Alliance.BLUE : FieldConstants.Alliance.RED;
        align = new AlignPID(FieldConstants.hive(alliance, aimHiveA));

    }

    @Override
    public void loop() {

        if (gamepad1.dpad_left) aimHiveA = true;
        if (gamepad1.dpad_right) aimHiveA = false;
        align.setTarget(FieldConstants.hive(alliance, aimHiveA));

        DrivePowers robotRel = new DrivePowers(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
        DrivePowers powers = ManualDrive.fieldCentric(robotRel, follower.pose().heading(), -FieldConstants.driverForward(alliance));

        // while shooting (both triggers), the robot turns itself to face the goal
        boolean shooting = gamepad1.right_trigger > 0.02 && gamepad1.left_trigger > 0.02;
        if (shooting) {
            if (!wasShooting) align.reset();
            powers = align.aim(follower, powers);
        }
        wasShooting = shooting;

        follower.manual(powers);
        follower.update();


        // relocalise button
        if (headingReset.wasJustPressed()) {
            Pose cornerPose = new Pose(8.270, 8.224, Math.toRadians(90));
            follower.setPose(cornerPose);
        }

        Pose robotPose = follower.pose();

        headingReset.readValue();

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
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        telemetry.update();

    }
}