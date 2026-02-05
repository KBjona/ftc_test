package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.mechanisms.ArcadeDrive;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;

enum AutoStateMachine
{
    ROTATESERVO,
    SHOOT,
    LEAVE,
    DONE



}
@Autonomous(name = "Far-Auto", group = "StarterBot")
public class FarAuto extends LinearOpMode {

    ArcadeDrive Drive = new ArcadeDrive();
    Launcher launcher = new Launcher();
    private Servo servo;
    int balls;
    AutoStateMachine autostatemachine;


    public void runOpMode()
    {
        Drive.init(hardwareMap);
        launcher.init(hardwareMap);
        servo = hardwareMap.get(Servo.class,"hood");
        balls = 3;
        autostatemachine = AutoStateMachine.DONE;

        waitForStart();
        Drive.init(hardwareMap);
        launcher.init(hardwareMap);
        servo = hardwareMap.get(Servo.class,"hood");
        balls = 3;
        autostatemachine = AutoStateMachine.ROTATESERVO;
        if (opModeIsActive())
        {
            while (opModeIsActive()) {
                    switch (autostatemachine) {
                        case ROTATESERVO:
                            telemetry.addLine("MOVINGSERVO");
                            servo.setPosition(0.75);
                            sleep(1000);
                            autostatemachine = AutoStateMachine.SHOOT;
                            break;
                        case SHOOT:
                            if (balls > 0) {
                                telemetry.addLine("shooting");
                                launcher.startLauncher(2000, 1900);
                                balls--;
                                launcher.updateState();
                                sleep(2000);
                            } else if (balls == 0) {
                                telemetry.addLine("NOBALLS");
                                launcher.updateState();
                                autostatemachine = AutoStateMachine.LEAVE;
                                sleep(1000);
                            }
                            break;
                        case LEAVE:
                            telemetry.addLine("LEAVING");
                            Drive.driveDistance(35);
                            sleep(3000);
                            break;
                        case DONE:
                            telemetry.addLine("DONE");
                            break;
                    }

                    telemetry.addData("state", autostatemachine);
                    telemetry.update();
            }
        }

    }


}
