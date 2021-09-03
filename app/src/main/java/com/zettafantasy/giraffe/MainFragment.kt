package com.zettafantasy.giraffe

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.rizafu.coachmark.CoachMark
import com.zettafantasy.giraffe.common.Preferences
import com.zettafantasy.giraffe.databinding.MainFragmentBinding
import com.zettafantasy.giraffe.feature.WordCloudFragment
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
                return when (position) {
                    0 -> RecordFragment()
                    else -> WordCloudFragment()
                }
            }

            override fun getItemCount(): Int {
                return 2
            }
        }
//        binding.viewPager.offscreenPageLimit = 1 //미리 로딩

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // 네비게이션 메뉴 아이템 체크상태
                binding.bottomNavigation.menu.getItem(position).isChecked = true
            }
        })

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                // itemId에 따라 viewPager 바뀜
                R.id.menu_record -> binding.viewPager.currentItem = 0
                R.id.menu_insight -> binding.viewPager.currentItem = 1
            }
            true
        }

        initFab()

        return binding.root
    }

    private fun initFab() {
        binding.fab.setOnClickListener {
            if (Preferences.shownRecordIntro) {
                Navigation.findNavController(binding.root)
                    .navigate(MainFragmentDirections.actionGoGoodOrBad())
            } else {
                Navigation.findNavController(binding.root)
                    .navigate(MainFragmentDirections.actionGoIntroDesc())
            }
        }

        if (!Preferences.shownCoachMarkStartBtn) {
            val coachMark = showCoachMark(binding.fab)

            Handler(Looper.getMainLooper()).postDelayed({
                coachMark?.dismiss()
                Preferences.shownCoachMarkStartBtn = true
            }, GiraffeConstant.HIDE_COACH_MARK_MILLIS)
        }
    }

    private fun showCoachMark(view: View) =
        CoachMark.Builder(requireActivity())
            .setTarget(view)
            .addTooltipChildText(
                requireActivity(),
                getString(R.string.tooltip_start_btn),
                android.R.color.black
            )
            .setTooltipAlignment(CoachMark.TARGET_TOP_RIGHT)
            .setTooltipPointer(CoachMark.POINTER_RIGHT)
            .setTooltipBackgroundColor(R.color.accent)
            .setDismissible()
            .show()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.record, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_setting -> {
                Navigation.findNavController(binding.root)
                    .navigate(MainFragmentDirections.actionGoSetting())
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}