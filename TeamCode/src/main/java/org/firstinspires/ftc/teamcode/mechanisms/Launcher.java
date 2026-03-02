package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Launcher {
    private final double FEED_TIME_SECONDS = 0.20; //The feeder servos run this long when a shot is requested.
    private final double STOP_SPEED = 0.0; //We send this power to the servos when we want them to stop.
    private final double FULL_SPEED = 1.0;
    private double LAUNCHER_TARGET_VELOCITY = 1200;
    private double LAUNCHER_MIN_VELOCITY = 1150;

    private DcMotorEx launcher;
    private CRServo leftFeeder;
    private CRServo rightFeeder;

    ElapsedTime feederTimer = new ElapsedTime();

    LookUpTable shooterLookUpTable = new LookUpTable(2);

    private enum LaunchState {
        IDLE,
        SPIN_UP,
        LAUNCH,
        LAUNCHING,
    }
    private LaunchState launchState;


    public void init(HardwareMap hwMap)
    {
        launcher = hwMap.get(DcMotorEx.class,"launcher");
        leftFeeder = hwMap.get(CRServo.class, "left_feeder");
        rightFeeder = hwMap.get(CRServo.class, "right_feeder");

        launcher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launcher.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(300,0,0,10));

        leftFeeder.setDirection((DcMotorSimple.Direction.REVERSE));

        launchState = LaunchState.IDLE;
        stopLauncher();
        addPoint();
    }


    /**
    * 
    * // Shooter characterization: distance → (RPM, angle)
    * LookUpTable shooterTable = new LookUpTable(2);  // 2 outputs
    * shooterTable.add(1.0, 3000, 45);  // 1m: 3000 RPM, 45°
    * shooterTable.add(2.0, 3500, 50);  // 2m: 3500 RPM, 50°
    * shooterTable.add(3.0, 4000, 55);  // 3m: 4000 RPM, 55°
     */

    public void addPoint(){
        shooterLookUpTable.add(0,0,0);
    }

    public void stopfeeders()
    {
        leftFeeder.setPower(STOP_SPEED);
        rightFeeder.setPower(STOP_SPEED);
    }

    public void updateState()
    {
        switch (launchState)
        {
            case IDLE:
                break;
            case SPIN_UP:
                launcher.setVelocity(LAUNCHER_TARGET_VELOCITY);
                if(launcher.getVelocity() >= LAUNCHER_MIN_VELOCITY)
                    launchState = LaunchState.LAUNCH;
                break;
            case LAUNCH:
                leftFeeder.setPower(FULL_SPEED);
                rightFeeder.setPower(FULL_SPEED);
                launcher.setVelocity(shooterLookUpTable.get(5));
                feederTimer.reset();
                launchState =LaunchState.LAUNCHING;
                break;
            case LAUNCHING:
                if(feederTimer.seconds() > FEED_TIME_SECONDS)
                {
                    stopfeeders();
                    launchState = LaunchState.IDLE;
                }
                break;

        }
    }

    public void startLauncher(double velocity, double minvelocity)
    {
        LAUNCHER_MIN_VELOCITY = minvelocity;
        LAUNCHER_TARGET_VELOCITY = velocity;
        if (launchState == LaunchState.IDLE)
        {
            launchState = LaunchState.SPIN_UP;
        }
    }
    public void stopLauncher()
    {
        stopfeeders();
        launcher.setVelocity(STOP_SPEED);
        launchState = LaunchState.IDLE;
    }

    public String getState() {return launchState.toString(); }
    public double getVelocity() {return launcher.getVelocity(); }

    public void spinLauncher() {launcher.setVelocity(LAUNCHER_TARGET_VELOCITY);}

}
