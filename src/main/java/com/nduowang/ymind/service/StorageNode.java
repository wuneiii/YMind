package com.nduowang.ymind.service;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
class StorageNode implements Serializable {
    private Integer id;
    private String text;
    private List<StorageNode> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<StorageNode> getChildren() {
        return children;
    }

    public void setChildren(List<StorageNode> children) {
        this.children = children;
    }
}