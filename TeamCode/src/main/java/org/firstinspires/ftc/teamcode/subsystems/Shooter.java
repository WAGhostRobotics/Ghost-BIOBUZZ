package org.firstinspires.ftc.teamcode.subsystems;


import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Shooter {

    DcMotorEx shooterL, shooterR;
    Servo gateL, gateR, hood;
<<<<<<< HEAD:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/subsystems/Shooter.java

    private static final double SHOOT_POWER = 1360;
    private static final double IDLE_HOOD_POS = 0.25;
    double OPEN_GATE_L = .5, OPEN_GATE_R = 0.5, CLOSE_GATE_L = 0.34, CLOSE_GATE_R = 0.75;
    double P = 0.0055 , I = 0.0006 , D = 0, F = 0.009;
    public double currentVelocity;
    public double targetVelocity;
=======
    double openGateL = .5, openGateR = 0.5, closeGateL = 0.34, closeGateR = 0.75;
    double P = 0.0055 , I=0.0006 , D=0, F=0.009;
    double currentVelocity;
    double targetVelocity;
>>>>>>> origin/master:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/components/Shooter.java
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
        gateL.setPosition(OPEN_GATE_L);
        gateR.setPosition(OPEN_GATE_R);
    }

    public void closeGates() {
        gateL.setPosition(CLOSE_GATE_L);
        gateR.setPosition(CLOSE_GATE_R);
    }

    public void setPIDF(double kP, double kI, double kD, double kF){
        P = kP;
        I = kI;
        D = kD;
        F = kF;

        shooterPID.setPIDF(P,I,D,F);
    }

<<<<<<< HEAD:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/subsystems/Shooter.java
    public double getCurrentVelocity(){ return shooterR.getVelocity(); }
    public void setTargetVelocity(double tarV) { targetVelocity = tarV; }

    public double getHoodPos() { return hood.getPosition(); }

    public void shooterUpdate(){
        currentVelocity = getCurrentVelocity();
=======
    public double getCurrentVelocity(){

        return shooterR.getVelocity();
    }
    public void setTargetVelocity(double tarV) {
        targetVelocity = tarV;
    }

    public void shooterUpdate(){
        currentVelocity = shooterR.getVelocity();
>>>>>>> origin/master:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/components/Shooter.java
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

<<<<<<< HEAD:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/subsystems/Shooter.java
    public void setHoodPos(double pos) { hood.setPosition(pos); }

    public void spinUp() { setTargetVelocity(SHOOT_POWER); }
    public void stopShooter() { setShooterPower(0); }
    public void setIdleHood() { hood.setPosition(IDLE_HOOD_POS); }
=======
    public void setHoodPos(double pos) {
        hood.setPosition(pos);
    }
    public double getHoodPos() {return hood.getPosition();}
>>>>>>> origin/master:TeamCode/src/main/java/org/firstinspires/ftc/teamcode/components/Shooter.java
}
