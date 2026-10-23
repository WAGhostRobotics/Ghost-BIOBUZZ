package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

public class Vision {

    public Limelight3A limelight;

    public double MAX_SHOOTING_DISTANCE = 70.0;
    public double MAX_LATERAL_OFFSET = 20.0;

    public enum TargetState {
        A_UP_IN_RANGE, B_UP_IN_RANGE,
        A_UP_OUT_OF_RANGE, B_UP_OUT_OF_RANGE,
        NONE
    }

    public Vision(HardwareMap hwMap) {
        limelight = hwMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.setPollRateHz(100);
        limelight.start();
    }

    private boolean contains(int[] array, int target) {
        for (int id : array) if (id == target) return true;
        return false;
    }

    public TargetState getTargetState(int[] tagsA, int[] tagsB) {
        LLResult result = limelight.getLatestResult();
        if (result == null || !result.isValid()) return TargetState.NONE;

        double sumXa = 0, sumYa = 0, sumZa = 0;
        int countA = 0;

        double sumXb = 0, sumYb = 0, sumZb = 0;
        int countB = 0;

        for (LLResultTypes.FiducialResult tag : result.getFiducialResults()) {
            int id = tag.getFiducialId();
            Pose3D camPose = tag.getTargetPoseCameraSpace();

            double xInches = camPose.getPosition().x * 39.3701;
            double yInches = camPose.getPosition().y * 39.3701;
            double zInches = camPose.getPosition().z * 39.3701;

            if (contains(tagsA, id)) {
                sumXa += xInches; sumYa += yInches; sumZa += zInches;
                countA++;
            } else if (contains(tagsB, id)) {
                sumXb += xInches; sumYb += yInches; sumZb += zInches;
                countB++;
            }
        }

        if (countA == 0 && countB == 0) return TargetState.NONE;

        //compare heights to definitively know which side is UP
        boolean isA_Up;
        if (countA > 0 && countB > 0) {
            double avgYa = sumYa / countA;
            double avgYb = sumYb / countB;
            isA_Up = avgYa > avgYb;
        } else {
            isA_Up = (countA > 0);
        }

        //check range constraints against the side that is confirmed UP
        if (isA_Up) {
            double avgXa = sumXa / countA;
            double avgZa = sumZa / countA;
            boolean inRange = Math.abs(avgXa) <= MAX_LATERAL_OFFSET && avgZa <= MAX_SHOOTING_DISTANCE;
            return inRange ? TargetState.A_UP_IN_RANGE : TargetState.A_UP_OUT_OF_RANGE;
        } else {
            double avgXb = sumXb / countB;
            double avgZb = sumZb / countB;
            boolean inRange = Math.abs(avgXb) <= MAX_LATERAL_OFFSET && avgZb <= MAX_SHOOTING_DISTANCE;
            return inRange ? TargetState.B_UP_IN_RANGE : TargetState.B_UP_OUT_OF_RANGE;
        }
    }

    public Pose getRobotFieldPose() {
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            Pose3D botpose = result.getBotpose();
            if (botpose != null) {
                double xInches = botpose.getPosition().x * 39.3701;
                double yInches = botpose.getPosition().y * 39.3701;
                double headingRadians = botpose.getOrientation().getYaw(AngleUnit.RADIANS);

                return new Pose(xInches, yInches, headingRadians);
            }
        }
        return null;
    }

    public void stop() {
        limelight.stop();
    }
}