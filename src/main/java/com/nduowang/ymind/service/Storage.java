package com.nduowang.ymind.service;

import com.nduowang.ymind.entity.YNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Storage {
    private Logger log = LoggerFactory.getLogger(getClass());

    public YNode loadFile(File file) throws Exception {
        StorageNode storageNode = null;

        if (!file.exists()) {
            throw new FileNotFoundException("file:" + file);
        }
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
        storageNode = (StorageNode) objectInputStream.readObject();
        objectInputStream.close();

        return storageNode2YNode(storageNode,null);

    }


    public void saveFile(File file, YNode rootNode) {
        log.debug("save File :" + file);
        StorageNode storageNode = yNode2StorageNode(rootNode);
        System.out.println(storageNode);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(storageNode);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StorageNode yNode2StorageNode(YNode yNode) {
        StorageNode storageNode = new StorageNode();
        storageNode.setId(yNode.getId());
        storageNode.setText(yNode.getText());
        List<YNode> child = yNode.getInnerChildList();
        if (child.size() == 0) {
            return storageNode;
        }
        List<StorageNode> childStorageNodeList = new ArrayList<>();
        for (YNode item : child) {
            childStorageNodeList.add(yNode2StorageNode(item));
        }
        storageNode.setChildren(childStorageNodeList);
        return storageNode;
    }


    private YNode storageNode2YNode(StorageNode storageNode,YNode parent) {
        YNode yNode = YNode.createNode(parent);
        yNode.setId(storageNode.getId());
        yNode.setText(storageNode.getText());

        if (storageNode.getChildren() == null || storageNode.getChildren().size() == 0) {
            return yNode;
        }
        for (StorageNode item : storageNode.getChildren()) {
            yNode.addChildNode(storageNode2YNode(item,yNode));
        }

        return yNode;
    }
}


