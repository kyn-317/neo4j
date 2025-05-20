package com.kyn.neo4j.category;

import java.util.UUID;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.kyn.neo4j.common.dto.ProductGraph;
import com.kyn.neo4j.common.dto.ProductWithCategory;

import reactor.core.publisher.Mono;

@Repository
public interface CategoryRepository extends ReactiveNeo4jRepository<Category, UUID> {
    // find category by category String and merge with product
    @Query("""
        WITH split($categoryString, '|') AS parts
        WITH [part IN parts | trim(part)] AS names
        MATCH (root:Category {name: names[0]})
        CALL (root, names) {
            MATCH path = (root)-[:SUBCATEGORY*0..]->(lastCategory:Category)
            WHERE lastCategory.name = names[size(names)-1]
            WITH path, nodes(path) AS nodes, lastCategory
            WHERE size(nodes) = size(names)
            AND ALL(i IN RANGE(0, size(names)-1) WHERE nodes[i].name = names[i])
            RETURN lastCategory AS exactCategory
            LIMIT 1
        }
        MERGE (product:Product {id: $productId})
        ON CREATE SET 
          product.name = $name,
          product.description = $description,
          product.sellingPrice = CASE WHEN $sellingPrice IS NOT NULL THEN $sellingPrice ELSE 0.0 END,
          product.productSpecification = $productSpecification,
          product.image = $image,
          product.categoryString = $categoryString
        ON MATCH SET
          product.name = $name,
          product.description = $description,
          product.sellingPrice = CASE WHEN $sellingPrice IS NOT NULL THEN $sellingPrice ELSE product.sellingPrice END,
          product.productSpecification = $productSpecification,
          product.image = $image,
          product.categoryString = $categoryString
          MERGE (product)-[:BELONGS_TO]->(exactCategory)
          RETURN product, exactCategory
    """)
    Mono<ProductWithCategory> saveProductWithExactCategory(
        UUID productId, 
        String name, 
        String description, 
        Double sellingPrice,
        String productSpecification,
        String image,
        String categoryString
    );

    @Query("""
        WITH split($categoryString, '|') AS parts
        WITH [part IN parts | trim(part)] AS names
        MATCH (root:Category {name: names[0]}) 
        CALL(root, names){
            MATCH path = (root)-[:SUBCATEGORY*0..]->(lastCategoryNode:Category) 
            WHERE lastCategoryNode.name = names[size(names)-1] 
            WITH path, nodes(path) AS pathNodes, lastCategoryNode
            WHERE size(pathNodes) = size(names) 
                AND ALL(i IN RANGE(0, size(names)-1) WHERE pathNodes[i].name = names[i])
            RETURN lastCategoryNode AS foundExactCategory, pathNodes AS fullPathNodes
            LIMIT 1
        }
        WITH foundExactCategory,fullPathNodes
        WHERE foundExactCategory IS NOT NULL
        MATCH (foundExactCategory)-[:SUBCATEGORY*0..]->(relevantCategory:Category) 
        OPTIONAL MATCH (product:Product)-[:BELONGS_TO]->(relevantCategory)
        RETURN
            collect(DISTINCT relevantCategory) AS categories,
            collect(DISTINCT product) AS products
    """)
    Mono<ProductGraph> findProductsByCategoryString(String categoryString);
}
