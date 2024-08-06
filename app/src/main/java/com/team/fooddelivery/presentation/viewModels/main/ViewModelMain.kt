package com.team.fooddelivery.presentation.viewModels.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.fooddelivery.domain.entity.user.state.ResponseGetCurrentUser
import com.team.fooddelivery.domain.entity.user.state.ResponseGetUserInfo
import com.team.fooddelivery.domain.entity.user.state.ResponseUserSignOut
import com.team.fooddelivery.domain.usecase.user.GetCurrentUserUseCase
import com.team.fooddelivery.domain.usecase.user.GetUserInfoUseCase
import com.team.fooddelivery.domain.usecase.user.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelMain @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {

    private var _isLogout = MutableStateFlow<ResponseUserSignOut>(
        ResponseUserSignOut.Initial
    )
    val isLogout: StateFlow<ResponseUserSignOut> = _isLogout.asStateFlow()

    private var _firebaseUser = MutableStateFlow<ResponseGetCurrentUser?>(
        ResponseGetCurrentUser.Initial
    )
    val firebaseUser: StateFlow<ResponseGetCurrentUser?> = _firebaseUser.asStateFlow()

    private var _firebaseUserInfo = MutableStateFlow<ResponseGetUserInfo>(
        ResponseGetUserInfo.Initial
    )
    val firebaseUserInfo: StateFlow<ResponseGetUserInfo> = _firebaseUserInfo.asStateFlow()

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase.signOut()
                .collect { response ->
                    _isLogout.value = response
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

    fun getUserInfo(userId: String) {
        viewModelScope.launch {
            getUserInfoUseCase.getUserInfo(userId)
                .collect { result ->
                    _firebaseUserInfo.value = result
                }
        }
    }
}