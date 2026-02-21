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
    double targetvelocity = 2000.0;

    private Servo servo;

    double targethood = 0.66;

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
        Drive.drive(-(gamepad1.left_stick_y) , (gamepad1.right_stick_x));

        telemetry.addData("left y", gamepad1.left_stick_y);
        telemetry.addData("right x", gamepad1.right_stick_x);


        if (gamepad2.y)
            launcher.spinLauncher();
        else if (gamepad2.b)
            launcher.stopLauncher();

        if (gamepad2.right_bumper) {
            servo.setPosition(0.46);
            launcher.startLauncher(1225, 1205);
        }
        else if (gamepad2.right_trigger_pressed) {
            servo.setPosition(0.58);
            launcher.startLauncher(2065, 2050);
        }
        else if(gamepad2.left_bumper)
        {
            servo.setPosition(0.48);
            launcher.startLauncher(1600,1550);
        }
        /*
        if (gamepad1.leftBumperWasReleased())
            targetvelocity -= 10;

        if (gamepad1.rightBumperWasReleased())
            targetvelocity += 10;

        if (gamepad1.dpadUpWasReleased())
            targethood +=0.01;
        if (gamepad1.dpadDownWasReleased())
            targethood -=0.01;
        if (gamepad1.left_trigger_pressed)
            servo.setPosition(targethood);



        if (gamepad1.right_trigger_pressed)
            launcher.startLauncher(targetvelocity,(targetvelocity-20));
*/

        if (gamepad2.dpadDownWasReleased())
            servo.setPosition(0.58);
        servo.getPosition();
        if (gamepad2.dpadUpWasReleased())
            servo.setPosition(0.46);
        if (gamepad2.dpadLeftWasPressed())
            servo.setPosition(0.5);



        launcher.updateState();
        telemetry.addData("current target velocity", targetvelocity);
        telemetry.addData("hood pos", targethood);
        telemetry.addData("Hood position", servo.getPosition());
        telemetry.addData("Launcher state", launcher.getState());
        telemetry.addData("Launcher velocity", launcher.getVelocity());
        telemetry.update();
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