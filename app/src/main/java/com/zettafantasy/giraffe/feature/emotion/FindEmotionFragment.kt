package com.zettafantasy.giraffe.feature.emotion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zettafantasy.giraffe.R

class FindEmotionFragment : Fragment() {
    companion object {
        const val TAG = "EmotionFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.find_emotion_fragment, container, false)
    }
}
