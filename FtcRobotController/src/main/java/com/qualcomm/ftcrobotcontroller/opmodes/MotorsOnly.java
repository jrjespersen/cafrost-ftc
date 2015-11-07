package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class MotorsOnly extends OpMode
{
    DcMotor motorLeftWheel;
    DcMotor motorRightWheel;

    String configurationName = "Carl";

    /*
     * Constructor
     */
    public MotorsOnly()
    {

    }

    @Override
    public void start()
    {
        telemetry.addData(configurationName, "*** start ***");
        super.start();
    }

    @Override
    public void init()
    {
        telemetry.addData(configurationName, "*** init started ***");

        motorLeftWheel = hardwareMap.dcMotor.get("m2");
        motorLeftWheel.setDirection(DcMotor.Direction.REVERSE);
        motorRightWheel = hardwareMap.dcMotor.get("m1");
        telemetry.addData(configurationName, "*** init finished ***");
    }

    @Override
    public void loop()
    {
        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.right_stick_x;
        float left = throttle + direction;
        float right = throttle - direction;
        left = Range.clip(left, -1, 1);
        right = Range.clip(right, -1, 1);

        motorLeftWheel.setPower(left);
        motorRightWheel.setPower(right);

        telemetry.addData("Left Power", "Left: " + String.format("%.2f", left));
        telemetry.addData("Right Power", "Right: " + String.format("%.2f", right));
    }

    @Override
    public void stop()
    {
        telemetry.addData(configurationName, "*** stop ***");
        super.stop();
        motorLeftWheel.setPower(0);
        motorRightWheel.setPower(0);

    }
}
