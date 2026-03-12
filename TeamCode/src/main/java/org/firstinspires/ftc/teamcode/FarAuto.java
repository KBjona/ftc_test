package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.mechanisms.ArcadeDrive;
import org.firstinspires.ftc.teamcode.mechanisms.AutoRotate;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.MecanumDrive;

enum AutoStateMachine
{
    ROTATESERVO,
    SHOOT,
    LEAVE,
    DONE

}
@Config
@Autonomous(name = "Far-Auto", group = "StarterBot")
public class FarAuto extends LinearOpMode {

    MecanumDrive Drive = new MecanumDrive();
    Launcher launcher = new Launcher();
    private Servo servo;
    int balls;
    AutoStateMachine autostatemachine;
    ElapsedTime runtime = new ElapsedTime();
    double left_pos;
    double rotate = 1;
    int tag = 20;
    AutoRotate rotation = new AutoRotate();

    public void runOpMode()
    {
        resetRuntime();
        time = getRuntime();
        waitForStart();
        Drive.init(hardwareMap);
        launcher.init(hardwareMap);
        rotation.init(hardwareMap,telemetry);
        servo = hardwareMap.get(Servo.class,"hood");
        balls = 4;
        autostatemachine = AutoStateMachine.ROTATESERVO;
        runtime.reset();


        while (opModeIsActive()) {
                switch (autostatemachine) {
                    case ROTATESERVO:
                        telemetry.addLine("MOVINGSERVO");
                        servo.setPosition(0.36); // was 0.74
                        if (runtime.seconds() > 0.05 && runtime.seconds() < 0.33)
                        {
                            Drive.startDriving(0.17); //TODO change direction
                        }
                        {
                            double rotate = rotation.getRotate(time, 20);
                            Drive.drive(0,0,rotate);
                        }
                        if (runtime.seconds() >2.5)
                        {
                            Drive.stopMotors();
                            runtime.reset();
                            autostatemachine = AutoStateMachine.SHOOT;
                        }

                        break;
                    case SHOOT:
                        if (balls > 0) {
                            telemetry.addLine("shooting");
                            telemetry.addData("velocity",launcher.getVelocity());
                            if (runtime.seconds() > (15 - balls*3.4)) {
                                servo.setPosition(0.36);
                                launcher.startLauncher(2102, 2100);
                                servo.setPosition(0.36);
                                balls--;
                            }
                            launcher.updateState();
                        } else if (balls == 0 && runtime.seconds() > 12) {
                            telemetry.addLine("NOBALLS");
                            launcher.updateState();
                            launcher.stopLauncher();
                            runtime.reset();
                            autostatemachine = AutoStateMachine.LEAVE; //DONE change back to leave when done testing
                        }
                        break;
                    case LEAVE:
                        telemetry.addLine("LEAVING");
                        Drive.startDriving(0.6);
                        if (runtime.seconds() > 0.4)
                        {
                            autostatemachine = AutoStateMachine.DONE;
                            Drive.stopMotors();

                        }
                        break;
                    case DONE:
                        telemetry.addLine("DONE");
                        break;
                }
                telemetry.addData("balls", balls);
                telemetry.addData("left_pos",left_pos);
                telemetry.addData("time", runtime);
                telemetry.addData("state", autostatemachine);
                telemetry.update();
        }
    }

}


