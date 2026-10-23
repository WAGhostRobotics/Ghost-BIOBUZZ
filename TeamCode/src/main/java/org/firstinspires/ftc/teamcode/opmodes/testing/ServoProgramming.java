package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Servo Zeroing", group="Test")
public class ServoProgramming extends LinearOpMode {
    private Servo servo;
    private double servoPos;

    @Override
    public void runOpMode() {
        servo = hardwareMap.get(Servo.class, "myServo");
        servoPos = 0;
        servo.setPosition(servoPos);


        telemetry.addData("Status", "Initialized. Servo set to 0.0");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.a) { servo.setPosition(0); } //zero
            else if (gamepad1.y) { servo.setPosition(1); } //full
            else if (gamepad1.right_bumper) { servoPos += 0.01; servo.setPosition(servoPos); }
            else if (gamepad1.left_bumper) { servoPos -= 0.01; servo.setPosition(servoPos); }

            telemetry.addData("Current Position", servo.getPosition());
            telemetry.update();
        }
    }

}
