package com.demir.starter.ui.dashboard.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.demir.starter.R
import com.demir.starter.data.ProductCategory
import com.demir.starter.ui.BaseFragment
import com.demir.starter.ui.dashboard.home.product.ProductListFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : BaseFragment() {
    private val viewPager get() = requireView().findViewById<ViewPager2>(R.id.viewPager)
    private val tabLayout get() = requireView().findViewById<TabLayout>(R.id.tabLayout)
    private val products = ProductCategory.entries

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setupTabLayout()
    }

    private fun setupViewPager() {
        viewPager.isSaveEnabled = false
        viewPager.adapter = object : FragmentStateAdapter(childFragmentManager, lifecycle) {
            override fun createFragment(position: Int): Fragment {
                return ProductListFragment.newInstance(
                    products[position]
                )
            }

            override fun getItemCount(): Int {
                return products.size
            }
        }
    }

    private fun setupTabLayout() {
        TabLayoutMediator(
            tabLayout, viewPager, true
        ) { tab, position ->
            tab.setCustomView(R.layout.view_product_category_tab)
            tab.view.findViewById<TextView>(R.id.textView).let {
                it.text = getString(products[position].nameResId)
            }
            tab.view.findViewById<ImageView>(R.id.imageView).setImageDrawable(
                AppCompatResources.getDrawable(
                    tab.view.context,
                    products[position].iconResId
                )
            )
        }.attach()
    }
}