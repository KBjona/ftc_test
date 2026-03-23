package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.Vision;

@TeleOp(name = "LookUpTable test", group = "test")

public class LookUpTableTest extends OpMode {
    Launcher launcher = new Launcher();
    Vision vision = new Vision();
    private Servo servo;


    @Override
    public void init()
    {
        launcher.init(hardwareMap);
        vision.init(hardwareMap,telemetry);
        servo = hardwareMap.get(Servo.class,"hood");
    }
    @Override
    public void loop()
    {
        double dist = vision.getDist(24);
        double angle = launcher.lookUpTable.get(dist)[0];
        double velocity = launcher.lookUpTable.get(dist)[1];

        if (gamepad2.left_trigger_pressed)
        {
            servo.setPosition(angle);
            launcher.startLauncher(velocity,velocity-10);
            telemetry.addLine("firing");
        }
        telemetry.addData("Dist", dist);
        telemetry.addData("angle", angle);
        telemetry.addData("velocity", velocity);

        launcher.updateState();


    }


}
