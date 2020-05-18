package com.nduowang.ymind.ui;

import com.nduowang.ymind.common.Config;
import com.nduowang.ymind.common.Lang;
import com.nduowang.ymind.context.AppContext;
import com.nduowang.ymind.entity.YNode;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;


public class YMindLabel extends HBox {
    public static final short STATUS_NORMAL = 1;
    public static final short STATUS_EDIT = 2;
    public static final short STATUS_HOVER = 3;

    private short status = STATUS_NORMAL;


    private StackPane stackPane;
    private TextField textField;
    private Label label;
    private Button plusButton;

    private final YNode yNode;

    public YMindLabel(YNode node) {

        yNode = node;

        label = new Label();
        textField = new TextField();
        label.textProperty().bind(textField.textProperty());

        stackPane = new StackPane();
        if (AppContext.getInstance().isDebug) {
            stackPane.setStyle("-fx-border-color: red;-fx-border-width: 1px;");
        }
        stackPane.getChildren().addAll(label, textField);
        getChildren().addAll(stackPane);

        //plusButton
        plusButton = new Button("╋");
        plusButton.getStyleClass().add("plusButton");

        plusButton.setOnMouseClicked(event -> {
            yNode.newChildNode();
        });
        getChildren().add(plusButton);

        initContextMenu();
        initHandler();
        setText(Lang.get("string.newTopic"));
        changeYNodeLabelStatus(STATUS_NORMAL);

        //Style
        getStyleClass().add("yNodeLabel");
        if (yNode.getLevel() > 1) {
            getStyleClass().add("yNodeLabel_Level_More");
        } else {
            getStyleClass().add("yNodeLabel_Level_" + yNode.getLevel());

        }


    }


    void initHandler() {
        /**
         * label 文字变化了，要标记doc isDirty
         * 这里要监听
         * //TODO
         */
        AppContext.getInstance().getDocManager()
                .activeDocProperty.addListener((obj, old, activeDoc) -> {
            label.textProperty().addListener((obj1, oldValue, newValue) -> {
                activeDoc.isDirtyProperty.setValue(true);
            });
        });


        /* 编辑态，回车按下*/
        textField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                changeYNodeLabelStatus(STATUS_NORMAL);
            }
        });

        /**
         * 双击
         */
        stackPane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && event.getButton().equals(MouseButton.PRIMARY)) {
                changeYNodeLabelStatus(STATUS_EDIT);
            }
        });

        /**
         * hover out
         */
        stackPane.setOnMouseExited(event -> {
            changeYNodeLabelStatus(STATUS_NORMAL);
        });

    }


    public String getText() {
        return textField.getText();
    }

    void setText(String text) {
        textField.setText(text);
    }

    public void changeYNodeLabelStatus(short s) {
        status = s;
        switch (s) {
            case STATUS_EDIT:


                label.setVisible(false);
                textField.setVisible(true);
                textField.requestFocus();
                textField.selectAll();
                break;
            case STATUS_HOVER:
                label.setVisible(true);
                textField.setVisible(false);
                break;
            case STATUS_NORMAL:
                if (textField.getText().length() == 0) {
                    setText("无内容");
                }
                label.setVisible(true);
                textField.setVisible(false);
                break;
        }

    }


    public void initContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        Map<String, String> map = new HashMap<>();
        map.put("new", "新增");
        map.put("del", "删除");
        MenuItem menuItem;
        for (String id : map.keySet()) {
            menuItem = new MenuItem(map.get(id));
            menuItem.setId(id);
            menuItem.setOnAction(event -> {
                MenuItem item = (MenuItem) event.getSource();
                if (item.getId().equals("new")) {
                    yNode.newChildNode();
                } else if (item.getId().equals("del")) {
                    System.out.println("delItem");
                }
            });
            contextMenu.getItems().add(menuItem);
        }

        stackPane.setOnContextMenuRequested((event) -> {
            contextMenu.show(this, event.getScreenX(), event.getScreenY());
        });

    }

}

