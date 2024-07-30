package com.team.fooddelivery.presentation.viewModels.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.fooddelivery.domain.entity.user.state.UserFirebaseResult
import com.team.fooddelivery.domain.usecase.user.AuthEmailAndPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelRegister @Inject constructor(
    private val authEmailAndPasswordUseCase: AuthEmailAndPasswordUseCase,
) : ViewModel() {

    private var _isRegisterSuccess = MutableStateFlow<UserFirebaseResult>(
        UserFirebaseResult.Initial
    )
    val isRegisterSuccess: StateFlow<UserFirebaseResult> = _isRegisterSuccess.asStateFlow()

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            authEmailAndPasswordUseCase
                .authEmailAndPassword(email, password)
                .collect {
                    _isRegisterSuccess.value = it
                }
        }
    }
}