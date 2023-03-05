// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.hal.PWMConfigDataResult;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LinearServo extends SubsystemBase
{
    private final Servo linearServo = new Servo(0);

    private final PWMConfigDataResult bounds;
    
    /** Creates a new LinearServo. */
    public LinearServo()
    {
        bounds = linearServo.getRawBounds();
        SmartDashboard.putNumber("LS Min", bounds.min);
        SmartDashboard.putNumber("LS DB Min", bounds.deadbandMin);
        SmartDashboard.putNumber("LS Center", bounds.center);
        SmartDashboard.putNumber("LS DB Max", bounds.deadbandMax);
        SmartDashboard.putNumber("LS Max", bounds.max);
    }

    public  void setPosition(double position)
    {
        var raw = (int) Math.round((bounds.max - bounds.min) * position + bounds.min);
        //SmartDashboard.putNumber("LS Raw Out", raw);
        //linearServo.setRaw(raw);

        //linearServo.setPosition(position);
        linearServo.setAngle(position);
    }

    @Override
    public void periodic()
    {
        SmartDashboard.putNumber("LS Raw Pos", linearServo.getRaw());
        SmartDashboard.putNumber("LS Cur Pos", linearServo.getPosition());
        SmartDashboard.putNumber("LS Cur Ang", linearServo.getAngle());
        // This method will be called once per scheduler run
    }
}
