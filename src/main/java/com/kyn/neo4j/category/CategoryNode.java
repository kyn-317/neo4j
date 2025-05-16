package com.kyn.neo4j.category;

import java.util.HashMap;
import java.util.Map;

public class CategoryNode {
    String name;
    Map<String, CategoryNode> children = new HashMap<>();

    public String getName() {
        return name;
    }

    public Map<String, CategoryNode> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return toString(0);
    }

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
