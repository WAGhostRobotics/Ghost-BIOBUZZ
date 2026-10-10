package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {

    DcMotor intake, transfer;
    Servo polliRoller;
    public double polliUp, polliMid, polliDown; // TODO: set up positions

    public Intake(HardwareMap h) {
        intake = h.get(DcMotorEx.class, "intake");
        transfer = h.get(DcMotorEx.class, "transfer");

        polliRoller = h.get(Servo.class,"polliRoller");

        polliRoller.setPosition(polliUp);

        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake.setDirection(DcMotorSimple.Direction.REVERSE);

        transfer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        transfer.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        transfer.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setPolliUp() {
        polliRoller.setPosition(polliUp);
    }

    public void setPolliMid() {
        polliRoller.setPosition(polliMid);
    }

    public void setPolliDown() {
        polliRoller.setPosition(polliDown);
    }

    public void setIntakeSpeed(double speed) {
        intake.setPower(speed);
    }

    public void setTransferSpeed(double speed) {
        transfer.setPower(speed);
    }
}
