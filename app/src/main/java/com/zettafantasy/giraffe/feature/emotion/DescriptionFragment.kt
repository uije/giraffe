package com.zettafantasy.giraffe.feature.emotion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.model.EmotionType

class DescriptionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.emotion_description_fragment, container, false)
        view.findViewById<AppCompatButton>(R.id.start_btn_good).setOnClickListener {
            val args = Bundle()
            args.putSerializable(EmotionType::class.simpleName, EmotionType.SATISFIED)
            Navigation.findNavController(view)
                .navigate(R.id.action_description_to_find_emotion, args)
        }

        view.findViewById<AppCompatButton>(R.id.start_btn_bad).setOnClickListener {
            val args = Bundle()
            args.putSerializable(EmotionType::class.simpleName, EmotionType.UNSATISFIED)
            Navigation.findNavController(view)
                .navigate(R.id.action_description_to_find_emotion, args)
        }
        return view
    }
}
