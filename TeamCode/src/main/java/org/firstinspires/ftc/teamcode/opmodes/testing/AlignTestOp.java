package org.firstinspires.ftc.teamcode.opmodes.testing;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.utils.FieldConstants;
import org.firstinspires.ftc.teamcode.utils.alignment.AlignPID;

@TeleOp(name="align test op")
public class AlignTestOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Follower f = Constants.create(hardwareMap);
        FieldConstants.Alliance alliance = FieldConstants.Alliance.RED; //change
        AlignPID align;
        boolean aimHiveA = true;
        boolean wasAiming = false;

        Pose startPose = new Pose(0,0,0);

        //if previous_pose != null then .setPose(previous_pose) else startPose
        f.setPose(startPose);

        align = new AlignPID(FieldConstants.hive(alliance,aimHiveA));

        waitForStart();
        while(opModeIsActive()){
            f.update();
            Pose p = f.pose();

            if (gamepad1.dpad_left){
                aimHiveA = true;
            }
            if (gamepad1.dpad_right){
                aimHiveA = false;
            }
            align.setTarget(FieldConstants.hive(alliance,aimHiveA));

            DrivePowers robotRel = new DrivePowers(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
            DrivePowers fieldDrive = ManualDrive.fieldCentric(robotRel, p.heading(), -FieldConstants.driverForward(alliance));

            boolean aiming = gamepad1.right_bumper;
            if (aiming && !wasAiming){
                align.reset();
            }
            wasAiming = aiming;

            if (aiming){
                fieldDrive = align.aim(f,fieldDrive);
            }
            f.manual(fieldDrive);

            telemetry.addData("Pose", "x %.4f  y %.4f  h %.4f°", p.x(), p.y(), Math.toDegrees(p.heading()));
            telemetry.addData("Target", "%s cell %s", alliance, aimHiveA ? "A" : "B");
        }
    }
}
