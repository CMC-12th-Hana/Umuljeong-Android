package com.hana.fieldmate.ui.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.FieldMateScreen
import com.hana.fieldmate.data.ResultWrapper
import com.hana.fieldmate.domain.usecase.QuitMemberUseCase
import com.hana.fieldmate.ui.DialogAction
import com.hana.fieldmate.ui.DialogState
import com.hana.fieldmate.ui.Event
import com.hana.fieldmate.util.BAD_REQUEST_ERROR_MESSAGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WithdrawalViewModel @Inject constructor(
    private val quitMemberUseCase: QuitMemberUseCase
) : ViewModel() {
    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    fun sendEvent(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }

    fun quitMember() {
        viewModelScope.launch {
            quitMemberUseCase()
                .collect { result ->
                    if (result is ResultWrapper.Success) {
                        sendEvent(
                            Event.NavigatePopUpTo(
                                destination = FieldMateScreen.Login.name,
                                popUpDestination = FieldMateScreen.Login.name,
                                inclusive = true,
                                launchOnSingleTop = true
                            )
                        )
                    } else if (result is ResultWrapper.Error) {
                        if (result.errorMessage != BAD_REQUEST_ERROR_MESSAGE) {
                            sendEvent(
                                Event.Dialog(
                                    DialogState.JwtExpired,
                                    DialogAction.Open,
                                    result.errorMessage
                                )
                            )
                        } else {
                            sendEvent(
                                Event.Dialog(
                                    DialogState.Error,
                                    DialogAction.Open,
                                    result.errorMessage
                                )
                            )
                        }
                    }
                }
        }
    }
}