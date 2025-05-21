package com.kyn.neo4j.category.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class MasterNode {
    private Map<String,CategoryNode> children = new HashMap<>();

    //add category path to master node
    public void addCategoryPath(String categoryPath) {
        List<String> categories = Arrays.stream(categoryPath.split("\\|"))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toList());

        if (categories.isEmpty()) return;

        CategoryNode current = children
            .computeIfAbsent(categories.get(0), getCategoryNode());

        categories.stream()
            .skip(1)
            .reduce(current, 
                (parent, cat) -> parent.children
                    .computeIfAbsent(cat, getCategoryNode()),
                (a, b) -> b);  
    }
    
    private Function<String, CategoryNode> getCategoryNode(){
        return  root -> {
                CategoryNode node = new CategoryNode();
                node.name = root;
                return node;
            };
    }

    //print from master node to all children nodes
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
