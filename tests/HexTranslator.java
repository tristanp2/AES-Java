//Implementation of AES for SENG360

import java.io.*;
import java.util.Scanner;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;
import java.util.Random;
import java.math.BigInteger;


public class HexTranslator{
	//initializations of AES lookup tables taken from wikipedia.
    //java byte is signed and would require casts, so short was used
    //Convert hex string to binary
    private static byte[] strToBinary(String s) {
        return new BigInteger(s, 16).toByteArray();
    }
    private static String binaryToString(byte[] b){
        return new BigInteger(b).toString(16);
    }

    private static void textToHex(Scanner inputFile, FileOutputStream outputFile){
        String buffer = new String(), outputString, line;
        do{
            if(inputFile.hasNextLine())   buffer += inputFile.nextLine() + '\n';
            else if(buffer.length() < 16){
                System.out.printf("%d bytes omitted from output. Sorry.\n", buffer.length());
                return;
            }
            if(buffer.length() >= 16){   //2 hex vals per char
                line = buffer.substring(0,16);
                buffer = buffer.substring(16);
                outputString = binaryToString(line.getBytes());
                if(line.getBytes()[0] < 0x10)
                    outputString = '0' + outputString;
   //             System.out.printf("Line: %s\nBuffer: %s\n", line, buffer);
                try{
                    outputFile.write(outputString.getBytes());
                    outputFile.write((byte)'\n');
                }catch(IOException e){
                    System.out.printf("IO exception\n");
                    return;
                }
            }
        }while(true);
    }
    private static void hexToText(Scanner inputFile, FileOutputStream outputFile){
        String buffer;
        byte[] outputBytes;
        while(inputFile.hasNextLine()){
            buffer = inputFile.nextLine();
            outputBytes = strToBinary(buffer);
            try{
                outputFile.write(outputBytes);
            }catch(IOException e){
                System.out.printf("IO exception\n");
                return;
            }
        }
    }
	public static void main(String[] args){
		Scanner inputFile;
        FileOutputStream outputFile = null;
        boolean toHex = false;    //because of the annoying compiler warnings

		if (args.length == 2){
            if(args[0].equals("a")) toHex = true;
            else if(args[0].equals("h")) toHex = false;
            else{
                System.out.printf("Unknown option: %s\n%s not equal to %s or %s", args[0], args[0], "a", "h");
                return;
            }

			try{
				inputFile = new Scanner(new File(args[1]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[1]);
				return;
			}
			System.out.printf("Reading input from %s.\n",args[1]);
		}
        else{
            System.out.printf("Usage: java AES <ascii/hex input (a/h)> <inputfile>\n");
            return;
		}
        if(toHex){
            try{
                outputFile  = new FileOutputStream(new File(args[1] + ".hex"));
			}catch(java.io.FileNotFoundException e){
                System.out.printf("Something weird happened\n");
                return;
            }
            textToHex(inputFile, outputFile);
        }
        else{
            try{
                outputFile  = new FileOutputStream(new File(args[1] + ".txt"));
			}catch(java.io.FileNotFoundException e){
                System.out.printf("Something weird happened\n");
                return;
            }
            hexToText(inputFile, outputFile);
        }
	}
}

