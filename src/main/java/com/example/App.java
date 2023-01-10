package com.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.stage.Stage;


public class App extends Application {
    private static final int WIDTH = 1400;
    private static final int HEIGHT = 800;
    private SerialReader reader = new SerialReader();

    @Override
    public void start(Stage primaryStage) {

        Box box = new Box(100, 20, 50);

        //Prepare transformable Group container
        Group group = new Group();
        group.getChildren().add(box);

        Camera camera = new PerspectiveCamera();
        Scene scene = new Scene(group, WIDTH, HEIGHT);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);
        //Move to center of the screen
        group.translateXProperty().set(WIDTH / 2);
        group.translateYProperty().set(HEIGHT / 2);
        group.translateZProperty().set(-1200);

        reader.setOnRotationRead(reading -> Platform.runLater(() -> {

                    matrixRotateNode(box, Math.toRadians(reading.getRoll()), Math.toRadians(reading.getPitch()), 0);
                }
        ));

        reader.openPort("COM4");
        Thread thread = new Thread(reader);

        thread.start();

        primaryStage.setTitle("ADXL345 Cube");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //where alf is roll, bet is pitch and gam is yaw.
    private void matrixRotateNode(Node n, double alf, double bet, double gam) {
        double A11 = Math.cos(alf) * Math.cos(gam);
        double A12 = Math.cos(bet) * Math.sin(alf) + Math.cos(alf) * Math.sin(bet) * Math.sin(gam);
        double A13 = Math.sin(alf) * Math.sin(bet) - Math.cos(alf) * Math.cos(bet) * Math.sin(gam);
        double A21 = -Math.cos(gam) * Math.sin(alf);
        double A22 = Math.cos(alf) * Math.cos(bet) - Math.sin(alf) * Math.sin(bet) * Math.sin(gam);
        double A23 = Math.cos(alf) * Math.sin(bet) + Math.cos(bet) * Math.sin(alf) * Math.sin(gam);
        double A31 = Math.sin(gam);
        double A32 = -Math.cos(gam) * Math.sin(bet);
        double A33 = Math.cos(bet) * Math.cos(gam);

        double d = Math.acos((A11 + A22 + A33 - 1d) / 2d);
        if (d != 0d) {
            double den = 2d * Math.sin(d);
            Point3D p = new Point3D((A32 - A23) / den, (A13 - A31) / den, (A21 - A12) / den);
            n.setRotationAxis(p);
            n.setRotate(Math.toDegrees(d));

        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}