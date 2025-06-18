package com.example.mvvmdemo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mvvmdemo.data.OfficialAccount
import com.example.mvvmdemo.fragment.OfficialArticleFragment

class OfficialPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val officialAccounts: List<OfficialAccount>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = officialAccounts.size

    override fun createFragment(position: Int): Fragment {
        val officialAccount = officialAccounts[position]
        return OfficialArticleFragment.newInstance(officialAccount)
    }

    fun getPageTitle(position: Int): CharSequence? {
        return if (position < officialAccounts.size) {
            officialAccounts[position].name
        } else {
            ""
        }
    }
} 