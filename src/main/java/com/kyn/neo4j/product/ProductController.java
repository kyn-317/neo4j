package com.kyn.neo4j.product;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kyn.neo4j.category.Category;
import com.kyn.neo4j.service.ProductInsertService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;



@RestController
@Slf4j
@RequestMapping("product")
public class ProductController {
    private final ProductInsertService productInsertService;

    public ProductController(ProductInsertService productInsertService) {
        this.productInsertService = productInsertService;
    }

 /*    @PostMapping
    public Mono<Product> postMethodName(@RequestBody ProductData entity) {
        return productInsertService.createProduct(entity);
        
    } */
    
    @GetMapping("/treePath")
    public Mono<String> getTreePath(){
        return productInsertService.createTreePathFromMasterNode();
    }

    @GetMapping("/categoryHierarchy")
    public Mono<Void> getCategoryHierarchy(){
        return productInsertService.createCategoryHierarchyFromMasterNode();
    }

    @PostMapping("/getCategoryByString")
    public Mono<Category> getCategoryByCategoryString(@RequestBody categoryStringBody categoryStringBody){
        log.info("Getting category by category string: {}", categoryStringBody);
        return productInsertService.getCategoryByCategoryString(categoryStringBody.categoryString());
    }


    @PostMapping("/insertProducts")
    public Mono<ProductData> insertProducts(@RequestBody ProductData productData){
        log.info("Inserting products: {}", productData);
        return productInsertService.insertProductsP(productData);

    }

    private record categoryStringBody(String categoryString){}
}
