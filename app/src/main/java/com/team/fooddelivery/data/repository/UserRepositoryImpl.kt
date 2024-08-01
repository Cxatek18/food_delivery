package com.team.fooddelivery.data.repository

import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.team.fooddelivery.data.model_firebase.UserAuthEmailAndPassword
import com.team.fooddelivery.domain.entity.user.Card
import com.team.fooddelivery.domain.entity.user.state.CodePhoneResult
import com.team.fooddelivery.domain.entity.user.state.ResponseGetCurrentUser
import com.team.fooddelivery.domain.entity.user.state.ResponseUserAuthEmailAndPassword
import com.team.fooddelivery.domain.entity.user.state.ResponseUserResetPassword
import com.team.fooddelivery.domain.entity.user.state.ResponseUserSignOut
import com.team.fooddelivery.domain.entity.user.state.UserFirebaseResult
import com.team.fooddelivery.domain.repository.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit


class UserRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) : UserRepository {

    override fun getCurrentUser(): Flow<ResponseGetCurrentUser?> = flow {
        if (auth.currentUser == null) {
            emit(ResponseGetCurrentUser.Error)
        } else {
            emit(ResponseGetCurrentUser.UserSuccess(auth.currentUser))
        }
    }

    override fun authEmailAndPassword(
        email: String,
        password: String
    ): Flow<UserFirebaseResult> = flow {
        try {
            var result: AuthResult = auth.createUserWithEmailAndPassword(email, password).await()
            if (result.user == null) {
                emit(UserFirebaseResult.UserErrorIsRegister)
            } else {
                saveUserInFirebaseBase(
                    userId = result.user!!.uid,
                    email = email,
                )
                emit(UserFirebaseResult.UserIsRegister)
            }
        } catch (e: Exception) {
            emit(UserFirebaseResult.UserErrorIsRegister)
        }
    }

    override fun sendVerifyCode(
        userPhone: String
    ): Flow<CodePhoneResult> = channelFlow {
        val channel = Channel<CodePhoneResult>()

        launch(Dispatchers.IO) {
            try {
                val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        CoroutineScope(Dispatchers.IO).launch {
                            channel.send(CodePhoneResult.Success(credential.smsCode ?: ""))
                        }
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        CoroutineScope(Dispatchers.IO).launch {
                            channel.send(CodePhoneResult.Error)
                        }
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        CoroutineScope(Dispatchers.IO).launch {
                            channel.send(CodePhoneResult.Success(verificationId))
                        }
                    }
                }

                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(userPhone)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setCallbacks(callbacks)
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)

            } catch (e: Exception) {
                CoroutineScope(Dispatchers.IO).launch {
                    channel.send(CodePhoneResult.Error)
                }
            }
        }
        for (result in channel) {
            send(result)
        }
    }

    override fun verifyCode(
        verificationId: String,
        code: String,
        userPhone: String
    ): Flow<UserFirebaseResult> = flow {
        try {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            val user = auth.signInWithCredential(credential).await().user
            if (user != null) {
                saveUserInFirebaseBase(
                    userId = userPhone,
                    phoneUser = userPhone,
                    isRegisterPhone = true
                )
                emit(UserFirebaseResult.UserIsRegister)
            } else {
                emit(UserFirebaseResult.UserErrorIsRegister)
            }
        } catch (e: Exception) {
            emit(UserFirebaseResult.UserErrorIsRegister)
        }
    }

    private fun saveUserInFirebaseBase(
        userId: String,
        email: String = "",
        phoneUser: String = "",
        isRegisterPhone: Boolean = false
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = UserAuthEmailAndPassword(
                email = email,
                username = "",
                photoUser = "",
                address = "",
                order = emptyList(),
                cart = emptyList(),
                cardData = Card(
                    numberCard = "",
                    fullNameOwner = "",
                    dateEndings = "",
                    cvv = ""
                ),
                phoneUser = phoneUser,
                isRegisterPhone = isRegisterPhone
            )
            firebaseDatabase.reference.child("Users").child(userId).setValue(user)
        }
    }

    override fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<ResponseUserAuthEmailAndPassword?> = flow {
        try {
            var result: AuthResult = auth.signInWithEmailAndPassword(email, password).await()
            if (result.user == null) {
                ResponseUserAuthEmailAndPassword.Error
            } else {
                emit(ResponseUserAuthEmailAndPassword.Success(result.user))
            }
        } catch (e: Exception) {
            emit(ResponseUserAuthEmailAndPassword.Error)
        }
    }

    override fun restPassword(email: String): Flow<ResponseUserResetPassword> = flow {
        try {
            auth.sendPasswordResetEmail(email).await()
            emit(ResponseUserResetPassword.Success)
        } catch (e: Exception) {
            emit(ResponseUserResetPassword.Error)
        }
    }

    override fun signOut(): Flow<ResponseUserSignOut> = flow {
        try {
            emit(ResponseUserSignOut.Success)
            auth.signOut()
        } catch (e: Exception) {
            emit(ResponseUserSignOut.Error)
        }
    }
}