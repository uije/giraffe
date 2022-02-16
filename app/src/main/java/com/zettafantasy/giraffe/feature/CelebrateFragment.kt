package com.zettafantasy.giraffe.feature

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.zettafantasy.giraffe.GiraffeConstant
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.KonfettiPresets
import com.zettafantasy.giraffe.common.Preferences
import com.zettafantasy.giraffe.databinding.CelebrateFragmentBinding

class CelebrateFragment : Fragment() {

    private lateinit var binding: CelebrateFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.celebrate_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.konfettiView.start(KonfettiPresets.parade())

        Handler(Looper.getMainLooper()).postDelayed({
            if (isVisible) {
                navigateLounge()
            }
        }, GiraffeConstant.HIDE_COACH_MARK_MILLIS)

        Preferences.shownCelebrateScreen = true
    }

    private fun navigateLounge() {
        Navigation.findNavController(binding.root)
            .navigate(CelebrateFragmentDirections.actionCelebrateToRecord())
    }
}