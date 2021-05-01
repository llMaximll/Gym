package com.github.llmaximll.gym.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.llmaximll.gym.R
import com.github.llmaximll.gym.fragments.hello.Hello3Fragment

class LogInFragment : Fragment() {

    interface Callbacks {
        fun onLogInFragment()
    }

    private lateinit var signUp: TextView

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_log_in, container, false)

        signUp = view.findViewById(R.id.sign_up_textView)

        return view
    }

    override fun onStart() {
        super.onStart()
        signUp.setOnClickListener {
            callbacks?.onLogInFragment()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance(): LogInFragment = LogInFragment()
    }
}