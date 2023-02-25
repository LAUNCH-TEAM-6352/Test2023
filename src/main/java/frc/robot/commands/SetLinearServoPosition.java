// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LinearServo;

public class SetLinearServoPosition extends CommandBase
{
    private final LinearServo linearServo;

    /** Creates a new SetLinearServoPosition. */
    public SetLinearServoPosition(LinearServo linearServo)
    {
        this.linearServo = linearServo;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(linearServo);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {
        linearServo.setPosition(SmartDashboard.getNumber("LS Position", 0.0));
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        // All work is done in initialize().
        return true;
    }
}
