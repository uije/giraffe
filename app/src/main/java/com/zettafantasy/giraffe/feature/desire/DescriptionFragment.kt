package com.zettafantasy.giraffe.feature.desire

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.feature.emotion.EmotionType
import com.zettafantasy.giraffe.model.Emotion

class DescriptionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.desire_description_fragment, container, false)

        val emotionType = getEmotionType()
        val emotions =
            arguments?.getParcelableArrayList<Emotion>(Emotion::class.simpleName)

        emotions?.let {
            view.findViewById<TextView>(R.id.state).text = HtmlCompat.fromHtml(
                String.format(
                    "현재 <font color=\"#%s\">%s</font> 상태이시군요.",
                    Integer.toHexString(
                        ContextCompat.getColor(requireContext(), R.color.accent)
                    ).substring(2),
                    TextUtils.join(", ", emotions)
                ),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
        return view
    }

    private fun getEmotionType(): EmotionType =
        arguments?.get(EmotionType::class.simpleName) as EmotionType
}
