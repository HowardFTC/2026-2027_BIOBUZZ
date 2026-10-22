package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.text.DecimalFormat;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp

public class BasicDrive extends LinearOpMode {

    //initialize motor variables
    DcMotor frontLeft = null;
    DcMotor frontRight = null;
    DcMotor backLeft = null;
    DcMotor backRight = null;
    
    public void runOpMode() {
        
        //set motor variables
        frontLeft = hardwareMap.get(DcMotor.class, "FrontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "FrontRight");
        backLeft = hardwareMap.get(DcMotor.class, "BackLeft");
        backRight = hardwareMap.get(DcMotor.class, "BackRight");
        
        //reverse right wheels
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        
        telemetry.addData("Status", "Init successful");
        telemetry.update();
        
        ElapsedTime runtime = new ElapsedTime();
        
        waitForStart();
        
        runtime.reset();
        while(opModeIsActive()) {
            
            //get gamepad stick data
            double axial = gamepad1.left_stick_y;
            double lateral = -gamepad1.left_stick_x;
            double yaw = -gamepad1.right_stick_x;
 
            //calculate motor powers
            double frontLeftPower = axial + lateral + yaw;
            double frontRightPower = (axial - lateral) - yaw;
            double backLeftPower = (axial - lateral) + yaw;
            double backRightPower = (axial + lateral) - yaw;
            
            //find the highest motor power
            double max = Math.max(frontLeftPower, frontRightPower);
            max = Math.max(max, backLeftPower);
            max = Math.max(max, backRightPower);
            
            //normalize the motor powers
            if (max > 1.0) {
                frontLeftPower /= max;
                frontRightPower /= max;
                backLeftPower /= max;
                backRightPower /= max;
            }
            
            //crawl mode
            if (gamepad1.left_bumper) {
                frontLeftPower /= 3;
                frontRightPower /= 3;
                backLeftPower /= 3;
                backRightPower /= 3;
            }
            
            //set motor powers
            frontLeft.setPower(frontLeftPower);
            frontRight.setPower(frontRightPower);
            backLeft.setPower(backLeftPower);
            backRight.setPower(backRightPower);
            

            //update with status data
            telemetry.addData("Runtime", runtime);
            
            DecimalFormat format = new DecimalFormat("0.000");
            telemetry.addData("Front L/R", format.format(frontLeftPower) + " / " + format.format(frontRightPower));
            telemetry.addData("Back L/R", format.format(backLeftPower) + " / " + format.format(backRightPower));
            telemetry.update();
        }
        
    }
}