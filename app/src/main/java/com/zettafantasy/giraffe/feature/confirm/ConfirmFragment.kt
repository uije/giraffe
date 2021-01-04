package com.zettafantasy.giraffe.feature.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.zettafantasy.giraffe.R

class ConfirmFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.confirm_fragment, container, false)
        view.findViewById<AppCompatButton>(R.id.btn_save).setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_confirm_to_history)
        }
        return view
    }
}