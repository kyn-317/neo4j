# Neo4j Reactive Demo Project

A Spring Boot application that demonstrates the use of reactive programming with Neo4j to create and manage hierarchical category structures and products.

## Overview

This demo project showcases how to efficiently use Spring Data Neo4j's reactive capabilities to:

1. Read product data from a JSONL file
2. Create a hierarchical category tree structure in Neo4j
3. Connect products to their appropriate categories
4. Persist all data in a Neo4j database using reactive patterns

## Tech Stack

- Spring Boot 3.4.5


- Spring Data Neo4j (Reactive)
- Project Reactor (Flux/Mono)
- Neo4j Graph Database
- Java 21

## Project Structure

- `Neo4jApplication.java` - Spring Boot application entry point
- `ReactiveNeo4jConfig.java` - Neo4j connection configuration
- `initConfig.java` - Initialization configuration that handles data import
- `category/` - Category-related domain models and repositories
- `product/` - Product-related domain models and repositories
- `common/` - Shared utilities and services including ProductInsertService
- `resources/product.jsonl` - Source data file containing product information

## How It Works

The application follows a sequential but reactive process:

1. **Data Loading Phase**:
   - Reads `product.jsonl` file line by line using reactive streams
   - Converts each line to a `ProductData` object
   - Temporarily stores these objects in memory

2. **Category Tree Creation**:
   - Extracts category paths from each product (e.g., "Electronics|Computers|Laptops")
   - Builds a hierarchical tree structure from these paths
   - Persists the category nodes and their relationships in Neo4j
   - Uses reactive Neo4j operations to ensure non-blocking I/O

3. **Product Creation and Association**:
   - For each product, finds its corresponding leaf category
   - Creates the product in Neo4j
   - Establishes a relationship between the product and its category
   - All operations are performed reactively to maximize throughput

## Features

- **Non-blocking I/O**: Uses reactive programming principles throughout
- **Batch Processing**: Efficiently processes data in a streaming fashion
- **Hierarchical Data Modeling**: Demonstrates how to model and query hierarchical data in Neo4j
- **Optimized Neo4j Queries**: Uses Cypher queries optimized for category path lookups
- **Resource Management**: Proper handling of file resources with reactive patterns

## Running the Project

1. Ensure you have a Neo4j instance running
2. Configure your Neo4j connection in `application.properties` or environment variables
3. Place your product data in `src/main/resources/product.jsonl`
4. Run the application: `./mvnw spring-boot:run`

## Data Format

The `product.jsonl` file should contain product data in JSON format, one product per line. Example:

```json
{"Product Name": "Wireless Headphones", "Category": "Electronics|Audio|Headphones", "Description": "High-quality wireless headphones", "Selling Price": 99.99, "Product Specification": "Bluetooth 5.0|40h battery life", "Image": "headphones.jpg"}
```

Category strings use the pipe character (`|`) as a delimiter to indicate the hierarchical path.

## Notes

- The project uses UUID for entity identification
- Category nodes are linked with `SUBCATEGORY` relationships
- Products are linked to categories with `BELONGS_TO` relationships

## License

[MIT](LICENSE) 