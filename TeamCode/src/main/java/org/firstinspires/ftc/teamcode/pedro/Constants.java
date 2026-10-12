package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("fl");
        c.frontRightName.set("fr");
        c.backLeftName.set("bl");
        c.backRightName.set("br");
        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(-4.71904334120863);
        c.yPodOffset.set(-0.8108188599113404);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.24470428572400527);
                Controller secondaryTranslationalForward = Controller.proportional(0.09041169495608194);
                Controller primaryTranslationalLateral = Controller.proportional(0.36391353976028246);
                Controller secondaryTranslationalLateral = Controller.proportional(0.13445632899255344);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.011924248662149629));
                c.brake.set(Controller.proportionalFeedforward(0.010135611362827184));

                c.headingFeedback.set(Controller.proportional(3.479133631847602));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.05216195587054739, 0.002936193739712674));

                c.linearBrakeCoefficients.set(Matrix.diag(0.10155766033629442, 0.04169242250603562));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0011834466348752926, 0.0016540745874902639));

                c.maxAchievableForwardVelocity.set(78.73834450300335);
                c.maxAchievableStrafeVelocity.set(69.73579900730213);
                c.naturalForwardDeceleration.set(35.02438127017048);
                c.naturalStrafeDeceleration.set(69.30580864310332);

                c.brakeAggression.set(1.2); // makes the autos overshoot a little
            }
    );


}