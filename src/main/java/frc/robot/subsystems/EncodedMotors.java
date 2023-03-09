// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.FaultID;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class EncodedMotors extends SubsystemBase
{
    private final CANSparkMax leftMotor = new CANSparkMax(10, MotorType.kBrushless);
    private final CANSparkMax rightMotor = new CANSparkMax(11, MotorType.kBrushless);

    private double targetPosition;
    private double lastPosition;
    private double positionTolerance;
    private boolean isAtTargetPosition;
    private boolean isPositioningStarted;

    // The following limits based upon:
    //   - The motor controller resports position in revolutions.
    //   - There is a 70:1 gear reduction.
    //   - Total back to front motion of the arm is about 200 degrees
    //   - Arm zero start position is about 70 degrees up from back position
    private final float gearReduction = 70.0f;
    private final float revsPerDegree = gearReduction / 360.0f;
    private final float forwardLimitDegrees = 130.0f;
    private final float reverseLimitDegrees = -70.0f;
    private final float forwardLimitRevs = revsPerDegree * forwardLimitDegrees;
    private final float reverseLimitRevs = revsPerDegree * reverseLimitDegrees;
    /** Creates a new TalonSRX. */
    public EncodedMotors()
    {
        // Set configuration common to both pivot motors:
		for (CANSparkMax motor : new CANSparkMax[] { leftMotor, rightMotor})
		{
            motor.restoreFactoryDefaults();
            motor.clearFaults();
            motor.setIdleMode(IdleMode.kBrake);
            motor.setSoftLimit(SoftLimitDirection.kForward, forwardLimitRevs);
            motor.setSoftLimit(SoftLimitDirection.kReverse, reverseLimitRevs);
            motor.enableSoftLimit(SoftLimitDirection.kForward, true);
            motor.enableSoftLimit(SoftLimitDirection.kReverse, true);
		}

        leftMotor.setInverted(true);
        rightMotor.follow(leftMotor, true);

        // Set PID controller parameters on the pivot leader:
        var pidController = leftMotor.getPIDController();
        pidController.setP(0.15);
        pidController.setI(0);
        pidController.setD(1);
        pidController.setIZone(0);
        pidController.setFF(0);
        
        resetPosition();
    }

    public void resetPosition()
    {
        leftMotor.getEncoder().setPosition(0);
        rightMotor.getEncoder().setPosition(0);
    }

    public double getPosition()
    {
        return leftMotor.getEncoder().getPosition();
    }

    /**
	 * Sets the shooter motor speeds in velocity (RPM).
	 */
	public void setPosition(double position, double tolerance)
	{ 
        if (position > forwardLimitRevs)
        {
            position = forwardLimitRevs;
        }    
        else if (position < reverseLimitRevs)
        {
            position = reverseLimitRevs;
        }
        positionTolerance = tolerance;  
        targetPosition = position;
        lastPosition = getPosition();
        var pidMotorSpeed = SmartDashboard.getNumber("Motor PID %", 0.05);
        leftMotor.getPIDController().setOutputRange(-pidMotorSpeed, pidMotorSpeed);
		leftMotor.getPIDController().setReference(position, ControlType.kPosition);
        isAtTargetPosition = false;
        isPositioningStarted = true;
	}

    public void setSpeed(double speed)
    {
        if ((speed < 0 && isAtRevLimit()) || (speed > 0 && isAtFwdLimit()))
        {
            speed = 0;
        }
        isAtTargetPosition = false;
        isPositioningStarted = false;
        leftMotor.set(speed);
    }

    public void stop()
    {
        leftMotor.set(0);
        isPositioningStarted = false;
    }

    public boolean isAtTargetPosition()
    {
        return isAtTargetPosition;
    }

    public boolean isAtFwdLimit()
    {
        return leftMotor.getFault(FaultID.kSoftLimitFwd) || rightMotor.getFault(FaultID.kSoftLimitFwd);
    }

    public boolean isAtRevLimit()
    {
        return leftMotor.getFault(FaultID.kSoftLimitRev) || rightMotor.getFault(FaultID.kSoftLimitRev);
    }

    @Override
    public void periodic()
    {
        double leftPosition = leftMotor.getEncoder().getPosition();
        double rightPosition = rightMotor.getEncoder().getPosition();

        SmartDashboard.putNumber("Left Motor Pos", leftPosition);
        SmartDashboard.putNumber("Right Motor Pos", rightPosition);
        SmartDashboard.putBoolean("Fwd Limit", !isAtFwdLimit());
        SmartDashboard.putBoolean("Rev Limit", !isAtRevLimit());

        if (isPositioningStarted)
        {
            if (Math.abs(leftPosition - targetPosition) < positionTolerance && Math.abs(leftPosition - lastPosition) < positionTolerance)
            {
                isPositioningStarted = false;
                isAtTargetPosition = true;
            }
            else
            {
                lastPosition = leftPosition;
            }
        }
    }
}
