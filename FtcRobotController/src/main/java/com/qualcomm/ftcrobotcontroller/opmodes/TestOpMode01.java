package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class TestOpMode01 extends OpMode
{
    DcMotor motorLeftWheel;
    DcMotor motorRightWheel;
    Servo s1;
    Servo s2;

    final static double S1_MIN_RANGE  = 0.20;
    final static double S1_MAX_RANGE  = 0.90;
    final static double S2_MIN_RANGE  = 0.20;
    final static double S2_MAX_RANGE  = 0.70;

    double s1Position;
    double s1Delta = 0.1;
    double s2Position;
    double s2Delta = 0.1;

    String configurationName = "Carl";

    /*
     * Constructor
     */
    public TestOpMode01()
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
        s1 = hardwareMap.servo.get("s1");
        s2 = hardwareMap.servo.get("s2");
        s1Position = 0.2;
        s2Position = 0.2;
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
        s1Position = Range.clip(s1Position, S1_MIN_RANGE, S1_MAX_RANGE);
        s2Position = Range.clip(s2Position, S2_MIN_RANGE, S2_MAX_RANGE);

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
}
