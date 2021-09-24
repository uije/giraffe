package com.zettafantasy.giraffe.feature

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.lifecycle.observe
import com.zettafantasy.giraffe.GiraffeApplication
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.databinding.WordCloudFragmentBinding
import com.zettafantasy.giraffe.feature.record.RecordWrapper
import com.zettafantasy.giraffe.model.Emotion
import net.alhazmy13.wordcloud.ColorTemplate
import net.alhazmy13.wordcloud.WordCloud
import java.util.*


class WordCloudFragment : Fragment() {
    private lateinit var needInventory: NeedInventory
    private lateinit var emotionInventory: EmotionInventory
    private lateinit var repository: GiraffeRepository
    private lateinit var binding: WordCloudFragmentBinding

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

        val emotionMap = mutableMapOf<Emotion, WordCloud>()

        Transformations.map(repository.findRecordsSince(oneMonthAgo()).asLiveData()) { data ->
            data.map { RecordWrapper(it, emotionInventory, needInventory) }.toList()
        }.observe(viewLifecycleOwner) { records ->
            for (record in records) {
                for (emotion in record.emotions) {
                    if (emotionMap.containsKey(emotion)) {
                        emotionMap[emotion]?.weight = emotionMap[emotion]!!.weight + 1
                    } else {
                        emotionMap[emotion!!] = WordCloud(emotion.getName(), 1)
                    }
                }
            }

            val dataset = emotionMap.values.toList()
            Log.d(javaClass.simpleName, dataset.toString())
            binding.wordCloud.setDataSet(dataset)
            binding.wordCloud.setColors(ColorTemplate.MATERIAL_COLORS)
            binding.wordCloud.notifyDataSetChanged()
        }

        binding.wordCloud.setBackgroundColor(Color.TRANSPARENT)

        return binding.root
    }

    private fun oneMonthAgo(): Long {
        return Calendar.getInstance().run {
            add(Calendar.MONTH, -1)
            timeInMillis
        }
    }
}
