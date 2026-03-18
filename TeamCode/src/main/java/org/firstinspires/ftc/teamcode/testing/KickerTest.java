package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;

@TeleOp (name = "Kicker test", group = "test")
public class KickerTest extends OpMode {

    private DcMotor kicker;

    @Override
    public void init()

    {
        kicker = hardwareMap.get(DcMotor.class, "kicker");
        kicker.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop()
    {
        if (gamepad1.right_trigger_pressed)
            kicker.setPower(-1);
        if (gamepad1.right_bumper)
            kicker.setPower(0);
        if (gamepad1.left_trigger_pressed)
            kicker.setPower(1);
        if (gamepad1.left_bumper)
            kicker.setPower(0);
    }

}
