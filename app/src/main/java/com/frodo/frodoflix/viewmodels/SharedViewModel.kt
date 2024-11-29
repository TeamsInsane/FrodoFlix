package com.frodo.frodoflix.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.frodo.frodoflix.data.Movie

class SharedViewModel : ViewModel() {
    var selectedMovie: Movie? = null
    var navController: NavController? = null
}