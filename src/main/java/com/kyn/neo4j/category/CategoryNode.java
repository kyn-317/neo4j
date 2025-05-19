package com.kyn.neo4j.category;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class CategoryNode {
    //node name
    String name;
    //children nodes
    Map<String, CategoryNode> children = new HashMap<>();

    @Override
    public String toString() {
        return toString(0);
    }

    //print category node
    public String toString(int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String indent = "  ".repeat(indentLevel);
        sb.append(indent).append("- ").append(name).append("\n");
        
        for (Map.Entry<String, CategoryNode> entry : children.entrySet()) {
            CategoryNode child = entry.getValue();
            sb.append(child.toString(indentLevel + 1));
        }
        return sb.toString();
    }
}
