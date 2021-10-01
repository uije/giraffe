package com.zettafantasy.giraffe.feature.wordcloud

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
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zettafantasy.giraffe.GiraffeApplication
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.databinding.WordCloudFragmentBinding
import com.zettafantasy.giraffe.databinding.WordCloudViewBinding
import java.util.*


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

        Log.d(javaClass.simpleName, viewModel.name)
        binding.pager.adapter = adapter

//        val emotionMap = mutableMapOf<Emotion, WordCloud>()
//
//        Transformations.map(repository.findRecordsSince(oneMonthAgo()).asLiveData()) { data ->
//            data.map { RecordWrapper(it, emotionInventory, needInventory) }.toList()
//        }.observe(viewLifecycleOwner) { records ->
//            for (record in records) {
//                for (emotion in record.emotions) {
//                    if (emotionMap.containsKey(emotion)) {
//                        emotionMap[emotion]?.weight = emotionMap[emotion]!!.weight + 1
//                    } else {
//                        emotionMap[emotion!!] = WordCloud(emotion.getName(), 1)
//                    }
//                }
//            }
//
//            val dataset = emotionMap.values.toList()
//            Log.d(javaClass.simpleName, dataset.toString())
//            binding.wordCloud.setDataSet(dataset)
//            binding.wordCloud.setColors(ColorTemplate.MATERIAL_COLORS)
//            binding.wordCloud.notifyDataSetChanged()
//        }
//
//        binding.wordCloud.setBackgroundColor(Color.TRANSPARENT)

        return binding.root
    }

    private fun oneMonthAgo(): Long {
        return Calendar.getInstance().run {
            add(Calendar.MONTH, -1)
            timeInMillis
        }
    }
}

class WordCloudPagerAdapter(@NonNull fragmentManager: FragmentManager, @NonNull lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    @NonNull
    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment: Fragment = DemoObjectFragment()
        val args = Bundle()
        // Our object is just an integer :-P
        args.putInt(DemoObjectFragment.ARG_OBJECT, position + 1)
        fragment.arguments = args
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}

// Instances of this class are fragments representing a single
// object in our collection.
class DemoObjectFragment : Fragment() {
    private lateinit var binding: WordCloudViewBinding

    val viewModel: WordCloudViewModel by viewModels({ requireParentFragment() })

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.word_cloud_view, container, false)

        binding.text?.text = viewModel.name
        Log.d(javaClass.simpleName, viewModel.name)

        return binding.wordCloud
    }

    companion object {
        const val ARG_OBJECT = "object"
    }
}


