import java.util.HashSet;
import java.util.Objects;

public class DOMNode<T> {
    private HashSet<DOMNode<T>> children = new HashSet<>();
    private DOMNode<T> parent = null;
    private T data;

    public DOMNode(T data) {
        this.data = data;
    }

    public DOMNode(T data, final DOMNode<T> parent) {
        this.data = data;
        var parentCopy = new DOMNode<>(parent.getData());
        this.parent = parentCopy;
    }

    public HashSet<DOMNode<T>> getChildren() {
        var newChildren = new HashSet<DOMNode<T>>(children);
        return newChildren;
    }

    public void setParent(DOMNode<T> parent) {
        var parentCopy = new DOMNode<>(parent.getData());
        this.parent = parentCopy;
    }

    public void addChild(T data) {
        DOMNode<T> child = new DOMNode<>(data);
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(DOMNode<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public DOMNode<T> getChild(T data) {
        DOMNode<T> node = null;
        for (DOMNode<T> child : this.children) {
            if (child.getData().equals(data)) {
                node = child;
            }
        }
        return node;
    }

    public boolean samePathExistsInChildren(T data) {
        boolean samePathExistsInChildren = false;
        for (DOMNode<T> child : this.children) {
            if (child.getData().equals(data)) {
                samePathExistsInChildren = true;
            }
        }
        return samePathExistsInChildren;
    }

    public String getUrl() {
        if (this.parent == null) {
            return ".";
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
