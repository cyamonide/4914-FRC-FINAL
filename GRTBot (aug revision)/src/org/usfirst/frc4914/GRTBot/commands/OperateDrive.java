// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc4914.GRTBot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc4914.GRTBot.Robot;


public class OperateDrive extends Command {

    public OperateDrive() {
    	requires(Robot.drivetrain);
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
    }

    protected void initialize() {
    }

    protected void execute() {
    	
    	if (Robot.drivetrain.isInvertedDrive) { invertedTankDrive(); }
    	else if (!Robot.drivetrain.isInvertedDrive) { normalTankDrive(); }
    	// codriver shoot Z-axis rotation
    	if (Robot.oi.driverLJ() == 0 && Robot.oi.driverRJ() == 0) {
    		Robot.drivetrain.setLeftVictor(Robot.oi.codriverZ() * 0.5);
    		Robot.drivetrain.setRightVictor(-Robot.oi.codriverZ() * 0.5);
    	}
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	Robot.drivetrain.stop();
    }

    protected void interrupted() {
    	end();
    }
    
    /******* CUSTOM METHODS *******/
    
    public void normalTankDrive() {
    	Robot.drivetrain.setLeftVictor(Robot.oi.driverRJ() * Robot.drivetrain.speedMultiplier);
		Robot.drivetrain.setRightVictor(Robot.oi.driverLJ() * Robot.drivetrain.speedMultiplier);
    }
    
    public void invertedTankDrive() {
    	Robot.drivetrain.setLeftVictor(-Robot.oi.driverLJ() * Robot.drivetrain.speedMultiplier);
		Robot.drivetrain.setRightVictor(-Robot.oi.driverRJ() * Robot.drivetrain.speedMultiplier);
    }
    
    public void normalStraightDrive() {
    	Robot.drivetrain.driveStraight(Robot.oi.driverLJ() * Robot.drivetrain.speedMultiplier);
    }
    
    public void invertedStraightDrive() {
    	Robot.drivetrain.driveStraight(-Robot.oi.driverLJ() * Robot.drivetrain.speedMultiplier);
    }
}