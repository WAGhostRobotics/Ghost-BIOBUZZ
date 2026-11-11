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

        if(gamepad1.a) {
            if(gamepad1.dpad_up) {
                P = P + 0.001;
            }
            else if(gamepad1.dpad_down) {
                P-= 0.001;
            }
        }

        if(gamepad1.b) {
            if(gamepad1.dpad_up) {
                I = I + 0.001;
            }
            else if(gamepad1.dpad_down) {
                I-= 0.001;
            }
        }

        if(gamepad1.x) {
            if(gamepad1.dpad_up) {
                D = D + 0.001;
            }
            else if(gamepad1.dpad_down) {
                D-= 0.001;
            }
        }

        if(gamepad1.y) {
            if(gamepad1.dpad_up) {
                F = F + 0.001;
            }
            else if(gamepad1.dpad_down) {
                F-= 0.001;
            }
        }

        if(gamepad1.right_bumper) {
            if(gamepad1.dpad_up) {
                transferSpeed+= 0.01;
            }
            else if(gamepad1.dpad_down) {
                transferSpeed-= 0.01;
            }
        }

        if(gamepad1.left_bumper ) {
            if(gamepad1.dpad_up) {
                intakeSpeed+= 0.01;
            }
            else if(gamepad1.dpad_down) {
                intakeSpeed-= 0.01;
            }
        }

        if (gamepad1.dpad_right) {
            targetVel += 100;
        }
        else if(gamepad1.dpad_left) {
            targetVel -= 100;
        }

        us.setTransferSpeed(transferSpeed);
        us.setIntakeSpeed(intakeSpeed);

        us.setPID(P,I,D,F);
        us.setTargetVel(targetVel);
        us.update();

        telemetry.addData("current velocity: ", us.getCurrentVelocity());
        telemetry.addData("target velocity: ", targetVel);
        telemetry.addData("error: ", targetVel- us.getCurrentVelocity());
        telemetry.addData("power: ", us.power);
        telemetry.addData("P: ", P);
        telemetry.addData("I: ", I);
        telemetry.addData("D: ", D);
        telemetry.addData("F: ", F);
        telemetry.update();
    }
}
