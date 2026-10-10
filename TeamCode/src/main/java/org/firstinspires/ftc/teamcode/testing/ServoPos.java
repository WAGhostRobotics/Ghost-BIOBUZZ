package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp
public class ServoPos extends OpMode {

    Servo s;

    public static double servoPos;

    @Override
    public void init() {

        s = hardwareMap.get(Servo.class, "s");
        servoPos = 0.5;

    }

    @Override
    public void loop() {

        s.setPosition(servoPos);

        telemetry.addData("servo position: ", servoPos);
        telemetry.update();

    }
}
