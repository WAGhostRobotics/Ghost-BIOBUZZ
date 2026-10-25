package org.firstinspires.ftc.teamcode.autos;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static org.firstinspires.ftc.teamcode.core.Charlie.follower;
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
public class BlueDown extends OpMode {
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(79.3242, 8.5183, 90);
    private final Pose path1 = poseFactory.of(82.4341, 18.8956, 90);
    private final Pose point2 = poseFactory.of(94.4258, 15.1291, -90);
    private final Pose point2Control1 = poseFactory.of(92.0055, 33.8278, 0);
    private final Pose point3 = poseFactory.of(94.3745, 8.6923, -90.4565);
    private final Pose point4Start = poseFactory.of(94.3745, 8.6923, -90);
    private final Pose point4 = poseFactory.of(82.38, 18.7601, 90);
    private final Pose point4Control1 = poseFactory.of(93.685, 35.9689, 0);
    private final Pose point5 = poseFactory.of(124.8736, 44.0421, 250);

    public Path shoot1() {
        return Paths.line(start, path1).constant(path1);
    }

    public Path path2() {
        return Paths.curve(path1, point2Control1, point2).linear(path1, point2);
    }

    public Path path3() {
        return Paths.line(point2, point3).tangent();
    }

    public Path path4() {
        return Paths.curve(point4Start, point4Control1, point4).linear(point4Start, point4);
    }

    public Path path5() {
        return Paths.line(point4, point5).linear(point4, point5);
    }

    private Command autoRoutine() {
        return sequential(
                follow(follower, shoot1()),
                //shoot
                follow(follower, path2()),
                // put down the polliroller
                follow(follower, path3()),
                // flower intake
                follow(follower, path4()),
                //shoot
                follow(follower, path5())
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
