package com.example;

import com.fazecast.jSerialComm.*;

import java.io.DataInputStream;
import java.io.IOException;

public class SerialReader {
   DataInputStream input;
   SerialPort comPort;
   public  SerialReader()
   {
      comPort = SerialPort.getCommPort("COM4"); // device name TODO: must be changed
      comPort.setComPortParameters(9600, 8, 1, 0); // default connection settings for Arduino
      comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0); // block until bytes can be written
   }
   public double readPort() {
      comPort.openPort();

      if (comPort.isOpen()) {
         System.out.println("Port COM4 initialized!");
         input = new DataInputStream(comPort.getInputStream());

      } else {
         System.out.println("Port not available");
         return 0;
      }

         try {
            double decimal = Double.parseDouble(input.readLine());
            System.out.println(decimal);
            comPort.closePort();
            return decimal;
//            Thread.sleep(10);
         } catch (IOException e) {
            try {
               input.close();
               comPort.closePort();
            } catch (IOException ex) {
               throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
         }


   }


}