/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Timer;



public class Arms 
{
    public AnalogChannel armFSensor;
    public AnalogChannel armBSensor;
    public Talon armFMotor;
    public Talon armFRoller;
    public Talon armBMotor;
    public Talon armBRoller;
    public boolean isHold;
    public boolean isB;
    public boolean isF;
    public boolean isShoot;
    public boolean isPass;
    public boolean isSteady;
    public boolean isOpen;
    public int holdCount;
    public int bCount;
    public int fCount;
    public int shootCount;
    public int passCount;
    private boolean rollFlag;
    private boolean fFlag;
    private boolean bFlag;
    private boolean timerReady;
    private boolean timeResetFlag;
    public boolean steadyFlag;
    public boolean openFlag;
    
    Arms(Talon armFMotor, Talon armFRoller, Talon armBMotor, Talon armBRoller)
    {
        this.armFMotor = armFMotor;
        this.armFRoller = armFRoller;
        this.armBMotor = armBMotor;
        this.armBRoller = armBRoller;
//        armFSensor = new AnalogChannel(3);
//        armBSensor = new AnalogChannel(4);
        isHold = rollFlag = isB = isF = isShoot = isSteady = isOpen = fFlag = bFlag = steadyFlag = openFlag = isPass = false;
        holdCount = bCount = fCount = shootCount = passCount = 0;
        timerReady = timeResetFlag = true;
        
    }
    Arms()
    {
        
    }
    //arms hold ball
    public void armHold()
    {
        isHold = true;
    }
    //front arm spin and down
    public void armF()
    {
        isF = true;
    }
    //back arm spin and down
    public void armB()
    {
        isB = true;
    }
    //arms drop, both spin, only call when shoot
    public void armShoot()
    {
        isShoot = true;
    }
    //front arm drops while spinning rollers out to pass
    public void armPass()
    {
        isPass = true;
    }
    //holds arm up while button is pressed
//    public void armSteady()
//    {
//        isSteady = true;
//        steadyFlag = true;
//    }
//    public void armOpen()
//    {
//        isOpen = true;
//        openFlag = true;
//    }
    
    private double time;
    public double atomicTime()
    {
        if (timerReady) {
            time = Timer.getFPGATimestamp();
            timerReady = false;
        }
        
        return time;
    }
    
    public double deltaTime()
    {
        return Timer.getFPGATimestamp() - atomicTime();
    }
    
    public void run() 
    {
        SmartDashboard.putNumber("armF", armFMotor.get());
        SmartDashboard.putNumber("armB", armBMotor.get());
        SmartDashboard.putBoolean("hold:", isHold);
        SmartDashboard.putBoolean("isF", isF);
        SmartDashboard.putBoolean("isB", isB);
        SmartDashboard.putBoolean("fFlag", fFlag);
        SmartDashboard.putBoolean("bFlag", bFlag);
        SmartDashboard.putBoolean("isShoot", isShoot);
        
        if (isHold) {
            if (fFlag) {
                armFMotor.set(0.6);
                armBMotor.set(0);
            } else if (bFlag) {
                armBMotor.set(0.6);
                armFMotor.set(0);
            } else {
                armFMotor.set(0.6);
                armBMotor.set(0.6);
            }
           
            if(rollFlag)
            {
                armFRoller.set(-0.4);
            }
            else if(!rollFlag)
            {
                armBRoller.set(-0.4);
            }
            if (timeResetFlag) {
                timerReady = true;
                timeResetFlag = false;
            }
            holdCount++;
            fCount = 0;
            isF = false;
            bCount = 0;
            isB = false;
            shootCount = 0;
            isShoot = false;
            isPass = false;
            passCount = 0;
            if (deltaTime() >= 0.7) {
                armFRoller.set(0);
                armBRoller.set(0);
                armFMotor.set(0);
                armBMotor.set(0);
                isHold = fFlag = bFlag = false;
                holdCount = 0;
                timerReady = true;
                timeResetFlag = true;
            }
        } else {
            if (isF) {
                armFMotor.set(-0.45);
                armBMotor.set(0);
                armFRoller.set(-1);
                fCount++;
                fFlag = true;
                if (deltaTime() >= 0.85) {
                    armFMotor.set(0);
                    armBMotor.set(0);
                    isF = false;
                    fCount = 0;
                    rollFlag =  true;
                    timerReady = true;
                    
                }
            }
            if (isB) {
                armFMotor.set(0);
                armBMotor.set(-0.45);
                armBRoller.set(-1);
                bCount++;
                bFlag = true;
                if (deltaTime() >= 0.85) {
                    armFMotor.set(0);
                    armBMotor.set(0);
                    isB = false;
                    bCount = 0;
                    rollFlag = false;
                    timerReady = true;
                    
                }
            }
            if (isShoot) {
                armFMotor.set(-0.45);
                armBMotor.set(-0.45);
//                armFRoller.set(1);
//                armBRoller.set(1);
                shootCount++;
                if (deltaTime() >= 0.7) {
                    armFMotor.set(0);
                    armBMotor.set(0);
                    isShoot = false;
                    isHold = false;
                    shootCount = 0;
                    timerReady = true;
                }
            }
            if (isPass) {
                
                armFRoller.set(1);
                armBRoller.set(-1); 
                if (deltaTime() >= 0.3)
                {
                  armFMotor.set(-0.55); 
                }
                passCount++;
                if (deltaTime() >= 1) {
                    armFMotor.set(0);
                    isPass = false;
                    passCount = 0;
                    timerReady = true;
                }
            }
//            if (isSteady) 
//            {
//                if (steadyFlag)
//                {
//                    armFMotor.set(0.3);
//                    armBMotor.set(0.3);
//                }
//                else
//                {
//                    armFMotor.set(0);
//                    armBMotor.set(0);
//                }
//                
//            }
//            if (isOpen) 
//            {
//                if (openFlag)
//                {
//                    armFMotor.set(-0.6);
//                    armBMotor.set(-0.6);
//                }
//                else
//                {
//                    armFMotor.set(0);
//                    armBMotor.set(0);
//                }
//                
//            }
        }
    }
    
}
