package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.mechanisms.Vision;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@Autonomous (name = "Vision test", group = "test")
public class AprilTagTest extends OpMode {

    Vision aprilTagWebcam = new Vision();


    public void init()
    {
        aprilTagWebcam.init(hardwareMap, telemetry);
    }

    public void loop()
    {
        aprilTagWebcam.update();
        AprilTagDetection id20 = aprilTagWebcam.getTagById(20);
        aprilTagWebcam.displayDetectionTelemetry(id20);
    }
}
