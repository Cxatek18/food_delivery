package com.team.fooddelivery.presentation.viewModels.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.fooddelivery.data.model_firebase.ResponseUserAuthPhone
import com.team.fooddelivery.domain.entity.user.state.CodePhoneResult
import com.team.fooddelivery.domain.usecase.user.SendVerifyCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelRegisterPhone @Inject constructor(
    private val sendVerifyCodeUseCase: SendVerifyCodeUseCase
) : ViewModel() {
    private val _verificationId = MutableStateFlow<ResponseUserAuthPhone>(
        ResponseUserAuthPhone.Initial
    )
    val verificationId: StateFlow<ResponseUserAuthPhone> = _verificationId.asStateFlow()

    fun sendVerificationCode(phoneNumber: String) {
        viewModelScope.launch {
            sendVerifyCodeUseCase.sendVerifyCode(phoneNumber)
                .collect { result ->
                    when (result) {
                        CodePhoneResult.Error -> {
                            _verificationId.value = ResponseUserAuthPhone.Error
                        }

                        CodePhoneResult.Initial -> {}
                        is CodePhoneResult.Success -> {
                            _verificationId.value = ResponseUserAuthPhone.Success(result.result)
                        }
                    }
                }
        }
    }
}