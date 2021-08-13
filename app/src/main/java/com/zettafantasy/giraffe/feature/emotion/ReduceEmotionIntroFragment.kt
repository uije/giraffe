package com.zettafantasy.giraffe.feature.emotion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.zettafantasy.giraffe.GiraffeConstant
import com.zettafantasy.giraffe.R

class ReduceEmotionIntroFragment : Fragment() {
    private val args by navArgs<ReduceEmotionIntroFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.reduce_emotion_intro_fragment, container, false)

        view.findViewById<TextView>(R.id.desc2).text = HtmlCompat.fromHtml(
            getString(
                R.string.reduce_emotion_intro_desc,
                Integer.toHexString(
                    ContextCompat.getColor(requireContext(), R.color.accent)
                ).substring(2), GiraffeConstant.ITEM_COUNT.toString()
            ),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        view.findViewById<AppCompatButton>(R.id.start_btn).setOnClickListener {
            Navigation.findNavController(view)
                .navigate(ReduceEmotionIntroFragmentDirections.actionIntroToReduceEmotion(args.record))
        }
        return view
    }
}
