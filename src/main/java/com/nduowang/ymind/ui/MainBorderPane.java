package com.nduowang.ymind.ui;

import com.nduowang.ymind.common.Lang;
import com.nduowang.ymind.context.AppContext;
import com.nduowang.ymind.entity.YNode;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.List;

public class MainBorderPane extends BorderPane {
    public static final String RIGHT_OUTLINE = "outline";
    public static final String RIGHT_RECENT = "recent";
    public static final String RIGHT_CLOSE = "null";

    private AppContext appContext = AppContext.getInstance();

    public MainBorderPane() {
        super();
        if (AppContext.getInstance().isDebug) {
            setStyle("-fx-border-width: 1px;-fx-border-color: red");
        }
        setTop(new TopButtonBar());

        Label label = new Label("加载中");
        setCenter(label);

        initBottom();
        initRight();
    }

    public void initRight() {

        final TreeView<String> treeView = new TreeView<>();
        treeView.getStyleClass().add("outLineTreeView");
        treeView.setPrefWidth(200d);

        /**
         * 切换tab的时候，大纲内容也切
         */
        appContext.getDocManager()
                .activeDocProperty.addListener((obj, oldDoc, activeDoc) -> {
            if (appContext.rightBoxContentProperty.getValue().equals(RIGHT_OUTLINE)) {
                YNode rootNode = activeDoc.getRootNode();
                if (rootNode != null) {
                    treeView.setRoot(getTreeItem(rootNode));
                }
            }

        });


        /**
         * 根据 rightBoxContentProperty  参数，控制右侧边栏的内容
         */
        appContext.rightBoxContentProperty
                .addListener((obj, oldValue, newValue) -> {
                    if (newValue.equals(RIGHT_OUTLINE)) {
                        YNode rootNode = appContext.getDocManager().getActiveDoc().getRootNode();
                        if (rootNode != null) {
                            treeView.setRoot(getTreeItem(rootNode));
                        }
                        setRight(treeView);
                    } else {
                        setRight(null);
                    }

                });
        appContext.rightBoxContentProperty.setValue(RIGHT_CLOSE);
    }


    private TreeItem<String> getTreeItem(YNode yNode) {
        TreeItem<String> treeItem = new TreeItem<>();
        treeItem.setExpanded(true);
        treeItem.setValue(yNode.getText());
        List<YNode> childList = yNode.getInnerChildList();

        if (childList == null || childList.size() == 0) {
            treeItem.setGraphic(new Graphic("/images/tree-view-vline2.png"));
            return treeItem;
        }

        treeItem.setGraphic(new Graphic("/images/tree-view-plus3.png"));
        for (YNode yNodeItem : childList) {
            treeItem.getChildren().add(getTreeItem(yNodeItem));
        }
        return treeItem;
    }

    public void initBottom() {
        HBox hBox = new HBox();
        hBox.getStyleClass().add("bottomBar");

        Label isDirty = new Label(Lang.get("doc.saved"));
        HBox.setHgrow(isDirty, Priority.NEVER);
        hBox.getChildren().add(isDirty);

        Label file = new Label("file");
        HBox.setHgrow(file, Priority.ALWAYS);
        hBox.getChildren().add(file);

        AppContext.getInstance().getDocManager()
                .activeDocProperty.addListener((obj, oldDoc, activeDoc) -> {
            System.out.println("active doc change");

            file.setText(Lang.get("string.path") + ":" + activeDoc.saveFileProperty.getValue());
            if (!activeDoc.isDirtyProperty.getValue()) {
                isDirty.setText(Lang.get("doc.saved"));
            } else {
                isDirty.setText(Lang.get("doc.unsave"));
            }

            activeDoc.saveFileProperty.addListener((obj1, oldValue, newValue) -> {
                file.setText(Lang.get("string.path") +":" + newValue);
            });
            activeDoc.isDirtyProperty.addListener((obj1, oldValue, isDirtyValue) -> {
                if (!isDirtyValue) {
                    isDirty.setText(Lang.get("doc.saved"));
                } else {
                    isDirty.setText(Lang.get("doc.unsave"));
                }
            });
        });

        setBottom(hBox);

    }
}
