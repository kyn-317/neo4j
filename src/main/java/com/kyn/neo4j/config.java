package com.kyn.neo4j;


import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyn.neo4j.product.ProductData;
import com.kyn.neo4j.service.ProductInsertService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class config {

    private final ProductInsertService productInsertService;
	private final ObjectMapper objectMapper;
    public config(ProductInsertService productInsertService) {
        this.productInsertService = productInsertService;
        this.objectMapper = new ObjectMapper();
    }
    
/*     @Bean
	CommandLineRunner demo(PersonService personService) {
		return args -> {
			Person greg = new Person("Greg");
			Person roy = new Person("Roy");
			Person craig = new Person("Craig");

			personService.deleteAll()
				.thenMany(
					Flux.just(greg, roy, craig)
					.flatMap(personService::save)
					.then(personService.setTeammateByName("Greg", "Roy"))
					.then(personService.setTeammateByName("Craig", "Roy"))
					.then(personService.setTeammateByName("Craig", "Chris"))
				)
				.subscribe();
		};
	} */

	@Bean
	CommandLineRunner command(ProductInsertService productInsertService) {
		return args -> {
			log.info("Starting category import process");
			productInsertService.deleteAllProducts().subscribe();
			createCategoryTree(productInsertService).subscribe();
            productInsertService.createCategoryHierarchyFromMasterNode().subscribe();
			insertProducts(productInsertService).subscribe();
		};
	}

	private Flux<Void> insertProducts(ProductInsertService productInsertService) {
		log.info("Starting category tree creation from file");
		
		try {
			ClassPathResource resource = new ClassPathResource("product.jsonl");
			Path filePath = Path.of(resource.getURI());
			
			return Flux.fromStream(Files.lines(filePath))
				.map(line -> convertLineToProduct(line, false))
				.filter(product -> product != null)
				.flatMap(productInsertService::insertProducts)
				.doFinally(signalType -> {
					try {
						log.info("Closing file stream");
						Files.lines(filePath).close();
					} catch (Exception e) {
						log.error("Failed to close file stream", e);
					}
				});
				
		} catch (Exception e) {
			log.error("Error creating category tree: {}", e.getMessage());
			return Flux.error(e);
		}
	}

	private Mono<Void> createCategoryTree(ProductInsertService productInsertService) {
		log.info("Starting category tree creation from file");
		
		try {
			ClassPathResource resource = new ClassPathResource("product.jsonl");
			Path filePath = Path.of(resource.getURI());
			
			return Flux.fromStream(Files.lines(filePath))
				.map(line -> convertLineToProduct(line, true))
				.filter(product -> product != null)
				.concatMap(product -> 
					productInsertService.createTreePath(product.getCategoryString())
						.doOnSuccess(v -> log.debug("Processed category path: {}", product.getCategoryString()))
				)
				.then()
				.doFinally(signalType -> {
					try {
						log.info("Closing file stream");
						Files.lines(filePath).close();
					} catch (Exception e) {
						log.error("Failed to close file stream", e);
					}
				});
				
		} catch (Exception e) {
			log.error("Error creating category tree: {}", e.getMessage());
			return Mono.error(e);
		}
	}
    
    /**
     * convert one line of JSONL file to ProductBasEntity
     */
    private ProductData convertLineToProduct(String line, Boolean isCategory) {
        try {
            return mapJsonToProduct(objectMapper.readTree(line), isCategory);
        } catch (Exception e) {
            log.error("Failed to parse line: {}", line, e);
            return null;
        }
    }
    
    /**
     * map JsonNode's fields to ProductBasEntity
     */
    private ProductData mapJsonToProduct(JsonNode jsonNode , boolean isCategory) {
        ProductData product = new ProductData();
        if (jsonNode.has("Product Name") && !isCategory) {
            product.setProductName(jsonNode.get("Product Name").asText());
        }
        
        if (jsonNode.has("Category")) {
            product.setCategoryString(jsonNode.get("Category").asText());
        }
        
        if (jsonNode.has("Description")&& !isCategory) {
            product.setDescription(jsonNode.get("Description").asText());
        }
        
        if (jsonNode.has("Product Specification") && !isCategory) {
            product.setSpecification(jsonNode.get("Product Specification").asText());
        }
        
        if (jsonNode.has("Selling Price") && !isCategory) {
            String price = String.valueOf(jsonNode.get("Selling Price").asDouble());
            product.setPrice(Double.parseDouble(price));
        }
        
        if (jsonNode.has("Image") && !isCategory) {
            product.setImage(jsonNode.get("Image").asText());
        }
		log.info("Product: {}", product);
        return product;
    }
}
