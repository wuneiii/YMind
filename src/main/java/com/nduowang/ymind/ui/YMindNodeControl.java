package com.nduowang.ymind.ui;

import com.nduowang.ymind.context.AppContext;
import com.nduowang.ymind.entity.YNode;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class YMindNodeControl extends StackPane {
    // 曲线放在背景里
    private Pane backGround;
    // 实体放在前景里
    private HBox frontGround;
    // 控件对应的数据结构
    private YNode yNode;
    //右侧输入框
    private YMindLabel yMindLabel;
    // 左侧的子节点数
    private VBox vBox;


    public YMindNodeControl(YNode yNode) {
        if (AppContext.getInstance().isDebug) {
            setStyle("-fx-border-color: orange;-fx-border-width: 5px;");
        }
        //Assert.checkNonNull(yNode, "ControlYLeftNode init param YNode is null");
        this.yNode = yNode;
        initBackAndFront();
        initLabel();
        initVBox();

        // 控件拖动事件处理
        setOnMousePressed(mouseEventEventHandler);
        setOnMouseDragged(mouseEventEventHandler);


    }


    private void initBackAndFront() {

        // 前景和背景，坐标系相同
        // 背景用来画曲线
        backGround = new Pane();
        getChildren().add(backGround);

        // 前景用来画控件
        frontGround = new HBox();

        frontGround.setAlignment(Pos.CENTER_LEFT);
        frontGround.setFillHeight(false);
        // 内边距
        frontGround.setPadding(new Insets(0));
        // label和右边vbox的间距
        frontGround.setSpacing(30d);
        getChildren().add(frontGround);
    }

    private void initLabel() {
        yMindLabel = new YMindLabel(yNode);
        frontGround.getChildren().add(yMindLabel);
    }

    private void initVBox() {
        vBox = new VBox();
        vBox.setFillWidth(true);
        // vbox中，兄弟节点，上下的间隔
        // 1级节点的间距大一点
        if (yNode.getLevel().equals(0)) {
            vBox.setSpacing(40d);
        } else {
            vBox.setSpacing(2d);
        }
        if (AppContext.getInstance().isDebug) {
            vBox.setStyle("-fx-border-color: red;-fx-border-width: 1px;");
        }
        // vbox中，所有节点周围的padding
        vBox.setPadding(new Insets(2));
        frontGround.getChildren().add(vBox);

        if (yNode.getLevel() > 0) {
            vBox.getStyleClass().add("yNodeVBox_Level_More");
        } else {
            vBox.getStyleClass().add("yNodeVBox_Level_" + yNode.getLevel());
        }
    }

    public String getText() {
        return yMindLabel.getText();
    }

    public void setText(String txt) {
        yMindLabel.setText(txt);
    }


    // 增加子节点
    public void attachChild(YMindNodeControl control) {
        vBox.getChildren().add(control);
        if (AppContext.getInstance().isDebug) {
            control.setStyle("-fx-border-width: 1px;-fx-border-color: green;");
        }
        attachCurveToChild(control);
    }

    private void attachCurveToChild(YMindNodeControl control) {
        ArrowCurve arrowCurve = new ArrowCurve(50);

        arrowCurve.startX.bind(
                yMindLabel.layoutXProperty().add(yMindLabel.widthProperty().divide(2))
        );
        arrowCurve.startY.bind(
                yMindLabel.layoutYProperty().add(yMindLabel.heightProperty().divide(2))
        );
        arrowCurve.endX.bind(
                //减去三角形的距离
                vBox.layoutXProperty().add(control.layoutXProperty()).subtract(10)
        );
        arrowCurve.endY.bind(
                vBox.layoutYProperty().add(control.layoutYProperty()).add(control.heightProperty().divide(2))
        );
        backGround.getChildren().add(arrowCurve);
    }


    // 控件拖动控制
    private EventHandler<MouseEvent> mouseEventEventHandler = new EventHandler<MouseEvent>() {
        private Point2D lastPoint;
        private Point2D newPoint;
        private Point2D delta;

        @Override
        public void handle(MouseEvent event) {
            //System.out.println(event.getEventType().getName());
            if (event.getEventType().getName().equals("MOUSE_PRESSED")) {
                if (event.getClickCount() == 1) {
                    lastPoint = new Point2D(event.getSceneX(), event.getSceneY());
                    //setCursor(Cursor.HAND);
                }
            } else if (event.getEventType().getName().equals("MOUSE_DRAGGED")) {

                layoutYProperty().unbind();
                newPoint = new Point2D(event.getSceneX(), event.getSceneY());
                delta = newPoint.subtract(lastPoint);

                layoutYProperty().setValue(layoutYProperty().getValue() + delta.getY());
                layoutXProperty().setValue(layoutXProperty().getValue() + delta.getX());
                lastPoint = newPoint;
            }
        }
    };

}