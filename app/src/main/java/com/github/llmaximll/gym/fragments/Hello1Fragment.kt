package com.github.llmaximll.gym.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.github.llmaximll.gym.R

private const val TAG = "Hello1Fragment"

class Hello1Fragment : Fragment() {

    interface Callbacks {
        fun onHello1Fragment()
    }

    private lateinit var imageButton1: ImageButton
    private lateinit var imageButton2: ImageButton
    private lateinit var imageButton3: ImageButton

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_hello_1, container, false)

        imageButton1 = view.findViewById(R.id.imageButton1)
        imageButton2 = view.findViewById(R.id.imageButton2)
        imageButton3 = view.findViewById(R.id.imageButton3)

        return view
    }

    override fun onStart() {
        super.onStart()
        imageButton1.setOnClickListener {
            callbacks?.onHello1Fragment()
            Log.i(TAG, "callback")
        }
        imageButton2.setOnClickListener {
            callbacks?.onHello1Fragment()
            Log.i(TAG, "callback")
        }
        imageButton3.setOnClickListener {
            callbacks?.onHello1Fragment()
            Log.i(TAG, "callback")
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance() : Hello1Fragment =
                Hello1Fragment()
    }
}