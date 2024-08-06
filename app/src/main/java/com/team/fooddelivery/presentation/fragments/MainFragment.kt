package com.team.fooddelivery.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.team.fooddelivery.R
import com.team.fooddelivery.databinding.FragmentMainBinding
import com.team.fooddelivery.domain.entity.user.User
import com.team.fooddelivery.domain.entity.user.state.ResponseGetCurrentUser
import com.team.fooddelivery.domain.entity.user.state.ResponseGetUserInfo
import com.team.fooddelivery.presentation.fragments.user_auth.LoginFragment
import com.team.fooddelivery.presentation.navigate.NavigateHelper
import com.team.fooddelivery.presentation.utils.onBackPressedExit
import com.team.fooddelivery.presentation.utils.setVisibleNavMenu
import com.team.fooddelivery.presentation.viewModels.main.ViewModelMain
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw RuntimeException(
            getString(R.string.text_is_null_binding_main_fragment)
        )

    private val navigateHelper: NavigateHelper by lazy {
        (requireActivity() as? NavigateHelper)
            ?: throw RuntimeException(
                getString(R.string.text_error_no_implements_interface)
            )
    }

    private val viewModel: ViewModelMain by viewModels()

    //lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setVisibleNavMenu(requireActivity())
        viewModel.getCurrentUser()
        onBackPressedExit(requireActivity())
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
                .collect { result ->
                    when (result) {
                        ResponseGetCurrentUser.Error -> {
                            navigateHelper.navigateTo(LoginFragment.newInstance())
                        }

                        ResponseGetCurrentUser.Initial -> {}
                        is ResponseGetCurrentUser.UserSuccess -> {
                            viewModel.getUserInfo(userId = result.user?.uid ?: "")
                        }

                        null -> {}
                    }
                }
        }

        lifecycleScope.launch {
            viewModel.firebaseUserInfo.flowWithLifecycle(lifecycle)
                .collect { result ->
                    when (result) {
                        ResponseGetUserInfo.Error -> {
                            Log.d("MainFragment", "No User")
                        }

                        ResponseGetUserInfo.Initial -> {}
                        is ResponseGetUserInfo.Success -> {
                            setUserInfoInHeaderMenu(result.userObj)
                        }
                    }
                }
        }
    }

    private fun setUserInfoInHeaderMenu(user: User) {
        val userInfoInFragment = requireActivity()
            .findViewById<AppCompatTextView>(R.id.userInfo)
        if (user.email.isNotEmpty()) {
            userInfoInFragment.text = user.email
        } else {
            userInfoInFragment.text = user.phoneUser
        }
    }

    companion object {

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}