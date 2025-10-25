# Course Progress Service

A Spring Boot microservice for tracking and analyzing course progress events in an e-learning platform.

## Features

- Track course progress events (STARTED, PASSED, FAILED)
- Analyze course success rates
- RESTful API with proper validation
- In-memory H2 database for development

## API Endpoints

- `POST /v1/events` - Create a new course progress event
- `GET /v1/events/user/{userId}` - Get all events for a user
- `GET /v1/analysis/course/{courseId}` - Get course analysis with success rates

## Test Strategy

### Overview

This project implements a comprehensive testing strategy with multiple testing layers to ensure code quality, reliability, and maintainability.

### Testing Layers

#### 1. Unit Tests
- **Location**: `src/test/java/com/example/courseprogress/service/`
- **Purpose**: Test business logic in isolation
- **Tools**: JUnit 5, Mockito
- **Scope**: Service layer methods with mocked dependencies
- **Coverage Focus**: Business logic, edge cases, error conditions

**Key Unit Test Scenarios:**
- Event creation and validation
- Course analysis calculations
- Edge cases (division by zero, empty datasets)
- Boundary conditions

#### 2. Integration Tests
- **Location**: `src/test/java/com/example/courseprogress/controller/`
- **Purpose**: Test API endpoints and database interactions
- **Tools**: Spring Boot Test, MockMvc, Testcontainers (optional)
- **Scope**: Controller endpoints with real database
- **Coverage Focus**: HTTP status codes, JSON responses, data persistence

**Key Integration Test Scenarios:**
- REST API contract validation
- Database operations
- End-to-end workflow testing
- Error response formats

### Test Execution

```bash
# Run all tests
mvn test

# Run tests with coverage report
mvn clean test jacoco:report

# Run only unit tests
mvn test -Dtest=*Test

# Run only integration tests  
mvn test -Dtest=*IntegrationTest


## CI/CD Pipeline

This project uses GitHub Actions for continuous integration and deployment.

### Workflow Features:

- **Automated Testing**: Runs on every push and pull request
- **Code Coverage**: Enforces 80% coverage minimum
- **Docker Builds**: Automatically builds and pushes Docker images to GitHub Container Registry
- **Quality Gates**: Build fails if tests fail or coverage targets aren't met

### Pipeline Steps:

1. **Checkout**: Fetches the latest code
2. **JDK Setup**: Configures Java 21 environment
3. **Build**: Compiles the application with Maven
4. **Test**: Runs all unit and integration tests
5. **Coverage**: Generates and verifies code coverage reports
6. **Docker Build** (main branch only): Builds and pushes Docker image to GHCR

### Docker Image

The application is containerized and available at:

```bash
ghcr.io/your-username/course-progress-service:latest