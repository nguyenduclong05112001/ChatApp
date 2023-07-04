package com.longhrk.mf.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MFChatViewModel @Inject constructor() : ViewModel() {
    private var _isHideFavorite = MutableStateFlow(false)
    val isHideFavorite = _isHideFavorite

    private var _isResetIndex = MutableStateFlow(false)
    val isResetIndex = _isResetIndex

    fun resetIndex(reset: Boolean){
        _isResetIndex.value = reset
    }

    fun hideFavorite(status: Boolean) {
        _isHideFavorite.value = status
    }
}