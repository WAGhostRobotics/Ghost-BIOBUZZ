package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.utils.FieldConstants;

public abstract class BaseAuto extends OpMode {

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

    public Path preload() { return Paths.line(start, path1).linear(start, path1); }

    public Path garden() { return Paths.curve(point2Start, point2Control1, point2).linear(point2Start, point2); }

    public Path gardenShoot() { return Paths.curve(point2, point3Control1, point3Control2, point3).linear(point2, point3); }

    public Path park() { return Paths.line(point3, point4).constant(point4); }

    Robot charlie;

    protected FieldConstants.Alliance alliance;
    protected int[] upTags;
    protected int[] downTags;

    protected boolean isSideA;

    Command shootTime = waitMs(2000);
    Command spinUp = waitMs(3000);

    Command openGate = instant(() -> charlie.shooter.openGates());
    Command closeGate = instant(() -> charlie.shooter.closeGates());

    Command intakeOn = instant(() -> charlie.intake.setIntakeSpeed(0.8));

    Command intakeOff = instant(() -> charlie.intake.setIntakeSpeed(0));
    Command transferOn = instant(() -> charlie.intake.setTransferSpeed(0.8));
    Command transferOff = instant(() -> charlie.intake.setTransferSpeed(0));
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
                follow(charlie.follower, preload()),
                openGate,
                gateOpenWait,
                shootBalls(),
                closeGate,
                transferOff,
                follow(charlie.follower, garden()),
                shootTime,
                intakeOff,
                follow(charlie.follower, gardenShoot()),
                openGate,
                shootBalls(),
                follow(charlie.follower, park())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();
        charlie = new Robot(hardwareMap);

        //calc pose based on starting side
        Pose actualStartPose;
        if (isSideA) { actualStartPose = start; }
        else {
            double mirroredY = 144.0 - start.y(); //mirror the Y coordinate across the field
            double mirroredHeading = start.heading() + Math.PI; //flip orientation in radians
            actualStartPose = new Pose(start.x(), mirroredY, mirroredHeading);
        }

        charlie.follower.setPose(actualStartPose);
        charlie.follower.update();

        charlie.shooter.closeGates();
        charlie.shooter.setIdleHood();

        telemetry.addData("Auto Status", "Initialized");
        telemetry.addData("Alliance", alliance);
        telemetry.addData("Starting Side", isSideA ? "Side A (Up)" : "Side B (Down)");
        telemetry.addData("Start Y", actualStartPose.y());
        telemetry.update();
    }

    @Override
    public void start() { schedule(autoRoutine()); }

    @Override
    public void loop() {

        charlie.follower.update();
        Scheduler.execute();

        charlie.shooter.spinUp();

        telemetry.addData("X", charlie.follower.pose().x());
        telemetry.addData("Y", charlie.follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(charlie.follower.pose().heading()));
        telemetry.addData("Follower Mode", charlie.follower.mode());
        telemetry.update();
    }
}
