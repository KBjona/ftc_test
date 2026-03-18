package org.firstinspires.ftc.teamcode.testing;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp (name = "Feeder test", group = "test")
public class FeederTest extends OpMode {

    private CRServo leftFeeder;
    private CRServo rightFeeder;

    public void init()
    {
        leftFeeder = hardwareMap.get(CRServo.class, "left_feeder");
        rightFeeder = hardwareMap.get(CRServo.class, "right_feeder");
        leftFeeder.setDirection((DcMotorSimple.Direction.REVERSE));

    }
    public void loop()
    {
        leftFeeder.setPower(1);
        rightFeeder.setPower(1);
    }
}
