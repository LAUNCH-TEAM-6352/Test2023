// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.EncodedMotors;

public class SetEncodedMotorsSpeed extends CommandBase
{
    private final EncodedMotors motors;
    private final String speedKey;
    private double speed;

    /** Creates a new SetLinearServoPosition. */
    public SetEncodedMotorsSpeed(EncodedMotors motors, String speedKey)
    {
        this.motors = motors;
        this.speedKey = speedKey;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(motors);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {
        speed = SmartDashboard.getNumber(speedKey, 0);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        motors.setSpeed(speed);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        motors.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return false;
    }
}
