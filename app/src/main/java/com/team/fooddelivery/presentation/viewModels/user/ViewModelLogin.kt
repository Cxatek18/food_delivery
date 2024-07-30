package com.team.fooddelivery.presentation.viewModels.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.fooddelivery.domain.entity.user.state.ResponseGetCurrentUser
import com.team.fooddelivery.domain.entity.user.state.ResponseUserAuthEmailAndPassword
import com.team.fooddelivery.domain.usecase.user.GetCurrentUserUseCase
import com.team.fooddelivery.domain.usecase.user.SignInWithEmailAndPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelLogin @Inject constructor(
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private var _isLogin = MutableStateFlow<ResponseUserAuthEmailAndPassword?>(
        ResponseUserAuthEmailAndPassword.Initial
    )
    val isLogin: StateFlow<ResponseUserAuthEmailAndPassword?> = _isLogin.asStateFlow()

    private var _firebaseUser = MutableStateFlow<ResponseGetCurrentUser?>(
        ResponseGetCurrentUser.Initial
    )
    val firebaseUser: StateFlow<ResponseGetCurrentUser?> = _firebaseUser.asStateFlow()

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            signInWithEmailAndPasswordUseCase
                .signInWithEmailAndPassword(email, password)
                .collect {
                    _isLogin.value = it
                }
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            getCurrentUserUseCase.getCurrentUser()
                .collect { result ->
                    _firebaseUser.value = result
                }
        }
    }
}