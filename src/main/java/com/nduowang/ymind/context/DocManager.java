package com.nduowang.ymind.context;

import com.nduowang.ymind.common.Constant;
import com.nduowang.ymind.common.Lang;
import com.nduowang.ymind.entity.Document;
import com.nduowang.ymind.entity.YNode;
import com.nduowang.ymind.service.Storage;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DocManager {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String docNamePrefix = Lang.get("string.newDoc");
    private Integer index = 0;
    public ObjectProperty<Document> activeDocProperty = new SimpleObjectProperty<>();

    private List<Document> documentList = new ArrayList<>();
    public ObservableList<Document> docListProperty = FXCollections.observableArrayList(documentList);

    public List<Document> getDocList() {
        return documentList;
    }


    public void newDoc() {
        index++;
        docListProperty.add(new Document(docNamePrefix + index));
    }

    public Document getActiveDoc() {
        return activeDocProperty.getValue();
    }

    public void bindDocList(ListChangeListener<Document> listener) {
        docListProperty.addListener(listener);
    }

    public void saveFile() {
        String saveFileName = null;
        File saveFile = null;
        Document activeDoc = activeDocProperty.getValue();
        if (activeDoc != null) {
            saveFileName = activeDoc.saveFileProperty.getValue();
        }
        if (StringUtils.isEmpty(saveFileName)) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("保存文件");

            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
                    Constant.FILE_DESC, "*" + Constant.FILE_EXT)
            );
            fileChooser.setInitialFileName(activeDoc.tabNameProperty.getValue() + Constant.FILE_EXT);
            saveFile = fileChooser.showSaveDialog(AppContext.getInstance().stage);
        } else {
            saveFile = new File(saveFileName);
        }
        if (saveFile == null) {
            log.info("保存动作取消");
            return;
        } else {
            log.info("保存文件路径:" + saveFile + "|文件名：" + saveFile.getName());
        }


        Storage storage = new Storage();
        storage.saveFile(saveFile, activeDoc.getRootNode());

        activeDoc.isDirtyProperty.set(false);
        activeDoc.saveFileProperty.setValue(saveFile.getAbsolutePath());
        activeDoc.tabNameProperty.setValue(saveFile.getName().replace(Constant.FILE_EXT, ""));
    }

    public void openDoc() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择文件");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(Constant.FILE_DESC, "*" + Constant.FILE_EXT)
        );

        File file = fileChooser.showOpenDialog(AppContext.getInstance().stage);
        if (file == null) {
            log.info("打开文件动作取消");
            return;
        } else {
            log.info("打开文件路径:" + file + "|文件名：" + file.getName());
        }
        try {
            Storage storage = new Storage();

            YNode yNode = storage.loadFile(file);
            String tabName = file.getName().replace(Constant.FILE_EXT, "");

            Document document = new Document(tabName, file.getAbsolutePath(), yNode);
            docListProperty.add(document);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
