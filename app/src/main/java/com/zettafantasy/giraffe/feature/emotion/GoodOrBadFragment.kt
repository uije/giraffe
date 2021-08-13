package com.zettafantasy.giraffe.feature.emotion

import android.os.Bundle
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
            val args = Bundle()
            args.putSerializable(EmotionType::class.simpleName, EmotionType.SATISFIED)
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_description_to_find_stimulus, args)
        }

        binding.startBtnBad.setOnClickListener {
            val args = Bundle()
            args.putSerializable(EmotionType::class.simpleName, EmotionType.UNSATISFIED)
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_description_to_find_stimulus, args)
        }

        return binding
    }
}
