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
import com.kyn.neo4j.category.MasterNode;
import com.kyn.neo4j.common.service.DataInsertServiceImpl;
import com.kyn.neo4j.product.ProductData;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class initConfig {

    private final DataInsertServiceImpl dataInsertService;
	private final ObjectMapper objectMapper;
	
    public initConfig(DataInsertServiceImpl dataInsertService) {
        this.dataInsertService = dataInsertService;
        this.objectMapper = new ObjectMapper();
	}
    
	@Bean
	CommandLineRunner command(DataInsertServiceImpl dataInsertService) {
		return args -> {
			log.info("Starting category import process");
			//delete all products and categories
			dataInsertService.deleteAllProducts()
				.then(convertLineToProduct()) //get product data to list
				.flatMap(productDataList -> Mono.zip(
					createCategoryTree(productDataList), //createTree
					Mono.just(productDataList)
				))
				.flatMap(tuple -> dataInsertService
					.createCategoryHierarchyFromMasterNode(tuple.getT1()) //createHierarchy
					.then(insertProducts(tuple.getT2())))
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


	private Mono<MasterNode> createCategoryTree(List<ProductData> productDataList) {
		MasterNode masterNode = new MasterNode();
		return Flux.fromIterable(productDataList)
			.doOnNext(data -> masterNode.addCategoryPath(data.getCategoryString()))
			.doOnComplete(() -> log.info("createCategoryTree - Flux.fromIterable COMPLETED"))
			.doOnError(e -> log.error("createCategoryTree - Error in Flux.fromIterable chain", e))
			.then(Mono.just(masterNode));
	}



	private Mono<Void> insertProducts(List<ProductData> productDataListParam) {
		if (productDataListParam.isEmpty()) {
			log.info("Product data list is empty for insertProducts, returning Mono.empty().");
			return Mono.empty();
		}
		log.info("start insertProducts ");
		return Flux.fromIterable(productDataListParam)
			.flatMap(product -> 
			dataInsertService.insertProducts(product),5)
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
