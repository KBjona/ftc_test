package org.firstinspires.ftc.teamcode.testing;

import static com.qualcomm.robotcore.util.Range.clip;

import android.util.Range;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Vision;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name = "Auto rotate test", group = "test")
public class AutoRotateToAprilTag extends OpMode {

    Vision aprilTagWebcam = new Vision();
    MecanumDrive Drive = new MecanumDrive();

    double foward, strafe ,rotate;


    double kP = 0.2;
    double error = 0;
    double lastError = 0;
    double goal = 0;
    double Tolerance = 0.5;
    double kD = 0.00001;
    double time = 0;
    double lastTime = 0;
    boolean saw = false;

    public void start()
    {
        resetRuntime();
        time = getRuntime();
    }

    public void init()
    {
        aprilTagWebcam.init(hardwareMap, telemetry);
        Drive.init(hardwareMap);
    }

    public void loop()
    {
        foward = -gamepad1.left_stick_y;
        strafe = -gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        aprilTagWebcam.update();
        AprilTagDetection id20 = aprilTagWebcam.getTagById(20);

        if (gamepad1.right_trigger >= 0.4)
        {

            if (id20 != null)
            {
                saw = true;
                error = goal - id20.ftcPose.bearing;

                if (Math.abs(error) < Tolerance)
                {
                    rotate = 0;
                }
                else
                {
                    double pTerm = error * kP;

                    time = getRuntime();
                    double dT = time - lastTime;
                    double dTerm = ((error - lastError) / dT) * kD;
                    rotate = clip(pTerm + dTerm,-0.4,0.4);

                    lastError = error;
                    lastTime = time;
                }
            }
            else
            {
                saw = false;
                lastTime = getRuntime();
                lastError = 0;
            }
        }
        else
        {
            lastTime = getRuntime();
            lastError = 0;
        }

        Drive.drive(foward /3,strafe /3,rotate);


        if (id20 != null)
        {
            telemetry.addLine("AUTO ALLIGNING");
            aprilTagWebcam.displayDetectionTelemetry(id20);
            telemetry.addData("Error", error);
        }
        telemetry.addData("stick", gamepad1.right_trigger);
        telemetry.addData("P ", "%.4f", kP);
        telemetry.addData("D ", "%.4f", kD);




    }
}
