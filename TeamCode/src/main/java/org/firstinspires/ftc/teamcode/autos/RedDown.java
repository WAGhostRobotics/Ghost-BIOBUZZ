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
public class RedDown extends OpMode {
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(62.2198, 133.1731, 90);
    private final Pose path1 = poseFactory.of(58.0733, 119.7079, 270);
    private final Pose point2 = poseFactory.of(47.4899, 126.0797, 90);
    private final Pose point2Control1 = poseFactory.of(44.1685, 104.8397, 0);
    private final Pose point3 = poseFactory.of(47.5348, 133.2005, 89.639);
    private final Pose point4Start = poseFactory.of(47.5348, 133.2005, 90);
    private final Pose point4 = poseFactory.of(58.2198, 119.7894, 270);
    private final Pose point4Control1 = poseFactory.of(42.9487, 102.5696, 0);
    private final Pose point5 = poseFactory.of(12.7, 92.31, 60);

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
        return Paths.curve(point4Start, point4Control1, point4).linear(point4Start, point4);
    }

    public Path path5() {
        return Paths.line(point4, point5).linear(point4, point5);
    }

    private Command autoRoutine() {
        return sequential(
                follow(follower, path1()),
                //shoot
                follow(follower, path2()),
                follow(follower, path3()),
                // flower
                follow(follower, path4()),
                // shoot
                follow(follower, path5())
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
