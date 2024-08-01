package com.team.fooddelivery.presentation.viewModels.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.fooddelivery.domain.entity.user.state.ResponseUserResetPassword
import com.team.fooddelivery.domain.usecase.user.RestPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelRestPassword @Inject constructor(
    private val restPasswordUseCase: RestPasswordUseCase
) : ViewModel() {

    private var _isReset = MutableStateFlow<ResponseUserResetPassword>(
        ResponseUserResetPassword.Initial
    )
    val isReset: StateFlow<ResponseUserResetPassword> = _isReset.asStateFlow()

    fun resetPassword(email: String) {
        viewModelScope.launch {
            restPasswordUseCase.restPassword(email)
                .collect { result ->
                    _isReset.value = result
                }
        }
    }
}