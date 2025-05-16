package com.kyn.neo4j.category;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MasterNode {
    private Map<String,CategoryNode> children = new HashMap<>();

    public void addCategoryPath(String categoryPath) {
        List<String> categoryPathArray = Arrays.asList(categoryPath.split("\\|"))
                        .stream().map(String::trim).collect(Collectors.toList());
        if (categoryPathArray == null || categoryPathArray.isEmpty()) return;
        String root = categoryPathArray.get(0); 
        CategoryNode current = this.children.get(root);
        if (current == null) {
            current = new CategoryNode();
            current.name = root;
            this.children.put(root, current);
        }
        CategoryNode parent = current;
        for (int i = 1; i < categoryPathArray.size(); i++) {
            String cat = categoryPathArray.get(i);
            CategoryNode child = parent.children.get(cat);
            if (child == null) {
                child = new CategoryNode();
                child.name = cat;
                parent.children.put(cat, child);
            }
            parent = child;
        }
    }


    public MasterNode getMasterNode() {
        return this;
    }

    public Map<String, CategoryNode> getChildren() {
        return children;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MasterNode:\n");
        for (Map.Entry<String, CategoryNode> entry : children.entrySet()) {
            sb.append(entry.getValue().toString(1));  // Start with indentation level 1
        }
        return sb.toString();
    }
    
}
