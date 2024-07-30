package com.team.fooddelivery.presentation.fragments.user_auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.team.fooddelivery.R
import com.team.fooddelivery.databinding.FragmentLoginBinding
import com.team.fooddelivery.domain.entity.user.state.ResponseGetCurrentUser
import com.team.fooddelivery.domain.entity.user.state.ResponseUserAuthEmailAndPassword
import com.team.fooddelivery.presentation.fragments.MainFragment
import com.team.fooddelivery.presentation.navigate.NavigateHelper
import com.team.fooddelivery.presentation.viewModels.user.ViewModelLogin
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding ?: throw RuntimeException(
            getString(R.string.text_is_null_binding_login_fragment)
        )

    private val navigateHelper: NavigateHelper by lazy {
        (requireActivity() as? NavigateHelper)
            ?: throw RuntimeException(
                getString(R.string.text_error_no_implements_interface)
            )
    }

    private val viewModel: ViewModelLogin by viewModels()

    //lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCurrentUser()
        onClickBtnRegister()
        onClickRegisterPhone()
        loginUser()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //lifecycle

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.firebaseUser.flowWithLifecycle(lifecycle)
                .collect { responseUser ->
                    when (responseUser) {
                        ResponseGetCurrentUser.Error -> {}
                        ResponseGetCurrentUser.Initial -> {}
                        is ResponseGetCurrentUser.UserSuccess -> {
                            navigateHelper.navigateTo(MainFragment.newInstance())
                        }

                        null -> {}
                    }
                }
        }

        lifecycleScope.launch {
            viewModel.isLogin.flowWithLifecycle(lifecycle)
                .collect { responseUserAuth ->
                    when (responseUserAuth) {
                        ResponseUserAuthEmailAndPassword.Error -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.text_error_sign_in),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        ResponseUserAuthEmailAndPassword.Initial -> {}
                        is ResponseUserAuthEmailAndPassword.Success -> {
                            navigateHelper.navigateTo(MainFragment.newInstance())
                        }

                        null -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.text_error_sign_in),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
        }
    }

    private fun loginUser() {
        with(binding) {
            btnSignIn.setOnClickListener {
                val email: String = editTextEmailLogin.text.toString()
                val password: String = editTextTextPassword.text.toString()
                if (checkEmailAndPasswordIsNotEmpty(email, password)) {
                    viewModel.loginUser(email, password)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(
                            R.string.text_error_password_and_login_is_password
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun checkEmailAndPasswordIsNotEmpty(
        email: String,
        password: String
    ): Boolean {
        return email.trim() != "" && password.trim() != ""
    }

    private fun onClickRegisterPhone() {
        binding.textSignInWithPhone.setOnClickListener {
            navigateHelper.navigateTo(RegisterPhoneFragment.newInstance())
        }
    }

    private fun onClickBtnRegister() {
        binding.btnSignUp.setOnClickListener {
            navigateHelper.navigateTo(RegisterFragment.newInstance())
        }
    }

    companion object {

        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}