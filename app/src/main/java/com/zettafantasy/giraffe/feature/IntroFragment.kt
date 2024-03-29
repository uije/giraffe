package com.zettafantasy.giraffe.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.BaseBindingFragment
import com.zettafantasy.giraffe.common.Preferences
import com.zettafantasy.giraffe.databinding.IntroFragmentBinding

class IntroFragment : BaseBindingFragment<IntroFragmentBinding>() {

    override fun init(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): IntroFragmentBinding {
        val binding: IntroFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.intro_fragment,
            container,
            false
        )

        binding.startBtn.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_intro_to_emotion_description)
        }

        Preferences.shownRecordIntro = true

        return binding
    }

    companion object {
        const val TAG = "IntroFragment"
    }
}
