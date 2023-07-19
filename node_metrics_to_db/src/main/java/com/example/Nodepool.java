package com.example;

import java.util.List;

public class Nodepool {
    List<CustomNode> nodes;
    String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNodes(List<CustomNode> nodes) {
        this.nodes = nodes;
    }

    public List<CustomNode> getNodes() {
        return nodes;
    }

    // public void addNode(CustomNode node) {
    // this.nodes.add(node);
    // }
    public Nodepool(String name) {
        this.name = name;
    }
}
