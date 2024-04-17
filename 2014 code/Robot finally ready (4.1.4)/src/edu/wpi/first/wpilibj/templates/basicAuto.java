/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.templates.ShooterLogic;
import edu.wpi.first.wpilibj.templates.Arms;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Rambotics
 */
public class basicAuto
{
    private RobotDrive gingerDrive;
    
    basicAuto(RobotDrive drive)
    {
        this.gingerDrive = drive;
        
    }
    
    
    public void basicAutono()
    {
        gingerDrive.tankDrive(1, 1);
        Timer.delay(0.5);
        gingerDrive.tankDrive(0, 0);
    }
    
    
}
