/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
/**
 *
 * @author Rambotics
 */
public class autoTwo {
    RobotDrive gingerDrive;
    ShooterLogicPot shooterlogic;
    
    autoTwo(RobotDrive robodrive, ShooterLogicPot shooterlogic) {
        this.gingerDrive = robodrive;
        this.shooterlogic = shooterlogic;
    }
    
    public void run() {
        gingerDrive.setSafetyEnabled(false);
        shooterlogic.isShootMode = true;
        shooterlogic.armpot.armShoot();
//        while((!shooterlogic.armpot.BarmIsDown || !shooterlogic.armpot.BarmIsShoot) 
//                && (!shooterlogic.armpot.FarmIsDown || !shooterlogic.armpot.FarmIsShoot))
//        {
//            shooterlogic.armpot.run();
//        }
//        shooterlogic.isDoneShooting = false;
//        System.out.println("after...");
        shooterlogic.camWait = true;
        shooterlogic.isDoneShooting = false;
        while ( shooterlogic.isDoneShooting == false)
        {
            shooterlogic.run();
            System.out.println("ran shooter run");
        }
        shooterlogic.isWinchMode = true;
        while (!shooterlogic.shootArmDown.get())
        {
            shooterlogic.winchArm.set(-1);
        }
        shooterlogic.winchArm.set(0);
        shooterlogic.armpot.armFRoller.set(-1);
        Timer.delay(0.75);
        shooterlogic.armpot.armHold();
        while (!shooterlogic.armpot.FarmIsHold && !shooterlogic.armpot.BarmIsHold)
        {
            System.out.println("arm up/ hold");
            shooterlogic.run();
            shooterlogic.armpot.armFRoller.set(0);
        }
        shooterlogic.armpot.armFRoller.set(0);
        Timer.delay(0.75);
        gingerDrive.tankDrive(-0.5, 0.5);
        Timer.delay(0.75);
        System.out.println("Drive code");
        gingerDrive.tankDrive(1.0, 1.0);
        Timer.delay(1);
        gingerDrive.drive(0,0);
    }
    
}
