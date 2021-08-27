package com.zettafantasy.giraffe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.zettafantasy.giraffe.databinding.MainFragmentBinding
import com.zettafantasy.giraffe.feature.record.RecordFragment

class MainFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)

        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return RecordFragment()
            }

            override fun getItemCount(): Int {
                return 2
            }
        }

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = "기록"
        }.attach()

        return binding.root
    }
}