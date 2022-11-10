package com.genne23v.openssg;

import java.util.HashSet;
import java.util.Objects;

public class DomNode<T> {
    private HashSet<DomNode<T>> children = new HashSet<>();
    private DomNode<T> parent = null;
    private T data;

    public DomNode(T data) {
        this.data = data;
    }

    public DomNode(T data, final DomNode<T> parent) {
        this.data = data;
        parent.addChild(this);
        this.parent = parent;
    }

    public HashSet<DomNode<T>> getChildren() {
        var newChildren = new HashSet<DomNode<T>>(children);
        return newChildren;
    }

    public void setParent(DomNode<T> parent) {
        this.parent = parent;
    }

    public DomNode<T> getParent(){
        return this.parent;
    }

    public void addChild(T data) {
        DomNode<T> child = new DomNode<>(data);
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(DomNode<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public DomNode<T> getChild(T data) {
        DomNode<T> node = null;
        for (DomNode<T> child : this.children) {
            if (child.getData().equals(data)) {
                node = child;
            }
        }
        return node;
    }

    public boolean samePathExistsInChildren(T data) {
        boolean samePathExistsInChildren = false;
        for (DomNode<T> child : this.children) {
            if (child.getData().equals(data)) {
                samePathExistsInChildren = true;
            }
        }
        return samePathExistsInChildren;
    }

    public String getUrl() {
        if (this.parent == null) {
            return "./" + this.data;
        }
        return this.parent.getUrl() + "/" + this.data;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        return this.children.size() == 0;
    }

    public T getData() {
        return this.data;
    }

}
