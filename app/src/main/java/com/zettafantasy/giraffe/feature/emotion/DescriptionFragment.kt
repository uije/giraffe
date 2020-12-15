package com.zettafantasy.giraffe.feature.emotion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.zettafantasy.giraffe.R

class DescriptionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.emotion_description_fragment, container, false)
        view.findViewById<AppCompatButton>(R.id.start_btn).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_description_to_find_emotion)
        }
        return view
    }
}
