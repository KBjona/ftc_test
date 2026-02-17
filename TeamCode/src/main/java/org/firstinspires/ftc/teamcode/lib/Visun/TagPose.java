// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.firstinspires.ftc.teamcode.lib.Visun;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.demacia.utils.log.LogEntryBuilder.LogLevel;
import frc.demacia.utils.log.LogManager;

import static frc.demacia.vision.utils.VisionConstants.*;
import static frc.robot.RobotCommon.*;

/** Add your docs here. */
public class TagPose {
    // NetworkTables communication for each camera
    private NetworkTable Table;

    private NetworkTableEntry cropEntry;
    private NetworkTableEntry pipeEntry;

    private double wantedPip = 0;

    private Field2d field;

    // Vision processing variables
    private double camToTagYaw;
    private double camToTagPitch;
    private double id;
    private Camera camera;
    private double confidence;

    private double dist;
    private double alpha;
    private double height;


    // vector for camera
    private Translation2d cameraToTag;

    // vector for robot
    private Translation2d robotToTag;
    private Translation2d originToRobot;

    //vector for tag
    private Translation2d origintoTag;

    private Rotation2d RobotAngleCommon;
    private ChassisSpeeds speeds;

    //robot pose
    public Pose2d pose = new Pose2d();

    private double latency;

    @SuppressWarnings("unchecked")
    public TagPose(Camera camera){
        wantedPip = 0;
        confidence = 0;
        this.camera = camera;
        Table = NetworkTableInstance.getDefault().getTable(camera.getTableName());
        latency = 0;
        field = new Field2d();
        LogManager.addEntry("dist", this::GetDistFromCamera).withLogLevel(LogLevel.LOG_AND_NT_NOT_IN_COMP).build();
        SmartDashboard.putData("field-tag" + camera.getName(), field);

    }

    public void updateValues(){
        cropEntry = Table.getEntry("crop");
        pipeEntry = Table.getEntry("pipeline");
        camToTagPitch = Table.getEntry("ty").getDouble(0.0);
        camToTagYaw = (-Table.getEntry("tx").getDouble(0.0));
        id = (int) Table.getEntry("tid").getDouble(0.0);
        speeds = robotRelativeSpeeds;
        RobotAngleCommon = robotAngle;
    }


    public Pose2d getRobotPose2d(){
        updateValues();
        if(Table.getEntry("tv").getDouble(0.0) != 0)
        {
            if(camera.getIsCroping()){
                crop();
            }

            if (id > 0 && id < TAG_HEIGHT.length) {
                pose = new Pose2d(getOriginToRobot(), RobotAngleCommon);
                field.setRobotPose(pose);
                confidence = getConfidence();
                wantedPip = GetDistFromCamera() > 1 ? 0 : 0;
            }
        }else {
            cropStop();
            wantedPip = 0;
            pose = new Pose2d();
        }if(wantedPip != Table.getEntry("getpipe").getDouble(0.0)){
            pipeEntry.setDouble(wantedPip);
        }
        return pose;
    }

    /**
     * Calculates robot position relative to field origin
     * Uses known AprilTag position and measured vector to tag
     * * @return Translation2d representing robot position on field
     */
    public Translation2d getOriginToRobot() {

        origintoTag = O_TO_TAG[(int) this.id == -1 ? 0 : (int) this.id];

        height = TAG_HEIGHT[(int) this.id];
        if (origintoTag != null) {

            originToRobot = origintoTag.minus(getRobotToTagFieldRel());

            return originToRobot;
        }
        return new Translation2d();

    }

    /**
     * Calculates vector from robot center to detected AprilTag
     * Accounts for camera offset from robot center
     * * @return Translation2d representing vector to tag
     */
    public Translation2d getRobotToTagFieldRel() {
        // Convert camera measurements to vector
        cameraToTag = new Translation2d(GetDistFromCamera(),
                Rotation2d.fromDegrees(camToTagYaw+camera.getYaw()));
        // LogManager.log("cameraToTag :" +cameraToTag);
        // LogManager.log("Camera to Tag Yaw :" + camToTagYaw);
        // Add camera offset to get robot center to tag vector
        robotToTag = (camera.getRobotToCamPosition().toTranslation2d()
                .plus(cameraToTag)).rotateBy(RobotAngleCommon);
        // LogManager.log("Robot to Tag :" + robotToTag);
        return robotToTag;
    }

    public double GetDistFromCamera() {

        alpha = Math.abs(camToTagPitch + camera.getPitch());
        dist = (Math.abs(height - camera.getHeight())) / (Math.tan(Math.toRadians(alpha)));
        // dist = dist / Math.abs(Math.cos(Math.abs(Math.toRadians(camToTagYaw))));

        return dist;
    }

    private void crop() {
        double YawCrop = getYawCrop();
        double PitchCrop = getPitchCrop();
        double[] crop = { YawCrop - getCropOfset(), YawCrop + getCropOfset(), PitchCrop - getCropOfset(),
                PitchCrop + getCropOfset() };
        cropEntry.setDoubleArray(crop);
    }

    private double getCropOfset() {
        double crop = GetDistFromCamera() * CROP_CONSTAT;
        return MathUtil.clamp(crop, MIN_CROP, MAX_CROP);
    }

    private double getYawCrop() {
        double TagYaw = ((-camToTagYaw) + camera.getYaw()) / 31.25;
        return TagYaw + speeds.vyMetersPerSecond * PREDICT_Y + speeds.omegaRadiansPerSecond * PREDICT_OMEGA;
    }

    private double getPitchCrop() {
        double TagPitch = camToTagPitch / 24.45;
        return TagPitch + speeds.vxMetersPerSecond * PREDICT_X;
    }

    private void cropStop() {
        double[] crop = { -1, 1, -1, 1 };
        cropEntry.setDoubleArray(crop);
    }

    private double getConfidence() {
        // Get the current distance to tag
        double currentDist = GetDistFromCamera();


        // If we're within reliable range, give high confidence
        if (currentDist <= BEST_RELIABLE_DISTANCE) {
            return 1.0;
        }

        // Calculate how far we are into the falloff range (0 to 1)
        double normalizedDist = (currentDist - BEST_RELIABLE_DISTANCE)
                / ((WORST_RELIABLE_DISTANCE) - BEST_RELIABLE_DISTANCE);

        // Apply cubic falloff function
        return Math.pow(1 - normalizedDist, 3);
    }

    public double getPoseEstemationConfidence() {
        return this.confidence;
    }

    public boolean isSeeTag(int id, double distance) {
        return Table.getEntry("tid").getDouble(0.0) == id && getRobotToTagFieldRel().getNorm() <= distance;
    }

    public boolean isSeeTag() {
        return Table.getEntry("tid").getDouble(0.0) > 0;
    }

    public Camera getCamera() {
        return camera;
    }

    public double getCamToTagYaw(){
        return camToTagYaw;
    }


    public double getTimestamp() {
        return latency;
    }

    public int getTagId() {
        return (int) Table.getEntry("tid").getDouble(0.0);
    }

    public double getAngle() {
        return Table.getEntry("botpose").getDoubleArray(new double[] { 0, 0, 0, 0, 0, 0 })[5];
    }

    public Rotation2d getRobotAngle() {
        return null;
    }

    public boolean getIsObjectCamera() {
        return camera.getIsObjectCamera();
    }
}