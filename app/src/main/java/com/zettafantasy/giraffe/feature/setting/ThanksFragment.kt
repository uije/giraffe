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
            "<li>내 삶에 구원자 <font color=\"#color\"><b>예수님</b></font></li>\n" +
            "<li>비폭력대화를 알려주신 <font color=\"#color\"><b>마셜 로젠버그</b></font> 박사님</li>\n" +
            "<li>명상하는 법을 알려준 <font color=\"#color\"><b>진영</b></font>님</li>\n" +
            "<li>개발하는데 자극이 되어준 <font color=\"#color\"><b>장준</b></font>님</li>\n" +
            "<li>삶에 활력이 되어준 <font color=\"#color\"><b>임프로그</b></font> 멤버들</li>\n" +
            "<li>꾸준한 책읽기를 도와준 <font color=\"#color\"><b>내아인</b></font> 사람들</li>\n" +
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