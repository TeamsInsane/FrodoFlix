package com.frodo.frodoflix.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frodo.frodoflix.api.TMDB
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONArray

data class GenreList(
    val genre: String,
    val id: Int,
    val status: Boolean
)

data class GenresUiState(
    val genresList: List<GenreList> = emptyList()
)

class GenresViewModel: ViewModel() {
    private val _genresUiState = MutableStateFlow(GenresUiState())
    val genresUiState: StateFlow<GenresUiState> = _genresUiState.asStateFlow()

    fun loadGenresFromApi(savedGenres: List<String>) {
        viewModelScope.launch {
            val genresJSONArray: JSONArray? = TMDB.getDataFromTMDB("https://api.themoviedb.org/3/genre/movie/list?language=en", "genres") as JSONArray?

            val genresList = genresJSONArray?.let {
                (0 until it.length()).map { index ->
                    val item = it.getJSONObject(index)
                    GenreList(
                        id = item.getInt("id"),
                        genre = item.getString("name"),
                        status = false
                    )
                }
            } ?: emptyList()

            _genresUiState.update { currentState ->
                currentState.copy(genresList = genresList)
            }

            loadSavedGenres(savedGenres)
        }
    }

    fun getFavouriteGenresList(): List<String> {
        return genresUiState.value.genresList
            .filter { it.status }
            .map { it.genre }
    }

    fun toggleGenreStatus(index: Int) {
        _genresUiState.update { currentState ->
            val updatedList = currentState.genresList.toMutableList()
            val updatedGenre = updatedList[index].copy(status = !updatedList[index].status)
            updatedList[index] = updatedGenre
            currentState.copy(genresList = updatedList)
        }
    }

    fun getFavouriteGenreIds(): List<Int> {
        return genresUiState.value.genresList
            .filter { it.status }
            .map { it.id }
    }

   fun loadSavedGenres(savedGenres: List<String>) {
       _genresUiState.update { currentState ->
           val updatedGenresList = currentState.genresList.map { genreItem ->
               genreItem.copy(status = savedGenres.contains(genreItem.genre))
           }

           currentState.copy(genresList = updatedGenresList)
       }
   }
}
