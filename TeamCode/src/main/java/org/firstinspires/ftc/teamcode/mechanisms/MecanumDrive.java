package org.firstinspires.ftc.teamcode.mechanisms;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import android.media.Ringtone;

import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.sql.Driver;
import java.util.Base64;

public class MecanumDrive {
    private DcMotor leftFront, leftBack, rightFront, rightBack;
    private IMU imu;
    private IMU.Parameters imuparams = new IMU.Parameters(
            new RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                    RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
            )
    );
    private final double pi = Math.PI;

    public void init(HardwareMap hwMap)
    {
        imu = hwMap.get(IMU.class, "imu");
        imu.initialize(imuparams);

        leftFront = hwMap.get(DcMotor.class, "left_front");
        leftBack = hwMap.get(DcMotor.class, "left_back");
        rightFront = hwMap.get(DcMotor.class, "right_front");
        rightBack = hwMap.get(DcMotor.class, "right_back");

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.FORWARD);

        leftFront.setZeroPowerBehavior(BRAKE);
        leftBack.setZeroPowerBehavior(BRAKE);
        rightFront.setZeroPowerBehavior(BRAKE);
        rightBack.setZeroPowerBehavior(BRAKE);
    }

    public void drive(double forward, double strafe, double rotate)
    {
        double leftFrontP, leftBackP, rightFrontP, rightBackP;

        leftFrontP = forward + strafe + rotate;
        leftBackP = forward - strafe + rotate;
        rightFrontP = forward - strafe - rotate;
        rightBackP = forward + strafe - rotate;

        double denominator = Math.max(Math.abs(forward) + Math.abs(strafe) + Math.abs(rotate),1);

        /*
         * Send calculated power to wheels
         */
        leftFront.setPower(leftFrontP / denominator);
        leftBack.setPower(leftBackP / denominator);
        rightFront.setPower(rightFrontP / denominator);
        rightBack.setPower(rightBackP / denominator);
    }

    public void FieldDrive(double forward, double strafe, double rotate)
    {
        double leftFrontP, leftBackP, rightFrontP, rightBackP;

        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double fieldStrafe = strafe * Math.cos(-botHeading) - forward * Math.sin(-botHeading);
        double fieldForward = strafe * Math.sin(-botHeading) + forward * Math.cos(-botHeading);

        fieldStrafe *= 1.1;

        leftFrontP = fieldForward + fieldStrafe + rotate;
        leftBackP = fieldForward - fieldStrafe + rotate;
        rightFrontP = fieldForward - fieldStrafe - rotate;
        rightBackP = fieldForward + fieldStrafe - rotate;

        double denominator = Math.max(Math.abs(fieldForward) + Math.abs(fieldStrafe) + Math.abs(rotate),1);
        /*
         * Send calculated power to wheels
         */
        leftFront.setPower(leftFrontP / denominator);
        leftBack.setPower(leftBackP / denominator);
        rightFront.setPower(rightFrontP / denominator);
        rightBack.setPower(rightBackP / denominator);
    }

    public void driveDistance(double dist)
    {
        //currently unavainable
    }

    public void stopMotors()
    {
        drive(0,0,0);
    }

    public void startDriving(double power)
    {
        drive(power,0,0);
    }

    public void turnRight(double rot)
    {
        drive(0,0,rot);
    }
    public void turnLeft(double rot)
    {
        drive(0,0,rot);
    }
    public void resetIMU()
    {
        imu.resetYaw();
    }
}
