package org.firstinspires.ftc.teamcode.mechanisms;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import android.media.Ringtone;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ArcadeDrive {
    private DcMotor leftDrive, rightDrive;
    private final double pi = Math.PI;

    public void init(HardwareMap hwMap)
    {
        leftDrive = hwMap.get(DcMotor.class, "left_drive");
        rightDrive = hwMap.get(DcMotor.class, "right_drive");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftDrive.setZeroPowerBehavior(BRAKE);
        rightDrive.setZeroPowerBehavior(BRAKE);

    }

    public void drive(double forward, double rotate)
    {
        double leftPower, rightPower;
        leftPower = forward + rotate;
        rightPower = forward - rotate;

        /*
         * Send calculated power to wheels
         */
        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
    }

    public double driveDistance(double dist)
    {
        double rots = dist /(96 * pi);
        double motordist = rots * 537.7;

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDrive.setPower(0.5);
        rightDrive.setPower(0.5);
        leftDrive.setTargetPosition((int)motordist);
        rightDrive.setTargetPosition((int)motordist);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        return  leftDrive.getCurrentPosition();
    }

    public void stopMotors()
    {
        leftDrive.setPower(0);
        rightDrive.setPower(0);
    }

    public void startDriving(double power)
    {
        leftDrive.setPower(power);
        rightDrive.setPower(power);
    }

    public void turnRight()
    {
        rightDrive.setPower(-0.5);
    }
    // Inside ArcadeDrive.java

}
