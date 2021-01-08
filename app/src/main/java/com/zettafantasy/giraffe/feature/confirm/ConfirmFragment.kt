package com.zettafantasy.giraffe.feature.confirm

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.zettafantasy.giraffe.GiraffeApplication
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.model.Emotion
import com.zettafantasy.giraffe.model.Need

class ConfirmFragment : Fragment() {
    private lateinit var emotions: List<Emotion>
    private lateinit var needs: List<Need>
    private lateinit var repository: GiraffeRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        repository = (activity?.application as GiraffeApplication).repository

        val view = inflater.inflate(R.layout.confirm_fragment, container, false)
        view.findViewById<AppCompatButton>(R.id.btn_save).setOnClickListener {
            //repository.insert()
            navigateRecordScreen(view)
        }

        emotions = arguments?.getParcelableArrayList<Emotion>(Emotion::class.simpleName)!!
        emotions?.let {
            view.findViewById<TextView>(R.id.emotions).text = TextUtils.join(", ", it)
        }


        val needs =
            arguments?.getParcelableArrayList<Need>(Need::class.simpleName)
        needs?.let {
            view.findViewById<TextView>(R.id.needs).text = TextUtils.join(", ", it)
        }

        return view
    }

    private fun navigateRecordScreen(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_confirm_to_record)
    }
}