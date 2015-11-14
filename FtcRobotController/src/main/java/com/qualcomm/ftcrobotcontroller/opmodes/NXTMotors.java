package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LegacyModule;
//import com.qualcomm.robotcore.hardware.configuration.LegacyModuleControllerConfiguration;
import com.qualcomm.robotcore.util.Range;

public class NXTMotors extends OpMode
{
    DcMotor motorLeftWheel;
    DcMotor motorRightWheel;
    DcMotor nxt1;
    DcMotor nxt2;
    DcMotorController.DeviceMode devMode;
    DcMotorController armController;

    String configurationName = "Carl";
    int numOpLoops = 1;

    final static double MAX_MOTOR_POWER = 1.00;
    final static double MAX_MOTOR_STICK_POWER = 0.50;
    final static double MOTOR_TURBO_POWER = 0.50;

    /*
     * Constructor
     */
    public NXTMotors()
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

        devMode = DcMotorController.DeviceMode.WRITE_ONLY;
        LegacyModule x = hardwareMap.legacyModule.get("Legacy Module");
        nxt1 = hardwareMap.dcMotor.get("arm1");
        nxt2 = hardwareMap.dcMotor.get("arm1");
        nxt1.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        nxt2.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        armController = hardwareMap.dcMotorController.get("arms");

        telemetry.addData(configurationName, "*** init finished ***");
    }

    @Override
    public void loop()
    {
        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.right_stick_x;
        float left = throttle + direction;
        float right = throttle - direction;

        left = Range.clip(left, -(float) MAX_MOTOR_STICK_POWER, (float) MAX_MOTOR_STICK_POWER);
        right = Range.clip(right, -(float) MAX_MOTOR_STICK_POWER, (float) MAX_MOTOR_STICK_POWER);


        float turbo = gamepad1.left_trigger;
        if (turbo > 0)
        {
            left = left + (float)MOTOR_TURBO_POWER;
            right = right + (float)MOTOR_TURBO_POWER;
        }

        left = Range.clip(left, -(float) MAX_MOTOR_POWER, (float) MAX_MOTOR_POWER);
        right = Range.clip(right, -(float) MAX_MOTOR_POWER, (float) MAX_MOTOR_POWER);
        motorLeftWheel.setPower(left);
        motorRightWheel.setPower(right);


        if (devMode == DcMotorController.DeviceMode.WRITE_ONLY)
        {

            if (gamepad1.dpad_left) {
                // Nxt devices start up in "write" mode by default, so no need to switch modes here.
                nxt1.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
                nxt2.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
            }
            if (gamepad1.dpad_right) {
                // Nxt devices start up in "write" mode by default, so no need to switch modes here.
                nxt1.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
                nxt2.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            }

            float arm1 = -gamepad2.left_stick_y;
            float arm2 = gamepad2.right_stick_y;
            // clip the right/left values so that the values never exceed +/- 1
            arm1 = Range.clip(arm1, -1, 1);
            arm2 = Range.clip(arm2, -1, 1);
            nxt1.setPower(arm1);
            nxt2.setPower(arm2);
        }

        if (numOpLoops % 17 == 0)
        {
            armController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
        }
        if (armController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY)
        {
            telemetry.addData("Text", "free flow text");
            telemetry.addData("nxt1 motor", nxt1.getPower());
            telemetry.addData("nxt2 motor", nxt2.getPower());
            telemetry.addData("RunMode: ", nxt1.getMode().toString());
            armController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
            numOpLoops = 0;
        }
        devMode = armController.getMotorControllerDeviceMode();
        numOpLoops++;


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
