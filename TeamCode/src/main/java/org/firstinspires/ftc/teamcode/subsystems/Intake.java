package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {

    DcMotorEx intakeMotor, transferMotor;
    Servo polliRollerServo;
    public double polliUp, polliMid, polliDown; // TODO: set up positions

    //constants
    private final double INTAKE_POWER = 0.8;
    private final double TRANSFER_POWER = 0.8;

    public Intake(HardwareMap h) {
        intakeMotor = h.get(DcMotorEx.class, "intakeMotor");
        transferMotor = h.get(DcMotorEx.class, "transferMotor");

        polliRollerServo = h.get(Servo.class,"polliRollerServo");

        intakeMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        intakeMotor.setDirection(DcMotorEx.Direction.REVERSE);

        transferMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        transferMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        transferMotor.setDirection(DcMotorEx.Direction.REVERSE);
    }

    //dynamic polli setters
    public void setPolliUp() { polliRollerServo.setPosition(polliUp); }
    public void setPolliMid() { polliRollerServo.setPosition(polliMid); }
    public void setPolliDown() { polliRollerServo.setPosition(polliDown); }

    //dynamic intake setters
    public void setIntakeSpeed(double speed) { intakeMotor.setPower(speed); }
    public void setTransferSpeed(double speed) { transferMotor.setPower(speed); }

    //static setters for abstraction
    public void startIntake() { intakeMotor.setPower(INTAKE_POWER); }
    public void stopIntake() { intakeMotor.setPower(0); }
    public void startTransfer() { transferMotor.setPower(TRANSFER_POWER); }
    public void stopTransfer() { transferMotor.setPower(0); }
}
