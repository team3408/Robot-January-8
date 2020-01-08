/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {

  double encoderPosition;
  double encoderVelocity;
  Joystick joystick = new Joystick(0);
  CANSparkMax FL = new CANSparkMax(0, MotorType.kBrushless);  
  CANSparkMax BL = new CANSparkMax(1, MotorType.kBrushless);
  CANSparkMax FR = new CANSparkMax(2, MotorType.kBrushless);
  CANSparkMax BR = new CANSparkMax(3, MotorType.kBrushless);
  CANEncoder encoder = FL.getEncoder(); 


  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
  }

  /**
   * This function is run once each time the robot enters autonomous mode.
   */
  @Override
  public void autonomousInit() {
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  /**
   * This function is called once each time the robot enters teleoperated mode.
   */
  @Override
  public void teleopInit() {
  }

  /**
   * This function is called periodically during teleoperated mode.
   */
  @Override
  public void teleopPeriodic() {

    FL.set(joystick.getRawAxis(1));
    BL.set(joystick.getRawAxis(1));
    FR.set(joystick.getRawAxis(5));
    BR.set(joystick.getRawAxis(5));
    encoderPosition = encoder.getPosition(); // returns position based on revolution
    encoderVelocity = encoder.getVelocity(); // returns RPM
    SmartDashboard.putNumber("Encoder Position", encoderPosition);
    SmartDashboard.putNumber("Encoder Velocity", encoderVelocity);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
