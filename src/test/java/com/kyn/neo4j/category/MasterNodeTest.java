package com.kyn.neo4j.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MasterNodeTest {
    private MasterNode masterNode;

    @BeforeEach
    void setUp() {
        masterNode = new MasterNode();
    }

    @Test
    void testAddSingleCategoryPath() {
        // Given
        String categoryPath = "Toys & Games/Arts & Crafts/Craft Kits/Paper Craft";

        // When
        masterNode.addCategoryPath(categoryPath);

        // Then
        String expected = "MasterNode:\n" +
                         "  - Toys & Games\n" +
                         "    - Arts & Crafts\n" +
                         "      - Craft Kits\n" +
                         "        - Paper Craft\n";
        System.out.println(masterNode.toString());
        assertEquals(expected, masterNode.toString());
    }

    @Test
    void testAddMultipleCategoryPaths() {
        // Given
        String categoryPath1 = "Toys & Games/Arts & Crafts/Craft Kits/Paper Craft";
        String categoryPath2 = "Toys & Games/Arts & Crafts/Craft Kits/Knitting";
        String categoryPath3 = "Toys & Games/Games & Accessories/Board Games";
        String categoryPath4 = "Video Games/Xbox One";

        // When
        masterNode.addCategoryPath(categoryPath1);
        masterNode.addCategoryPath(categoryPath2);
        masterNode.addCategoryPath(categoryPath3);
        masterNode.addCategoryPath(categoryPath4);

        // Then
        String expected =
                         "MasterNode:\n" +
                         "  - Video Games\n" +
                         "    - Xbox One\n"+
                         "  - Toys & Games\n" +
                         "    - Arts & Crafts\n" +
                         "      - Craft Kits\n" +
                         "        - Paper Craft\n" +
                         "        - Knitting\n" +
                         "    - Games & Accessories\n" +
                         "      - Board Games\n" ;


                         System.out.println(masterNode.toString());
        assertEquals(expected, masterNode.toString());
    }

    @Test
    void testAddEmptyCategoryPath() {
        // Given
        String emptyPath = "";

        // When
        masterNode.addCategoryPath(emptyPath);

        // Then
        String expected = "MasterNode:\n"
                        +"  - \n";
        assertEquals(expected, masterNode.toString());
    }

    @Test
    void testAddDuplicateCategoryPath() {
        // Given
        String categoryPath = "Toys & Games/Arts & Crafts";
        
        // When
        masterNode.addCategoryPath(categoryPath);
        masterNode.addCategoryPath(categoryPath);  // Add same path again

        // Then
        String expected = "MasterNode:\n" +
                         "  - Toys & Games\n" +
                         "    - Arts & Crafts\n";
        assertEquals(expected, masterNode.toString());
    }
} 