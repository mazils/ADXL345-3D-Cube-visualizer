package com.example;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

/**
     *
     * @author IDEA Developers
     */
    public class App extends Application {
    private static final int WIDTH = 1400;
    private static final int HEIGHT = 800;
    static SerialReader reader = new SerialReader();

        @Override
        public void start(Stage primaryStage) {

            Box box = new Box(100, 20, 50);

            //Prepare transformable Group container
            SmartGroup group = new SmartGroup();
            group.getChildren().add(box);

            Camera camera = new PerspectiveCamera();
            Scene scene = new Scene(group, WIDTH, HEIGHT);
            scene.setFill(Color.SILVER);
            scene.setCamera(camera);
            //Move to center of the screen
            group.translateXProperty().set(1400 / 2);
            group.translateYProperty().set(800 / 2);
            group.translateZProperty().set(-1200);

            //rotate in x axis
            Transform transform = new Rotate(65,Rotate.X_AXIS);
            box.getTransforms().add(transform);

            //Add keyboard control.
            primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                switch (event.getCode()) {
                    case Q:
                        group.rotateByX(reader.readPort());
                        break;
                    case E:
                        group.rotateByX(-10);
                        break;
                    case NUMPAD6:
                        group.rotateByY(10);
                        break;
                    case NUMPAD4:
                        group.rotateByY(-10);
                        break;
                }
            });


            primaryStage.setTitle("Hello World!");
            primaryStage.setScene(scene);
            primaryStage.show();
            group.rotateByX(reader.readPort());
        }






    public static void main(String[] args)  {
        launch(args);


    }
    
}