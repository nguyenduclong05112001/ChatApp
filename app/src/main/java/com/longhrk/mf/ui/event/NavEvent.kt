package com.longhrk.mf.ui.event

import com.longhrk.mf.ui.NavTarget

sealed class NavEvent {
    object None : NavEvent()
    class NavigateUp : NavEvent() {
        override fun equals(other: Any?): Boolean {
            return this === other
        }

        override fun hashCode(): Int {
            return System.identityHashCode(this)
        }
    }

    class Action(val target: NavTarget): NavEvent(){
        override fun toString(): String {
            return super.toString() + "($target)"
        }
    }

    class ActionWithValue(val target: NavTarget, val value: Pair<String, String>) : NavEvent() {
        override fun toString(): String {
            return super.toString() + "($target,$value)"
        }
    }

    class ActionWithPopUp(
        val target: NavTarget,
        val popupTarget: NavTarget,
        val inclusive: Boolean = true
    ) : NavEvent() {
        override fun toString(): String {
            return super.toString() + "($target,$popupTarget)"
        }
    }

    class ActionInclusive(val target: NavTarget, val inclusiveTarget: NavTarget) : NavEvent() {
        override fun toString(): String {
            return super.toString() + "($target,$inclusiveTarget)"
        }
    }

    class PopBackStackWithTarget(val target: NavTarget, val inclusive: Boolean = false) :
        NavEvent() {
        override fun toString(): String {
            return super.toString() + "($target,$inclusive)"
        }
    }

    class PopBackStack(val inclusive: Boolean = false) : NavEvent()
}