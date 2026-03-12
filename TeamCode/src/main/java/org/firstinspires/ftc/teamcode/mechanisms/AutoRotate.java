package org.firstinspires.ftc.teamcode.mechanisms;

import static com.qualcomm.robotcore.util.Range.clip;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
@Config
public class AutoRotate {

    public Vision aprilTagWebcam = new Vision();

    public static double kP = 0.025;
    double error = 0;
    double lastError = 0;
    double goal = -12;
    public static double Tolerance = 2;
    public static double kD = 0;
    double time = 0;
    double lastTime = 0;
    double rotate = 0;
    private Telemetry telemetry;

    public void init(HardwareMap hwMap, Telemetry telemetryinit)
    {
        aprilTagWebcam.init(hwMap, telemetryinit);
        this.telemetry = telemetryinit;
    }

    public double getRotate(double Time, int tagId)
    {
        if (tagId == 24)
            goal = 12;
        int tagNum = tagId;
        rotate = 0;
        time = Time;
        aprilTagWebcam.update();
        AprilTagDetection tag = aprilTagWebcam.getTagById(tagNum);
        telemetry.addData("TAG IS NULL", tag);
         if (tag != null)
            {
                error = goal - tag.ftcPose.bearing;

                if (Math.abs(error) < Tolerance)
                {
                    rotate = 0;
                }
                else
                {

                    double pTerm = error * kP;
                    telemetry.addData("pTerm",pTerm);
                    telemetry.addData("time",time);
                    telemetry.addData("lasttimme",lastTime);
                    telemetry.addData("ERRORRR",error);
                    time = Time;
                    double dT = time - lastTime;
                    double dTerm = ((error - lastError) / dT) * kD;
                    rotate = clip(pTerm + dTerm,-0.4,0.4);
                    telemetry.addData("Rotate",rotate);

                    lastError = error;
                    lastTime = time;
                }
            }
            else
            {
                lastTime = Time;
                lastError = 0;
            }


        if (tag != null)
        {
            telemetry.addLine("AUTO ALLIGNING");
            aprilTagWebcam.displayDetectionTelemetry(tag);
            telemetry.addData("Error", error);
        }
        telemetry.addData("P ", "%.4f", kP);
        telemetry.addData("D ", "%.4f", kD);
        telemetry.update();
        return rotate;
    }
}
