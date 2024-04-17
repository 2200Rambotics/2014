/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Encoder; //for encoders
import edu.wpi.first.wpilibj.GenericHID; //unknown use (joystick buttons?
import edu.wpi.first.wpilibj.SimpleRobot; //the basic GingerDrive, required
import edu.wpi.first.wpilibj.RobotDrive; //used for GingerDrive, somewhat required
import edu.wpi.first.wpilibj.Timer; //timer library
import edu.wpi.first.wpilibj.Joystick; //joystick library
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard; //used for smartdash
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser; //used for selected autonomous
import edu.wpi.first.wpilibj.command.Command;        
import edu.wpi.first.wpilibj.DigitalInput; //used 
import edu.wpi.first.wpilibj.HiTechnicCompass; //used for the compass on I2c
import edu.wpi.first.wpilibj.Talon; //used for all the non-GingerDrive speed controllers
import edu.wpi.first.wpilibj.DigitalOutput; //used for the spike
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.templates.ShooterLogic;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.templates.autoTwo;
import edu.wpi.first.wpilibj.templates.AutoTurn;


public class DriveClass extends SimpleRobot
{
//    DriveClass()
//    {
//        System.out.println("Made Drive object");
//    }
    //joystick objects
    Joystick joyL = new Joystick(1);
    Joystick joyR = new Joystick(2);

    //drive motors
    RobotDrive GingerDrive = new RobotDrive(1, 2);
    
    //arm motor objects
    public Talon arm1 = new Talon(3);
    Talon arm2 = new Talon(4);
    
    //roller motor objects
    Talon roller1 = new Talon(5);
    Talon roller2 = new Talon(6);
    
    //winch objects
    Talon wench = new Talon(7);
    Relay spikeTrig = new Relay(5);
    
    //shot adjuster objects
    Talon adjuster = new Talon(8);
    
    //encoder objects
    //Encoder encL = new Encoder(1, 2); //declare left encoder object, port 1 a chan, port 2 bchan
    //Encoder encR = new Encoder(3, 4);
    
    
    ShooterLogicPot shooterlogic = new ShooterLogicPot(1,1, spikeTrig, 
            wench, arm1, roller1, arm2, roller2);
    

    
    String autonomousCommand;
    SendableChooser autoChooser;
    double teleStartTime;
    
    
    boolean isManlyMode = false;
    boolean one, two, three, four, five, six, seven, eight, nine, ten, eleven; //declaring buttonstate container variable
       
    
    
    
    
    
    public void robotInit()
    {
        System.out.println("robotinit started");
        autoChooser = new SendableChooser();
        autoChooser.addDefault("Single Ball", "Single Ball");
        autoChooser.addObject("Two Ball", "Two Ball");
        autoChooser.addObject("No Autonomous", "No Autonomous");
        autoChooser.addObject("Turn", "Turn");
        SmartDashboard.putData("Autonomous mode Chooser", autoChooser);
        System.out.println("robotinit finished");   
    }
//    public void disabledContinuous()
//    {
//        SmartDashboard.putData("Autonomous Chooooooser: ", autoChooser);
//        SmartDashboard.putString("Test smartdash", "teswt value");
//    }
//    public void autonomousInit()
//    {
//          System.out.println("autonomousinit started");
//        autoCommand = (Command) autoChooser.getSelected();
//        autoCommand.start();
//          System.out.println("autonomousinit finished");
//    }
//    public void autonomousPeriodic()
//    {
//          System.out.println("autonomous periodic started");
//        Scheduler.getInstance().run();
//          System.out.println("autonomous periodic finished");
//    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void manlyMode()
    {
        while(isOperatorControl() &&  isEnabled())
        {
            one = joyL.getRawButton(1);  //getting boolean values for each
            two = joyL.getRawButton(2);  //button, the name signifying which
            three = joyL.getRawButton(3);//button on joystick
            four = joyL.getRawButton(4);
            five = joyL.getRawButton(5);
            six = joyL.getRawButton(6);
            seven = joyL.getRawButton(7);
            eight = joyL.getRawButton(8);
            nine = joyL.getRawButton(9);
            ten = joyL.getRawButton(10);
            eleven = joyL.getRawButton(11);
            //following if else statements check buttonstate containers to 
            //activate certain motors. Separate else statements required for
            //multiple silmultanious button operation
                        
            if(one)
            {
                spikeTrig.set(Relay.Value.kForward); //trigger open
            }
            else if (eight)
            {
                spikeTrig.set(Relay.Value.kReverse);
            }
            else
            {
                spikeTrig.set(Relay.Value.kOff); //trigger close
            }
            
            if(two)
            {
                wench.set(1);  //pull back winch
            }
            else if (three)
            {
                wench.set(-1);  //stop pulling back winch
            }
            else
            {
                wench.set(0);
            }
            
            if(three)
            {
                //empty as of now
            }
            else
            {
                //empty as of now
            }
            
            if(four)
            {
                roller1.set(1);
                roller2.set(1); //both rollers go in
            }           
            else if(five)
            {
                roller1.set(-1);
                roller2.set(-1); //set both rollers out
            }
            else
            {
                roller1.set(0);
                roller2.set(0); //set both rollers not moving
            }
            
            if(six)
            {
                arm1.set(0.5); //set arm 1 going up
            }
            
           else if(seven)
            {
                arm1.set(-0.5); //set arm1 going down
            }
            else
            {
                arm1.set(0.0); //set arm 1 not moving
            }
            
            if(eight)
            {
                //empty, or used to rotate robot 90 degrees
            }
            else
            {
                //empty, if eight is used for 90* turn, DO NOT USE ELSE
            }
            
            if(nine)
            {
                //empty, or used to rotate robot 90 degrees
            }
            else
            {
                //empty, if eight is used for 90* turn, DO NOT USE ELSE
            }
            
            if(eleven)
            {
                arm2.set(0.5); //set arm 2 going up
            }
                     
            else if(ten)
            {
                arm2.set(-0.5); //set arm2 moving down
            }
            else
            {
                arm2.set(0.0); //set arm2 not moving
            }
            GingerDrive.arcadeDrive(((joyR.getX())* -1), joyR.getY());  //regular moving code, arcade GingerDrive
            Timer.delay(0.01); //delay for robotGingerDrive library
        }
    }
    
