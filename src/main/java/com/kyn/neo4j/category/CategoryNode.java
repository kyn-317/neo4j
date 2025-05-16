package com.kyn.neo4j.category;

import java.util.HashMap;
import java.util.Map;

public class CategoryNode {
    String name;
    Map<String, CategoryNode> children = new HashMap<>();
}
