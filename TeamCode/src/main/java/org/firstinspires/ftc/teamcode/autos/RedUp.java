package org.firstinspires.ftc.teamcode.autos;

import static org.firstinspires.ftc.teamcode.core.Charlie.follower;
import static org.firstinspires.ftc.teamcode.core.Charlie.intake;
import static org.firstinspires.ftc.teamcode.core.Charlie.shooter;

import static com.pedropathing.api.Paths.*;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.Paths;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.Path;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.core.Charlie;

@Autonomous
public class RedUp extends OpMode {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(32.1575, 8.5183, 90);
    private final Pose path1 = poseFactory.of(41.4872, 14.4899, 56);
    private final Pose point2Start = poseFactory.of(41.4872, 14.4899, 180);
    private final Pose point2 = poseFactory.of(9.5797, 8.2701, 180);
    private final Pose point2Control1 = poseFactory.of(27.6584, 6.8022, 0);
    private final Pose point3 = poseFactory.of(43.4954, 123.2726, 300);
    private final Pose point3Control1 = poseFactory.of(28.8114, 29.7304, 0);
    private final Pose point3Control2 = poseFactory.of(13.0922, 84.1365, 0);
    private final Pose point4 = poseFactory.of(17.5293, 117.1923, 300);

    public Path preload() {
        return Paths.line(start, path1).linear(start, path1);
    }

    public Path garden() {
        return Paths.curve(point2Start, point2Control1, point2).linear(point2Start, point2);
    }

    public Path gardenShoot() {
        return Paths.curve(point2, point3Control1, point3Control2, point3).linear(point2, point3);
    }

    public Path park() {
        return Paths.line(point3, point4).constant(point4);
    }

    Command shootTime = waitMs(2000);
    Command spinUp = waitMs(3000);

    Command openGate = instant(() -> shooter.openGates());
    Command closeGate = instant(() -> shooter.closeGates());

    Command intakeOn = instant(() -> intake.setIntakeSpeed(0.8));

    Command intakeOff = instant(() -> intake.setIntakeSpeed(0));
    Command transferOn = instant(() -> intake.setTransferSpeed(0.8));
    Command transferOff = instant(() -> intake.setTransferSpeed(0));
    Command gateOpenWait = waitMs(300);

    private Command shootBalls() {
        return parallel(
                transferOn,
                intakeOn,
                shootTime
        );
    }



    private Command autoRoutine() {
        return sequential(
                spinUp,
                follow(follower, preload()),
                openGate,
                gateOpenWait,
                shootBalls(),
                closeGate,
                transferOff,
                follow(follower, garden()),
                shootTime,
                intakeOff,
                follow(follower, gardenShoot()),
                openGate,
                shootBalls(),
                follow(follower, park())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();

        Charlie.init(hardwareMap);
        follower.setPose(start);
        follower.update();
        shooter.closeGates();

        shooter.setHoodPos(0.27);
    }

    @Override
    public void start() {
        schedule(autoRoutine());
    }

    @Override
    public void loop() {

        follower.update();
        Scheduler.execute();

        shooter.setShooterPower(0.56);

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());
        telemetry.update();
    }
}
