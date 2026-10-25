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
public class RedUp_Normal extends OpMode {

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(32.1124, 8.4829, 90);
    private final Pose path1 = poseFactory.of(36.2819, 14.2971, 60);
    private final Pose point2 = poseFactory.of(8.3187, 8.3287, 270);
    private final Pose point2Control1 = poseFactory.of(14.1319, 36.5769, 0);
    private final Pose point3 = poseFactory.of(12.5485, 92.2903, 60);
    private final Pose point3Control1 = poseFactory.of(38.8755, 44.2811, 0);

    public Path path1() {
        return Paths.line(start, path1).linear(start, path1);
    }

    public Path path2() {
        return Paths.curve(path1, point2Control1, point2).linear(path1, point2);
    }

    public Path path3() {
        return Paths.curve(point2, point3Control1, point3).linear(point2, point3);
    }

    private Command autoRoutine() {
        return sequential(
                follow(follower, path1()),
                //shoot
                follow(follower, path2()),
                // garden
                follow(follower, path3())
                //park
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
