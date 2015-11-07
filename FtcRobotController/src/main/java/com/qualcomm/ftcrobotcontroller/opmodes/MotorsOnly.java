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

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);

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

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }
}
