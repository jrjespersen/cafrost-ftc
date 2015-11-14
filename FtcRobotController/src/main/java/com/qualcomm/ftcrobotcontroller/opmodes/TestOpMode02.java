package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

public class TestOpMode02 extends OpMode
{
    String configurationName = "Carl";
    String rightmotorname = "m1";
    String leftmotorname = "m2";
    String servo1name = "s1";
    String servo2name = "s2";
    String irseekername = "ir1";
    String distancesensorname = "od1";
    String touchsensorname = "t1";

    DcMotor motorLeftWheel;
    DcMotor motorRightWheel;
    Servo s1;
    Servo s2;
    OpticalDistanceSensor od1;
    TouchSensor t1;
    IrSeekerSensor ir1;

    double s1Position = 0.0;
    double s1Delta = 0.1;
    double s2Position = 0.0;
    double s2Delta = 0.1;

    /*
     * Constructor
     */
    public TestOpMode02()
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

        try
        {
            motorLeftWheel = hardwareMap.dcMotor.get(leftmotorname);
            motorLeftWheel.setDirection(DcMotor.Direction.REVERSE);
        }
        catch (Exception e)
        {
            DbgLog.msg(e.getLocalizedMessage());
        }
        try
        {
            motorRightWheel = hardwareMap.dcMotor.get(rightmotorname);
        }
        catch (Exception e)
        {
            DbgLog.msg(e.getLocalizedMessage());
        }
        try
        {
            s1 = hardwareMap.servo.get(servo1name);
        }
        catch (Exception e)
        {
            DbgLog.msg(e.getLocalizedMessage());
        }
        try
        {
            s2 = hardwareMap.servo.get(servo2name);
        }
        catch (Exception e)
        {
            DbgLog.msg(e.getLocalizedMessage());
        }
        try
        {
            od1 = hardwareMap.opticalDistanceSensor.get(distancesensorname);
        }
        catch (Exception e)
        {
            DbgLog.msg(e.getLocalizedMessage());
        }
        try
        {
            t1 = hardwareMap.touchSensor.get(touchsensorname);
        }
        catch (Exception e)
        {
            DbgLog.msg(e.getLocalizedMessage());
        }
        try
        {
            ir1 = hardwareMap.irSeekerSensor.get(irseekername);
        }
        catch (Exception e)
        {
            DbgLog.msg(e.getLocalizedMessage());
        }

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

        // update the position of servo1.
        if (gamepad2.a) {
            s1Position += s1Delta;
        }

        if (gamepad2.y) {
            s1Position -= s1Delta;
        }

        // update the position of servo2.
        if (gamepad2.x) {
            s2Position += s2Delta;
        }
        if (gamepad2.b) {
            s2Position -= s2Delta;
        }

        // clip the position values so that they never exceed their allowed range.

        // write position values to the wrist and claw servo
        s1.setPosition(s1Position);
        s2.setPosition(s2Position);

        telemetry.addData("Left Power", "Left: " + String.format("%.2f", left));
        telemetry.addData("Right Power", "Right: " + String.format("%.2f", right));
        telemetry.addData("S1", "S1:" + String.format("%.2f", s1Position));
        telemetry.addData("S2", "S2:" + String.format("%.2f", s2Position));
    }

    @Override
    public void stop()
    {
        telemetry.addData(configurationName, "*** stop ***");
        super.stop();
        motorLeftWheel.setPower(0);
        motorRightWheel.setPower(0);

    }

    boolean isTouchSensorPressed(TouchSensor sensor)
    {
        boolean returnValue = false;

        if (sensor != null)
        {
            returnValue = sensor.isPressed ();
        }
        return returnValue;
    }
}
