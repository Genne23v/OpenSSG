import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

public class DOMNode<T> {
    private HashSet<DOMNode<T>> children = new HashSet<DOMNode<T>>();
    private DOMNode<T> parent = null;
    private T data = null;

    public DOMNode(T data) {
        this.data = data;
    }

    public DOMNode(T data, DOMNode<T> parent) {
        this.data = data;
        this.parent = parent;
    }

    public HashSet<DOMNode<T>> getChildren() {
        return children;
    }

    public void setParent(DOMNode<T> parent) {
        this.parent = parent;
    }

    public void addChild(T data) {
        DOMNode<T> child = new DOMNode<T>(data);
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(DOMNode<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public DOMNode<T> getChild(T data){
        DOMNode<T> node = null;
        for (DOMNode<T> child : this.children){
           if (child.getData().equals(data)){
                node = child;
           }
        }
        return node;
    }

    public boolean doesChildrenHaveData(T data){
        boolean doesChildrenHaveData = false;
        for (DOMNode<T> child : this.children){
            if (child.getData().equals(data)){
                doesChildrenHaveData = true;
            }
        }
        return doesChildrenHaveData;
    }

    public String getUrl() {
        if (this.parent == null){
            return "./" + this.data;
        }
        return this.parent.getUrl() + "/" + this.data;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        return this.children.size() == 0;
    }

    public void removeParent() {
        this.parent = null;
    }

}
