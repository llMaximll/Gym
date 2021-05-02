package com.github.llmaximll.gym.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.llmaximll.gym.R

class LessonsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_lessons, container, false)



        return view
    }

    companion object {
        fun newInstance(): LessonsFragment = LessonsFragment()
    }
}