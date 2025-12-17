# FrodoFlix Project Analysis

This document provides an overview of the FrodoFlix Android application's architecture and key components. The application is a movie rating app that allows users to search for movies, view details, maintain personal movie lists, and rate movies.

## Key Components

### 1. `MainActivity.kt`

The entry point of the application. It is responsible for:

*   **UI Setup**: Initializes the user interface using Jetpack Compose.
*   **Navigation**: Manages navigation between different screens of the app using a `NavHostController`. The app's navigation graph is defined here, connecting various composable screens.
*   **ViewModel Integration**: It creates and uses a `SharedViewModel` to share data across different screens.
*   **Lifecycle Management**: It handles Android activity lifecycle events, for example, to display a "Welcome back" message when the app resumes.

### 2. `SharedViewModel.kt`

This is a crucial component of the app's architecture. It serves as a central data holder and business logic handler for the entire application. Its responsibilities include:

*   **User Authentication**: Manages user login, registration, and session persistence using `SharedPreferences`.
*   **Data Management**: Holds and manages all application data, including movie information, user preferences, and lists (favorites, watchlist, watched).
*   **Database Interaction**: Communicates with `FrodoDatabase` to perform CRUD operations on user and movie data.
*   **UI State Management**: Manages the state of the UI, such as the current theme (dark or light mode), and exposes it to the composable functions.
*   **API Interaction**: It indirectly interacts with the TMDB API through other components to fetch movie data.

### 3. `api/TMDB.kt`

This file contains the networking client for The Movie Database (TMDB) API. Its main functions are:

*   **API Request Handling**: It uses `OkHttpClient` to send HTTP requests to the TMDB API.
*   **Authentication**: It retrieves the TMDB API key from an `env` file to authenticate the requests.
*   **Data Fetching**: The `getDataFromTMDB` function provides a generic way to fetch various types of movie-related data from the API, such as movie lists, details, and search results.

### 4. `database/FrodoDatabase.kt`

This class encapsulates all interactions with the application's backend database, for which Firebase is used. Its key responsibilities are:

*   **User Data Persistence**: Stores and retrieves user information, including usernames, hashed passwords, and email addresses.
*   **Movie List Management**: Manages user-specific movie lists, such as favorites, watchlist, and watched movies.
*   **Rating System**: Stores and retrieves movie ratings provided by users.
*   **Group Management**: Handles the creation, joining, and searching of groups.

### 5. `screens/` Directory

This directory contains all the composable functions that represent the different screens of the application. Each file in this directory corresponds to a specific screen, such as:

*   **`MainPage.kt`**: The home screen of the application.
*   **`SearchPage.kt`**: The screen where users can search for movies.
*   **`Profile.kt`**: The user's profile screen.
*   **`LoginPage.kt` and `RegisterPage.kt`**: Screens for user authentication.
*   **`DisplayMoviePage.kt`**: The screen that displays the details of a selected movie.
*   **`RateMovie.kt`**: A screen for users to rate movies.
*   **`GroupPage.kt`**: Displays a list of the user's groups and provides buttons to join or create new groups.
*   **`CreateGroup.kt`**: A screen with input fields for group ID, name, and description, and a button to create a new group.
*   **`JoinGroup.kt`**: A screen with a search bar to find and join existing groups.
*   **`ChatPage.kt`**: A screen for group messages that contains previous messages and an input for sending new messages.
*   And various other screens for managing user lists and settings.

## Application Flow

1.  The app starts with `MainActivity`, which sets up the navigation and initializes the `SharedViewModel`.
2.  The user is initially directed to the `LoginPage` or `RegisterPage`.
3.  Upon successful login, the `SharedViewModel` fetches user data from `FrodoDatabase` and navigates the user to the `home_page`.
4.  From the home page, the user can navigate to different screens like `SearchPage`, `Profile`, and access their movie lists.
5.  When a user performs an action, such as adding a movie to their watchlist, the action is handled by the `SharedViewModel`, which updates the data in `FrodoDatabase` and the UI state.
6.  The `TMDB.kt` client is used to fetch movie data from the TMDB API whenever needed, for example, when the user searches for a movie or views movie details.