package com.zettafantasy.giraffe.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * navigation 사용시 뒤로가기 누르면 매번 새로 onCreateView 호출됨
 * 뷰 재사용을 위해 공통화시킴
 */
abstract class BaseBindingFragment<T : ViewBinding> : Fragment() {

    protected lateinit var binding: T
    private var initialized = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!initialized) {
            binding = init(inflater, container)
            initialized = true
        }

        return binding.root
    }

    abstract fun init(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): T
}