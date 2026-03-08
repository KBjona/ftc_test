package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.mechanisms.ArcadeDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;

@TeleOp (name = "Hood test", group = "test")
public class HoodTest extends OpMode {
    Launcher launcher = new Launcher();
    Servo servo;

    double pos = 0.5;

    @Override
    public void init()
    {
        servo = hardwareMap.get(Servo.class, "hood");
    }

    @Override
    public void loop()
    {
        if (gamepad1.leftBumperWasReleased())
        {
            pos -= 0.01;
        }
        if (gamepad1.rightBumperWasReleased())
        {
            pos += 0.01;
        }
        if (gamepad1.right_trigger_pressed)
        {
            servo.setPosition(pos);
        }
        if (gamepad1.left_trigger_pressed)
        {
            launcher.startLauncher(2107, 2105);
        }
        telemetry.addData("pos",pos);
    }

}
