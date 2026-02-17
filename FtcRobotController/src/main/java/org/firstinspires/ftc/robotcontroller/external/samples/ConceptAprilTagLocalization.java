package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class AprilTagLocalization {

    private Position cameraPosition = new Position(DistanceUnit.INCH,
            0, 0, 0, 0);
    private YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES,
            0, -90, 0, 0);  // Yaw, Pitch, Roll

    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;

    private Pose2D robotPose = null;
    private double robotX   = 0;
    private double robotY   = 0;
    private double robotYaw = 0;

    @Override
    public void runOpMode() {

        initAprilTag();

        telemetry.addData(">", "לחץ START להתחלה");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

            updateRobotPose();   // עדכן את מיקום הרובוט
            displayTelemetry();  // הצג על Driver Station

            // שליטה על הסטרימינג
            if (gamepad1.dpad_down) {
                visionPortal.stopStreaming();
            } else if (gamepad1.dpad_up) {
                visionPortal.resumeStreaming();
            }

            sleep(20);
        }

        visionPortal.close();
    }

    private void updateRobotPose() {
        List<AprilTagDetection> detections = aprilTag.getDetections();

        double sumX   = 0;
        double sumY   = 0;
        double sumYaw = 0;
        int count = 0;

        for (AprilTagDetection detection : detections) {
            if (detection.metadata != null && detection.robotPose != null) {
                sumX   += detection.robotPose.getPosition().x;
                sumY   += detection.robotPose.getPosition().y;
                sumYaw += detection.robotPose.getOrientation().getYaw(AngleUnit.DEGREES);
                count++;
            }
        }

        if (count > 0) {
            robotX = sumX / count;
            robotY = sumY / count;
            robotYaw = sumYaw / count;
        }
    }

    private void displayTelemetry() {
        List<AprilTagDetection> detections = aprilTag.getDetections();
        int knownTagCount = 0;

        for (AprilTagDetection detection : detections) {
            if (detection.metadata != null && detection.robotPose != null) {
                knownTagCount++;
            }
        }

        telemetry.addData("תגים מזוהים", detections.size());
        telemetry.addData("תגים עם מיקום", knownTagCount);
        telemetry.addLine("---- מיקום הרובוט בשדה ----");

        if (knownTagCount > 0) {
            telemetry.addData("X (ימין+)  ", "%.2f אינץ'", robotX);
            telemetry.addData("Y (קדימה+) ", "%.2f אינץ'", robotY);
            telemetry.addData("Yaw (כיוון)", "%.2f מעלות", robotYaw);
        } else {
            telemetry.addLine("⚠ לא נמצאו תגים - מיקום אחרון נשמר");
            telemetry.addData("X אחרון", "%.2f", robotX);
            telemetry.addData("Y אחרון", "%.2f", robotY);
            telemetry.addData("Yaw אחרון", "%.2f", robotYaw);
        }

        telemetry.addLine("\nDPAD UP = התחל סטרימינג");
        telemetry.addLine("DPAD DOWN = עצור סטרימינג");
        telemetry.update();
    }

    private void initAprilTag() {
        aprilTag = new AprilTagProcessor.Builder()
                .setCameraPose(cameraPosition, cameraOrientation)
                .build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
                .addProcessor(aprilTag)
                .build();
    }

    public Pose2D getRobotPose(){
        return robotPose;
    }
}