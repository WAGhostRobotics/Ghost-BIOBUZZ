package org.firstinspires.ftc.teamcode.opmodes;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.AutoPaths;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.SideAPaths;
import org.firstinspires.ftc.teamcode.opmodes.auto.paths.SideBPaths;
import org.firstinspires.ftc.teamcode.utils.FieldConstants;

public abstract class BaseAuto extends OpMode {

    Robot charlie;
    AutoPaths paths;

    protected FieldConstants.Alliance alliance;
    protected int[] upTags;
    protected int[] downTags;

    protected boolean isSideA;

    Command shootTime = waitMs(2000);

    Command spinUp = sequential(
            instant(() -> charlie.shooter.spinUp()),
            waitMs(3000)
    );

    Command openGate = instant(() -> charlie.shooter.openGates());
    Command closeGate = instant(() -> charlie.shooter.closeGates());

    Command intakeOn = instant(() -> charlie.intake.startIntake());
    Command intakeOff = instant(() -> charlie.intake.idleIntake());
    Command transferOn = instant(() -> charlie.intake.startTransfer());
    Command transferOff = instant(() -> charlie.intake.idleTransfer());

    Command polliUp = instant(() -> charlie.intake.setPolliUp());
    Command polliDown = instant(() -> charlie.intake.setPolliDown());

    Command gateOpenWait = waitMs(300);

    Command updatePose = instant(() -> FieldConstants.endAutoPose = charlie.follower.pose());

    private Command shootBalls() {
        return parallel(
                transferOn,
                intakeOn,
                shootTime
        );
    }

    private Command autoRoutine() {
        return sequential(
                //preloaded balls cycle
                spinUp,
                follow(charlie.follower, paths.preload()),
                openGate,
                gateOpenWait,
                shootBalls(),
                closeGate,
                transferOff,
                intakeOff,

                //garden cycle (not applicable for ri6w)
//                polliDown,
//                follow(charlie.follower, garden()),
//                intakeOn,
//                shootTime,
//                intakeOff,
//                polliUp,
//                follow(charlie.follower, gardenShoot()),
//                openGate,
//                gateOpenWait,
//                shootBalls(),
//                closeGate,
//                transferOff,
//                intakeOff,

                //flower cycle 1
                polliDown,
                follow(charlie.follower, paths.flower()),
                intakeOn,
                shootTime,
                follow(charlie.follower, paths.flowerBackup()),
                follow(charlie.follower, paths.flowerShoot()),
                openGate,
                gateOpenWait,
                shootBalls(),
                closeGate,
                polliUp,
                transferOff,
                intakeOff,

                //flower cycle 2
                polliDown,
                follow(charlie.follower, paths.flower2()),
                intakeOn,
                shootTime,
                follow(charlie.follower, paths.flower2shot()),
                openGate,
                gateOpenWait,
                shootBalls(),
                closeGate,
                polliUp,
                transferOff,
                intakeOff,

                //park
                follow(charlie.follower, paths.park()),
                updatePose //updates for teleop
        );
    }

    @Override
    public void init() {
        Scheduler.reset();
        charlie = new Robot(hardwareMap);

        //calc pose based on starting side
        paths = isSideA ? new SideAPaths() : new SideBPaths();

        charlie.follower.setPose(paths.getStartPose());
        charlie.follower.update();

        charlie.shooter.closeGates();
        charlie.shooter.setIdleHood();

        telemetry.addData("Auto Status", "Initialized");
        telemetry.addData("Alliance", alliance);
        telemetry.addData("Starting Side", isSideA ? "Side A (Up)" : "Side B (Down)");
        telemetry.addData("Start Y", paths.getStartPose().y());
        telemetry.update();
    }

    @Override
    public void start() {
        charlie.intake.setPolliUp();
        schedule(autoRoutine());
    }

    @Override
    public void loop() {
        charlie.follower.update();

        charlie.shooter.spinUp();
        charlie.shooter.shooterUpdate();

        Scheduler.execute();

        telemetry.addData("X", charlie.follower.pose().x());
        telemetry.addData("Y", charlie.follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(charlie.follower.pose().heading()));
        telemetry.addData("Follower Mode", charlie.follower.mode());
        telemetry.update();
    }
}