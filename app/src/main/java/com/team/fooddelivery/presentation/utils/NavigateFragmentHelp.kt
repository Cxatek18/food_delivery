package com.team.fooddelivery.presentation.utils

import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.team.fooddelivery.presentation.navigate.NavigateHelper

fun listeningOnBackPressed(
    requireActivity: FragmentActivity,
    navigateHelper: NavigateHelper,
    fragmentTo: Fragment
) {
    requireActivity.onBackPressedDispatcher.addCallback {
        navigateHelper.navigateTo(fragmentTo)
    }
}