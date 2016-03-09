
package org.usfirst.frc.team4914.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.usfirst.frc.team4914.robot.commands.ExampleCommand;
import org.usfirst.frc.team4914.robot.subsystems.ExampleSubsystem;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ParticleFilterCriteria2;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.AxisCamera;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;

    Command autonomousCommand;
    SendableChooser chooser;
    private AxisCamera camera = new AxisCamera("10.49.14.11");
    private int cameraPixelHeight = 480;
    private ParticleAnalysisReport[] reports;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		oi = new OI();
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", new ExampleCommand());
//        chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", chooser);
        
        camera.writeMaxFPS(20);
        camera.writeCompression(30);
        camera.writeResolution(AxisCamera.Resolution.k640x480);
    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
    public void autonomousInit() {
        autonomousCommand = (Command) chooser.getSelected();
        
		/* String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
		switch(autoSelected) {
		case "My Auto":
			autonomousCommand = new MyAutoCommand();
			break;
		case "Default Auto":
		default:
			autonomousCommand = new ExampleCommand();
			break;
		} */
    	
    	// schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    /*
     * ATTEMPT AT VISION PROCESSING TAKE 1
     */    
    
    public void processImage() {
    	ColorImage image = null;
    	BinaryImage thresholdImage = null;
    	BinaryImage bigObjectsImage = null;
    	BinaryImage convexHullImage = null;
    	BinaryImage filteredImage = null;
    	ParticleFilterCriteria2[] cc = new ParticleFilterCriteria2[2];
    	
    	cc[0] = new ParticleFilterCriteria2(NIVision.MeasurementType.MT_BOUNDING_RECT_WIDTH, 30, 400, 0, 0);
    	cc[1] = new ParticleFilterCriteria2(NIVision.MeasurementType.MT_BOUNDING_RECT_HEIGHT, 40, 400, 0, 0);
    	
    	try {
    		image = camera.getImage();
    		thresholdImage = image.thresholdHSV(111, 149, 222, 255, 91, 255);
    		bigObjectsImage = thresholdImage.removeSmallObjects(false, 2);
    		convexHullImage = bigObjectsImage.convexHull(false);
    		filteredImage = convexHullImage.particleFilter(cc);
    		reports = filteredImage.getOrderedParticleAnalysisReports(1);
    		// for (int i = 0; i <= reports.length; i++) {
    			System.out.println("Center of mass x: " + reports[0].center_mass_x);
    			System.out.println("Center of mass y: " + reports[0].center_mass_y);
    			System.out.println("Bounding rect width: " + reports[0].boundingRectWidth);
    			System.out.println("Bounding rect height: " + reports[0].boundingRectHeight);
    			
    			double distance;
    			double Tft = 2.66;
    			double FOVpixel = cameraPixelHeight;
    			double Tpixel = 2 * 85;
    			
    			distance = Tft * FOVpixel / (2 * Tpixel * Math.tan(51));
    			
    			System.out.println("Distance To Target: " + distance);
    			
    			// 49 degrees optimal vertical FOV for M1013
    			// 51 degrees according to manual tech specs
    			// 67 degrees horizontal according to manual tech specs
    			// 37.4 degrees optimal vertcal FOV for M1011
    			
    			System.out.println("Centered: " + isCentered());
    			
    		// }
    	}
    	catch (Exception e) { }
    	
    	try {
        	image.free();
        	thresholdImage.free();
        	bigObjectsImage.free();
        	convexHullImage.free();
        	filteredImage.free();
    	}
    	catch (NIVisionException e) { }
    } // end of method processImage()
    
    public boolean isCentered() {		
		double midpoint = cameraPixelHeight / 2;
		double epsilon = 50;
		return reports[0].center_mass_x < midpoint + epsilon &&
			   reports[0].center_mass_x > midpoint - epsilon;
    }
    
    public void centerRobot() {
    	// 30 degrees left and right
    	// Robot.driveTrain.rotateCW(30);
    	// Robot.driveTrain.rotateCCW(60);
    	
    	// 30 degrees CW
    	
    	/*
    	
    	double initialBearing = Robot.driveTrain.gyro.getAngle();
     	double finalBearing = initialBearing + 30;
     	
     	initialBearing += 360;
     	initialBearing %= 360;
     	finalBearing += 360;
     	finalBearing %= 360;
     	
     	Robot.driveTrain.resetGyro();
     	
       	while (!(Robot.driveTrain.gyro.getAngle() > finalBearing - Robot.driveTrain.ANGLE_EPSILON && 
       			Robot.driveTrain.gyro.getAngle() < finalBearing + Robot.driveTrain.ANGLE_EPSILON)) {
       		
       		Robot.driveTrain.setLeftVictor(1);
 	      	Robot.driveTrain.setRightVictor(-1);
 	      	
 	      	processImage();
 	      	if (isCentered()) { return; }
       	}
       	Robot.driveTrain.stop();
       	
       	// 60 degrees CCW
       	
       	initialBearing = Robot.driveTrain.gyro.getAngle();
    	finalBearing = initialBearing - 60;
    	
    	initialBearing += 360;
    	initialBearing %= 360;
    	finalBearing += 360;
    	finalBearing %= 360;
     	
     	Robot.driveTrain.resetGyro();
    	
      	while (!(Robot.driveTrain.gyro.getAngle() > finalBearing - Robot.driveTrain.ANGLE_EPSILON && 
      			Robot.driveTrain.gyro.getAngle() < finalBearing + Robot.driveTrain.ANGLE_EPSILON)) {
      		Robot.driveTrain.setLeftVictor(-1);
      		Robot.driveTrain.setRightVictor(1);
      		
      		processImage();
      		if (isCentered()) { return; }
      	}
      	
      	Robot.driveTrain.stop();
      	*/
    	
    	// above code should theoretically work
    }
}
