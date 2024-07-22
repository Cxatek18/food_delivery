package com.team.fooddelivery.presentation.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.team.fooddelivery.R
import com.team.fooddelivery.databinding.ActivityMainBinding
import com.team.fooddelivery.presentation.fragments.MainFragment
import com.team.fooddelivery.presentation.fragments.ViewPagerOpened
import com.team.fooddelivery.presentation.navigate.NavigateHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigateHelper {

    private lateinit var binding: ActivityMainBinding

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

    // lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkingActivationIsOnBoarding()
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

    override fun navigateTo(fragment: Fragment) {
        router.navigateTo(FragmentScreen { fragment })
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
            navigateTo(MainFragment.newInstance())
        } else {
            navigateTo(ViewPagerOpened.newInstance())
        }
    }
}