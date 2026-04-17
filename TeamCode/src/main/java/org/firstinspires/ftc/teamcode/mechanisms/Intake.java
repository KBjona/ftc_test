package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {

    private DcMotor intake;
    private Servo gate;
    final double closePos = 0.49;
    final double openPos = 0.0; // TODO FIND POS
    public int state = 0;

    public void init(HardwareMap HwMap)

    {
        intake = HwMap.get(DcMotor.class, "intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        gate = HwMap.get(Servo.class, "gate");
    }

    public void enableIntake()
    {
        intake.setPower(-1);
        gate.setPosition(closePos);
        state = 1; // intake is on
    }
    public void disableIntake()
    {
        intake.setPower(0);
        gate.setPosition(openPos);
        state = 0; // intake is off
    }
    public void reverseIntake()
    {
        intake.setPower(1);
        state = -1; // intake is reversed
    }
}
