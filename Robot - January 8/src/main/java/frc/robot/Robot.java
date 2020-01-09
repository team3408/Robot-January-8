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
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.AnalogInput;

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
  TalonSRX FL = new TalonSRX(0);
  TalonSRX BL = new TalonSRX(1);
  TalonSRX FR = new TalonSRX(2);
  TalonSRX BR = new TalonSRX(3);
  //CANSparkMax FL = new CANSparkMax(0, MotorType.kBrushless);  
  //CANSparkMax BL = new CANSparkMax(1, MotorType.kBrushless);
  //CANSparkMax FR = new CANSparkMax(2, MotorType.kBrushless);
  //CANSparkMax BR = new CANSparkMax(3, MotorType.kBrushless);
  //CANEncoder encoder = FL.getEncoder(); 
  I2C.Port i2cPort = I2C.Port.kOnboard;
  ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);
  ColorMatch colorMatcher = new ColorMatch(); 
  final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
  final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
  final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
  final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);
  final double kHoldDistance = 12.0; // In inches
  final double kValueToInches = 0.125;
  final double kP = 0.05;
  final int kUltrasonicPort = 0;
  AnalogInput ultrasonic = new AnalogInput(kUltrasonicPort);

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    colorMatcher.addColorMatch(kBlueTarget);
    colorMatcher.addColorMatch(kGreenTarget);
    colorMatcher.addColorMatch(kRedTarget);
    colorMatcher.addColorMatch(kYellowTarget); 
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

    //encoderPosition = encoder.getPosition(); // returns position based on revolution
    //encoderVelocity = encoder.getVelocity(); // returns RPM
    //SmartDashboard.putNumber("Encoder Position", encoderPosition);
    //SmartDashboard.putNumber("Encoder Velocity", encoderVelocity);

    Color detectedColor = colorSensor.getColor();
    String colorString;
    ColorMatchResult match = colorMatcher.matchClosestColor(detectedColor);

    if (match.color == kBlueTarget) {
      colorString = "Blue";
    } else if (match.color == kRedTarget) {
      colorString = "Red";
    } else if (match.color == kGreenTarget) {
      colorString = "Green";
    } else if (match.color == kYellowTarget) {
      colorString = "Yellow";
    } else {
      colorString = "Unknown";
    }

    SmartDashboard.putNumber("Red", detectedColor.red);
    SmartDashboard.putNumber("Green", detectedColor.green);
    SmartDashboard.putNumber("Blue", detectedColor.blue);
    SmartDashboard.putNumber("Confidence", match.confidence);
    SmartDashboard.putString("Detected Color", colorString);

    double currentDistance = ultrasonic.getValue() * kValueToInches;
    double currentSpeed = (kHoldDistance - currentDistance) * kP;

    if (joystick.getRawButton(1)) {
      FL.set(ControlMode.PercentOutput, currentSpeed / 4);
      BL.set(ControlMode.PercentOutput, currentSpeed / 4);
      FR.set(ControlMode.PercentOutput, currentSpeed / 4);
      BR.set(ControlMode.PercentOutput, currentSpeed / 4);
      SmartDashboard.putNumber("Current Distance", currentDistance);
      SmartDashboard.putNumber("Current Speed", currentSpeed);
    } else {
      FL.set(ControlMode.PercentOutput, joystick.getRawAxis(1));
      BL.set(ControlMode.PercentOutput, joystick.getRawAxis(1));
      FR.set(ControlMode.PercentOutput, joystick.getRawAxis(5));
      BR.set(ControlMode.PercentOutput, joystick.getRawAxis(5));
    }

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
