package org.firstinspires.ftc.teamcode.autos;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static org.firstinspires.ftc.teamcode.core.Charlie.follower;

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
public class BlueUp_Interference extends OpMode {
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(109.3864, 133.4322, 90);
    private final Pose path1 = poseFactory.of(94.6145, 122.0403, 250);
    private final Pose point2 = poseFactory.of(133.3727, 129.5018, 450);
    private final Pose point2Control1 = poseFactory.of(119.9835, 106.8315, 0);
    private final Pose point3 = poseFactory.of(133.3443, 133.3159, 90.4264);
    private final Pose point4Start = poseFactory.of(133.3443, 133.3159, 450);
    private final Pose point4 = poseFactory.of(97.6255, 26.7234, 120);
    private final Pose point4Control1 = poseFactory.of(108.0568, 104.7976, 0);
    private final Pose point4Control2 = poseFactory.of(128.5668, 32.7656, 0);
    private final Pose point5 = poseFactory.of(123.9524, 94.0788, 0);
    private final Pose point5Control1 = poseFactory.of(117.2711, 41.0815, 0);
    private final Pose point6 = poseFactory.of(132.9725, 94.0907, 0.0756);
    private final Pose point7 = poseFactory.of(124.5275, 93.9496, 0.9567);
    private final Pose point8Start = poseFactory.of(124.5275, 93.9496, 0);
    private final Pose point8 = poseFactory.of(94.9075, 122.0174, 250);
    private final Pose point9 = poseFactory.of(125.17, 44.11, 250);

    public Path path1() {
        return Paths.line(start, path1).constant(path1);
    }

    public Path path2() {
        return Paths.curve(path1, point2Control1, point2).linear(path1, point2);
    }

    public Path path3() {
        return Paths.line(point2, point3).tangent();
    }

    public Path path4() {
        return Paths.curve(point4Start, point4Control1, point4Control2, point4).linear(point4Start, point4);
    }

    public Path path5() {
        return Paths.curve(point4, point5Control1, point5).linear(point4, point5);
    }

    public Path path6() {
        return Paths.line(point5, point6).tangent();
    }

    public Path path7() {
        return Paths.line(point6, point7).reverseTangent();
    }

    public Path path8() {
        return Paths.line(point8Start, point8).linear(point8Start, point8);
    }

    public Path path9() {
        return Paths.line(point8, point9).constant(point9);
    }

    private Command autoRoutine() {
        return sequential(
                follow(follower, path1()),
                //shoot
                follow(follower, path2()),
                follow(follower, path3()),
                //garden intake
                follow(follower, path4()),
                //shoot
                follow(follower, path5()),
                //polli down
                follow(follower, path6()),
                // flower intake
                follow(follower, path7()),
                follow(follower, path8()),
                // shoot
                follow(follower, path9())
                // park
        );
    }

    @Override
    public void init() {
        Scheduler.reset();

        Charlie.init(hardwareMap);
        follower.setPose(start);
        follower.update();
    }

    @Override
    public void start() {
        schedule(autoRoutine());
    }

    @Override
    public void loop() {

        follower.update();
        Scheduler.execute();

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());
        telemetry.update();
    }
}
