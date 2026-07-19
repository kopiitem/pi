# Java Code Analysis Patterns

## Component Classification

### Controller Layer (Entry Points)
- Classes annotated with `@RestController` or `@Controller`
- Methods annotated with `@GetMapping`, `@PostMapping`, etc.
- Typically handle HTTP requests
- Export as API endpoints

### Service Layer (Business Logic)
- Classes annotated with `@Service` or `@Component`
- Methods implementing business logic
- Often call repository/external services
- Show as processing nodes

### Repository Layer (Data Access)
- Classes extending `Repository`, `CrudRepository`, `JpaRepository`
- Methods for CRUD operations
- Query annotations like `@Query`
- Link to database entities

### Entity Layer (Data Models)
- Classes annotated with `@Entity` or `@Document`
- Fields with `@Column`, `@Id` annotations
- Represent database tables
- Show relationships via `@OneToMany`, `@ManyToOne`, etc.

## Relationship Detection

### Dependency Injection
- `@Autowired` field injections → show dependencies
- Constructor injection → show required dependencies
- Field type provides dependency target

### Data Relationships
- `@OneToMany` → One entity references many of another
- `@ManyToOne` → Multiple entities reference one
- `@ManyToMany` → Bidirectional many-to-many
- `@OneToOne` → One-to-one relationships

### REST Relationships
- `@RequestMapping` on class → base path
- `@PathVariable` → path parameters
- `@RequestBody` → input type
- `@ResponseBody` → output type

## Flow Tracing

### Entry Point → Service → Repository → DB
1. Identify REST controller method
2. Trace injected services called
3. Identify repository methods called
4. Link to entity accessed

### Cross-Service Communication
1. Check for HTTP clients (`RestTemplate`, `WebClient`)
2. Identify URLs and endpoints called
3. Map to other services in system
4. Show data transformation

### Event/Message Flows
1. Look for messaging annotations (`@KafkaListener`, `@RabbitListener`)
2. Identify event publishers
3. Map event types
4. Show async flows

## Common Patterns

### MVC Pattern
- Controllers → Views (JSP, templates)
- Controllers → Services → Repositories
- Data flows through layers

### Repository Pattern
- Services don't know about database
- All data access through repositories
- Queries abstracted away

### Service Locator
- Central service registry
- Components look up dependencies
- More dynamic than DI

### Dependency Injection
- Constructor or field injection
- Spring Auto-wiring
- Explicit dependency declaration

### Builder Pattern
- Static builder methods
- Fluent API for object creation
- Often seen in configuration

### Factory Pattern
- Factory methods creating objects
- Encapsulates object creation
- Often used for complex objects

## Database Analysis

### Entity Mapping
- `@Entity` → Table name (from `@Table`)
- Field names → Column names
- `@Id` → Primary key
- `@GeneratedValue` → Auto-increment

### Relationships
- `@OneToMany(mappedBy="field")` → Foreign key
- Join table name from `@JoinTable`
- Cascade operations from `CascadeType`
- Lazy vs Eager loading strategy

### Indexes & Constraints
- `@Column(unique=true)` → Unique constraint
- `@Column(nullable=false)` → NOT NULL
- Composite keys from `@EmbeddedId`
- Check constraints in validation annotations

## API Documentation Extraction

### Endpoint Info
- Method: GET, POST, PUT, DELETE
- Path: from `@RequestMapping`, `@GetMapping`, etc.
- Parameters: from `@PathVariable`, `@RequestParam`
- Request body: from `@RequestBody` parameter
- Response: from method return type

### Input/Output Types
- Simple types: String, Integer, etc.
- Complex types: POJOs, custom classes
- Collections: List, Set, Map
- Optional types: Optional<T>

### Error Handling
- Exception classes thrown
- Error response types
- HTTP status codes in `@ExceptionHandler`
- Error message patterns

## External Integrations

### HTTP Clients
- `RestTemplate` usage
- `WebClient` (reactive)
- URLs and endpoints called
- Authentication headers

### Database Drivers
- JDBC connections
- JPA/Hibernate configuration
- Connection pooling
- Multi-database support

### Message Brokers
- Kafka topics
- RabbitMQ exchanges/queues
- Event types
- Consumer groups

### Third-Party Services
- API keys/credentials
- Endpoints accessed
- Data transformations
- Retry/fallback logic

## Code Smells & Patterns to Note

### Tight Coupling
- Direct instantiation of classes
- Hard-coded dependencies
- Circular dependencies

### Hidden Dependencies
- Reflection-based loading
- Dynamic proxy creation
- Service locator patterns

### Unmaintained Code
- TODO comments
- Deprecated methods
- Unused imports

### Complex Flows
- Multiple levels of indirection
- Callback chains
- Observable/Stream chains
