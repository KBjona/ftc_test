package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Vision;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;

@Config
@TeleOp(name = "Main-Teleop", group = "StarterBot")
public class MainTeleop extends OpMode {
    MecanumDrive Drive = new MecanumDrive();
    Launcher launcher = new Launcher();
    Vision vision = new Vision();
    Intake intake = new Intake();
    public static double  targetvelocity = 2110.0;
    int tryPark = 1;
    private Servo servo;
    public static double targethood = 0.42;
    int tag = 0;
    boolean driveMode = false;
    private DcMotor kicker;
    double angleTolerance = 2;


    @Override
    public void init()
    {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        servo = hardwareMap.get(Servo.class,"hood");
        Drive.init(hardwareMap);
        launcher.init(hardwareMap);
        vision.init(hardwareMap,telemetry);
        kicker = hardwareMap.get(DcMotor.class, "kicker");
        kicker.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void init_loop()
    {
        if (gamepad1.dpadRightWasReleased()) {
            telemetry.addLine("TAG IS NOW RED");
            tag = 24;
        }
        if (gamepad1.dpadLeftWasReleased()) {
            telemetry.addLine("TAG IS NOW BLUE");
            tag = 20;
        }
        telemetry.addLine("SCREAM AT ME OFIRRRRRRRRRRRRRRRRRRRRR");
        telemetry.addData("TAG (20 = blue, 24 = red)", tag);
    }


    public void start()
    {
        resetRuntime();
        time = getRuntime();
    }

    @Override
    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x; // For movement

        double dist = vision.getDist(tag);
        double angle = launcher.lookUpTable.get(dist - 2)[0];
        double velocity = launcher.lookUpTable.get(dist - 2)[1]; // For shooting

        { // Gamepad 1

            if (gamepad1.right_bumper)
                tryPark = 4;
            if (gamepad1.left_bumper)
                tryPark = 1;
            if (gamepad1.dpad_left)
                driveMode = true;
            if (gamepad1.dpad_right)
                driveMode = false;


            if (gamepad1.right_trigger > 0.5) {
                rotate = vision.getRotate(time, tag);
                telemetry.addData("rotation", rotate);
            }

            if (driveMode)
                Drive.FieldDrive(forward / tryPark, strafe / tryPark, rotate / tryPark);
            else
                Drive.drive(forward / tryPark, strafe / tryPark, rotate / tryPark);
        }

        { //Gamepad 2
            if (gamepad2.y)
                launcher.spinLauncher();
            else if (gamepad2.b)
                launcher.stopLauncher();
            if (gamepad2.left_trigger > 0.5 && vision.getOffset(tag) < angleTolerance) {
                servo.setPosition(angle);
                launcher.startLauncher(velocity, velocity - 2);
            } else if (gamepad2.right_trigger_pressed) // 52cm, 0.24, 1250
            {
                servo.setPosition(0.23); // 0.26
                launcher.startLauncher(1220, 1220 - 2); //1380
            }
        }
        launcher.updateState(); // CLOSE - 0.23, 1TILE - 0.26, 2TILE - 0.28
        //                         CLOSE - 1220, 1TILE - 1370, 2TILE - 1500
        // first - 0.23, 1220 - tzamod
        // second - 0.25, 1300, 1.24-0.31
        // third - 0.29. 1560,
        // fourth - 0.29, 1700
        telemetry.addData("current target velocity", launcher.getTargetVelocity());
        telemetry.addData("Hood position", servo.getPosition());
        telemetry.addData("Launcher state", launcher.getState());
        telemetry.addData("Launcher velocity", launcher.getVelocity());
        telemetry.addData("flywheel cur", launcher.getVoltage());
        telemetry.addData("dist",dist);
        telemetry.addData("angle",angle);
        telemetry.addData("velocity",velocity);
        telemetry.addData("Intake state (1 - on), (0-off), (-1 - rev)", intake.state);
        telemetry.update();

        if (gamepad1.ps)
        {
            Drive.resetIMU();
        }
    }

}
