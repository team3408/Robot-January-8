/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;


public class Robot extends TimedRobot {
  Joystick joystick = new Joystick(0);
  TalonSRX spinner = new TalonSRX(5);
  CANSparkMax FL = new CANSparkMax(8, MotorType.kBrushless);
  CANSparkMax BL = new CANSparkMax(9, MotorType.kBrushless);
  CANSparkMax FR = new CANSparkMax(10, MotorType.kBrushless);
  CANSparkMax BR = new CANSparkMax(11, MotorType.kBrushless);
  CANEncoder encoder = FL.getEncoder();
  I2C.Port i2cPort = I2C.Port.kOnboard;
  ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);
  ColorMatch colorMatcher = new ColorMatch(); 
  String wantedColor;
  String chosenColor;
  final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
  final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
  final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
  final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

  @Override
  public void robotInit() {
    colorMatcher.addColorMatch(kBlueTarget);
    colorMatcher.addColorMatch(kGreenTarget);
    colorMatcher.addColorMatch(kRedTarget);
    colorMatcher.addColorMatch(kYellowTarget); 
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    if (joystick.getRawButton(3)) {
      FL.restoreFactoryDefaults();
    }
    SmartDashboard.putNumber("Encoder Position", encoder.getPosition());
    SmartDashboard.putNumber("Encoder Velocity", encoder.getVelocity());

    if (joystick.getRawButton(5)) {
      encoder.setPosition(5);
    }

    FL.set(joystick.getRawAxis(1));

    //spinner.set(ControlMode.PercentOutput, joystick.getRawAxis(1) / 4);
    Color detectedColor = colorSensor.getColor();
    String colorString;
    ColorMatchResult match = colorMatcher.matchClosestColor(detectedColor);

    if (match.color == kBlueTarget) {
      colorString = "B";
    } else if (match.color == kRedTarget) {
      colorString = "R";
    } else if (match.color == kGreenTarget) {
      colorString = "G";
    } else if (match.color == kYellowTarget) {
      colorString = "Y";
    } else {
      colorString = "U";
    }


    SmartDashboard.putNumber("Red", detectedColor.red);
    SmartDashboard.putNumber("Green", detectedColor.green);
    SmartDashboard.putNumber("Blue", detectedColor.blue);
    SmartDashboard.putNumber("Confidence", match.confidence);
    SmartDashboard.putString("Detected Color", colorString);

    // TEST CASE!!!! - chosen color = red
    chosenColor = "G";

    // Color Wheel II
    if (joystick.getRawButton(4)) {
      //Code for Color Wheel Part II (Color Picker)
      if (chosenColor.equals("B")) {
        wantedColor = "R";
      } else if (chosenColor.equals("R")) {
        wantedColor = "B";
      } else if (chosenColor.equals("G")) {
        wantedColor = "Y";
      } else if (chosenColor.equals("Y")) {
        wantedColor = "G";
      }

      spinner.set(ControlMode.PercentOutput, 0.8);
      if (colorString.equals(wantedColor)) {
        spinner.set(ControlMode.PercentOutput, 0);
      }

    }

    if (joystick.getRawButton(3)) {
      spinner.set(ControlMode.PercentOutput, 0);
    }

    if (joystick.getRawButton(2)) {

      for (int i = 0; i < 16; i++) {

        //spinner.set(ControlMode.PercentOutput, 0);
        DriverStation.reportWarning("Start of Loop", true);
        DriverStation.reportWarning(match.color.toString(), true);

        Color thisIsAColor = colorSensor.getColor();
        ColorMatchResult thisIsAColorMatch = colorMatcher.matchClosestColor(thisIsAColor);

        // breaker
        if (joystick.getRawButton(1)) {
          spinner.set(ControlMode.PercentOutput, 0);
          break;
        }

        if (thisIsAColorMatch.color == kRedTarget) {

          DriverStation.reportWarning(match.color.toString(), true);
          Color loopColor1;
          ColorMatchResult loopColorMatch1 = thisIsAColorMatch;

          while (loopColorMatch1.color != kBlueTarget) {
            loopColor1 = colorSensor.getColor();
            loopColorMatch1 = colorMatcher.matchClosestColor(loopColor1);
            spinner.set(ControlMode.PercentOutput, 0.5);
            if (joystick.getRawButton(1)) {
              spinner.set(ControlMode.PercentOutput, 0);
              break;
            }
          }
        } else if (thisIsAColorMatch.color == kBlueTarget) {

          DriverStation.reportWarning(match.color.toString(), true);
          Color loopColor2;
          ColorMatchResult loopColorMatch2 = thisIsAColorMatch;

          while (loopColorMatch2.color != kRedTarget) {
            loopColor2 = colorSensor.getColor();
            loopColorMatch2 = colorMatcher.matchClosestColor(loopColor2);
            spinner.set(ControlMode.PercentOutput, 0.5);
            if (joystick.getRawButton(1)) {
              spinner.set(ControlMode.PercentOutput, 0);
              break;
            }
          }
        } else {
          DriverStation.reportWarning(match.color.toString(), true);
          Color loopColor3;
          ColorMatchResult loopColorMatch3 = thisIsAColorMatch;

          while (loopColorMatch3.color != kRedTarget) {
            loopColor3 = colorSensor.getColor();
            loopColorMatch3 = colorMatcher.matchClosestColor(loopColor3);
            spinner.set(ControlMode.PercentOutput, 0.5);
            if (joystick.getRawButton(1)) {
              spinner.set(ControlMode.PercentOutput, 0);
              break;
            }
          }
        }
      }
    }
  }

  @Override
  public void testPeriodic() {
  }
}
