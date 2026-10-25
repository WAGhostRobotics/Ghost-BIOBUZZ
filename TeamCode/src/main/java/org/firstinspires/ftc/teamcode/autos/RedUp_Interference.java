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
public class RedUp_Interference extends OpMode {
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(32.1124, 8.4829, 90);
    private final Pose path1 = poseFactory.of(36.2819, 14.2971, 60);
    private final Pose point2 = poseFactory.of(8.3187, 8.3287, 270);
    private final Pose point2Control1 = poseFactory.of(14.1319, 36.5769, 0);
    private final Pose point3 = poseFactory.of(45.0394, 116.293, 300);
    private final Pose point3Control1 = poseFactory.of(23.8956, 36.1886, 0);
    private final Pose point3Control2 = poseFactory.of(24.0165, 99.5156, 0);
    private final Pose point4 = poseFactory.of(18.3516, 47.5659, 180);
    private final Pose point4Control1 = poseFactory.of(26.9716, 90.4789, 0);
    private final Pose point5 = poseFactory.of(8.5321, 47.4167, -179.1291);
    private final Pose point6Start = poseFactory.of(8.5321, 47.4167, 180);
    private final Pose point6 = poseFactory.of(39.8984, 23.8755, 60);
    private final Pose point7 = poseFactory.of(12.5742, 92.1355, 60);

    public Path path1() {
        return Paths.line(start, path1).linear(start, path1);
    }

    public Path path2() {
        return Paths.curve(path1, point2Control1, point2).linear(path1, point2);
    }

    public Path path3() {
        return Paths.curve(point2, point3Control1, point3Control2, point3).linear(point2, point3);
    }

    public Path path4() {
        return Paths.curve(point3, point4Control1, point4).linear(point3, point4);
    }

    public Path path5() {
        return Paths.line(point4, point5).tangent();
    }

    public Path path6() {
        return Paths.line(point6Start, point6).linear(point6Start, point6);
    }

    public Path path7() {
        return Paths.line(point6, point7).constant(point7);
    }

    private Command autoRoutine() {
        return sequential(
                follow(follower, path1()),
                //shoot
                follow(follower, path2()),
                // garden
                follow(follower, path3()),
                // shoot
                follow(follower, path4()),
                follow(follower, path5()),
                // flower
                follow(follower, path6()),
                // shoot
                follow(follower, path7())
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
