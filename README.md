# Kotlin Multiplatform Demo Project

This project is a Kotlin Multiplatform application targeting Android, iOS, and Backend. It demonstrates sharing business logic and UI code across platforms, with features for extracting text from images and generating jokes using AI (not connected with UI yet).

## Features
- Extract text from images using AI (OpenAI integration)
- Upload images to AWS S3
- Generate and serve jokes in multiple languages
- Compose Multiplatform UI for Android and iOS
- REST API server (Spring Boot, Ktor)
- Redis-backed joke storage and user tracking

## Project Structure
- `/composeApp` — Shared Compose Multiplatform UI and logic
- `/iosApp` — iOS entry point (SwiftUI integration)
- `/server` — Spring Boot server with REST APIs
- `/shared` — Shared business logic and models

## Main Dependencies
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Ktor](https://ktor.io/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [AWS S3 SDK](https://github.com/aws/aws-sdk-java-v2)
- [OpenAI (Spring AI)](https://github.com/spring-projects/spring-ai)
- [Redis](https://redis.io/)
- [Peekaboo Image Picker](https://github.com/onseok/peekaboo)
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)

## Build & Run

### Prerequisites
- JDK 17+
- Android Studio (for Android/iOS)
- Xcode (for iOS)
- Docker (for Redis, optional)

### Environment Configuration

Configuration is set differently for each platform:

- **Server**: Uses environment variables or `application.properties`/`application-test.properties` in `server/src/main/resources` and `server/src/test/resources`.
- **Android**: Uses `local.properties` in the project root for build-time variables (e.g., `AUTH_USERNAME`, `AUTH_PASSWORD`, `SERVER_HOST`).
- **iOS**: Set environment variables or use Xcode build settings (e.g., in `Config.xcconfig` or via Xcode scheme environment variables).

## Testing
To be completed

## API Endpoints
- `POST /api/extract/text-from-image?fileId=...` — Extract text from an image in S3
- `POST /s3/pre-signed-url?filename=...` — Get a pre-signed S3 upload URL
- `GET /api/joke?userId=...&lang=...` — Get a joke for a user

### Architecture & Communication

The project consists of three main components—Android, iOS, and Backend (Server)—which interact as follows:

- **Android & iOS Apps**: Both use shared business logic and UI code from the `composeApp` and `shared` modules. They provide a native user interface (Compose for Android, SwiftUI for iOS) and interact with the backend via HTTP APIs.

- **Backend (Server)**: Implemented with Spring Boot and Ktor, the backend exposes REST API endpoints for:
  - Uploading images (to AWS S3 via pre-signed URLs)
  - Extracting text from images (using OpenAI via Spring AI)
  - Serving jokes and managing user state (with Redis)

- **Communication**: 
  - The mobile apps (Android/iOS) communicate with the backend exclusively via HTTP(S) REST API calls.
  - Image uploads are performed by first requesting a pre-signed S3 URL from the backend, then uploading the image directly to S3.
  - After upload, the app requests text extraction by passing the S3 file ID to the backend, which processes the image and returns the extracted text.
  - Jokes are requested from the backend using a user ID and language parameter.

All data exchange between mobile clients and the backend is done using JSON over HTTP.