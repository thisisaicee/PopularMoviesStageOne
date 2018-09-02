# PopularMoviesStageOne

## Project Overview
Popular Movies App is part of the project required to be completed for Udacity Android Intermediate Nanodegree Course

## Common Features & Specifications

- App is written solely in the Java Programming Language.
- Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.
- UI contains an element (i.e a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.
- UI contains a screen for displaying the details for a selected movie.
- Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.
- App utilizes stable release versions of all libraries, Gradle, and Android Studio.

## User Interface - Function
- When a user changes the sort criteria (“most popular and highest rated”) the main view gets updated correctly.
- When a movie poster thumbnail is selected, the movie details screen is launched.

## Network API Implementation 
- In a background thread implemented in both the MainActivity and DetailsActivity, the app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.
