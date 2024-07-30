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
import com.team.fooddelivery.data.model_firebase.ResponseUserAuthPhone
import com.team.fooddelivery.databinding.FragmentRegisterPhoneBinding
import com.team.fooddelivery.presentation.navigate.NavigateHelper
import com.team.fooddelivery.presentation.utils.listeningOnBackPressed
import com.team.fooddelivery.presentation.viewModels.user.ViewModelRegisterPhone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterPhoneFragment : Fragment() {

    private var _binding: FragmentRegisterPhoneBinding? = null
    private val binding: FragmentRegisterPhoneBinding
        get() = _binding ?: throw RuntimeException(
            getString(R.string.text_is_null_binding_register_phone_fragment)
        )

    private val navigateHelper: NavigateHelper by lazy {
        (requireActivity() as? NavigateHelper)
            ?: throw RuntimeException(
                getString(R.string.text_error_no_implements_interface)
            )
    }

    private val viewModel: ViewModelRegisterPhone by viewModels()

    //lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeningOnBackPressed(
            requireActivity(),
            navigateHelper,
            LoginFragment.newInstance()
        )
        onClickBtnGetCode()
        observeViewModel()
        onClickSignInEmail()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }
    //lifecycle

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.verificationId.flowWithLifecycle(lifecycle)
                .collect { codePhoneResult ->
                    when (codePhoneResult) {
                        ResponseUserAuthPhone.Error -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.text_error_register_phone),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        ResponseUserAuthPhone.Initial -> {}
                        is ResponseUserAuthPhone.Success -> {
                            val userPhone = binding.editTextPhoneNumber.text.toString()
                            navigateHelper.navigateTo(
                                VerifyCodeFragment.newInstance(
                                    codePhoneResult.verifyCode,
                                    userPhone
                                )
                            )
                        }
                    }
                }
        }
    }

    private fun onClickBtnGetCode() {
        with(binding) {
            btnGetCode.setOnClickListener {
                val phone = editTextPhoneNumber.text.toString()
                if (checkIsNotEmptyPhone(phone)) {
                    if (checkPhoneLength(phone)) {
                        viewModel.sendVerificationCode(phone)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.text_error_phone_is_length_13),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.text_error_phone_is_empty),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun onClickSignInEmail() {
        binding.textSignIn.setOnClickListener {
            navigateHelper.navigateTo(RegisterFragment.newInstance())
        }
    }

    private fun checkIsNotEmptyPhone(phone: String): Boolean {
        return phone.trim() != ""
    }

    private fun checkPhoneLength(phone: String): Boolean {
        return phone.length == 16
    }

    companion object {

        fun newInstance(): RegisterPhoneFragment {
            return RegisterPhoneFragment()
        }
    }
}