    public void operatorControl() 
    {
        teleStartTime = Timer.getFPGATimestamp();
 //       encL.reset(); //reseting the count on the encoder
 //       encL.start(); //start counting pulses
        GingerDrive.setSafetyEnabled(true); //required for the RobotDrive library
                double count, angle; //encoder count, unused
        while(isOperatorControl() &&  isEnabled()) //while teleop is enabled
        {
            one = joyL.getRawButton(1);  //getting boolean values for each
            two = joyL.getRawButton(2);  //button, the name signifying which
            three = joyL.getRawButton(3);//button on joystick
            four = joyL.getRawButton(4);
            five = joyL.getRawButton(5);
            six = joyL.getRawButton(6);
            seven = joyL.getRawButton(7);
            eight = joyL.getRawButton(8);
            nine = joyL.getRawButton(9);
            ten = joyL.getRawButton(10);
            eleven = joyL.getRawButton(11);
            if(eight && nine && three)
        {
            isManlyMode = true;
            manlyMode();
        }
        
            shooterlogic.run();
           
            
            
            
   //         count = encL.get(); //get number of pulses on encoder L
    //        angle = compass.getAngle(); //get angleof compass
            GingerDrive.arcadeDrive((joyR.getY())*-1, joyR.getX());  //regular moving code, arcade GingerDrive
            SmartDashboard.putNumber("Time left: ", (130 -(teleStartTime - Timer.getFPGATimestamp())));
            Timer.delay(0.01); //delay for robotGingerDrive library
  //          SmartDashboard.putNumber("count", count);
  //          SmartDashboard.putNumber("angle", angle);
            //ITable table = compass.getTable();
            //SmartDashboard.putData("table", Itable);
        }
    }
    public void autonomous()
    {
        autoTwo autotwo = new autoTwo(GingerDrive, shooterlogic);
        autoThree autothree = new autoThree(GingerDrive, shooterlogic);
        AutoTurn autoTurn = new AutoTurn(GingerDrive, shooterlogic);
        basicAuto basicauto = new basicAuto(GingerDrive);
        autonomousCommand = (String) autoChooser.getSelected();
        if (autonomousCommand.equals("No Autonomous"))
        {
                basicauto.basicAutono();
                System.out.println("default ran! :D:D");

        }
        else if (autonomousCommand.equals("Single Ball"))
        {
            autotwo.run();
        }
        else if (autonomousCommand.equals("Two Ball"))
        {
            autothree.run();
        }
        else if (autonomousCommand.equals("Turn"))
        {
            autoTurn.run();
        }
        System.out.println("hello auto works");
        
        
        
    }
    
    public void testPeriodic() 
    {
    
    }
}
