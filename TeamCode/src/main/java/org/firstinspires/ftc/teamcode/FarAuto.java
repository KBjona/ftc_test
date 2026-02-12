package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

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

    ElapsedTime runtime = new ElapsedTime();

    double left_pos;

    public void runOpMode()
    {

        waitForStart();
        Drive.init(hardwareMap);
        launcher.init(hardwareMap);
        servo = hardwareMap.get(Servo.class,"hood");
        balls = 4;
        autostatemachine = AutoStateMachine.ROTATESERVO;
        runtime.reset();

        if (opModeIsActive())
        {
            while (opModeIsActive()) {
                    switch (autostatemachine) {
                        case ROTATESERVO:
                            telemetry.addLine("MOVINGSERVO");
                            servo.setPosition(0.75);
                            if (runtime.seconds() > 2)
                            {
                                runtime.reset();
                                autostatemachine = AutoStateMachine.SHOOT;
                            }
                            break;
                        case SHOOT:
                            if (balls > 0) {
                                telemetry.addLine("shooting");
                                telemetry.addData("velocity",launcher.getVelocity());
                                if (runtime.seconds() > (15 - balls*4)) {
                                    launcher.startLauncher(2100, 2050);
                                    balls--;
                                }
                                launcher.updateState();
                            } else if (balls == 0 && runtime.seconds() > 12) {
                                telemetry.addLine("NOBALLS");
                                launcher.updateState();
                                launcher.stopLauncher();
                                runtime.reset();
                                autostatemachine = AutoStateMachine.LEAVE;
                            }
                            break;
                        case LEAVE:
                            telemetry.addLine("LEAVING");
                            Drive.startDriving();
                            if (runtime.seconds() > 0.3)
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


}
