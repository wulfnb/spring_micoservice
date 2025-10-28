# Course Progress Service

A Spring Boot microservice for tracking and analyzing course progress events in an e-learning platform.

## Features

- Track course progress events (STARTED, PASSED, FAILED)
- Analyze course success rates
- RESTful API with proper validation
- In-memory H2 database for development
- **Data Model**: CourseProgressEvent (eventId, userId, courseId, timestamp, eventType)

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
```

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
ghcr.io/wulfnb/spring_micoservice/course-progress-service:latest
```

To run the application
```bash
docker run -p 8080:8080 ghcr.io/wulfnb/spring_micoservice/course-progress-service:latest
```

# Run for check its working
curl http://localhost:8080/v1/test

# Post data

curl -X POST http://localhost:8080/v1/events \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "courseId": "course456",
    "timestamp": "2024-01-15T10:30:00",
    "eventType": "COURSE_STARTED"
  }'


# Get user events

curl http://localhost:8080/v1/events/user/user123

# course analysis

curl http://localhost:8080/v1/analysis/course/course456

## Quality Gates

### Code Quality Tools

- **Checkstyle**: Enforces coding standards
- **PMD**: Static code analysis for potential bugs
- **SpotBugs**: Bytecode analysis for bug patterns
- **JaCoCo**: Code coverage enforcement (80% minimum)
- **SonarCloud**: Cloud-based code quality and security analysis
- **Trivy**: Vulnerability scanning for containers

### Quality Requirements

- ✅ All tests must pass
- ✅ 80% minimum code coverage
- ✅ No critical code smells (SonarCloud)
- ✅ No security vulnerabilities
- ✅ Coding standards compliance

### PR Requirements

Every pull request will automatically:

1. Run code quality checks
2. Execute all tests with coverage
3. Scan for security vulnerabilities
4. Post coverage report as PR comment
5. Require all checks to pass before merge

### SonarCloud Integration

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=wulfnb_spring_micoservice&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=wulfnb_spring_micoservice)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=wulfnb_spring_micoservice&metric=coverage)](https://sonarcloud.io/summary/new_code?id=wulfnb_spring_micoservice)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=wulfnb_spring_micoservice&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=wulfnb_spring_micoservice)


# Use of AI Tools

This project was developed with assistance from multiple AI tools to accelerate development and ensure code quality.
## AI Tools Used:

- **DeepSeek:** Primary code generation for Spring Boot structure, API endpoints, and business logic

- **Gemini 2.5 Pro:** Test case generation, CI/CD pipeline configuration, and troubleshooting

- **GitHub Copilot:** Real-time code suggestions and auto-completion during development

## Key Areas of AI Assistance:

- **Boilerplate Code:** Generated initial Spring Boot microservice structure with entities, controllers, and services

- **Test Cases:** Created comprehensive unit and integration tests with 80%+ coverage

- **CI/CD Pipeline:** Set up GitHub Actions with quality gates, code analysis, and Docker builds

- **Troubleshooting:** Resolved configuration issues with PMD, JaCoCo, and SonarCloud

## Most Helpful Prompt:

    "Create a GitHub Actions workflow for a Java Maven project that runs on PRs and main branch, includes Maven build, JaCoCo code coverage with 80% minimum, PMD, Checkstyle, SpotBugs, SonarCloud integration, Docker image building, and security scanning with Trivy. Also generate the corresponding Maven pom.xml configurations for all these tools."

## Efficiency vs Expertise:

- **AI Efficiency:** Rapid prototyping, test generation, and complex configuration setup

- **Manual Expertise:** Business logic validation, architecture decisions, and code review