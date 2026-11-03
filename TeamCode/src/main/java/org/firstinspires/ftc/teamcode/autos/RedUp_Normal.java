package org.firstinspires.ftc.teamcode.autos;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static org.firstinspires.ftc.teamcode.core.Charlie.follower;
import static org.firstinspires.ftc.teamcode.core.Charlie.intake;
import static org.firstinspires.ftc.teamcode.core.Charlie.shooter;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.core.Charlie;

@Autonomous
public class RedUp_Normal extends OpMode {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(32.1124, 8.4829, 90);
    private final Pose path1 = poseFactory.of(36.2819, 14.2971, 60);
    private final Pose point2 = poseFactory.of(8.3187, 8.3287, 270);
    private final Pose point2Control1 = poseFactory.of(14.1319, 36.5769, 0);
    private final Pose point3 = poseFactory.of(36.1894, 14.3814, 60);
    private final Pose point3Control1 = poseFactory.of(14.6433, 37.0896, 0);
    private final Pose point4 = poseFactory.of(13.1894, 91.6587, 90);

    public Path toShoot() {
        return Paths.line(start, path1).linear(start, path1);
    }

    public Path intakeGarden() {
        return Paths.curve(path1, point2Control1, point2).linear(path1, point2);
    }

    public Path back2Shoot() {
        return Paths.curve(point2, point3Control1, point3).linear(point2, point3);
    }

    public Path park() {
        return Paths.line(point3, point4).linear(point3, point4);
    }

    Command spinUp = waitMs(1000);
    Command shootTime = waitMs(1000);
    Command gardenTime = waitMs(3000);

    Command openGate = instant(() -> shooter.openGates());
    Command closeGate = instant(() -> shooter.closeGates());

    Command intakeOn = instant(() -> intake.setIntakeSpeed(0.8));

    Command intakeOff = instant(() -> intake.setIntakeSpeed(0.2));
    Command transferOn = instant(() -> intake.setTransferSpeed(0.5));
    Command transferOff = instant(() -> intake.setTransferSpeed(0));

    private Command shootBalls() {
        return parallel(
                transferOn,
                intakeOn,
                shootTime,
                openGate
        );
    }

    private Command stopShootBalls() {
        return parallel(
                transferOff,
                intakeOff,
                closeGate
        );
    }



    private Command intakeGardenCommand() {
        return parallel(
                follow(follower, intakeGarden()),
                intakeOn,
                gardenTime
        );
    }



    private Command autoRoutine() {
        return sequential(
                spinUp,
                follow(follower, toShoot()),
                shootBalls(),
                stopShootBalls(),
                intakeGardenCommand(),
                intakeOff,
                follow(follower, back2Shoot()),
                shootBalls(),
                stopShootBalls(),
                follow(follower, park())
        );
    }

    double hoodpos = 0.25;

    double targetVelocity = 1360;
    double P = 0.0055 , I=0.0006 , D=0, F=0.009;

    @Override
    public void init() {
        Scheduler.reset();

        Charlie.init(hardwareMap);
        follower.setPose(start);
        follower.update();

        intake.setPolliUp();

        shooter.setHoodPos(hoodpos);
    }

    @Override
    public void start() {
        schedule(autoRoutine());
    }

    @Override
    public void loop() {

        follower.update();
        Scheduler.execute();

        shooter.setPIDF(P,I,D,F);
        shooter.setTargetVelocity(targetVelocity);
        shooter.shooterUpdate();

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());
        telemetry.update();
    }
}
