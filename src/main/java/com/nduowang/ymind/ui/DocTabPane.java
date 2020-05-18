package com.nduowang.ymind.ui;

import com.nduowang.ymind.common.Lang;
import com.nduowang.ymind.context.AppContext;
import com.nduowang.ymind.context.DocManager;
import com.nduowang.ymind.entity.Document;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocTabPane extends TabPane implements ListChangeListener<Document>, ChangeListener<Tab> {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private Tab plusTab; // 这是一个特殊的tab，仅限时加号

    public DocTabPane() {
        plusTab = new Tab("+");
        plusTab.setId("plusTabButton");
        getTabs().add(plusTab);

        getSelectionModel().selectedItemProperty()
                .addListener(this);
    }

    @Override
    public void changed(ObservableValue<? extends Tab> observableValue, Tab oldTab, Tab currentTab) {
        DocManager docManager = AppContext.getInstance().getDocManager();
        if (plusTab.equals(currentTab)) {
            log.debug("选中了关闭tab，新建一个tab在index=0的位置");
            docManager.newDoc();
        } else {
            docManager.activeDocProperty.setValue((Document) currentTab.getUserData());
        }
    }


    @Override
    /**
     * 核心动作
     * 监听doclist，并更新tabPane
     */
    public void onChanged(Change<? extends Document> change) {
        while (change.next()) {
            for (Document doc : change.getAddedSubList()) {
                Tab tab = new Tab();
                // tab名称和doc名称绑定
                tab.setText(doc.tabNameProperty.getValue());
                tab.setUserData(doc);
                tab.setContent(doc.getBasePane());
                getTabs().add(0, tab);
                getSelectionModel().select(tab);
                doc.isDirtyProperty.addListener((obj, oldValue, isDirty) -> {
                    if (isDirty) {
                        tab.setText(doc.tabNameProperty.getValue() + "[" + Lang.get("unsave") + "]");
                    } else {
                        tab.setText(doc.tabNameProperty.getValue() + "[" + Lang.get("saved") + "]");
                    }
                });
                doc.tabNameProperty.addListener((obj, oldValue, newValue) -> {
                    tab.setText(doc.tabNameProperty.getValue());
                });
            }
        }
    }
}
