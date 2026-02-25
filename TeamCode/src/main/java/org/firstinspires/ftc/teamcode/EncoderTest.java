package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.ArcadeDrive;

@TeleOp (name = "Encoder test", group = "test")
public class EncoderTest extends OpMode {
    ArcadeDrive Drive = new ArcadeDrive();

    @Override
    public void init()
    {
        Drive.init(hardwareMap);
    }

    @Override
    public void loop()
    {
        Drive.drive(-(gamepad1.left_stick_y) , (gamepad1.right_stick_x));
        telemetry.addData("left pos", Drive.getLeftEncoder());
        telemetry.addData("right pos", Drive.getRightEncoder());

    }

}
