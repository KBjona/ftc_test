package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.ams.AMSColorSensor;
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

        if (gamepad2.y)
            launcher.spinLauncher();
        else if (gamepad2.b)
            launcher.stopLauncher();

        if (gamepad2.right_bumper)
            launcher.startLauncher(1200,1150);
        else if (gamepad2.left_bumper)
            launcher.startLauncher(2100,2000);
        /*
        if (gamepad1.leftBumperWasReleased())
            targetvelocity -= 3;

        if (gamepad1.rightBumperWasReleased())
            targetvelocity += 3;


        if (gamepad1.right_trigger_pressed)
            launcher.startLauncher(targetvelocity,(targetvelocity-20));
        */

        if (gamepad2.right_trigger_pressed)
            servo.setPosition(0.68);
        servo.getPosition();
        if (gamepad2.left_trigger_pressed)
            servo.setPosition(0.75);



        launcher.updateState();
        telemetry.addData("current target velocity", targetvelocity);
        telemetry.addData("Hood position", servo.getPosition());
        telemetry.addData("Launcher state", launcher.getState());
        telemetry.addData("Launcher velocity", launcher.getVelocity());
        telemetry.update();
    }

}
