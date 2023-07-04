package com.longhrk.mf.ui

import com.longhrk.mf.ui.event.NavEvent
import com.longhrk.mf.ui.viewmodel.NavViewModel

class EventHandler(
    private val navigationViewModel: NavViewModel
) {
    fun postNavEvent(event: NavEvent){
        navigationViewModel.updateEvent(event)
    }

    fun navEvent() = navigationViewModel.event

    override fun toString(): String {
        return navEvent().value.toString()
    }
}