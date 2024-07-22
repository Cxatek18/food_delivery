package com.team.fooddelivery.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.team.fooddelivery.R
import com.team.fooddelivery.databinding.FragmentViewPagerOpenedBinding
import com.team.fooddelivery.presentation.fragments.viewPagerFirstOpened.FirstFragment
import com.team.fooddelivery.presentation.fragments.viewPagerFirstOpened.SecondFragment
import com.team.fooddelivery.presentation.fragments.viewPagerFirstOpened.ThirdFragment
import com.team.fooddelivery.presentation.viewPager.ViewPagerAdapter

class ViewPagerOpened : Fragment() {

    private var _binding: FragmentViewPagerOpenedBinding? = null
    private val binding: FragmentViewPagerOpenedBinding
        get() = _binding ?: throw RuntimeException(
            getString(
                R.string.text_is_null_binding_view_pager_opened_fragment
            )
        )

    //lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewPagerOpenedBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentList = arrayListOf<Fragment>(
            FirstFragment(),
            SecondFragment(),
            ThirdFragment()
        )
        val adapter = ViewPagerAdapter(
            list = fragmentList,
            fragmentManager = requireActivity().supportFragmentManager,
            lifecycle = lifecycle
        )
        binding.viewPager.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //lifecycle

    companion object {

        fun newInstance(): ViewPagerOpened {
            return ViewPagerOpened()
        }
    }
}