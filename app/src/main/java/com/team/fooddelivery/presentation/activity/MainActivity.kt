package com.team.fooddelivery.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.team.fooddelivery.R
import com.team.fooddelivery.databinding.ActivityMainBinding
import com.team.fooddelivery.domain.entity.user.state.ResponseGetCurrentUser
import com.team.fooddelivery.domain.entity.user.state.ResponseUserSignOut
import com.team.fooddelivery.presentation.fragments.MainFragment
import com.team.fooddelivery.presentation.fragments.ViewPagerOpened
import com.team.fooddelivery.presentation.fragments.user_auth.LoginFragment
import com.team.fooddelivery.presentation.navigate.NavigateHelper
import com.team.fooddelivery.presentation.viewModels.main.ViewModelMain
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigateHelper {

    lateinit var binding: ActivityMainBinding

    private val navigator: Navigator by lazy {
        AppNavigator(
            this,
            R.id.main_fragment_container,
            supportFragmentManager,
        )
    }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    private val viewModel: ViewModelMain by viewModels()

    // lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkingActivationIsOnBoarding()
        onClickOpenNavMenu()
        onClickItemNavMenu()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
    // lifecycle

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.firebaseUser.flowWithLifecycle(lifecycle)
                .collect { result ->
                    when (result) {
                        ResponseGetCurrentUser.Error -> {
                            navigateTo(LoginFragment.newInstance())
                        }

                        ResponseGetCurrentUser.Initial -> {}
                        is ResponseGetCurrentUser.UserSuccess -> {

                        }

                        null -> {
                            navigateTo(LoginFragment.newInstance())
                        }
                    }
                }
        }

        lifecycleScope.launch {
            viewModel.isLogout.flowWithLifecycle(lifecycle)
                .collect { result ->
                    when (result) {
                        ResponseUserSignOut.Error -> {
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.text_error_has_occurred),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        ResponseUserSignOut.Initial -> {}
                        ResponseUserSignOut.Success -> {
                            binding.drawerLayout.close()
                            viewModel.getCurrentUser()
                        }
                    }
                }
        }
    }

    override fun navigateTo(fragment: Fragment) {
        router.navigateTo(FragmentScreen { fragment })
    }

    private fun onClickOpenNavMenu() {
        with(binding) {
            openToolbar.setOnClickListener {
                drawerLayout.open()
            }
        }
    }

    private fun onClickItemNavMenu() {
        binding.navMenu.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    binding.drawerLayout.close()
                    navigateTo(MainFragment.newInstance())
                    true
                }

                R.id.exit -> {
                    viewModel.signOut()
                    true
                }

                else -> {}
            }
            true
        }
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPreferences = this.getSharedPreferences(
            getString(R.string.text_key_shared_preferences_on_boarding),
            Context.MODE_PRIVATE
        )
        return sharedPreferences.getBoolean(
            getString(R.string.text_extra_finished_on_boarding),
            false
        )
    }

    private fun checkingActivationIsOnBoarding() {
        if (onBoardingFinished()) {
            navigateTo(LoginFragment.newInstance())
        } else {
            navigateTo(ViewPagerOpened.newInstance())
        }
    }

    companion object {
        fun newInstance(context: SplashScreen): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}