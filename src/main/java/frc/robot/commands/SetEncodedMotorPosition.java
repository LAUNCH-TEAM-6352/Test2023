// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.EncodedMotor;

public class SetEncodedMotorPosition extends CommandBase
{
    private final EncodedMotor motor;
    private final String positionKey;
    private final String toleranceKey;

    /** Creates a new SetLinearServoPosition. */
    public SetEncodedMotorPosition(EncodedMotor motor, String positionKey, String toleranceKey)
    {
        this.motor = motor;
        this.positionKey = positionKey;
        this.toleranceKey = toleranceKey;

        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(motor);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize()
    {
        motor.setPosition(
            SmartDashboard.getNumber(positionKey, 0),
            SmartDashboard.getNumber(toleranceKey, 10));
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute()
    {
        // All work is done in initialize().
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted)
    {
        motor.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished()
    {
        return motor.isAtTargetPosition();
    }
}
