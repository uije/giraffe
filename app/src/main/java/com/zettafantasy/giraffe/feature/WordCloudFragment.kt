package com.zettafantasy.giraffe.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.databinding.WordCloudFragmentBinding
import net.alhazmy13.wordcloud.ColorTemplate
import net.alhazmy13.wordcloud.WordCloud

class WordCloudFragment : Fragment() {
    private lateinit var binding: WordCloudFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val list = mutableListOf<WordCloud>()

        list.add(WordCloud("1", 1))
        list.add(WordCloud("2", 2))
        list.add(WordCloud("3", 3))
        list.add(WordCloud("4", 4))
        list.add(WordCloud("5", 5))


        binding = DataBindingUtil.inflate(inflater, R.layout.word_cloud_fragment, container, false)
        binding.wordCloud.setDataSet(list)
        binding.wordCloud.setColors(ColorTemplate.MATERIAL_COLORS)
        binding.wordCloud.notifyDataSetChanged()

        return binding.root
    }
}
