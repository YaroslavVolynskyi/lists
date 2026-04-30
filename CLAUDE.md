# Project Overview
Modern Android app using Jetpack Compose and Material 3.
Stack: Kotlin, Compose, Hilt DI, Coroutines/Flow, Room.

# Architecture Rules
- Follow Google's [Recommended App Architecture](https://android.com).
- Pattern: MVVM with Unidirectional Data Flow (UDF).
- Layers:
  - UI Layer: Composables only; ViewModels handle state and logic.
  - Domain Layer: UseCases for business logic.
  - Data Layer: Repositories as single source of truth; DataSources for API/DB.
- For navigation use Nav3, example https://github.com/android/nav3-recipes/tree/main/app/src/main/java/com/example/nav3recipes/basicparcelable  
- Strict separation between layers; maintain modularization (core, data, domain, feature modules).
- Dependency Injection: Strictly use Hilt. No manual instantiation of ViewModels or Repositories.
- Every Repository must have an interface for easy testing.

# Coding Standards
- Language: 100% Kotlin. No Java unless explicitly required for SDK compatibility.
- Async: Coroutines and Kotlin Flow only. Use `Dispatchers.IO` for data operations; never block the main thread.
- Formatting: Kotlin style guide (120-char line limit). Verify with `ktlint`.

# Compose Guidelines
- Jetpack Compose only — no XML layouts.
- Prefer stateless composables; apply state hoisting.
- UI state must be immutable.
- Use `collectAsStateWithLifecycle` for Flow.
- Use `LazyColumn` for large lists; avoid unnecessary recompositions.

# Naming Conventions
- Screens → `*Screen`
- UI state → `*UiState`
- Events → `*UiEvent`
- Repositories → `*Repository`

# Development Workflows
- Build: `./gradlew assembleDebug`
- Test: `./gradlew test` (Unit), `./gradlew connectedAndroidTest` (UI)
- Lint: `./gradlew lint` or `./gradlew detekt`

# Forbidden
- No business logic in composables.
- No direct database access in ViewModels.
- No LiveData or RxJava.
- No XML layouts.
- No Java files.