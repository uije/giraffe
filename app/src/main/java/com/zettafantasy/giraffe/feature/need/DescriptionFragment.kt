package com.zettafantasy.giraffe.feature.need

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.model.Emotion

class DescriptionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.need_description_fragment, container, false)

        val emotions =
            arguments?.getParcelableArrayList<Emotion>(Emotion::class.simpleName)

        emotions?.let {
            view.findViewById<TextView>(R.id.state).text = HtmlCompat.fromHtml(
                getString(
                    R.string.emotion_state_desc,
                    Integer.toHexString(
                        ContextCompat.getColor(requireContext(), R.color.accent)
                    ).substring(2),
                    TextUtils.join(", ", emotions)
                ),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }

        view.findViewById<AppCompatButton>(R.id.start_btn).setOnClickListener {
            navigateFindNeedScreen(emotions, view)
        }
        return view
    }

    private fun navigateFindNeedScreen(
        emotions: java.util.ArrayList<Emotion>?,
        view: View
    ) {
        val args = Bundle()
        args.putParcelableArrayList(
            Emotion::class.simpleName,
            emotions?.let { ArrayList(it.toMutableList()) }
        )
        Navigation.findNavController(view)
            .navigate(R.id.action_description_to_find_need, args)
    }
}
