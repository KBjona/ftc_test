package org.firstinspires.ftc.teamcode.lib.Visun;

import org.firstinspires.ftc.robotcontroller.external.samples.AprilTagLocalization;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class visun {

    AprilTagLocalization visun;

    public void init(){
        visun = new AprilTagLocalization();
    }

    public Pose2D getRobotPoseeMy(){
        return visun.getRobotPose();
    }

    public Pose2D Target = new Pose2D(DistanceUnit.METER,0, 3.65, AngleUnit.RADIANS, 0);

    public double getDictence(){
        return Math.sqrt(Math.pow(getRobotPoseeMy().getX(DistanceUnit.METER) - Target.getX(DistanceUnit.METER),2) +getRobotPoseeMy().getY(DistanceUnit.METER) - Target.getY(DistanceUnit.METER));
    }
}
