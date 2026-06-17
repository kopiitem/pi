---
name: java-system-analyzer
description: Analyze Java source code and generate comprehensive system design documentation. Reverse-engineers code to extract architecture, components, APIs, databases, and system flows. Generates multiple output formats including Mermaid diagrams, Markdown documentation, JSON structure, and interactive visualizations. Use this skill whenever analyzing Java codebases, creating system architecture documentation, documenting existing systems, generating design from legacy code, or creating architecture diagrams from source code.
compatibility: Requires Java code files
---

# Java Code to System Design Analyzer

Reverse-engineer Java codebases into comprehensive system design documentation with architecture diagrams, API mappings, database schemas, and system flows.

## Overview

This skill analyzes Java source code and generates four types of design artifacts:

1. **Architecture Diagrams** - Component relationships and dependencies
2. **System Flows** - Process flows and interactions between components
3. **API/Interface Mapping** - Public interfaces, methods, and contracts
4. **Database Schema & Relationships** - Entities, schemas, and relationships

Output is generated in multiple formats for different use cases.

## Key Capabilities

### 1. Component & Architecture Analysis
- Identifies classes, packages, and modules
- Maps class hierarchies and inheritance relationships
- Detects design patterns (MVC, Service Layer, Repository, etc.)
- Creates dependency graphs between components
- Categorizes components by layer (Controller, Service, Repository, Entity)

### 2. API & Interface Extraction
- Documents public interfaces and their methods
- Extracts method signatures and parameters
- Identifies REST endpoints (if using Spring)
- Maps service contracts and dependencies
- Categorizes by functionality (CRUD, Business Logic, Utils)

### 3. Database Schema Detection
- Identifies entity classes and their relationships
- Extracts JPA/Hibernate annotations
- Maps field types and constraints
- Detects relationships (OneToMany, ManyToOne, ManyToMany)
- Generates ER diagrams

### 4. System Flow Analysis
- Traces user request flows through the system
- Identifies key service interactions
- Maps data transformations
- Documents transaction boundaries
- Highlights external system interactions

## Output Formats

### Format 1: Mermaid Diagrams
```
- Component/Dependency Graph
- ER Diagram for Database
- Sequence Diagrams for Flows
- Class Hierarchy Diagrams
```

### Format 2: Markdown Documentation
```
- Architecture Overview
- Component Descriptions
- API Documentation
- Database Documentation
- System Flow Narratives
```

### Format 3: JSON/Structured Data
```
{
  "components": [...],
  "relationships": [...],
  "apis": [...],
  "database": {...},
  "flows": [...]
}
```

### Format 4: Interactive Visualization
- HTML-based component explorer
- Clickable dependency trees
- Filterable API browser
- Zoomable ER diagrams

## How to Use This Skill

### Step 1: Provide Java Code
Supply the Java source code in one of these ways:
- Upload a folder/archive with Java files
- Paste code snippets
- Provide file paths
- Link to GitHub repository

### Step 2: Specify Analysis Scope
Indicate which aspects to focus on:
- Full system analysis (default)
- Specific modules/packages
- API layer only
- Data layer only
- Specific flows/features

### Step 3: Choose Output Format(s)
Select which formats you want:
- All formats (comprehensive)
- Specific formats based on your needs
- Multiple outputs for different audiences

### Step 4: Review & Refine
- Review generated documentation
- Request clarifications or additional analysis
- Ask for specific flow traces
- Generate alternative visualizations

## Analysis Process

### 1. Parse & Extract
- Parse Java files using AST analysis
- Extract class definitions, interfaces, methods
- Identify annotations (REST, JPA, etc.)
- Build dependency graph

### 2. Categorize Components
- **Controllers/APIs** - Entry points, REST endpoints
- **Services** - Business logic and orchestration
- **Repositories** - Data access layer
- **Entities** - Domain models and data structures
- **Utils/Helpers** - Cross-cutting concerns

### 3. Generate Artifacts
- Compile extracted information into formats
- Create diagrams from relationship data
- Generate documentation narratives
- Structure JSON output

### 4. Validate & Link
- Cross-reference components
- Resolve dependencies
- Validate relationships
- Create interactive links

## Common Use Cases

### Use Case 1: Onboarding New Developers
"Analyze this codebase and create diagrams showing how requests flow through the system"
- Generates flow diagrams
- Documents each component's role
- Shows API contracts

### Use Case 2: Architecture Documentation
"Create architecture documentation for stakeholder presentation"
- Generates comprehensive overview
- Includes Mermaid diagrams
- Provides layer-by-layer breakdown

### Use Case 3: System Migration
"Map out the current system architecture before refactoring"
- Extracts current design
- Identifies coupling points
- Highlights dependencies to consider

### Use Case 4: API Integration
"Generate API documentation from our Java service"
- Documents all endpoints
- Extracts parameter/return types
- Shows authentication requirements

### Use Case 5: Database Understanding
"Create ER diagram from our data layer"
- Maps all entities
- Shows relationships
- Identifies orphaned tables

## Tips for Best Results

1. **Provide Complete Code** - Include all relevant packages and dependencies
2. **Include Annotations** - REST, JPA, and validation annotations help analysis
3. **Specify Scope** - Focus on specific modules for clearer results
4. **Request Specific Formats** - Tailor output to your audience
5. **Ask Clarifying Questions** - Request deep-dives into specific areas

## Limitations & Notes

- Analysis is based on code structure, not runtime behavior
- Complex meta-programming or reflection may be partially visible
- External libraries are shown as dependencies but not analyzed
- Database queries (SQL) aren't analyzed, but schema structure is
- Async/concurrent flows shown but timing information not captured

## Examples

### Example 1: E-Commerce System
Input: Spring Boot e-commerce codebase
Output: Architecture showing:
- Product Service Layer
- Order Processing Flows
- Payment Integration
- Inventory Management
- Customer Data Model

### Example 2: Microservices Mesh
Input: Multiple Spring Boot services
Output: 
- Inter-service dependencies
- API contracts between services
- Shared data models
- Event flows

### Example 3: Legacy System Modernization
Input: Complex enterprise application
Output:
- Component relationships
- Dead code identification
- Refactoring opportunities
- Migration path suggestion

---

## Quick Start

1. **Upload or paste your Java code**
2. **Tell me what you want to analyze** (full system, specific module, etc.)
3. **Specify output format** (all formats, or specific ones)
4. **Review generated diagrams and documentation**
5. **Request refinements** as needed

Ready to analyze your Java codebase? Provide the code and let me generate your system design documentation!
