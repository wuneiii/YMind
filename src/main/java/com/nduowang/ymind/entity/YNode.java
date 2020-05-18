package com.nduowang.ymind.entity;

import com.nduowang.ymind.context.AppContext;
import com.nduowang.ymind.common.NodeIdGenerator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import com.nduowang.ymind.ui.YMindNodeControl;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class YNode {

    private Integer level;
    private Integer id;
    private YNode parent;
    private List<YNode> _childList;
    private ObservableList<YNode> childList;

    // 不能在构造函数里 初始化
    // Ynode创建完后，才能创建ui控件
    // 因为ui控件构造里，依赖ynode的属性，来做不同的显示
    private YMindNodeControl yMindNodeControl;

    public static YNode createNode(YNode parent) {
        YNode newNode = new YNode();
        if (parent != null) {
            newNode.parent = parent;
            newNode.level = parent.getLevel() + 1;
        }
        // 控件构造，需要用到level，id等信息，
        // 所以构造前，各种属性要齐全
        newNode.yMindNodeControl = new YMindNodeControl(newNode);
        return newNode;
    }

    private YNode() {
        _childList = new ArrayList<>();
        childList = FXCollections.observableList(_childList);
        childList.addListener(childListChangeListener);

        this.id = NodeIdGenerator.getId();
        this.level = 0;
        this.parent = null;
    }


    private ListChangeListener<YNode> childListChangeListener = new ListChangeListener<YNode>() {

        @Override
        public void onChanged(Change<? extends YNode> c) {
            // 对这个Assert的说明
            // 理论上不会，控件在上界面的时候，初始化
            // 不应该在添加子节点的时候，还没初始化。
            while (c.next()) {
                List<? extends YNode> list = c.getAddedSubList();
                for (YNode item : list) {
                    yMindNodeControl.attachChild(item.yMindNodeControl);
                }
            }
        }
    };


    public void attachRootNode(Pane basePane) {
        basePane.getChildren().add(yMindNodeControl);
        //yMindNodeControl.layoutYProperty().setValue(basePane.getHeight() / 2);
        /**
         * 控件绑定了场景中央
         * 所以，在后边拖动的时候，需要操作y属性，和这个绑定冲突。
         *
         * 拖动之前，必须先接触这个绑定，否则拖动不成功；
         */

        yMindNodeControl.layoutYProperty().bind(
                basePane.heightProperty().divide(2).subtract(yMindNodeControl.heightProperty().divide(2))
        );
        yMindNodeControl.layoutXProperty().setValue(20d);
    }


    public List<YNode> getInnerChildList() {
        return _childList;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return yMindNodeControl.getText();
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setText(String txt) {
        this.yMindNodeControl.setText(txt);
    }

    /*
    子节点新增方法1
    直接新增没有任何历史负担的子节点
     */
    public void newChildNode() {
        AppContext.getInstance().getDocManager().getActiveDoc().isDirtyProperty.setValue(true);
        childList.add(YNode.createNode(this));
    }

    /**
     * 子节点新增方法2
     * openFile的时候，会根据文件内容，生成child.
     * 也是通过上边的createNode(parent)生成的，保证是此node的parent
     * 然后通过这个函数加入childlist
     *
     * @param ynode
     */
    public void addChildNode(YNode ynode) {
//        System.out.println("param:"+ ynode);
//        System.out.println("param parent:"+ ynode.parent);
//        System.out.println("this:" + this);
//        System.out.println("parent:" + parent);
        if (!ynode.parent.equals(this)) {
            throw new RuntimeException("Ynode.addChildNode param is not child");
        }
        childList.add(ynode);

    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id:" + id + "|");
        stringBuilder.append("level:" + level + "|");
        if (parent != null) {
            stringBuilder.append("parent:" + parent.getId() + "|");
        }
        stringBuilder.append("text:" + yMindNodeControl.getText() + "|");
        return stringBuilder.toString();
    }
}
