package org.firstinspires.ftc.teamcode.opmodes.testing;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;

@Autonomous
public class StateMachineTest extends OpMode {
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

    Robot stateMachineTest;

    Command shootTime = waitMs(2000);
    Command spinUpTime = waitMs(3000);

    Command openGate = instant(() -> stateMachineTest.shooter.openGates());
    Command closeGate = instant(() -> stateMachineTest.shooter.closeGates());

    Command intakeOn = instant(() -> stateMachineTest.intake.setIntakeSpeed(0.8));

    Command intakeOff = instant(() -> stateMachineTest.intake.setIntakeSpeed(0));
    Command transferOn = instant(() -> stateMachineTest.intake.setTransferSpeed(0.8));
    Command transferOff = instant(() -> stateMachineTest.intake.setTransferSpeed(0));
    Command gateOpenWait = waitMs(300); //the thing that blocks balls in the hood until intake is full power

    Double[] AimingHoodState = new Double[3]; //hood pos hood angle and aiming angle - returned from aiming
    Command setShooterPower = instant(() -> stateMachineTest.shooter.setShooterPower(AimingHoodState[2])); //set power based on intake
    Command resetShooter = instant(() -> stateMachineTest.shooter.setShooterPower(0));
    Command intakeWait = waitMs(500);
    //components

    public enum state {
        PRELOAD,
        FIND_AND_AIM,
        SHOOT,
        GO_TO_GARDEN,
        INTAKE_GARDEN,
        GO_OTHER_SIDE,
        PARK

    };
    private Command shootingProcedure() {
        return sequential( //adjust order maybe
                transferOn,
                intakeOn,
                setShooterPower,
                spinUpTime,
                openGate,
                gateOpenWait,
                shootTime,
                resetShooter,
                intakeOff,
                transferOff
        );
    }
    private Command intakeProcedure() {
        return sequential(
                //align stuff first
                intakeOn,
                transferOn,
                intakeWait,
                intakeOff,
                transferOff,
                intakeWait


        );
    }

    public void runAutoRoutine(state s) {
        switch(s) {
            case PRELOAD: {
                schedule(follow(stateMachineTest.follower, preload()));
            }
            case FIND_AND_AIM: {
                //no clue
            }
            case SHOOT: {
                schedule(shootingProcedure());
            }
            case GO_TO_GARDEN: {
                schedule(follow(stateMachineTest.follower, garden()));

            }
            case INTAKE_GARDEN: {
                schedule(intakeProcedure());
            }
            case GO_OTHER_SIDE: {
                schedule(follow(stateMachineTest.follower, gardenShoot()));

            }
            case PARK: {
                schedule(follow(stateMachineTest.follower, park()));
            }
        }
    }

    @Override
    public void init() {
        stateMachineTest = new Robot(hardwareMap);
        Scheduler.reset();

        stateMachineTest.follower.setPose(start);
        stateMachineTest.follower.update();
        stateMachineTest.shooter.closeGates();

        stateMachineTest.shooter.setHoodPos(0.27);
    }

    @Override
    public void loop() {
        stateMachineTest.follower.update();
        Scheduler.execute();

        stateMachineTest.shooter.setShooterPower(0.56);

    }
}