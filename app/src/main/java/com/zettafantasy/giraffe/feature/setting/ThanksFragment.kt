package com.zettafantasy.giraffe.feature.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.databinding.ThanksFragmentBinding

class ThanksFragment : Fragment() {

    private lateinit var binding: ThanksFragmentBinding

    private val thanks = "<ul>\n" +
            "<li>비폭력대화를 알려주신 <font color=\"#color\">마셜 로젠버그</font> 박사님</li>\n" +
            "<li>명상 멘토 <font color=\"#color\">진영</font>님</li>\n" +
            "<li>꾸준히 개발하도록 자극을 준 <font color=\"#color\">장준</font>님</li>\n" +
            "<li>삶에 활력이 되어준 <font color=\"#color\">임프로그</font> 멤버들</li>\n" +
            "<li>꾸준한 책읽기를 도와준 <font color=\"#color\">내아인</font> 사람들</li>\n" +
            "</ul>"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.thanks_fragment, container, false)

        binding.txt2.text = HtmlCompat.fromHtml(
            thanks.replace(
                "#color", "#" + Integer.toHexString(
                    ContextCompat.getColor(requireContext(), R.color.accent)
                ).substring(2), false
            ), HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        return binding.root
    }

}