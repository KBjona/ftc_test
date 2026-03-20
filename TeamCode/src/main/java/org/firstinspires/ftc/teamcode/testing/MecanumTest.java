package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;

@TeleOp (name = "Mecanum test", group = "test")
public class MecanumTest extends OpMode {

    MecanumDrive Drive = new MecanumDrive();
    Launcher launcher = new Launcher();
    @Override
    public void init()
    {
        Drive.init(hardwareMap);
        launcher.init(hardwareMap);
    }

    @Override
    public void loop()
    {
        Drive.drive(-(gamepad1.left_stick_y), -(gamepad1.left_stick_x) , (gamepad1.right_stick_x));
        if (gamepad2.left_bumper)
        {
            launcher.setLauncherVelocity(2200);
        }
    }

}
