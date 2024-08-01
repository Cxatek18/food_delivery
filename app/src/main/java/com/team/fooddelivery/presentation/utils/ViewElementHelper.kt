package com.team.fooddelivery.presentation.utils

import android.view.View
import android.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import com.google.android.material.navigation.NavigationView
import com.team.fooddelivery.R

fun setGoneVisibleNavMenu(requireActivity: FragmentActivity) {
    requireActivity.findViewById<Toolbar>(R.id.toolBar).visibility = View.GONE
    requireActivity.findViewById<NavigationView>(R.id.navMenu).visibility = View.GONE
}

fun setVisibleNavMenu(requireActivity: FragmentActivity) {
    requireActivity.findViewById<Toolbar>(R.id.toolBar).visibility = View.VISIBLE
    requireActivity.findViewById<NavigationView>(R.id.navMenu).visibility = View.VISIBLE
}