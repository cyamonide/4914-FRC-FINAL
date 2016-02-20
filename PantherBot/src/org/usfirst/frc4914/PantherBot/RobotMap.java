// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc4914.PantherBot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI.Port;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static SpeedController driveTrainRightDrive;
    public static SpeedController driveTrainLeftDrive;
    public static RobotDrive driveTrainRobotDrive;
    public static AnalogInput driveTrainUltra;
    public static SpeedController shooterTopFly;
    public static SpeedController shooterBottomFly;
    public static SpeedController shooterIntake;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static ADXRS450_Gyro gyro;
    public static PIDController driveControl;

    public static void init() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        driveTrainRightDrive = new Victor(8);
        LiveWindow.addActuator("Drive Train", "Right Drive", (Victor) driveTrainRightDrive);
        
        driveTrainLeftDrive = new Victor(9);
        LiveWindow.addActuator("Drive Train", "Left Drive", (Victor) driveTrainLeftDrive);
        
        driveTrainRobotDrive = new RobotDrive(driveTrainLeftDrive, driveTrainRightDrive);
        
        driveTrainRobotDrive.setSafetyEnabled(true);
        driveTrainRobotDrive.setExpiration(0.1);
        driveTrainRobotDrive.setSensitivity(0.5);
        driveTrainRobotDrive.setMaxOutput(1.0);

        driveTrainRobotDrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        driveTrainUltra = new AnalogInput(3);
        LiveWindow.addSensor("Drive Train", "Ultra", driveTrainUltra);
        
        shooterTopFly = new VictorSP(3);
        LiveWindow.addActuator("Shooter", "Top Fly", (VictorSP) shooterTopFly);
        
        shooterBottomFly = new VictorSP(0);
        LiveWindow.addActuator("Shooter", "Bottom Fly", (VictorSP) shooterBottomFly);
        
        shooterIntake = new TalonSRX(2);
        LiveWindow.addActuator("Shooter", "Intake", (TalonSRX) shooterIntake);
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        gyro = new ADXRS450_Gyro(Port.kOnboardCS0);
        LiveWindow.addActuator("Drive Train", "Gyro", (ADXRS450_Gyro) gyro);
        
        driveControl = new PIDController(1, 0.001, 0.0, Robot.driveTrain.ultra, (PIDOutput) driveTrainRobotDrive);
        LiveWindow.addActuator("AUTO DRIVE", "PID DRIVE", (PIDController) driveControl);
    }
}
