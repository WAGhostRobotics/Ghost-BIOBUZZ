package org.firstinspires.ftc.teamcode.opmodes;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.FieldConstants;
import org.firstinspires.ftc.teamcode.utils.alignment.AlignPID;
import org.firstinspires.ftc.teamcode.subsystems.Vision;

public abstract class BaseTeleop extends OpMode {

    protected Robot charlie;
    protected FieldConstants.Alliance alliance;

    // Arrays renamed to match A and B sides
    protected int[] tagsA;
    protected int[] tagsB;

    private final double FIELD_CENTER_Y = 72.0; //assume center is at 72

    protected AlignPID alignPID;
    protected boolean wasAiming = false;
    protected DrivePowers powers;

    @Override
    public void init() {
        charlie = new Robot(hardwareMap);

        charlie.shooter.closeGates();
        charlie.intake.setPolliUp();
        charlie.shooter.setIdleHood();

        alignPID = new AlignPID(Vector2D.cartesian(0, 0));
    }

    protected void handleAlignmentAndShooting() {
        boolean intentToShoot = gamepad1.right_trigger > 0.02 && gamepad1.left_trigger > 0.02;

        if (intentToShoot && !wasAiming) {
            alignPID.reset();
        }

        if (intentToShoot) {
            Vision.TargetState state = charlie.vision.getTargetState(tagsA, tagsB);

            //determine which half of the field the robot is currently on
            boolean onSideA = charlie.follower.pose().y() > FIELD_CENTER_Y;

            //if we are on that side with the direction up
            boolean validTarget = false;
            if (onSideA && state == Vision.TargetState.A_UP_IN_RANGE) {
                validTarget = true;
            } else if (!onSideA && state == Vision.TargetState.B_UP_IN_RANGE) {
                validTarget = true;
            }

            if (validTarget) {

                Pose visionPose = charlie.vision.getRobotFieldPose();
                if (visionPose != null) {
                    charlie.follower.setPose(new Pose(visionPose.x(), visionPose.y(), charlie.follower.pose().heading()));
                }

                //align explicitly to the side we are on
                powers = alignPID.autoAim(charlie.follower, powers, alliance, onSideA);

                charlie.shooter.openGates();

            } else {
                //wrong side of field, out of range, or NONE (e.g. not valid tags detected)
                charlie.shooter.closeGates();

                if (!wasAiming) {
                    gamepad1.rumble(1.0, 1.0, 500);
                }
            }
        } else {
            charlie.shooter.closeGates();
        }

        wasAiming = intentToShoot;
    }

    @Override
    public void loop() {
        //base translation
        powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_x,
                gamepad1.left_stick_y,
                -gamepad1.right_stick_x,
                charlie.follower.pose().heading()
        );

        //update based on camera vision
        handleAlignmentAndShooting();

        //update drivetrain
        ManualDrive.driveOrHold(charlie.follower, powers);
        charlie.follower.update();

        //other subsystems
        if (gamepad1.left_trigger > 0.02) charlie.intake.startIntake();
        else charlie.intake.stopIntake();

        if (gamepad1.right_trigger > 0.2)  charlie.intake.startTransfer();
        else charlie.intake.stopTransfer();

        charlie.shooter.spinUp();
    }
}