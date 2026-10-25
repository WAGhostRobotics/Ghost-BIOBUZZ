package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.gamepad.ToggleButtonReader;
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

    //charlie
    protected Robot charlie;
    protected FieldConstants.Alliance alliance;

    //vision
    protected int[] tagsA, tagsB;
    private final double FIELD_CENTER_Y = 72.0;
    protected boolean wasAiming = false;

    //pid
    protected AlignPID alignPID;
    protected DrivePowers powers;

    //controls
    private boolean polliDown = false;
    ToggleButtonReader polliButton, headingReset;

    @Override
    public void init() {
        charlie = new Robot(hardwareMap);

        charlie.shooter.closeGates();
        charlie.intake.setPolliUp();
        charlie.shooter.setIdleHood();

        polliButton = new ToggleButtonReader(new GamepadEx(gamepad1), GamepadKeys.Button.A);
        headingReset = new ToggleButtonReader(new GamepadEx(gamepad1), GamepadKeys.Button.B);

        alignPID = new AlignPID(Vector2D.cartesian(0, 0));

        //update from teleop
        charlie.follower.setPose(FieldConstants.endAutoPose);
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

        if (Robot.visionEnabled) {
            //update based on camera vision
            handleAlignmentAndShooting();
        }

        //update drivetrain
        ManualDrive.driveOrHold(charlie.follower, powers);
        charlie.follower.update();

        //intake
        if (gamepad1.left_trigger > 0.02) charlie.intake.startIntake();
        else charlie.intake.idleIntake();

        //transfer
        if (gamepad1.right_trigger > 0.2)  charlie.intake.startTransfer();
        else charlie.intake.stopTransfer();

        //polli
        if (polliButton.wasJustPressed()) {
            if (polliDown) { charlie.intake.setPolliUp(); }
            else { charlie.intake.setPolliDown(); }

            polliDown = !polliDown;
        }

        //shooter
        if (gamepad1.right_trigger > 0.02 && gamepad1.left_trigger > 0.02) { charlie.shooter.openGates(); }
        else { charlie.shooter.closeGates(); }

        charlie.shooter.spinUp();
    }
}