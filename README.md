
# 🧠 MyAssessmentApplication

An Android app showcasing REST API integration, robust UI error handling, modular architecture, Koin-based dependency injection, MVVM patterns, and professional-grade testing.

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](#)  
[![Test Coverage](https://img.shields.io/badge/coverage-95%25-blue)](#)  
![Demo GIF](docs/demo.gif)

---

## 📚 Table of Contents

1. [Project Overview](#project-overview)  
2. [Why It Matters](#why-it-matters)  
3. [Tech Stack & Libraries](#tech-stack--libraries)  
4. [Project Structure](#project-structure)  
5. [Prerequisites](#prerequisites)  
6. [Setup & Installation](#setup--installation)  
   - [Clone the Repository](#clone-the-repository)  
   - [Branching Model](#branching-model)  
   - [Import into Android Studio](#import-into-android-studio)  
7. [Configuration Guide](#configuration-guide)  
   - [API Base URL](#api-base-url)  
   - [Auth Endpoints](#auth-endpoints)  
8. [Building & Running](#building--running)  
9. [App User Guide](#app-user-guide)  
   - [Login Flow](#login-flow)  
   - [Dashboard Screen](#dashboard-screen)  
   - [Details Screen](#details-screen)  
   - [Logout & Back Navigation](#logout--back-navigation)  
10. [Architecture & Components](#architecture--components)  
    - [Dependency Injection (Koin)](#dependency-injection-koin)  
    - [Network Layer (Retrofit + Gson)](#network-layer-retrofit--gson)  
    - [UI Layer (MVVM + LiveData)](#ui-layer-mvvm--livedata)  
    - [Shared Utilities & BaseActivity](#shared-utilities--baseactivity)  
11. [Testing Strategy](#testing-strategy)  
12. [Git Workflow & Commits](#git-workflow--commits)  
13. [Troubleshooting & FAQs](#troubleshooting--faqs)  
14. [Appendix: Directory Tree](#appendix-directory-tree)  
15. [License](#license)

---

## 1. Project Overview

This is a three-screen Android application developed for the NIT3213 final assessment. It demonstrates:

- Login authentication via REST API  
- Dynamic dashboard rendering key-value summaries  
- Detail view for selected entities  
- Koin-based dependency injection  
- MVVM architecture with LiveData and ViewModel  
- Comprehensive error handling and test coverage  

### App Flow

1. **Login Screen**  
   - Validates name and student ID  
   - Authenticates using a dynamic campus-specific endpoint  

2. **Dashboard Screen**  
   - Displays a list of entities retrieved using the `keypass` token  
   - Formats each entity with two key-value fields separated by `•`, e.g.,  
     `Name: Alice • Age: 21`  
   - Supports logout  

3. **Details Screen**  
   - Renders all key-value fields of a selected entity dynamically  
   - Supports back navigation and logout  

---

## 2. Why It Matters

- Demonstrates end-to-end REST integration with dynamic server logic  
- Showcases clean, modular Android architecture  
- Handles real-world scenarios: empty state, input errors, network issues  
- Includes robust test strategy  
- Implements clean UI using ConstraintLayout and Material Components  

---

## 3. Tech Stack & Libraries

| Layer            | Tools / Libraries                      |
|------------------|----------------------------------------|
| Language         | Kotlin                                 |
| Dependency Inject| Koin                                   |
| Architecture     | MVVM (LiveData, ViewModel)             |
| Network          | Retrofit, Gson                         |
| UI               | ConstraintLayout, RecyclerView         |
| Testing          | JUnit4, MockK, Espresso                |

---

## 4. Project Structure

```text
app/
├── adapter/                # RecyclerView Adapter
│   └── EntityAdapter.kt
├── base/                   # Shared base activity
│   └── BaseActivity.kt
├── data/
│   ├── model/              # Data classes
│   │   ├── LoginRequest.kt
│   │   ├── LoginResponse.kt
│   │   └── DashboardResponse.kt
│   └── network/            # Retrofit + API
│       ├── ApiService.kt
│       └── RetrofitClient.kt
├── di/                     # Koin modules
│   └── AppModules.kt
├── ui/
│   ├── MainActivity.kt
│   ├── DashboardActivity.kt
│   └── DetailsActivity.kt
├── util/
│   └── AppConfig.kt
├── viewmodel/
│   └── DashboardViewModel.kt

res/layout/
├── activity_main.xml
├── activity_dashboard.xml
├── activity_details.xml
└── item_entity.xml
````

---

## 5. Prerequisites

* Android Studio Arctic Fox or higher
* Kotlin 1.9+
* Gradle 8+
* Internet connection (for live API testing)

---

## 6. Setup & Installation

### 🔁 Clone the Repository

```bash
git clone https://github.com/Ronisha-Manandhar/MyAssessmentApplication.git
cd MyAssessmentApplication
```

### 🪴 Branching Model

* `main`: production-ready
* `feature/<name>`: feature branches (e.g., `feature/login-ui`, `feature/di-refactor`)

### 🧠 Import into Android Studio

1. Open Android Studio → "Open Project"
2. Navigate to `MyAssessmentApplication/`
3. Let Gradle sync; install SDKs if prompted

---

## 7. Configuration Guide

### 🔗 API Base URL

Inside `AppConfig.kt`:

```kotlin
const val BASE_URL = "https://nit3213-api-h2b3-latest.onrender.com/"
```

### 📡 Auth & Dashboard Endpoints

In `ApiService.kt`, both endpoints are dynamic:

```kotlin
@POST("{campus}/auth")
fun login(@Path("campus") campus: String, @Body request: LoginRequest): Call<LoginResponse>

@GET("dashboard/{keypass}")
fun getDashboard(@Path("keypass") keypass: String): Call<List<Map<String, String>>>
```

---

## 8. Build & Run

### 📟 Command Line

```bash
./gradlew clean assembleDebug
./gradlew testDebugUnitTest
./gradlew connectedAndroidTest
```

### 🧪 Android Studio

* Click the ▶️ button to run
* Or use Build → Build APK(s)

---

## 9. App User Guide

### 🔐 Login Flow

* Inputs: First name and Student ID (format: `sXXXXXXX`)
* Validates that both fields are filled
* Calls `performLogin()` → sends POST request to server
* On success, saves keypass and navigates to Dashboard
* Errors are shown in a visible `TextView`

### 📊 Dashboard

* Displays `keypass` at the top
* Each item in the RecyclerView shows a summary:

```kotlin
val summary = entity.entries.take(2).joinToString(" • ") { (k, v) ->
    "${k.replaceFirstChar { it.uppercase() }}: $v"
}
```

* LiveData observers detect:

  * API errors → show `tvDashboardError`
  * Empty list → hide RecyclerView
  * Click → navigate to Details screen with JSON entity

### 🔍 Details Screen

* Deserializes entity JSON
* Dynamically creates a label + content `TextView` for each key-value pair
* Uses:

```kotlin
val label = TextView(this).apply { text = key.capitalize() }
val content = TextView(this).apply { text = value }
```

* Adds each pair to a vertical `LinearLayout`
* Supports Up navigation via Manifest and toolbar
* Logout button clears keypass and resets activity stack

---

## 10. Architecture & Components

### 🔧 Koin Dependency Injection

```kotlin
val appModule = module {
    single { Prefs }
    single { RetrofitClient.instance.create(ApiService::class.java) }
    viewModel { DashboardViewModel(get(), get()) }
}
```

### 🌐 Networking

* Retrofit is configured via `RetrofitClient.kt`
* Auth and dashboard handled by `ApiService.kt`

### 📐 UI Architecture (MVVM)

* `MainActivity`: triggers login, shows errors
* `DashboardActivity`: observes `LiveData`, updates UI
* `DetailsActivity`: renders dynamic content

### 🛠 Shared Utilities

* `Prefs`: wrapper around SharedPreferences
* `BaseActivity`: common UI setup, edge-to-edge handling

---

## 11. Testing Strategy

### 🧪 Unit Tests

* `DashboardViewModelTest`: mocks API success/failure
* `EntityAdapterTest`: verifies summary formatting
* `ApiServiceTest`: uses `MockWebServer`

### 📱 Instrumentation (Espresso)

* `LoginFlowTest`: validates login flow and navigation
* `DashboardRecyclerTest`: checks correct entity display
* `DetailsNavigationTest`: validates detail view rendering
* `LogoutTest`: ensures keypass is cleared + login shown again

---

## 12. Git Workflow & Commits

### 📌 Commit Format (Conventional Commits)

```bash
feat(login): validate input & authenticate
fix(dashboard): improve summary formatting
refactor(app): extract BaseActivity & AppConfig
test(unit): add ViewModel tests
chore: cleanup Gradle & resources
```

### 🔄 Example History

* `feat(dashboard): RecyclerView summaries`
* `test(android): login and logout flow`
* `refactor(di): switch to Koin modules`

---

## 13. Troubleshooting & FAQs

| Issue                   | Resolution                                     |
| ----------------------- | ---------------------------------------------- |
| Gradle sync fails       | Ensure JDK 11+ and correct SDK installed       |
| Login returns 401/404   | Double-check `campus` path and request body    |
| Dashboard is empty      | Confirm `keypass` is correctly stored in Prefs |
| Unit tests fail         | Run `./gradlew testDebugUnitTest` after clean  |
| Instrumented tests fail | Make sure emulator is running and API responds |

---

## 14. Appendix: Directory Tree

(See [Project Structure](#project-structure) above)

---

## 15. License

This repository is for educational purposes only under the NIT3213 coursework guidelines.
© 2025 Ronisha Manandhar (sXXXXXXXXX), Victoria University

```
