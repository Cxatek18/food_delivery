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
import com.team.fooddelivery.databinding.FragmentRestPasswordBinding
import com.team.fooddelivery.domain.entity.user.state.ResponseUserResetPassword
import com.team.fooddelivery.presentation.navigate.NavigateHelper
import com.team.fooddelivery.presentation.utils.listeningOnBackPressed
import com.team.fooddelivery.presentation.viewModels.user.ViewModelRestPassword
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RestPasswordFragment : Fragment() {

    private var _binding: FragmentRestPasswordBinding? = null
    private val binding: FragmentRestPasswordBinding
        get() = _binding ?: throw RuntimeException(
            getString(R.string.text_is_null_binding_view_reset_password_fragment)
        )

    private val navigateHelper: NavigateHelper by lazy {
        (requireActivity() as? NavigateHelper)
            ?: throw RuntimeException(
                getString(R.string.text_error_no_implements_interface)
            )
    }

    private val viewModel: ViewModelRestPassword by viewModels()

    //lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeningOnBackPressed(
            requireActivity(),
            navigateHelper,
            LoginFragment.newInstance()
        )
        observeViewModel()
        onClickResetPassword()
        onClickSignIn()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //lifecycle

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.isReset.flowWithLifecycle(lifecycle)
                .collect { response ->
                    when (response) {
                        ResponseUserResetPassword.Error -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.text_error_reset_password),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        ResponseUserResetPassword.Initial -> {}
                        ResponseUserResetPassword.Success -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.text_success_reset_password),
                                Toast.LENGTH_LONG
                            ).show()
                            navigateHelper.navigateTo(LoginFragment.newInstance())
                        }
                    }
                }
        }
    }

    private fun onClickResetPassword() {
        with(binding) {
            btnResetPassword.setOnClickListener {
                val email = editTextEmail.text.toString()
                if (checkEmailIsNotEmpty(email)) {
                    viewModel.resetPassword(email)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.text_email_is_not_empty),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun onClickSignIn() {
        binding.textSignIn.setOnClickListener {
            navigateHelper.navigateTo(LoginFragment.newInstance())
        }
    }

    private fun checkEmailIsNotEmpty(
        email: String,
    ): Boolean {
        return email.trim() != ""
    }

    companion object {

        fun newInstance(): RestPasswordFragment {
            return RestPasswordFragment()
        }
    }
}