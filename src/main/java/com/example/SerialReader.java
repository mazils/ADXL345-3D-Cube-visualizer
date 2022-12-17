package com.example;

import com.fazecast.jSerialComm.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.function.Consumer;

public class SerialReader extends Thread {
    private DataInputStream input;
    private SerialPort comPort;
    private ADXL345Data adxl345Datadata = new ADXL345Data();
    String pitch, roll;
    private Consumer<ADXL345Data> onRotationRead;

    public void setOnRotationRead(Consumer<ADXL345Data> listener) {
        this.onRotationRead = listener;
    }

    public void openPort(String port) {
        comPort = SerialPort.getCommPort(port);
        comPort.setComPortParameters(9600, 8, 1, 0); // default connection settings for Arduino
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0); // block until bytes can be written
        comPort.openPort();

        if (comPort.isOpen()) {
            System.out.println("Port " + port + " initialized!");
            input = new DataInputStream(comPort.getInputStream());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        } else {
            System.out.println("Port" + port + "not available");
            comPort.closePort();
        }
    }

    public void readPort() {
        try {
            //pitch
            String data = input.readLine();
            if (data.contains(",")) {
                try {
                    System.out.println(data);
                    pitch = data.split(",")[0];
                    roll = data.split(",")[1];
                    adxl345Datadata.setPitch(Double.parseDouble(pitch));
                    adxl345Datadata.setRoll(Double.parseDouble(roll));
                    //todo: set roll
//
                    this.onRotationRead.accept(adxl345Datadata);
                } catch (NumberFormatException e) {
                    System.out.println("this fucks up " + data);
                }
            }
        } catch (IOException e) {
            System.out.println("Something went wrong");
            try {
                input.close();
                comPort.closePort();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }


    @Override
    public void run() {
        while (true) {
            readPort();
        }

    }
}


