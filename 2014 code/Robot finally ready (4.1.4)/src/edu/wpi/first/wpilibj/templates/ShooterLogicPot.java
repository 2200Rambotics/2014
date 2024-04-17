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
public class ShooterLogicPot
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
//    public Talon adjuster;
    //private int winchCount;
    private boolean winchWaitForTrue;
    public boolean camWait;
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
    AnalogChannel analogUltra = new AnalogChannel(6);
    DigitalInput sideUltra = new DigitalInput(7);
    DigitalInput frontUltra = new DigitalInput(5);
    
    ///Gyro gyro = new Gyro(2);
    
    ArmPot armpot;
    public boolean joy3;
    public boolean joy2;
    public boolean joy5;
    public boolean joy6;
    public double FVolt;
    public double BVolt;
    public boolean isProx;
    public double analogUltraVal;
    public boolean sideUltraVal;
    public boolean frontUltraVal;
    public boolean isManlyMode;
    public boolean isOveride;
    public double camTimer;
    
    ShooterLogicPot(int joystickNumber, int joystickbuttonNumber, 
            Relay winchLock, Talon winchArm, Talon armFMotor,
            Talon armFRoller, Talon armBMotor, Talon armBRoller)
    {
        this.winchArm = winchArm;
        this.winchLock = winchLock;
//        this.adjuster = adjuster;
        //winchCount = 0;
        
        
        joystick = new Joystick(joystickNumber);
        joystickButton = joystickbuttonNumber;
        isOverTravel = winchWaitForTrue = isBallInShooter = isShootArmDown = isWinchCam = isWinchLockMoving =
                isDisarm = isOverride = isShootMode = isWinchMode = isOveride = joy3 = joy2 = joy5 = camWait = false;
        isBallInShooter = isDoneShooting = true;
        armpot = new ArmPot(armFMotor, armFRoller, armBMotor, armBRoller, this);
        camTimer = 0;
        this.checkSwitchs();
        
    }
    ShooterLogicPot()
    {
        
    }
       
    public void checkSwitchs()
    {
        FVolt = armFPot.getVoltage();
        BVolt = armBPot.getVoltage();
        SmartDashboard.putNumber("FVolt: ", FVolt);
        SmartDashboard.putNumber("BVolt: ", BVolt);
        sideUltraVal = sideUltra.get();
        frontUltraVal = frontUltra.get();
        analogUltraVal = analogUltra.getVoltage();
        SmartDashboard.putNumber("Analog Ultrasonic: ", analogUltraVal);
        SmartDashboard.putBoolean("Side Ultra", sideUltraVal);
        SmartDashboard.putBoolean("Front Ultra", frontUltraVal);
//        System.out.println(isProx);
//        System.out.println(isProxAna);
        //SmartDashboard.putNumber("Gyro", gyro.getAngle());
        
        
        
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
        
        if (joystick.getRawButton(3))
        {
            armpot.armF();
            
        } 
        else if (joystick.getRawButton(2))
        {
            armpot.armB();
        }
        else if (joystick.getRawButton(5))
        {
            armpot.armPass();
        }
        //steady mode
        else if (joystick.getRawButton(6))
        {
            armpot.armHold(); 
        }
//        open mode
        else if (joystick.getRawButton(7))
        {
            armpot.armShoot();
            if (joystick.getRawButton(8))
            {
                armpot.armBRoller.set(1);
                armpot.armFRoller.set(1);
            }
            else
            {
                armpot.armBRoller.set(0);
                armpot.armFRoller.set(0);
            }
        }
//        else if (joystick.getRawButton(11))
//        {
//            adjuster.set(1);
//        }
//        else if (joystick.getRawButton(10))
//        {
//            adjuster.set(-1);
//        }
//        else if(!joystick.getRawButton(11) && !joystick.getRawButton(10))
//        {
//            adjuster.set(0);
//        }
     //if none pressed go hold
        else if (!joystick.getRawButton(3) && !joystick.getRawButton(2) &&
                !joystick.getRawButton(5) && !isShootMode && isDoneShooting)
        {
            armpot.armHold();
        }
        armpot.run();
        SmartDashboard.putBoolean("isSHootMode: ", isShootMode);
        SmartDashboard.putBoolean("isDOneShooting", isDoneShooting);
        boolean buttonState = joystick.getRawButton(joystickButton);
        isOverride = joystick.getRawButton(9);
        isWinchMode = joystick.getRawButton(4);
        SmartDashboard.putBoolean("isWinchMode", isWinchMode);
        if (buttonState && isBallInShooter &&
                !isShootMode && !isWinchLockMoving && !isWinchMode && !isDisarm)
        {
            isShootMode = true;
            armpot.armShoot();
            camWait = true;
        }
        else if(isOverride && buttonState)
        {
            isShootMode = true;
        }
        if (isShootMode) {
            if(camWait) {
                camTimer = Timer.getFPGATimestamp();
                camWait = false;
            } else if (Timer.getFPGATimestamp() - camTimer > 0.2) {
                winchLock.set(Relay.Value.kForward);
 //               if (armpot.BarmIsShoot && armpot.FarmIsShoot) {
                    isWinchLockMoving = true;
                    isShootMode = isDoneShooting = false;
                    startTime = Timer.getFPGATimestamp();
   //             }
            }
        }
        if (isWinchLockMoving)
            {
                if(Timer.getFPGATimestamp() - startTime > 1.0)
                {
                    winchWaitForTrue = true;
                }
                if(isWinchCam && winchWaitForTrue)
                {
                    winchLock.set(Relay.Value.kOff);
                    isWinchLockMoving = false;
                    winchWaitForTrue = false;
                    isDoneShooting = true;
                    armpot.armHold();
                    
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
