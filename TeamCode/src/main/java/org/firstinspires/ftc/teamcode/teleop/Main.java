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

import org.firstinspires.ftc.teamcode.components.alignment.Align;
import org.firstinspires.ftc.teamcode.core.Charlie;

@TeleOp
@Config
public class Main extends OpMode {


    Pose cornerPose = new Pose(7.75, 8.21, Math.toRadians(90));
    double hoodpos = 0.25;
    double targetVelocity = 1360;
    double P = 0.0055 , I=0.0006 , D=0, F=0.009;
    ToggleButtonReader headingReset, autoHeading;

    boolean aligning;

    @Override
    public void init() {

        Charlie.init(hardwareMap);

        shooter.closeGates();
        intake.setPolliUp();

        shooter.setHoodPos(hoodpos);

        intake.setPolliUp();

        headingReset = new ToggleButtonReader(new GamepadEx(gamepad1), GamepadKeys.Button.B);
        autoHeading = new ToggleButtonReader(new GamepadEx(gamepad1), GamepadKeys.Button.DPAD_LEFT);

        follower.setPose(cornerPose);
        follower.update();

    }

    @Override
    public void loop() {

        aligning = gamepad1.right_trigger > 0.02 && gamepad1.left_trigger > 0.02;

        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
//                aligning ? 0 : -gamepad1.right_stick_x, // stick turn ignored while aligning
                follower.pose().heading()
        );

//        if (aligning) {
//            alignRobot.setTargetHive(follower.pose().y());
//            alignRobot.setTargetHeading();
//            follower.setHeading(alignRobot.getTargetHeading());
//        }

        follower.manual(powers);

        // relocalise button -- goes to some random position everytime but when done in init, it does what I want it to do
        if (headingReset.wasJustPressed()) {
            follower.setPose(cornerPose);
        }

        follower.update();
        Pose robotPose = follower.pose(); // returns a Pose object

        headingReset.readValue();

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

        /// FOR CUBSCOUTS

        if(gamepad2.left_trigger > 0.02) {
            intake.setIntakeSpeed(0.8);
        } else {
            intake.setIntakeSpeed(0.3);
        }

        if (gamepad2.right_trigger > 0.2) {
            intake.setTransferSpeed(0.6);
        } else {
            intake.setTransferSpeed(0);
        }

        if(gamepad2.right_trigger > 0.02 && gamepad2.left_trigger > 0.02) {
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
        telemetry.addData("target heading", alignRobot.getTargetHeading());
        telemetry.addData("heading error", alignRobot.getHeadingError());
        telemetry.update();
    }
}