package frc.robot; // might need to change this number

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;

import java.util.*;

public class REVDigitBoard
{
	/*
	 * DOCUMENTATION::
	 * 
	 * REVDigitBoard() : constructor void display(String str) : displays the first
	 * four characters of the string (only alpha (converted to uppercase), numbers,
	 * and spaces) void display(double batt) : displays a decimal number (like
	 * battery voltage) in the form of 12.34 (ten-one-decimal-tenth-hundredth) void
	 * clear() : clears the display boolean getButtonA() : button A on the board
	 * boolean getButtonB() : button B on the board double getPot() : potentiometer
	 * value
	 */

	private I2C i2c;
	private DigitalInput buttonA, buttonB;
	private AnalogInput pot;

	private Map<Character, byte[]> charmap;

    private final byte[] startBytes = new byte[] {(byte) (0b00001111), 0};

	REVDigitBoard()
	{
		i2c = new I2C(Port.kMXP, 0x70);
		buttonA = new DigitalInput(19);
		buttonB = new DigitalInput(20);
		pot = new AnalogInput(7);

		byte[] osc = new byte[1];
		byte[] blink = new byte[1];
		byte[] bright = new byte[1];
		osc[0] = (byte) 0x21;
		blink[0] = (byte) 0x81;
		bright[0] = (byte) 0xEF;

		i2c.writeBulk(osc);
		Timer.delay(.01);
		i2c.writeBulk(bright);
		Timer.delay(.01);
		i2c.writeBulk(blink);
		Timer.delay(.01);

		charmap = new HashMap<Character, byte[]>();

		charmap.put(' ', new byte[] {(byte) 0b00000000, (byte) 0b00000000}); // space
		charmap.put('0', new byte[] {(byte) 0b00111111, (byte) 0b00100100}); // 0
		charmap.put('1', new byte[] {(byte) 0b00000110, (byte) 0b00000000}); // 1
		charmap.put('2', new byte[] {(byte) 0b11011011, (byte) 0b00000000}); // 2
		charmap.put('3', new byte[] {(byte) 0b11001111, (byte) 0b00000000}); // 3
		charmap.put('4', new byte[] {(byte) 0b11100110, (byte) 0b00000000}); // 4
		charmap.put('5', new byte[] {(byte) 0b11101101, (byte) 0b00000000}); // 5
		charmap.put('6', new byte[] {(byte) 0b11111101, (byte) 0b00000000}); // 6
		charmap.put('7', new byte[] {(byte) 0b00000111, (byte) 0b00000000}); // 7
		charmap.put('8', new byte[] {(byte) 0b11111111, (byte) 0b00000000}); // 8
		charmap.put('9', new byte[] {(byte) 0b11101111, (byte) 0b00000000}); // 9

		charmap.put('-', new byte[] {(byte) 0b11000000, (byte) 0b00000000}); // -
		charmap.put('+', new byte[] {(byte) 0b11000000, (byte) 0b00010010}); // +
		charmap.put('.', new byte[] {(byte) 0b00000000, (byte) 0b01000000}); // .

		charmap.put('A', new byte[] {(byte) 0b11110111, (byte) 0b00000000}); // A
		charmap.put('B', new byte[] {(byte) 0b10001111, (byte) 0b00010010}); // B
		charmap.put('C', new byte[] {(byte) 0b00111001, (byte) 0b00000000}); // C
		charmap.put('D', new byte[] {(byte) 0b00001111, (byte) 0b00010010}); // D
		charmap.put('E', new byte[] {(byte) 0b11111001, (byte) 0b00000000}); // E
		charmap.put('F', new byte[] {(byte) 0b11110001, (byte) 0b00000000}); // F
		charmap.put('G', new byte[] {(byte) 0b10111101, (byte) 0b00000000}); // G
		charmap.put('H', new byte[] {(byte) 0b11110110, (byte) 0b00000000}); // H
		charmap.put('I', new byte[] {(byte) 0b00001001, (byte) 0b00010010}); // I
		charmap.put('J', new byte[] {(byte) 0b00011110, (byte) 0b00000000}); // J
		charmap.put('K', new byte[] {(byte) 0b01110000, (byte) 0b00001100}); // K
		charmap.put('L', new byte[] {(byte) 0b00111000, (byte) 0b00000000}); // L
		charmap.put('M', new byte[] {(byte) 0b00110110, (byte) 0b00000101}); // M
		charmap.put('N', new byte[] {(byte) 0b00110110, (byte) 0b00001001}); // N
		charmap.put('O', new byte[] {(byte) 0b00111111, (byte) 0b00000000}); // O
		charmap.put('P', new byte[] {(byte) 0b11110011, (byte) 0b00000000}); // P
		charmap.put('Q', new byte[] {(byte) 0b00111111, (byte) 0b00001000}); // Q
		charmap.put('R', new byte[] {(byte) 0b11110011, (byte) 0b00001000}); // R
		charmap.put('S', new byte[] {(byte) 0b10001101, (byte) 0b00000001}); // S
		charmap.put('T', new byte[] {(byte) 0b00000001, (byte) 0b00010010}); // T
		charmap.put('U', new byte[] {(byte) 0b00111110, (byte) 0b00000000}); // U
		charmap.put('V', new byte[] {(byte) 0b00110000, (byte) 0b00100100}); // V
		charmap.put('W', new byte[] {(byte) 0b00110110, (byte) 0b00101000}); // W
		charmap.put('X', new byte[] {(byte) 0b00000000, (byte) 0b00101101}); // X
		charmap.put('Y', new byte[] {(byte) 0b00000000, (byte) 0b00010101}); // Y
		charmap.put('Z', new byte[] {(byte) 0b00001001, (byte) 0b00100100}); // Z
	}

