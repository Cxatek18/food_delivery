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
import com.team.fooddelivery.databinding.FragmentRegisterBinding
import com.team.fooddelivery.domain.entity.user.state.UserFirebaseResult
import com.team.fooddelivery.presentation.navigate.NavigateHelper
import com.team.fooddelivery.presentation.utils.listeningOnBackPressed
import com.team.fooddelivery.presentation.viewModels.user.ViewModelRegister
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding
        get() = _binding ?: throw RuntimeException(
            getString(R.string.text_is_null_binding_register_fragment)
        )

    private val navigateHelper: NavigateHelper by lazy {
        (requireActivity() as? NavigateHelper)
            ?: throw RuntimeException(
                getString(R.string.text_error_no_implements_interface)
            )
    }

    private val viewModel: ViewModelRegister by viewModels()

    //lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickBtnSignIn()
        onClickBtnSignUp()
        observeFunctionalAuthViewModel()
        listeningOnBackPressed(
            requireActivity(),
            navigateHelper,
            LoginFragment.newInstance()
        )
    }

    override fun onStop() {
        super.onStop()
        binding.progressBarRegister.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //lifecycle

    private fun onClickBtnSignUp() {
        with(binding) {
            btnSignUp.setOnClickListener {
                val emailUser = editTextEmail.text.toString()
                val passwordUser = editTextPassword.text.toString()
                val passwordUserConfirm = editTextConfirmPassword.text.toString()
                if (checkPasswordValidate(passwordUser, passwordUserConfirm)) {
                    if (checkEmailAndPasswordIsNotEmpty(emailUser, passwordUser)) {
                        viewModel.registerUser(emailUser, passwordUser)
                        with(binding) {
                            progressBarRegister.visibility = View.VISIBLE
                            textResultRegister.visibility = View.VISIBLE
                            textResultRegister.text = getString(R.string.text_process_register)
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(
                                R.string.text_error_password_and_login_is_password
                            ),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.text_error_password_confirm),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun observeFunctionalAuthViewModel() {
        lifecycleScope.launch {
            viewModel.isRegisterSuccess.flowWithLifecycle(lifecycle)
                .collect { isRegister ->
                    binding.progressBarRegister.visibility = View.GONE
                    when (isRegister) {
                        UserFirebaseResult.Initial -> {}
                        UserFirebaseResult.UserErrorIsRegister -> {
                            with(binding) {
                                textResultRegister.text = getString(
                                    R.string.text_error_register
                                )
                            }
                        }

                        UserFirebaseResult.UserIsRegister -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.text_confirm_register),
                                Toast.LENGTH_LONG
                            ).show()
                            navigateHelper.navigateTo(LoginFragment.newInstance())
                        }
                    }
                }
        }
    }

    private fun checkEmailAndPasswordIsNotEmpty(
        email: String, password: String
    ): Boolean {
        return email.trim() != "" && password.trim() != ""
    }

    private fun checkPasswordValidate(
        password: String, passwordConfirm: String
    ): Boolean {
        return password == passwordConfirm
    }

    private fun onClickBtnSignIn() {
        binding.textSignIn.setOnClickListener {
            navigateHelper.navigateTo(LoginFragment.newInstance())
        }
    }

    companion object {

        fun newInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }
}