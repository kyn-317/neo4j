package com.kyn.neo4j;


import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyn.neo4j.common.ProductInsertService;
import com.kyn.neo4j.product.ProductData;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class initConfig {

    private final ProductInsertService productInsertService;
	private final ObjectMapper objectMapper;
	
    public initConfig(ProductInsertService productInsertService) {
        this.productInsertService = productInsertService;
        this.objectMapper = new ObjectMapper();
	}
    
	@Bean
	CommandLineRunner command(ProductInsertService productInsertService) {
		return args -> {
			log.info("Starting category import process");
			//delete all products and categories
			productInsertService.deleteAllProducts()
				.then(convertLineToProduct()) //get product data to list
				.flatMap(this::createCategoryTree) //create category tree
				.flatMap(productDataList ->
					productInsertService.createCategoryHierarchyFromMasterNode() //create category hierarchy
					.then(insertProducts(productDataList))) //insert products
				.subscribe();
		};
	}

	private Mono<List<ProductData>> convertLineToProduct() {
		try {
			ClassPathResource resource = new ClassPathResource("product.jsonl");
			Path filePath = Path.of(resource.getURI());

			return Flux.usingWhen(
					Mono.fromCallable(() -> Files.lines(filePath)),
					stream -> Flux.fromStream(stream)
									.map(this::convertLineToProduct),
					stream -> Mono.fromRunnable(() -> {
						log.info("Closing file stream in convertLineToProduct.");
						stream.close();
					}),
					(stream, err) -> Mono.fromRunnable(() -> {
						log.warn("Closing file stream in convertLineToProduct due to error: {}", err.getMessage());
						stream.close();
					}),
					stream -> Mono.fromRunnable(() -> {
						log.info("Closing file stream in convertLineToProduct due to cancellation.");
						stream.close();
					})
				)
				.filter(productData -> productData != null)
				.collectList()
				.map(objectList -> {
					List<ProductData> typedList = new ArrayList<>(objectList.size());
					for (Object obj : objectList) {
						typedList.add((ProductData) obj);
					}
					return typedList;
				})
				.doOnError(e -> log.error("Error in convertLineToProduct stream processing", e));

		} catch (Exception e) {
			log.error("Failed to initialize file reading in convertLineToProduct", e);
			return Mono.error(e);
		}
	}


	private Mono<List<ProductData>> createCategoryTree(List<ProductData> productDataList) {
		return Flux.fromIterable(productDataList)
			.flatMap(product ->  productInsertService.createTreePath(product.getCategoryString()))
			.doOnComplete(() -> log.info("createCategoryTree - Flux.fromIterable COMPLETED"))
			.doOnError(e -> log.error("createCategoryTree - Error in Flux.fromIterable chain", e))
			.then(Mono.just(productDataList));
	}



	private Mono<Void> insertProducts(List<ProductData> productDataListParam) {
		if (productDataListParam.isEmpty()) {
			log.info("Product data list is empty for insertProducts, returning Mono.empty().");
			return Mono.empty();
		}
		log.info("start insertProducts ");
		return Flux.fromIterable(productDataListParam)
			.flatMap(product -> 
				productInsertService.insertProducts(product),1)
			.doOnComplete(() -> log.info("insertProducts - Flux.fromIterable COMPLETED"))
			.doOnError(e -> log.error("insertProducts - Error in Flux.fromIterable chain", e))
			.then();
	}


	

	/**
	 * convert one line of JSONL file to ProductBasEntity
	 */
	private ProductData convertLineToProduct(String line) {
		try {
			return mapJsonToProduct(objectMapper.readTree(line));
		} catch (Exception e) {
			log.error("Failed to parse line: {}", line, e);
			return null;
		}
	}
	
	/**
	 * map JsonNode's fields to ProductBasEntity
	 */
	private ProductData mapJsonToProduct(JsonNode jsonNode) {
		ProductData product = new ProductData();
		if (jsonNode.has("Product Name")) {
			product.setProductName(jsonNode.get("Product Name").asText());
		}
		
		if (jsonNode.has("Category")) {
			product.setCategoryString(jsonNode.get("Category").asText());
		}
		
		if (jsonNode.has("Description")) {
			product.setDescription(jsonNode.get("Description").asText());
		}
		
		if (jsonNode.has("Product Specification")) {
			product.setSpecification(jsonNode.get("Product Specification").asText());
		}
		
		if (jsonNode.has("Selling Price")) {
			String price = String.valueOf(jsonNode.get("Selling Price").asDouble());
			product.setPrice(Double.parseDouble(price));
		}
		
		if (jsonNode.has("Image")) {
			product.setImage(jsonNode.get("Image").asText());
		}
		return product;
	}
}
