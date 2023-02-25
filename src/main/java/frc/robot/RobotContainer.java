// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OIConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.SetLinearServoPosition;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.LinearServo;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer
{
	private final XboxController gamepad;
	private final Joystick leftStick;
	private final Joystick rightStick;
    private final String gameData;
    private final LinearServo linearServo;
    
    // The robot's subsystems and commands are defined here...
    private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer()
    {
        gameData = DriverStation.getGameSpecificMessage();
        SmartDashboard.putString("Game Data", gameData);

        // Create OI devices:
        // Explicitly look for OI devices:
        gamepad = DriverStation.isJoystickConnected(OIConstants.gamepadPort)
            ? new XboxController(OIConstants.gamepadPort)
            : null;
        leftStick = DriverStation.isJoystickConnected(OIConstants.leftJoystickPort)
            ? new Joystick(OIConstants.leftJoystickPort)
            : null;
        rightStick = DriverStation.isJoystickConnected(OIConstants.rightJoystickPort)
            ? new Joystick(OIConstants.rightJoystickPort)
            : null;
        linearServo = gameData.contains("-ls-") ? new LinearServo() : null;

        if (gamepad != null)
        {
            SmartDashboard.putNumber("GP Axes", gamepad.getAxisCount());
        }
    
        SmartDashboard.putNumber("LS Position", 0.0);


        // Configure the trigger bindings
        configureBindings();

        configureSmartDashboard();
    }

    /**
     * Use this method to define your trigger->command mappings. Triggers can be
     * created via the
     * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
     * an arbitrary
     * predicate, or via the named factories in {@link
     * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
     * {@link
     * CommandXboxController
     * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
     * PS4} controllers or
     * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
     * joysticks}.
     */
    private void configureBindings()
    {
        // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
        new Trigger(m_exampleSubsystem::exampleCondition)
                        .onTrue(new ExampleCommand(m_exampleSubsystem));

        // Schedule `exampleMethodCommand` when the Xbox controller's B button is
        // pressed,
        // cancelling on release.
       // m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
    }

    private void configureSmartDashboard()
    {
        SmartDashboard.putBoolean("Gamepad Detected", gamepad != null);
        SmartDashboard.putBoolean("Left Joystick Detected", leftStick != null);
        SmartDashboard.putBoolean("Right Joystick Detected", rightStick != null);
        
        if (Robot.adis16470Imu != null)
        {
            SmartDashboard.putData("resetAngle", new InstantCommand(() -> Robot.adis16470Imu.reset()));
        }

        SmartDashboard.putString("koehringTest", "MR. K");

        if (linearServo != null)
        {
            SmartDashboard.putData("Move LS", new SetLinearServoPosition(linearServo));
        }
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand()
    {
        // An example command will be run in autonomous
        return Autos.exampleAuto(m_exampleSubsystem);
    }
}
