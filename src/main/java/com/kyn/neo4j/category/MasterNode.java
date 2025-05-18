package com.kyn.neo4j.category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MasterNode {
    private Map<String,CategoryNode> children = new HashMap<>();
    private List<String[]> nodeAndParent = new ArrayList<>();

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
    

    private void traverseNode(CategoryNode node, String parentName) {
        
        // Add current node with its parent
        if (parentName != null) {
            this.nodeAndParent.add(new String[]{node.name, parentName});
        }
        
        // Recursively traverse all children
        for (Map.Entry<String, CategoryNode> child : node.children.entrySet()) {
            traverseNode(child.getValue(), node.name);
        }
    }

    public void traverse() {
        log.info("Traversing Start");
        for(Map.Entry<String, CategoryNode> entry : this.children.entrySet()){
            this.nodeAndParent.add(new String[]{entry.getValue().name, null});
            traverseNode(entry.getValue(), null);
        }
        log.info("Traversing End");
        
    }

    public String toStringNodeAndParent(){
        return this.nodeAndParent.stream().map(Arrays::toString).collect(Collectors.joining(", "));
    }
}
