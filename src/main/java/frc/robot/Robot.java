// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.ADIS16470_IMU;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot
{
    private Command m_autonomousCommand;

    private RobotContainer m_robotContainer;

    private BuiltInAccelerometer builtInAccelerometer;

    public static ADIS16470_IMU adis16470Imu;

    public DigitalOutput digitalOutput;
    
    public XboxController gamepad;

    public REVDigitBoard revDigitBoard;

    /**
     * This function is run when the robot is first started up and should be used
     * for any
     * initialization code.
     */
    @Override
    public void robotInit()
    {
        boolean koehringTesting = false;

        // Instantiate our RobotContainer. This will perform all our button bindings,
        // and put our
        // autonomous chooser on the dashboard.
        m_robotContainer = new RobotContainer();
        builtInAccelerometer = new BuiltInAccelerometer();
        adis16470Imu = koehringTesting ? null : new ADIS16470_IMU(ADIS16470_IMU.IMUAxis.kX, SPI.Port.kOnboardCS0, ADIS16470_IMU.CalibrationTime._4s);
        digitalOutput = koehringTesting ? null : new DigitalOutput(0);
        gamepad = koehringTesting ? null : new XboxController(0);
        revDigitBoard = koehringTesting ? new REVDigitBoard() : null;

        if (revDigitBoard != null)
        {
            revDigitBoard.clear();
        }
    }

    /**
     * This function is called every 20 ms, no matter the mode. Use this for items
     * like diagnostics
     * that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>
     * This runs after the mode specific periodic functions, but before LiveWindow
     * and
     * SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic()
    {
        // Runs the Scheduler. This is responsible for polling buttons, adding
        // newly-scheduled
        // commands, running already-scheduled commands, removing finished or
        // interrupted commands,
        // and running subsystem periodic() methods. This must be called from the
        // robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();
    }

    /** This function is called once each time the robot enters Disabled mode. */
    @Override
    public void disabledInit()
    {
    }

    @Override
    public void disabledPeriodic()
    {
        if (revDigitBoard != null)
        {
            revDigitBoard.display(SmartDashboard.getString("koehringTest", "NULL"));
        }
    }

    /**
     * This autonomous runs the autonomous command selected by your
     * {@link RobotContainer} class.
     */
    @Override
    public void autonomousInit()
    {
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();

        // schedule the autonomous command (example)
        if (m_autonomousCommand != null)
        {
            m_autonomousCommand.schedule();
        }
    }

    /** This function is called periodically during autonomous. */
    @Override
    public void autonomousPeriodic()
    {
        if (builtInAccelerometer != null)
        {
            SmartDashboard.putNumber("onBoardX", Util.round(builtInAccelerometer.getX(), 2));
            SmartDashboard.putNumber("onBoardY", Util.round(builtInAccelerometer.getY(), 2));
            SmartDashboard.putNumber("onBoardZ", Util.round(builtInAccelerometer.getZ(), 2));
        }

        if (adis16470Imu != null)
        {
            SmartDashboard.putNumber("accelX", Util.round(adis16470Imu.getAccelX(), 2));
            SmartDashboard.putNumber("accelY", Util.round(adis16470Imu.getAccelY(), 2));
            SmartDashboard.putNumber("accelZ", Util.round(adis16470Imu.getAccelZ(), 2));
            SmartDashboard.putNumber("angle", Util.round(adis16470Imu.getAngle(), 2));
            SmartDashboard.putData("IMU", adis16470Imu);
        }

        if (revDigitBoard != null)
        {
            revDigitBoard.display(SmartDashboard.getString("koehringTest", "NULL"));
        }
    }

    @Override
    public void teleopInit()
    {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (m_autonomousCommand != null)
        {
            m_autonomousCommand.cancel();
        }
    }

    /** This function is called periodically during operator control. */
    @Override
    public void teleopPeriodic()
    {
        if (digitalOutput != null && gamepad != null)
        {
            digitalOutput.set(gamepad.getAButton());
        }
    }

    @Override
    public void testInit()
    {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    /** This function is called periodically during test mode. */
    @Override
    public void testPeriodic()
    {
    }

    /** This function is called once when the robot is first started up. */
    @Override
    public void simulationInit()
    {
    }

    /** This function is called periodically whilst in simulation. */
    @Override
    public void simulationPeriodic()
    {
    }
}
