![Kotlin](https://img.shields.io/badge/Kotlin-2.2-purple?logo=kotlin)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-brightgreen?logo=springboot)
![alt text](https://img.shields.io/badge/TDD-Applied-success)
![Apple](https://img.shields.io/badge/Apple%20RSS-Reviews-lightgrey?logo=apple)
![OpenAI](https://img.shields.io/badge/OpenAI-API-black)

# Review Insight API (Kotlin Version)

A Spring Boot API designed as a SaaS-ready service to transform unstructured mobile app reviews into actionable business intelligence using LLMs (OpenAI).

## Overview

- Fetches app reviews from Apple RSS
- Normalizes and processes review data
- Classifies sentiment (positive, neutral, negative)
- Uses OpenAI to extract insights:
    - Summary
    - Top problems (with examples)

## Tech Stack

- Kotlin 2.2
- Spring Boot 4.0
- WebClient (HTTP client)
- JUnit 5 and Mockk
- OpenAI API

## Running the Application

### 1. Mock Mode (Fast Track)

By default, the app is "Safe by Default". You can run it without any API Keys using the built-in mock providers:


```
# application.yml

app:
  mock-mode: true # Uses local JSON mocks for Apple and OpenAI
```
```
./mvnw spring-boot:run
```

### 2. Real Integration

To use real data, set your OpenAI key and disable mock mode:

```
export OPENAI_API_KEY=your_key_here
```
```
./mvnw spring-boot:run
```

The API will be available at http://localhost:8080

## TDD Strategy

This project follows TDD cycle for core logic:

- Domain & Application Layers: Services, Mappers, and Business Rules.
- Contract Testing: Verified JSON-to-DTO mapping using @JsonTest to ensure integration integrity with external APIs.

## Testing the API

```
POST /v1/insights
```

#### Request Body

```json
{
  "appId": "1052238659",
  "country": "gb",
  "pages": 1
}
```

A [Postman collection](https://github.com/franciscoreinalondon/review-insight-api-kt/tree/main/postman)
is included in this repository.

## OpenAI Integration

- Builds a prompt using reviews
- Sends request via WebClient
- Parses structured response
- Extracts:
    - Summary
    - Top 3 problems with examples

## Project Status

This project is currently under active development.

#### Future improvements:

- Asynchronous optimization (using suspend functions and awaitBody())
- Resilience Patterns (implement circuit breakers, retriers, fallbacks)
- Add Integration tests and increase coverage
- Observability and logging improvements (Splunk)
- Multi-platform support (Google Play)
