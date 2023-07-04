package com.longhrk.mf.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.longhrk.mf.ui.event.NavEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor() : ViewModel() {
    private val _event = MutableStateFlow<NavEvent>(NavEvent.None)
    val event: StateFlow<NavEvent> = _event

    fun updateEvent(navEvent: NavEvent){
        _event.value = navEvent
    }
}