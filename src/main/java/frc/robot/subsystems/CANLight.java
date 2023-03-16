// Copyright (c) FIRST and other WPILib contributors.rer
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.nio.charset.StandardCharsets;

import edu.wpi.first.hal.CANData;
import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Implements an interface to the mindsensors CANLight device.
 * 
 * Based on code in the following mindsensors files
 *   can_mindsensors.h
 *   can_light.h
 *   mindsensorsDriver.cpp
 *   CANLightDriver.cpp
 *   CANLight.java
 * 
 * CAN message ids are constructed as follows:
 *   0x000t tttt mmmm mmmm aaaa aaaa aadd dddd
 * where:
 *         t tttt is the device type id
 *      mmmm mmmm is the manufacturer id
 *   aa aaaa aaaa is the API id
 *        dd dddd is the device id
 */
public class CANLight extends SubsystemBase
{
    private final int deviceId = 3;
    private final int deviceManufacturer = 7;
    private final int deviceType = 8;

    public static final int colorSetApiId = 0;
    public static final int colorShowApiId = 1;
    public static final int colorBlinkApiId = 2;
    public static final int colorFadeApiId = 3;
    public static final int colorFlashApiId = 4;
    public static final int colorSweepApiId = 5;
    public static final int colorLoadApiId = 6;
    public static final int colorResetApiId = 7;

    // The following API ids came from the MindSensor files
    // can_mindsensors.h and can_light.h.
    public static final int ApiIdOffset = 6;
    public static final int statusDataApiId = 0x00002400 >> ApiIdOffset;
    public static final int firmwareVersionApiId = 0x00003000 >> ApiIdOffset;
    public static final int changeIdApiId = 0x00003400 >> ApiIdOffset;
    public static final int devNameApiId = 0x00003800 >> ApiIdOffset;
    public static final int bootLoaderApiId = 0x00003C00 >> ApiIdOffset;
    public static final int blinkApiId = 0x00004000 >> ApiIdOffset;
    public static final int devSerNoApiId = 0x00004400 >> ApiIdOffset;
    public static final int flipApiId = 0x00004800 >> ApiIdOffset;

    private final CAN canLight;

    private int currentApiId;

    /** Creates a new CANLight. */
    public CANLight()
    {
        canLight = new CAN(deviceId, deviceManufacturer, deviceType);
        writeRTRFrame(firmwareVersionApiId);
        byte[] bytes = "CANLight".getBytes(StandardCharsets.US_ASCII);
        SmartDashboard.putRaw("CANLight", bytes);
        //canLight.writePacket(bytes, devNameApiId);
    }

    public void writeRTRFrame(int apiId)
    {
        if (apiId != statusDataApiId)
        {
            canLight.writeRTRFrame(8, apiId);
        }
        currentApiId = apiId;
        SmartDashboard.putBoolean("CL Valid Data", false);
    }

    public void writePacket()
    {
        var data = new byte[8];
        data[0] = (byte) SmartDashboard.getNumber("CL data[0]", 0);
        data[1] = (byte) SmartDashboard.getNumber("CL data[1]", 0);
        data[2] = (byte) SmartDashboard.getNumber("CL data[2]", 0);
        data[3] = (byte) SmartDashboard.getNumber("CL data[3]", 0);
        data[4] = (byte) SmartDashboard.getNumber("CL data[4]", 0);
        data[5] = (byte) SmartDashboard.getNumber("CL data[5]", 0);
        data[6] = (byte) SmartDashboard.getNumber("CL data[6]", 0);
        data[7] = (byte) SmartDashboard.getNumber("CL data[7]", 0);
        SmartDashboard.putRaw("CL data[]", data);
        canLight.writePacket(data, (int)SmartDashboard.getNumber("CL API", 0));
    }

    @Override
    public void periodic()
    {
        CANData data = new CANData();
        if (canLight.readPacketNew(currentApiId, data))
        {
            SmartDashboard.putNumber("CL Raw Len", data.length);
            SmartDashboard.putRaw("CL Raw", data.data);
        }
        CANData status = new CANData();
        if (canLight.readPacketLatest(statusDataApiId, status))
        {
            SmartDashboard.putRaw("CL Status", status.data);
            var voltage = 2.8 * ((data.data[0] & 0xff) + (data.data[1] << 8)) / 1000.0;
            SmartDashboard.putNumber("CL Voltage", voltage);
        }
    }
}
