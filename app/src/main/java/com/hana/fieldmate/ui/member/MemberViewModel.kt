package com.hana.fieldmate.ui.member

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hana.fieldmate.R
import com.hana.fieldmate.data.local.fakeMemberDataSource
import com.hana.fieldmate.domain.model.MemberEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MemberUiState(
    val memberEntity: MemberEntity = MemberEntity(
        0L,
        R.drawable.ic_member_profile,
        "",
        "",
        "",
        "",
        ""
    )
)

class MemberViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(MemberUiState())
    val uiState: StateFlow<MemberUiState> = _uiState.asStateFlow()

    init {
        val id: Long = savedStateHandle["memberId"]!!
        if (id != -1L) loadMember(id)
    }

    fun loadMember(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(memberEntity = fakeMemberDataSource[id.toInt()]) }
        }
    }
}