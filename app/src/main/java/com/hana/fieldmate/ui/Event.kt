package com.hana.fieldmate.ui

sealed class Event {
    data class NavigateTo(val destination: String) : Event()
    object NavigateUp : Event()
    data class NavigatePopUpTo(
        val destination: String,
        val popUpDestination: String,
        val inclusive: Boolean = false,
        val launchOnSingleTop: Boolean = false
    ) : Event()

    data class Dialog(
        val dialog: DialogState,
        val action: DialogAction,
        val description: String = ""
    ) : Event()
}

enum class DialogState {
    AddEdit, Confirm, Delete, TimeOut, PhotoPick, Select, Error, Image, JwtExpired
}

enum class DialogAction {
    Open, Close
}