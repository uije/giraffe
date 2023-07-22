package com.zettafantasy.giraffe.feature.emotion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.BaseBindingFragment
import com.zettafantasy.giraffe.databinding.GoodOrBadFragmentBinding
import com.zettafantasy.giraffe.model.EmotionType

class GoodOrBadFragment : BaseBindingFragment<GoodOrBadFragmentBinding>() {

    override fun init(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): GoodOrBadFragmentBinding {
        val binding: GoodOrBadFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.good_or_bad_fragment,
            container,
            false
        )

        binding.startBtnGood.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(GoodOrBadFragmentDirections.actionDescriptionToFindStimulus(EmotionType.SATISFIED))
        }

        binding.startBtnBad.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(GoodOrBadFragmentDirections.actionDescriptionToFindStimulus(EmotionType.UNSATISFIED))
        }

        return binding
    }
}
