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
import com.team.fooddelivery.databinding.FragmentVerifyCodeBinding
import com.team.fooddelivery.domain.entity.user.state.UserFirebaseResult
import com.team.fooddelivery.presentation.navigate.NavigateHelper
import com.team.fooddelivery.presentation.utils.listeningOnBackPressed
import com.team.fooddelivery.presentation.viewModels.user.ViewModelVerifyCode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VerifyCodeFragment : Fragment() {

    private var _binding: FragmentVerifyCodeBinding? = null
    private val binding: FragmentVerifyCodeBinding
        get() = _binding ?: throw RuntimeException(
            getString(R.string.text_is_null_binding_verify_code_fragment)
        )

    private val navigateHelper: NavigateHelper by lazy {
        (requireActivity() as? NavigateHelper)
            ?: throw RuntimeException(
                getString(R.string.text_error_no_implements_interface)
            )
    }

    private lateinit var code: String
    private lateinit var userPhone: String

    private val viewModel: ViewModelVerifyCode by viewModels()

    //lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifyCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseArgs()
        listeningOnBackPressed(
            requireActivity(),
            navigateHelper,
            RegisterPhoneFragment.newInstance()
        )
        sendCode()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //lifecycle

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.verificationResult.flowWithLifecycle(lifecycle)
                .collect {
                    when (it) {
                        UserFirebaseResult.Initial -> {}
                        UserFirebaseResult.UserErrorIsRegister -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.text_error_sign_in),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        UserFirebaseResult.UserIsRegister -> {
                            navigateHelper.navigateTo(LoginFragment.newInstance())
                        }
                    }
                }
        }
    }

    private fun sendCode() {
        with(binding) {
            btnPostCode.setOnClickListener {
                val codeUser = editTextCode.text.toString()
                if (checkCodeNoEmpty(codeUser)) {
                    viewModel.verifyCode(
                        code,
                        codeUser,
                        userPhone
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.text_error_code_is_empty),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun checkCodeNoEmpty(code: String): Boolean {
        return code.trim() != ""
    }

    private fun parseArgs() {
        val args = requireArguments()
        code = args.getString(EXTRA_KEY_VERIFY).toString()
        userPhone = args.get(EXTRA_USER_PHONE).toString()
    }

    companion object {
        private const val EXTRA_KEY_VERIFY = "verify"
        private const val EXTRA_USER_PHONE = "user_phone"

        fun newInstance(verifyCode: String?, userPhone: String): VerifyCodeFragment {
            return VerifyCodeFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_KEY_VERIFY, verifyCode)
                    putString(EXTRA_USER_PHONE, userPhone)
                }
            }
        }
    }
}