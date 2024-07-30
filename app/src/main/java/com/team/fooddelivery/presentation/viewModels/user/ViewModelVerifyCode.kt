package com.team.fooddelivery.presentation.viewModels.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.fooddelivery.domain.entity.user.state.UserFirebaseResult
import com.team.fooddelivery.domain.usecase.user.VerifyCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelVerifyCode @Inject constructor(
    private val verifyCodeUseCase: VerifyCodeUseCase
) : ViewModel() {

    private val _verificationResult = MutableStateFlow<UserFirebaseResult>(
        UserFirebaseResult.Initial
    )
    val verificationResult: StateFlow<UserFirebaseResult> = _verificationResult.asStateFlow()

    fun verifyCode(verificationId: String, code: String, userPhone: String) {
        viewModelScope.launch {
            verifyCodeUseCase.verifyCode(verificationId, code, userPhone)
                .collect { result ->
                    _verificationResult.value = result
                }
        }
    }
}