/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.templates.Arms;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;


/**
 *
 * @author Rambotics
 */
public class ShooterLogic 
{
    public boolean isOverTravel;
    public boolean isShootArmDown;
    public boolean isWinchCam;
    public boolean isBallInShooter;
    public boolean isDisarm;
    private int joystickButton;
    private Joystick joystick;
    public boolean isOverride;
    public boolean isShootMode;
    public boolean isWinchMode;
    private boolean isWinchLockMoving;
    public boolean isDoneShooting;
    private Relay winchLock;
    public Talon winchArm;
    private int winchCount;
    private boolean winchWaitForTrue;
    public double armFWanted;
    public double armBWanted;
    public double armFActual;
    public double armBActual;
    public double startTime;
//    DigitalInput infraIn = new DigitalInput(10);
//    DigitalOutput infraOut = new DigitalOutput(11);
//    DigitalInput shootArmOverTravel = new DigitalInput(7);
    DigitalInput shootArmDown = new DigitalInput(8);
    DigitalInput winchCamSwitch = new DigitalInput(10);
    AnalogChannel armFPot = new AnalogChannel(3);
    AnalogChannel armBPot = new AnalogChannel(4);
    DigitalInput proxSensor = new DigitalInput(6);
    AnalogChannel proxSensorAna = new AnalogChannel(1);
    ///Gyro gyro = new Gyro(2);
    Arms arms;
    public boolean joy3;
    public boolean joy2;
    public boolean joy5;
    public boolean joy6;
    public double FVolt;
    public double BVolt;
    public boolean isProx;
    public double isProxAna;
    public boolean isManlyMode;
    public boolean isOveride;
    
    ShooterLogic(int joystickNumber, int joystickbuttonNumber, 
            Relay winchLock, Talon winchArm, Talon armFMotor,
            Talon armFRoller, Talon armBMotor, Talon armBRoller)
    {
        this.winchArm = winchArm;
        this.winchLock = winchLock;
        winchCount = 0;
        
        
        joystick = new Joystick(joystickNumber);
        joystickButton = joystickbuttonNumber;
        isOverTravel = winchWaitForTrue = isBallInShooter = isShootArmDown = isWinchCam = isWinchLockMoving =
                isDisarm = isOverride = isShootMode = isWinchMode = isOveride = joy3 = joy2 = joy5 = false;
        isBallInShooter = isDoneShooting = true;
        arms = new Arms(armFMotor, armFRoller, armBMotor, armBRoller);
        this.checkSwitchs();
        
    }
    ShooterLogic()
    {
        
    }
       
    public void checkSwitchs()
    {
        FVolt = armFPot.getVoltage();
        BVolt = armBPot.getVoltage();
        SmartDashboard.putNumber("FVolt: ", FVolt);
        SmartDashboard.putNumber("BVolt: ", BVolt);
        isProx = proxSensor.get();
        isProxAna = proxSensorAna.getVoltage();
//        System.out.println(isProx);
//        System.out.println(isProxAna);
        SmartDashboard.putNumber("Prosmimity Sensor Analog: ", isProxAna);
        SmartDashboard.putBoolean("Proximity Sensor", isProx);
        //SmartDashboard.putNumber("Gyro", gyro.getAngle());
        
        //Arm Over Travel Sensor     
//        if(shootArmOverTravel.get() == false)
//        {
//            isOverTravel = true;
//        }
//        if(isOverTravel == true)
//        {
//            SmartDashboard.putString("", "ARM NOT OKAY!");
//        }
        
        //Arm down switch, dig 8
        if(shootArmDown.get() == true)
        {
            isShootArmDown = true;
            SmartDashboard.putString("Shooter Arm Status: ", "Down/Charged");
        }
        else
        {
            SmartDashboard.putString("Shooter Arm Status: ", "Up/Launched");
            isShootArmDown = false;
        }
        
        //winch cam switch
        if(winchCamSwitch.get() == true)
        {
            isWinchCam = true;
            SmartDashboard.putString("Winch Cam Status: ", "Locked");
        }
        else
        {
            isWinchCam = false;
            SmartDashboard.putString("Winch Cam Status: ", "Unlocked");
        }
        
        //TODO: infrared
        
    }
    public void run()
    {
        
        checkSwitchs();
        arms.run();
        if (joystick.getRawButton(2) && !joy2)
        {
            arms.armF();
            joy2 = true;
        } 
        else if (joystick.getRawButton(3) && !joy3)
        {
            arms.armB();
            joy3 = true;
        }
        else if (joystick.getRawButton(5) && !joy5)
        {
            arms.armPass();
            joy5 = true;
        }
        //steady mode
        else if (joystick.getRawButton(6))
        {
            arms.armFMotor.set(0.5);
            arms.armBMotor.set(0.5);
            isOveride = true;
            
        }
//        open mode
        else if (joystick.getRawButton(7))
        {
            arms.armFMotor.set(-0.45);
            arms.armBMotor.set(-0.45);
            isOveride = true;
            
        }
        else if (!joystick.getRawButton(6) && !joystick.getRawButton(7) && isOveride)
        {
            arms.armFMotor.set(0);
            arms.armBMotor.set(0);
            isOveride = false;
        }
     
        else if ((joy2 || joy3 || joy5) && !joystick.getRawButton(3) && !joystick.getRawButton(2) &&
                !joystick.getRawButton(5))
        {
            arms.armHold();
            joy3 = joy2 = joy5 = false;
        }
        
        boolean buttonState = joystick.getRawButton(joystickButton);
        isOverride = joystick.getRawButton(9);
        isWinchMode = joystick.getRawButton(4);
        SmartDashboard.putBoolean("isWinchMode", isWinchMode);
        if (buttonState && isWinchCam && isBallInShooter &&
                !isShootMode && !isWinchLockMoving && !isWinchMode && !isDisarm)
        {
            isShootMode = true;
            arms.armShoot();
        }
        else if(isOverride && buttonState)
        {
            isShootMode = true;
        }
        if(isShootMode)
        {
            if(!arms.isShoot)
            {
                winchLock.set(Relay.Value.kForward);
                isWinchLockMoving = true;
                isShootMode = false;
                startTime = Timer.getFPGATimestamp();
            }

        }
        if (isWinchLockMoving)
            {
                if(Timer.getFPGATimestamp() - startTime > 0.5)
                {
                    winchWaitForTrue = true;
                }
                if(isWinchCam && winchWaitForTrue)
                {
                    winchLock.set(Relay.Value.kOff);
                    isWinchLockMoving = false;
                    winchWaitForTrue = false;
                    isDoneShooting = true;
                    arms.armHold();
                    
                }
            }
                
                    
        if(isWinchMode)
        {
            if(isShootArmDown)
            {
                winchArm.set(0);
                isWinchMode = false;
            }
            else 
            {
                SmartDashboard.putString("winching", "winching");
                winchArm.set(-1);
            }
            
        } else 
        {
            SmartDashboard.putString("winching", "not winch");
            winchArm.set(0);
        }
        
        
    }
    
    
    
}
