package com.zettafantasy.giraffe

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.core.view.doOnAttach
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.rizafu.coachmark.CoachMark
import com.zettafantasy.giraffe.GiraffeConstant.SCREEN_INSIGHT
import com.zettafantasy.giraffe.GiraffeConstant.SCREEN_RECORD
import com.zettafantasy.giraffe.common.DestinationScreen
import com.zettafantasy.giraffe.common.Preferences
import com.zettafantasy.giraffe.common.navigate
import com.zettafantasy.giraffe.common.navigateRecord
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.databinding.MainFragmentBinding
import com.zettafantasy.giraffe.feature.record.RecordsFragment
import com.zettafantasy.giraffe.feature.wordcloud.WordCloudFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class MainFragment : Fragment() {
    lateinit var binding: MainFragmentBinding
    private val args by navArgs<MainFragmentArgs>()
    var currentScreen by Delegates.notNull<Int>()
    private val navController by lazy {
        Navigation.findNavController(binding.root)
    }
    private val destinationScreen: DestinationScreen? by lazy {
        arguments?.getSerializable(GiraffeConstant.EXTRA_KEY_SCREEN_DESTINATION) as DestinationScreen?
    }
    private var destinationNavigated = false
    private val model: MainViewModel by activityViewModels()
    private val repository: GiraffeRepository by lazy {
        (requireActivity().application as GiraffeApplication).repository
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        currentScreen = if (hasHighLightItem()) SCREEN_RECORD else Preferences.defaultScreen
        model.highLightItemId = args.highLightItemId
        initViewPager()
        initFab()
        initEmptyView()

        return binding.root
    }

    private fun initEmptyView() {
        lifecycleScope.launch {
            repository.getRowCount().map { it == 0 }.collect { isEmpty ->
                Log.d(javaClass.simpleName, "Rows isEmpty : $isEmpty")
                binding.bottomNavigation.isVisible = !isEmpty
                binding.viewPager.isUserInputEnabled = !isEmpty
            }
        }
    }

    private fun hasHighLightItem() = args.highLightItemId > 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!destinationNavigated) {
            destinationScreen?.navigate(navController)
            destinationNavigated = true
        }
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
                    activity?.invalidateOptionsMenu()
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
            navController.navigateRecord()
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

        val shareItem = menu.findItem(R.id.menu_share)
        shareItem.isVisible = binding.viewPager.currentItem == SCREEN_INSIGHT
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting -> navController.navigate(MainFragmentDirections.actionGoSetting())
            R.id.menu_share -> model.callShareClickEvent()
        }

        return super.onOptionsItemSelected(item)
    }
}