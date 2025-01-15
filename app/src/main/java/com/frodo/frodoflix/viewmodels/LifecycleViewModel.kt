package com.frodo.frodoflix.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LifecycleViewModel : ViewModel() {
    private val _appStatus = MutableStateFlow("status")
    val appStatus: StateFlow<String> get() = _appStatus

    fun setAppStatus(status: String) {
        _appStatus.value = status
    }
}
