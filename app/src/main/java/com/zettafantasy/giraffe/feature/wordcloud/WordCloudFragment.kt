package com.zettafantasy.giraffe.feature.wordcloud

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.observe
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zettafantasy.giraffe.GiraffeApplication
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.databinding.WordCloudFragmentBinding
import com.zettafantasy.giraffe.databinding.WordCloudViewBinding
import net.alhazmy13.wordcloud.ColorTemplate


class WordCloudFragment : Fragment() {
    private lateinit var needInventory: NeedInventory
    private lateinit var emotionInventory: EmotionInventory
    private lateinit var repository: GiraffeRepository
    private lateinit var binding: WordCloudFragmentBinding
    private val viewModel: WordCloudViewModel by viewModels {
        WordCloudViewModelFactory(
            (requireActivity().application as GiraffeApplication).repository,
            resources
        )
    }

    private val adapter: WordCloudPagerAdapter by lazy {
        WordCloudPagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        repository = (requireActivity().application as GiraffeApplication).repository
        emotionInventory = EmotionInventory.getInstance(resources)
        needInventory = NeedInventory.getInstance(resources)
        binding = DataBindingUtil.inflate(inflater, R.layout.word_cloud_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.pager.adapter = adapter
        viewModel.load(viewLifecycleOwner)

        TabLayoutMediator(
            binding.tabLayout, binding.pager
        ) { tab: TabLayout.Tab, position: Int ->
            if (position == 0) {
                tab.text = getString(R.string.emotion)
            } else {
                tab.text = getString(R.string.need)
            }
        }.attach()

        return binding.root
    }
}

class WordCloudPagerAdapter(
    @NonNull fragmentManager: FragmentManager,
    @NonNull lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    @NonNull
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            EmotionCloudFragment()
        } else {
            NeedCloudFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}

class EmotionCloudFragment : Fragment() {
    private lateinit var binding: WordCloudViewBinding

    val viewModel: WordCloudViewModel by viewModels({ requireParentFragment() })

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.word_cloud_view, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.wordCloud.setBackgroundColor(Color.TRANSPARENT)

        viewModel.emotions.observe(viewLifecycleOwner) {
            binding.wordCloud.setDataSet(it)
            binding.wordCloud.setColors(ColorTemplate.MATERIAL_COLORS)
            binding.wordCloud.notifyDataSetChanged()
            Log.d(this.javaClass.simpleName, it.toString())
        }

        Log.d(javaClass.simpleName, "onCreateView")
        return binding.root
    }
}

class NeedCloudFragment : Fragment() {
    private lateinit var binding: WordCloudViewBinding

    val viewModel: WordCloudViewModel by viewModels({ requireParentFragment() })

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.word_cloud_view, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.wordCloud.setBackgroundColor(Color.TRANSPARENT)

        viewModel.needs.observe(viewLifecycleOwner) {
            binding.wordCloud.setDataSet(it)
            binding.wordCloud.setColors(ColorTemplate.MATERIAL_COLORS)
            binding.wordCloud.notifyDataSetChanged()
            Log.d(this.javaClass.simpleName, it.toString())
        }

        Log.d(javaClass.simpleName, "onCreateView")
        return binding.root
    }
}


