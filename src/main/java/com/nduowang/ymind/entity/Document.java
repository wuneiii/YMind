package com.nduowang.ymind.entity;


import com.nduowang.ymind.context.AppContext;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;

public class Document {
    public StringProperty tabNameProperty = new SimpleStringProperty();
    public StringProperty saveFileProperty = new SimpleStringProperty();
    public BooleanProperty isDirtyProperty = new SimpleBooleanProperty();
    private YNode rootNode;
    private Pane basePane;

    public Document(String tabName, String saveFile, YNode rootNote) {
        if (rootNote == null) {
            rootNote = YNode.createNode(null);
        }
        this.tabNameProperty.setValue(tabName);
        this.rootNode = rootNote;
        this.isDirtyProperty.setValue(true);
        this.saveFileProperty.setValue(saveFile);

        initUi();
    }

    public Document(String tabName, YNode rootNote) {
        this(tabName, null, rootNote);
    }

    public Document(String tabName) {
        this(tabName, null);
    }

    public void initUi() {

        this.basePane = new Pane();
        this.basePane.getStyleClass().add("documentPane");

        if (AppContext.getInstance().isDebug) {
            basePane.setStyle("-fx-border-width: 5px;-fx-border-color: red; -fx-background-color: green;");
        }
        rootNode.attachRootNode(basePane);


        /**
         * 初始化的时候，保证basePane和scrollPane的高度一致
         * 后期内容增多，pane会撑开scrollPane,要解除这个约束，否则scroll不会滚动
         */
    }


    public YNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(YNode rootNode) {
        this.rootNode = rootNode;
    }

    public Pane getBasePane() {
        return basePane;
    }


    /**
     * 核心函数
     * 这是一个独立于各种类
     *
     * @param
     */
    /*
    public void createChildNode(YNode parent) {
        parent.addChild(YNode.createNode(parent));
    }
    */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("tabName:" + tabNameProperty.getValue());
        stringBuilder.append("\thash:" + Integer.toHexString(this.hashCode()));
        stringBuilder.append("\tdirty:" + isDirtyProperty.getValue());
        stringBuilder.append("\tnode:" + Integer.toHexString(rootNode.hashCode()));
        return stringBuilder.toString();
    }
}
