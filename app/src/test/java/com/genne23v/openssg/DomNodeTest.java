package com.genne23v.openssg;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class DomNodeTest {

    @Test
    public void DomNodeSetParentTest() {
        DomNode<String> rootNode = new DomNode<>("root");
        DomNode<String> newNode = new DomNode<>("child");
        newNode.setParent(rootNode);

        Assertions.assertNotNull(rootNode.getChildren());
        Assertions.assertNull(rootNode.getParent());
    }

    @Test
    public void addChildTest() {
        DomNode<String> rootNode = new DomNode<>("root");
        rootNode.addChild("child");

        Assertions.assertNotNull(rootNode.getChild("child"));
        Assertions.assertNull(rootNode.getChild("invalid child"));

        DomNode<String> newNode = new DomNode<>("child2", rootNode);
        Assertions.assertEquals(2, rootNode.getChildren().size());
        Assertions.assertEquals("root", newNode.getParent().getData());
        Assertions.assertTrue(rootNode.isRoot());
        Assertions.assertFalse(rootNode.isLeaf());
        Assertions.assertFalse(newNode.isRoot());
        Assertions.assertTrue(newNode.isLeaf());

    }

    @Test
    public void samePathExistsInChildrenTest() {
        DomNode<String> rootNode = new DomNode<>("root");
        rootNode.addChild("child");

        Assertions.assertTrue(rootNode.samePathExistsInChildren("child"));
        Assertions.assertFalse(rootNode.samePathExistsInChildren("child not exist"));
    }

    @Test
    public void getUrlTest() {
        DomNode<String> rootNode = new DomNode<>("root");
        DomNode<String> childNode1 = new DomNode<>("child1");
        rootNode.addChild(childNode1);
        DomNode<String> childNode2 = new DomNode<>("child2");
        childNode2.setParent(childNode1);


        Assertions.assertEquals("./root/child1/child2", childNode2.getUrl());
    }
}
