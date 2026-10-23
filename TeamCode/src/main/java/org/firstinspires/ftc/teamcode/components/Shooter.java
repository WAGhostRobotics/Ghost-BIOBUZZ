package org.firstinspires.ftc.teamcode.components;


import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Shooter {

    DcMotorEx shooterL, shooterR;
    Servo gateL, gateR, hood;
    double openGateL = .4, openGateR = 0.5, closeGateL = 0.3278, closeGateR = 0.73; // TODO: change gateR positions
    double P, I, D, F;
    double currentVelocity;
    double targetVelocity;
    public double shooterError;
    public double power;
    PIDFController shooterPID;

    public Shooter(HardwareMap h) {

        shooterL = h.get(DcMotorEx.class, "shooterL");
        shooterR = h.get(DcMotorEx.class, "shooterR");

        gateL = h.get(Servo.class, "gateL");
        gateR = h.get(Servo.class, "gateR");

        hood = h.get(Servo.class, "hood");

        shooterR.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        shooterL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        shooterPID = new PIDFController(P,I,D,F);
        shooterPID.setIntegrationBounds(-10000000, 10000000);

        targetVelocity = 0;
    }

    public void openGates() {
        gateL.setPosition(openGateL);
        gateR.setPosition(openGateR);
    }

    public void closeGates() {
        gateL.setPosition(closeGateL);
        gateR.setPosition(closeGateR);
    }

    public void setPIDF(double kP, double kI, double kD, double kF){
        P = kP;
        I = kI;
        D = kD;
        F = kF;

        shooterPID.setPIDF(P,I,D,F);
    }

    public double getCurrentVelocity(){
        return shooterL.getVelocity();
    }

    public void setTargetVelocity(double tarV) {
        targetVelocity = tarV;
    }

    public void shooterUpdate(){
        currentVelocity = shooterL.getVelocity(AngleUnit.DEGREES);
        shooterError = targetVelocity - currentVelocity;

        power = shooterPID.calculate(0,shooterError);

        power = Range.clip(power, -1, 1);

        shooterL.setPower(power);
        shooterR.setPower(power);
    }

    public void setShooterPower(double power) {
        shooterL.setPower(power);
        shooterR.setPower(power);
    }

    public void setHoodPos(double pos) {
        hood.setPosition(pos);
    }
    public double getHoodPos() {return hood.getPosition();}
}
