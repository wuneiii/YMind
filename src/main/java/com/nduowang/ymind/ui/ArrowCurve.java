package com.nduowang.ymind.ui;

import com.nduowang.ymind.common.Config;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polygon;


public class ArrowCurve extends Group {
    private double control;
    private CubicCurve cubicCurve = new CubicCurve();
    private static Color color = Color.web(Config.curveColor);
    private static Double width = Double.parseDouble(String.valueOf(Config.curveWidth));
    private Polygon arrow;


    public DoubleProperty startX = new SimpleDoubleProperty(0d);
    public DoubleProperty startY = new SimpleDoubleProperty(0d);
    public DoubleProperty endX = new SimpleDoubleProperty(10d);
    public DoubleProperty endY = new SimpleDoubleProperty(10d);

    public ArrowCurve(double control) {
        super();
        this.control = control;
        cubicCurve.setStroke(color);
        cubicCurve.setStrokeWidth(width);
        cubicCurve.setFill(null);
        getChildren().addAll(cubicCurve);
        initPoint();
    }

    private void initPoint() {
        cubicCurve.startXProperty().bind(startX);
        cubicCurve.startYProperty().bind(startY);
        cubicCurve.endXProperty().bind(endX);
        cubicCurve.endYProperty().bind(endY);

        cubicCurve.controlX1Property().bind(startX.add(control));
        cubicCurve.controlY1Property().bind(startY);
        cubicCurve.controlX2Property().bind(endX.subtract(control));
        cubicCurve.controlY2Property().bind(endY);


        endX.addListener(changeListener);
        endY.addListener(changeListener);


    }

    private ChangeListener<Number> changeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            drawArrow();
        }
    };

    private void drawArrow() {
        if (arrow != null) {
            getChildren().remove(arrow);
        }
        double w = 10;
        arrow = new Polygon();
        arrow.getPoints().addAll(
                endX.get(), endY.get() + w / 2,
                endX.get() + w, endY.get(),
                endX.get(), endY.get() - w / 2);
        arrow.setFill(color);
        getChildren().add(arrow);
    }

}
