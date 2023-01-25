// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * General utility methods.
 * 
 * 
 * 
 * @author Koehring
 * 
 */
public class Util
{ 
    /** Rounds the provided value to the specified number of decimal places. */
    public static double round(double value, int decimalPlaces)
    {
        double factor = Math.pow(10.0, decimalPlaces);
        return Math.round(value * factor) / factor;
    }
}