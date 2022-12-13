package com.example;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class SmartGroup extends Group {
    Rotate r;
    Transform transform = new Rotate();

    void rotateByX(double angle)
    {
        r = new Rotate(angle,Rotate.X_AXIS);
        transform = transform.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(transform);
    }
    void rotateByY(double angle)
    {
        r = new Rotate(angle,Rotate.Y_AXIS);
        transform = transform.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(transform);
    }
}
