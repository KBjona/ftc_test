package org.firstinspires.ftc.teamcode.mechanisms;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import android.media.Ringtone;

import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Base64;

public class ArcadeDrive {
    private DcMotor leftDrive, rightDrive;
    private final double pi = Math.PI;

    public void init(HardwareMap hwMap)
    {
        leftDrive = hwMap.get(DcMotor.class, "left_drive");
        rightDrive = hwMap.get(DcMotor.class, "right_drive");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

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
        leftDrive.setPower(0);
        rightDrive.setPower(-0.48);
    }
    public void turnLeft()
    {
        rightDrive.setPower(0);
        leftDrive.setPower(-0.17);
    }
    public void brake()
    {
        //rightDrive.setMode();
    }
    public int getLeftEncoder() { return leftDrive.getCurrentPosition();}
    public int getRightEncoder() { return rightDrive.getCurrentPosition();}

    public void turnUsingEncoder(int left,int right)
    {
        leftDrive.setTargetPosition(left);
        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        rightDrive.setTargetPosition(right);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    // Inside ArcadeDrive.java

}
