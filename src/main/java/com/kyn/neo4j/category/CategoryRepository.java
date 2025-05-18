package com.kyn.neo4j.category;

import java.util.UUID;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CategoryRepository extends ReactiveNeo4jRepository<Category, UUID> {
    
    @Query("MATCH (c:Category) WHERE c.name = $name RETURN c")
    Mono<Category> findByName(String name);

    @Query("MATCH (c:Category) WHERE c.elementId = $elementId RETURN c")
    Mono<Category> findByElementId(String elementId);

    @Query("MATCH (c:Category {name: $name}) " +
           "OPTIONAL MATCH (c)-[r:SUBCATEGORY]->(p:Category) " +
           "RETURN c, collect(r), collect(p)")
    Mono<Category> findByNameWithParent(String name);

    @Query("MATCH (c:Category {name: $name}) " +
           "OPTIONAL MATCH (c)<-[r:SUBCATEGORY]-(p:Category) " +
           "RETURN c, collect(r), collect(p)")
    Mono<Category> findByNameWithChildren(String name);


    @Query("""
       MATCH (sportsOutdoors:Category {name: $name}) 
       MATCH (sportsOutdoors)-[:SUBCATEGORY*]->(descendant) 
       RETURN descendant
    """)
    Flux<Category> findAllDescendants(String name);

    @Query("""
        MATCH (root:Category {name: $rootName})
        MATCH path = (root)-[:SUBCATEGORY*1..]->(leaf:Category {name: $leafName})
        RETURN leaf
    """)
    Mono<Category> findLowestCategoryInPath(String rootName, String leafName);


    @Query("""
        MATCH p = (root:Category {name: $categoryNames[0]})
        WITH root
        UNWIND RANGE(1, size($categoryNames)-1) AS i
        MATCH (parent:Category), (child:Category {name: $categoryNames[i]})
        WHERE parent.name = $categoryNames[i-1] AND (parent)-[:SUBCATEGORY]->(child)
        WITH child
        ORDER BY i DESC
        LIMIT 1
        RETURN child
    """)
    Mono<Category> findCategoryByExactPath(String[] categoryNames);


    @Query("""
        WITH $categoryNames AS names
        MATCH (cat:Category {name: names[0]})
        WITH cat, names
        CALL {
            WITH cat, names
            UNWIND RANGE(1, size(names)-1) AS i
            WITH i, names[i-1] AS parentName, names[i] AS childName
            MATCH (parent:Category {name: parentName})-[:SUBCATEGORY]->(child:Category {name: childName})
            RETURN child, i
            ORDER BY i DESC
            LIMIT 1
        }
        RETURN child AS category
    """)
    Mono<Category> findCategoryByPath(String[] categoryNames);


    @Query("""
        WITH split($categoryPath, '|') AS parts
        WITH [part IN parts | trim(part)] AS names
        MATCH (root:Category {name: names[0]})
        WITH root, names
        WHERE size(names) >= 1
        RETURN CASE
            WHEN size(names) = 1 THEN root
            ELSE head([p IN apoc.path.subgraphNodes(root, {
                relationshipFilter: 'SUBCATEGORY>',
                maxLevel: size(names) - 1,
                nodeFilter: n WHERE n.name = names[size(names)-1]
            }) | p ])
        END AS category
    """)
    Mono<Category> findCategoryByPathString(String categoryPath);


}