	void display(String str)
	{
        // only displays first 4 chars
		byte[][] chars = new byte[4][];

		// uppercase the string
		str = str.toUpperCase();

        boolean lastCharContainsPeriod = true;
        int j = 0;
		for (int i = 0; i < str.length(); i++)
		{
            char c = str.charAt(i);
			byte[] bytes = charmap.get(c);
			if (bytes == null)
			{
				bytes = charmap.get(' ');
			}

            if (lastCharContainsPeriod || c != '.')
            {
                // this is a non-period or a stand-alone period:
                if (j == 4)
                {
                    // we have reached the maximum number of allowable characters
                    break;
                }

                // We need to copy the individual bytes in case a period
                // gets appended so as not sot corrupt what is in charmap:
                chars[j] = new byte[2];
                chars[j][0] = bytes[0];
                chars[j][1] = bytes[1];
                j++;
            }
            else if (c == '.')
            {
                // this is a period that needs to be added to the previous character
                chars[j - 1][0] |= bytes[0];
                chars[j - 1][1] |= bytes[1];
            }

            lastCharContainsPeriod = c == '.';
		}

        // see if need to pad to 4 characters:
        if (j < 4)
        {
            byte[] spaceBytes = charmap.get(' ');
            while (j < 4)
            {
                chars[j++] = spaceBytes;
            }
        }

		display(chars);
	}

    void display(double val)
    {
        display(val, false);
    }

	void display(double val, boolean displaySign)
	{
        // optimized for battery voltage, needs a double like 12.34
		byte[][] chars = new byte[4][];

        int i = 0;

        if (displaySign)
        {
            chars[i++] = val < 0 ? charmap.get('-') : charmap.get('+');
        }

        val = Math.abs(val);

		int ten = (int) ((val / 10) % 10);
		int one = (int) (val % 10);
		int tenth = (int) ((val * 10) % 10);
		int hundredth = (int) ((val * 100) % 10);
		int thousandth = (int) ((val * 1000) % 10);

		int dp;

		if (ten == 0)
		{
			chars[i++] = charmap.get((char) ('0' + one));
			chars[i++] = charmap.get((char) ('0' + tenth));
			chars[i++] = charmap.get((char) ('0' + hundredth));
            if (i < 4)
            {
			    chars[3] = charmap.get((char) ('0' + thousandth));
            }
			dp = displaySign ? 1 : 0;
		}
        else
		{
			chars[i++] = charmap.get((char) ('0' + ten));
			chars[i++] = charmap.get((char) ('0' + one));
			chars[i++] = charmap.get((char) ('0' + tenth));
            if (i < 4)
            {
			    chars[3] = charmap.get((char) ('0' + hundredth));
            }
			dp = displaySign ? 2 : 1;
		}

		display(chars, dp);
	}

	void clear()
	{
        display("    ");
	}

	boolean getButtonA()
	{
		return !buttonA.get();
	}

	boolean getButtonB()
	{
		return !buttonB.get();
	}

	double getPot()
	{
		return pot.getVoltage();
	}

	////// not supposed to be publicly used..

	private void display(byte[][] chars)
	{
		display(chars, -1);
	}

	// dp = position in which to display decimal point
	private void display(byte[][] chars, int dp)
	{
		byte[] bytes = new byte[8 + startBytes.length];
        int i = 0;
        for (; i < startBytes.length; i++)
        {
            bytes[i] = startBytes[i];
        }
		bytes[i++] = chars[3][0];
		bytes[i++] = chars[3][1];
		bytes[i++] = chars[2][0];
		bytes[i++] = chars[2][1];
		bytes[i++] = chars[1][0];
		bytes[i++] = chars[1][1];
		bytes[i++] = chars[0][0];
		bytes[i++] = chars[0][1];

		if (0 <= dp && dp <= 3)
		{
            byte[] dpBytes = charmap.get('.');
			bytes[(3 - dp) * 2 + startBytes.length + 0] |= dpBytes[0];
			bytes[(3 - dp) * 2 + startBytes.length + 1] |= dpBytes[1];
		}

		// send the array to the board
		i2c.writeBulk(bytes);
		Timer.delay(0.01);
	}

	String repeat(char c, int n)
	{
		char[] arr = new char[n];
		Arrays.fill(arr, c);
		return new String(arr);
	}
}
