package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.teamcode.mechanisms.AutoRotate;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Vision;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;


@TeleOp(name = "Main-Teleop", group = "StarterBot")
public class MainTeleop extends OpMode {
    MecanumDrive Drive = new MecanumDrive();
    Launcher launcher = new Launcher();
    Vision aprilTagWebcam = new Vision();
    AutoRotate rotation = new AutoRotate();
    double targetvelocity = 2000.0;
    boolean tryPark = false;
    private Servo servo;
    double targethood = 0.66;
    int tag = 0;

    @Override
    public void init()
    {
        servo = hardwareMap.get(Servo.class,"hood");
        Drive.init(hardwareMap);
        launcher.init(hardwareMap);
        rotation.init(hardwareMap,telemetry);

    }
    public void init_loop()
    {
        if (gamepad1.dpadRightWasReleased()) {
            telemetry.addLine("TAG IS NOW RED");
            tag = 24;
        }
        if (gamepad1.dpadLeftWasReleased()) {
            telemetry.addLine("TAG IS NOW BLUE");
            tag = 20;
        }
        telemetry.addData("TAG (20 = blue, 24 = red)", tag);
    }


    public void start()
    {
        resetRuntime();
        time = getRuntime();
    }

    @Override
    public void loop()
    {
        if (gamepad1.right_bumper)
            tryPark = true;
        if (gamepad1.left_bumper)
            tryPark = false;

        double forward = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;

        if (gamepad1.right_trigger > 0.5)
        {
            rotate = rotation.getRotate(time, tag);
        }

        if (tryPark)
            Drive.FieldDrive(forward / 4 , strafe / 4, rotate);
        else
            Drive.FieldDrive(forward,strafe, rotate);

        if (gamepad2.y)
            launcher.spinLauncher();
        else if (gamepad2.b)
            launcher.stopLauncher();

        if (gamepad2.right_bumper) {
            servo.setPosition(0.57); // ORIGNIAL 67
            launcher.startLauncher(1194, 1192);
        }
        else if (gamepad2.left_bumper) {
            servo.setPosition(0.64); // og was 58
            launcher.startLauncher(2107, 2105);
        }
        else if(gamepad2.left_trigger_pressed)
        {
            servo.setPosition(0.61);
            launcher.startLauncher(1725,1715);
        }
        else if(gamepad2.right_trigger_pressed)
        {
            servo.setPosition(0.60);
            launcher.startLauncher(1550,1540);
        }

        launcher.updateState();

        telemetry.addData("left y", gamepad1.left_stick_y);
        telemetry.addData("right x", gamepad1.right_stick_x);
        telemetry.addData("Speed state", tryPark);
        telemetry.addData("current target velocity", launcher.getTargetVelocity());
        telemetry.addData("hood pos", targethood);
        telemetry.addData("Hood position", servo.getPosition());
        telemetry.addData("Launcher state", launcher.getState());
        telemetry.addData("Launcher velocity", launcher.getVelocity());
        telemetry.update();

        if (gamepad1.right_trigger_pressed)
        {
            Drive.drive(1,0,0);
        }

        if (gamepad1.left_trigger_pressed)
        {
            Drive.resetIMU();
        }
    }

}
/*
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.teamcode.mechanisms.ArcadeDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;


@TeleOp(name = "Main-Teleop", group = "StarterBot")
public class MainTeleop extends OpMode {
    ArcadeDrive Drive = new ArcadeDrive();
    Launcher launcher = new Launcher();

    private Servo servo;

    @Override
    public void init()
    {
        servo = hardwareMap.get(Servo.class,"hood");
        Drive.init(hardwareMap);
        launcher.init(hardwareMap);

    }
    @Override
    public void loop()
    {
        Drive.drive(-(gamepad2.left_stick_y) , (gamepad2.right_stick_x));

        if (gamepad2.y)
            launcher.spinLauncher();
        else if (gamepad2.b)
            launcher.stopLauncher();

        if (gamepad2.right_bumper)
            launcher.startLauncher(1200,1160);
        else if (gamepad2.left_bumper)
            launcher.startLauncher(2100,2080);

        if (gamepad2.right_trigger_pressed)
            servo.setPosition(0.61);
        servo.getPosition();
        if (gamepad2.left_trigger_pressed)
            servo.setPosition(0.67);



        launcher.updateState();
        telemetry.addData("Hood position", servo.getPosition());
        telemetry.addData("Launcher state", launcher.getState());
        telemetry.addData("Launcher velocity", launcher.getVelocity());
        telemetry.update();
    }

}*/