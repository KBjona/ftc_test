package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
@Config
@TeleOp (name = "Intake test", group = "test")
public class IntakeTest extends OpMode {

    private DcMotor intake;
    private Servo servo;
    final public static double servopos = 0.49;

    @Override
    public void init()

    {
        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        servo = hardwareMap.get(Servo.class, "gate");

    }

    @Override
    public void loop()
    {
        if (gamepad1.right_trigger_pressed)
            intake.setPower(-1);
        if (gamepad1.right_bumper)
            intake.setPower(0);
        if (gamepad1.left_trigger_pressed)
            intake.setPower(1);
        if (gamepad1.left_bumper)
            intake.setPower(0);
        if (gamepad1.dpad_down)
            servo.setPosition(servopos);
    }

}
