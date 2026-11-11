package org.firstinspires.ftc.teamcode.testing;

import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

public class Undershooter {

    DcMotorEx shooterL, shooterR, intake, transfer;

    double P,I,D,F;
    double currentVel, targetVel, error,power;
    double intakeSpeed, transferSpeed;

    PIDFController undershooter;
    public Undershooter(HardwareMap h) {
        shooterL = h.get(DcMotorEx.class, "shooterL");
        shooterR = h.get(DcMotorEx.class, "shooterR");
        intake = h.get(DcMotorEx.class, "intake");
        transfer = h.get(DcMotorEx.class, "transfer");

        shooterL.setDirection(DcMotorSimple.Direction.REVERSE);

        transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        undershooter = new PIDFController(P,I,D,F);
        undershooter.setIntegrationBounds(-1000000, 1000000);
    }

    public void setPID(double kP, double kI, double kD, double kF) {
        P = kP;
        I = kI;
        D = kD;
        F = kF;

        undershooter.setPIDF(P,I,D,F);
    }

    public void setTargetVel(double tar) {
        targetVel = tar;
    }

    public double getCurrentVelocity(){
        currentVel = shooterR.getVelocity();
        return currentVel;
    }

    public void update() {
        currentVel = shooterR.getVelocity();
        error = targetVel - currentVel;

        power = undershooter.calculate(0,error);

        power = Range.clip(power, -1, 1);

        shooterL.setPower(power);
        shooterR.setPower(power);
    }

    public void setIntakeSpeed(double speed) {
        intake.setPower(speed);
    }

    public void setTransferSpeed(double speed) {
        transfer.setPower(speed);
    }




}
