package com.zettafantasy.giraffe

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.core.view.doOnAttach
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.rizafu.coachmark.CoachMark
import com.zettafantasy.giraffe.GiraffeConstant.SCREEN_INSIGHT
import com.zettafantasy.giraffe.GiraffeConstant.SCREEN_RECORD
import com.zettafantasy.giraffe.common.Preferences
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.databinding.MainFragmentBinding
import com.zettafantasy.giraffe.feature.wordcloud.WordCloudFragment
import com.zettafantasy.giraffe.feature.record.RecordsFragment

class MainFragment : Fragment() {
    lateinit var binding: MainFragmentBinding
    private val repository: GiraffeRepository by lazy {
        (requireActivity().application as GiraffeApplication).repository
    }
    var currentScreen: Int = Preferences.defaultScreen

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        initViewPager()
        initFab()
        return binding.root
    }

    private fun initViewPager() {
        binding.viewPager.doOnAttach {
            binding.viewPager.setCurrentItem(currentScreen, false)
            binding.bottomNavigation.menu.getItem(currentScreen).isChecked = true

            binding.viewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    // 네비게이션 메뉴 아이템 체크상태
                    binding.bottomNavigation.menu.getItem(position).isChecked = true
                    currentScreen = position
                }
            })
        }

        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    SCREEN_RECORD -> RecordsFragment()
                    SCREEN_INSIGHT -> WordCloudFragment()
                    else -> WordCloudFragment()
                }
            }

            override fun getItemCount(): Int {
                return 2
            }
        }
        binding.viewPager.offscreenPageLimit = 1 //미리 로딩

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                // itemId에 따라 viewPager 바뀜
                R.id.menu_record -> binding.viewPager.currentItem = SCREEN_RECORD
                R.id.menu_insight -> binding.viewPager.currentItem = SCREEN_INSIGHT
            }
            true
        }
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
        inflater.inflate(R.menu.records, menu)
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