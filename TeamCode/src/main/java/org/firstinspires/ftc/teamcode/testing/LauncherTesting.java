package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.mechanisms.ArcadeDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
@Config
@TeleOp (name = "Launcher test", group = "test")
public class LauncherTesting extends OpMode {
    Servo servo;

    private DcMotorEx launcher;
    private CRServo leftFeeder;
    private CRServo rightFeeder;
    double pos = 0.5;

    public static double kp = 15;
    public static double ki = 0;
    public static double kd = 0;
    public static double ff = 11;
    double acceleration;
    double lastVelocity = 0;
    long lastTime = 0;

    @Override
    public void init()
    {
        servo = hardwareMap.get(Servo.class, "hood");
        launcher = hardwareMap.get(DcMotorEx.class, "launcher");
        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,new PIDFCoefficients(kp,ki,kd,ff));
        leftFeeder = hardwareMap.get(CRServo.class, "left_feeder");
        rightFeeder = hardwareMap.get(CRServo.class, "right_feeder");
        rightFeeder.setDirection((DcMotorSimple.Direction.REVERSE));
    }
    @Override
    public void start()
    {
        launcher.setVelocity(2200);
    }
    @Override
    public void loop()
    {
/*
        long currentTime = System.nanoTime();
        double currentVelocity = launcher.getVelocity(); // ticks/sec

        double dt = (currentTime - lastTime) / 1e9; // המרה לשניות
        if (dt > 0) {
            acceleration = (currentVelocity - lastVelocity) / dt; // ticks/sec²
        }
        lastVelocity = currentVelocity;
        lastTime = currentTime;

        telemetry.addData("Velocity (ticks/s)", currentVelocity);
        telemetry.addData("Acceleration (ticks/s²)", acceleration);
*/
        servo.setPosition(0.38);
        telemetry.addData("kP", kp);
        telemetry.addData("kI",ki);
        telemetry.addData("kD",kd);
        telemetry.addData("ff",ff);
        telemetry.addData("velocity", launcher.getVelocity());
        telemetry.addData("current",launcher.getCurrent(CurrentUnit.AMPS));

        if (gamepad2.left_bumper)
        {
            leftFeeder.setPower(1);
            rightFeeder.setPower(1);
        }
        if (gamepad2.left_trigger_pressed)
        {
            leftFeeder.setPower(0);
            rightFeeder.setPower(0);
        }
        if (gamepad2.b)
        {
            launcher.setPower(0);
        }
        if (gamepad2.y)
        {
            launcher.setVelocity(2200);
        }
        if (gamepad2.ps)
            launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,new PIDFCoefficients(kp,ki,kd,ff));

    }

}
