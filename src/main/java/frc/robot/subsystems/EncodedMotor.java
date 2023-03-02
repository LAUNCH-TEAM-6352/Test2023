// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class EncodedMotor extends SubsystemBase
{
    private final TalonSRX motor = new TalonSRX(9);

    private double targetPosition;
    private double lastPosition;
    private double positionTolerance;
    private boolean isAtTargetPosition;
    private boolean isPositioningStarted;

    // The following limits based upon:
    //   - Encoder provides 4096 ticks per motor shaft rev
    //   - Gear reduction is 70:1
    //   - Total back to front motion of the arm is about 200 degrees
    //   - Arm zero start position is about 70 degrees up from back position
    private final double forwardLimit = 103538;
    private final double reverseLimit = -55751;

    /** Creates a new TalonSRX. */
    public EncodedMotor()
    {
        motor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);

        motor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        motor.configAllowableClosedloopError(0, 10, 30);
        motor.configClosedLoopPeakOutput(0, 0.07, 30);
        motor.configClosedloopRamp(0, 30);
        motor.configClosedLoopPeriod(0, 1, 30);
        motor.config_kP(0, 0.15, 30);
        motor.config_kI(0, 0.0, 30);
        motor.config_kD(0, 1, 30);
        motor.config_kF(0, 0.0, 30);
        motor.config_IntegralZone(0, 0, 0);
        motor.selectProfileSlot(0, 0);
        motor.setSensorPhase(false);
        motor.setNeutralMode(NeutralMode.Brake);

        motor.configForwardSoftLimitThreshold(forwardLimit, 30);
        motor.configReverseSoftLimitThreshold(reverseLimit, 30);
        motor.configForwardSoftLimitEnable(true);
        motor.configReverseSoftLimitEnable(true);

        resetPosition();
    }

    public void resetPosition()
    {
        motor.setSelectedSensorPosition(0);
    }

    public double getPosition()
    {
        return motor.getSelectedSensorPosition();
    }

    /**
	 * Sets the shooter motor speeds in velocity (RPM).
	 */
	public void setPosition(double position, double tolerance)
	{ 
        if (position > forwardLimit)
        {
            position = forwardLimit;
        }    
        else if (position < reverseLimit)
        {
            position = reverseLimit;
        }
        positionTolerance = tolerance;  
        targetPosition = position;
        lastPosition = getPosition();
		motor.set(ControlMode.Position, position);
        isAtTargetPosition = false;
        isPositioningStarted = true;
	}

    public void setSpeed(double speed)
    {
        isAtTargetPosition = false;
        isPositioningStarted = false;
        motor.set(ControlMode.PercentOutput, speed);
    }

    public void stop()
    {
        motor.set(ControlMode.PercentOutput, 0);
        isPositioningStarted = false;
    }

    public boolean isAtTargetPosition()
    {
        return isAtTargetPosition;
    }

    @Override
    public void periodic()
    {
        double position = getPosition();

        SmartDashboard.putNumber("Motor Pos", position);

        if (isPositioningStarted)
        {
            if (Math.abs(position - targetPosition) < positionTolerance && Math.abs(position - lastPosition) < positionTolerance)
            {
                isPositioningStarted = false;
                isAtTargetPosition = true;
            }
            else
            {
                lastPosition = position;
            }
        }
    }
}
