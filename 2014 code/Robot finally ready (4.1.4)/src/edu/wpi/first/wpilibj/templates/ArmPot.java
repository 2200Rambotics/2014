/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Joystick;


public class ArmPot
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
    public int holdCount;
    public int bCount;
    public int fCount;
    public int shootCount;
    public int passCount;
    private boolean rollFlag;
    private boolean fFlag;
    private boolean bFlag;
    private ShooterLogicPot shooterlogic;
    private double wantHoldF;
    private double wantShootF;
    private double wantDownF;
    private double wantHoldB;
    private double wantShootB;
    private double wantDownB;
    private double offset;
    public boolean FarmIsHold;
    public boolean FarmIsShoot;
    public boolean FarmIsDown;
    public boolean BarmIsHold;
    public boolean BarmIsShoot;
    public boolean BarmIsDown;
    public double oldPotB;
    public double oldPotF;
    public double deltaB;
    public double deltaF;
    
    
    ArmPot(Talon armFMotor, Talon armFRoller, Talon armBMotor, Talon armBRoller, ShooterLogicPot shooterLogicPot)
    {
        this.armFMotor = armFMotor;
        this.armFRoller = armFRoller;
        this.armBMotor = armBMotor;
        this.armBRoller = armBRoller;
        this.shooterlogic = shooterLogicPot;
//        armFSensor = new AnalogChannel(3);
//        armBSensor = new AnalogChannel(4);
        isHold = isB = isF = isShoot = isPass = FarmIsHold = FarmIsShoot = 
                FarmIsDown = BarmIsHold = BarmIsShoot = BarmIsDown = false;
        isHold = true;
        holdCount = bCount = fCount = shootCount = passCount = 0;
        
        oldPotB = shooterlogic.BVolt;
        oldPotF = shooterlogic.FVolt;
    }
    
    
    public void checkPot()
    {
        //back arm down
                if((wantDownB - offset) > shooterlogic.BVolt)
                {
                    BarmIsHold = BarmIsShoot = BarmIsDown = false;
                }
                else
                {
                    BarmIsDown = true;
                }
        //check if front arm is down
                if ((wantDownF - offset) > shooterlogic.FVolt)
                {
                    FarmIsHold = FarmIsShoot =FarmIsDown  = false;
                   
                }
                else
                {
                    FarmIsDown = true;
                    
                }
        //back arm up
        if((wantHoldB + offset ) < shooterlogic.BVolt)
                {
                    BarmIsHold = BarmIsShoot = BarmIsDown = false;
                    
                }
                else
                {
                    BarmIsHold = true;
                    
                }
                //front arm up
        if((wantHoldF + offset) < shooterlogic.FVolt)
                {
                    FarmIsHold = FarmIsShoot =FarmIsDown = false;
                }
                else if ((wantHoldF - offset)> shooterlogic.FVolt)
                {
                    FarmIsHold = FarmIsShoot =FarmIsDown = false;
                }
                else
                {
                    FarmIsHold = true;
                }
                
                
        
                
                
        if(shooterlogic.FVolt > wantShootF && shooterlogic.BVolt > wantShootB)
                {
                    FarmIsShoot = BarmIsShoot = true;
                }
        SmartDashboard.putBoolean("FarmIsDown", FarmIsDown);
        SmartDashboard.putBoolean("FarmIsHold", FarmIsHold);
        SmartDashboard.putBoolean("FarmIsShoot",FarmIsShoot);
        SmartDashboard.putBoolean("BarmIsDown", BarmIsDown);
        SmartDashboard.putBoolean("BarmIsHold", BarmIsHold);
        SmartDashboard.putBoolean("BarmIsShoot",BarmIsShoot);
    }
    //arms hold ball
    public void armHold()
    {
        isHold = true;
        isF = isB = isShoot = isPass = false;
    }
    //front arm spin and down
    public void armF()
    {
        isB = isShoot = isPass = isHold = false;
        isF = true;
    }
    //back arm spin and down
    public void armB()
    {
        isF = isShoot = isPass = isHold = false;
        isB = true;
    }
    //arms drop, both spin, only call when shoot
    public void armShoot()
    {
        isF = isB = isPass = isHold = false;
        isShoot = true;
    }
    //front arm drops while spinning rollers out to pass
    public void armPass()
    {
        isF = isB = isShoot = isHold = false;
        isPass = true;
    }
    
    public void run() 
    {
        wantHoldF = 2.471;
        wantShootF = 2.9285;
        wantDownF = 3.386;
        wantHoldB = 1.319;
        wantShootB = 1.792;
        wantDownB = 2.265;
        offset = 0.1;
        
        deltaB = shooterlogic.BVolt - oldPotB;
        deltaF = shooterlogic.FVolt - oldPotF;
        
        oldPotB = shooterlogic.BVolt;
        oldPotF = shooterlogic.FVolt;
        
        shooterlogic.checkSwitchs();
        checkPot();
        SmartDashboard.putNumber("armF", armFMotor.get());
        SmartDashboard.putNumber("armB", armBMotor.get());
        SmartDashboard.putBoolean("hold:", isHold);
        SmartDashboard.putBoolean("isF", isF);
        SmartDashboard.putBoolean("isB", isB);
        SmartDashboard.putBoolean("isShoot", isShoot);
        if (isHold) 
        {
            
            //back arm stay hold loop
            if((wantHoldB + offset) < shooterlogic.BVolt)
            {
                armBMotor.set((shooterlogic.BVolt - wantHoldB) * 2 + (deltaB * 9));
                armBRoller.set(-1);
            }
            else 
            {
                armBRoller.set(0);
                armBMotor.set(0);
            }
            
            //front arm stay hold loop
            if((wantHoldF + offset) < shooterlogic.FVolt)
            {
                armFRoller.set(-1);
                armFMotor.set((shooterlogic.FVolt - wantHoldF) * 2 + (deltaF * 9));
                SmartDashboard.putNumber("Delta F", deltaF * 9);
            }
            else
            {
                armFRoller.set(0);
                armFMotor.set(0);
            }
            
            
        } 
        else 
        {
            if (isF) 
            {
                armFRoller.set(-1);
                //front arm down
                if((wantDownF + offset) < shooterlogic.FVolt)
                {
                    armFMotor.set((shooterlogic.FVolt - wantDownF) * 2 + (deltaF * 9));
                }
                else if ((wantDownF - offset)> shooterlogic.FVolt)
                {
                    armFMotor.set((shooterlogic.FVolt - wantDownF) * 0.8 + (deltaF * 2));
                }
                else
                {
                    armFMotor.set(0);
                }
                
                //back arm up
                if((wantHoldB + offset)< shooterlogic.BVolt)
                {
                    armBMotor.set((shooterlogic.BVolt - wantHoldB) * 2 + (deltaB * 9));
                }
                else
                {
                    armBMotor.set(0);
                }
            }
            if (isB) 
            {
                armBRoller.set(-1);
                //front arm up
                if((wantHoldF + offset) < shooterlogic.FVolt)
                {
                    armFMotor.set((shooterlogic.FVolt - wantHoldF) * 2 + (deltaF * 9));
                }
                else if ((wantHoldF - offset)> shooterlogic.FVolt)
                {
                    armFMotor.set((shooterlogic.FVolt - wantHoldF) * 2 + (deltaF * 9));
                }
                else
                {
                    armFMotor.set(0);
                }
                
                //back arm down
                if((wantDownB + offset)< shooterlogic.BVolt)
                {
                    armBMotor.set((shooterlogic.BVolt - wantDownB) * 2 + (deltaB * 9));
                }
                else if((wantDownB - offset) > shooterlogic.BVolt)
                {
                    armBMotor.set((shooterlogic.BVolt - wantDownB) * 0.8 + (deltaB * 2));
                }
                else
                {
                    armBMotor.set(0);
                }
            }
            if (isShoot) 
            {
                
                //front arm down
                if((wantDownF + offset) < shooterlogic.FVolt)
                {
                    armFMotor.set((shooterlogic.FVolt - wantDownF) * 2 + (deltaF * 9));
                }
                else if ((wantDownF - offset)> shooterlogic.FVolt)
                {
                    armFMotor.set((shooterlogic.FVolt - wantDownF) * 0.8 + (deltaF * 2));
                }
                else
                {
                    armFMotor.set(0);
                }
                
                //back arm down
                if((wantDownB + offset)< shooterlogic.BVolt)
                {
                    armBMotor.set((shooterlogic.BVolt - wantDownB) * 2 + (deltaB * 9));
                }
                else if((wantDownB - offset) > shooterlogic.BVolt)
                {
                    armBMotor.set((shooterlogic.BVolt - wantDownB) * 0.8 + (deltaB * 2));
                }
                else
                {
                    armBMotor.set(0);
                }
                
            }
            if (isPass) 
            {
                armFRoller.set(1);
                armBRoller.set(-1);
                //front arm down
                if((wantDownF + offset) < shooterlogic.FVolt)
                {
                    armFMotor.set((shooterlogic.FVolt - wantDownF) * 2 + (deltaF * 9));
                }
                else if ((wantDownF - offset)> shooterlogic.FVolt)
                {
                    armFMotor.set((shooterlogic.FVolt - wantDownF) * 0.8 + (deltaF * 2));
                }
                else
                {
                    armFMotor.set(0);
                }
                //back arm up
                if((wantHoldB + offset)< shooterlogic.BVolt)
                {
                    armBMotor.set((shooterlogic.BVolt - wantHoldB) * 2 + (deltaB * 9));
                }
                else
                {
                    armBMotor.set(0);
                }
                
            }
        }
    }
    
}
