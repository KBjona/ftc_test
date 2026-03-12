package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.teamcode.mechanisms.AutoRotate;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Vision;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@Config
@TeleOp(name = "Main-Teleop", group = "StarterBot")
public class MainTeleop extends OpMode {
    MecanumDrive Drive = new MecanumDrive();
    Launcher launcher = new Launcher();
    AutoRotate rotation = new AutoRotate();
    public static double  targetvelocity = 2110.0;
    int tryPark = 1;
    private Servo servo;
    public static double targethood = 0.36;
    int tag = 0;
    boolean driveMode = false;

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
        telemetry.addLine("SCREAM AT ME OFIRRRRRRRRRRRRRRRRRRRRR");
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
            tryPark = 4;
        if (gamepad1.left_bumper)
            tryPark = 1;
        if (gamepad1.dpad_left)
            driveMode = true;
        if (gamepad1.dpad_right)
            driveMode = false;

        double forward = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;

        if (gamepad1.right_trigger > 0.5)
        {
            rotate = rotation.getRotate(time, tag);
            telemetry.addData("rotation",rotate);
        }

        if (driveMode)
            Drive.FieldDrive(forward / tryPark , strafe / tryPark, rotate / tryPark);
        else
            Drive.drive(forward / tryPark , strafe / tryPark, rotate / tryPark);

        if (gamepad2.y)
            launcher.spinLauncher();
        else if (gamepad2.b)
            launcher.stopLauncher();

        if (gamepad2.right_bumper) {
            servo.setPosition(0.28); // ORIGNIAL 67
            launcher.startLauncher(1194, 1192);

        }
        else if (gamepad2.left_bumper) {
            servo.setPosition(0.36); // og was 58
            launcher.startLauncher(2100,2098);
        }
        else if(gamepad2.left_trigger_pressed)
        {
            servo.setPosition(0.35);
            //servo.setPosition(targethood);

            launcher.startLauncher(1620,1620);
        }
        else if(gamepad2.right_trigger_pressed)
        {
            servo.setPosition(0.36);
            //servo.setPosition(targethood);

            launcher.startLauncher(1375,1370);
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
        telemetry.addData("leftstick X", gamepad1.left_stick_x);
        telemetry.update();

        if (gamepad1.ps)
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