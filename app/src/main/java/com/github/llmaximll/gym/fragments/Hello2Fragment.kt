package com.github.llmaximll.gym.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.llmaximll.gym.R

private const val NAME_SHARED_PREFERENCES = "shared_preferences"
private const val GENDER = "gender"

private const val TAG = "Hello2Fragment"

class Hello2Fragment : Fragment() {

    private var gender: Int? = null

    interface Callbacks {
        fun onHello2Fragment(gender: Int)
    }

    private lateinit var femaleImageButton: ImageButton
    private lateinit var maleImageButton: ImageButton
    private lateinit var nextImageButton: ImageButton

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_hello_2, container, false)

        femaleImageButton = view.findViewById(R.id.female_imageButton)
        maleImageButton = view.findViewById(R.id.male_imageButton)
        nextImageButton = view.findViewById(R.id.next_imageButton)

        return view
    }

    override fun onStart() {
        super.onStart()
        femaleImageButton.setOnClickListener {
            gender = 0
            femaleImageButton.setBackgroundResource(R.drawable.button_launch_rectangle_on)
            maleImageButton.setBackgroundResource(R.drawable.button_launch_rectangle_off)
        }
        maleImageButton.setOnClickListener {
            gender = 1
            femaleImageButton.setBackgroundResource(R.drawable.button_launch_rectangle_off)
            maleImageButton.setBackgroundResource(R.drawable.button_launch_rectangle_on)
        }
        nextImageButton.setOnClickListener {
            if (gender != null) {
                callbacks?.onHello2Fragment(gender!!)
            } else {
                Toast.makeText(
                        requireContext(),
                        "Choose gender",
                        Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (gender != null) {
            val sharedPreference =
                    context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val editor = sharedPreference?.edit()
            editor?.putInt(GENDER, gender!!)
            editor?.apply()
        }
        Log.i(TAG, "gender=$gender")
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance(): Hello2Fragment = Hello2Fragment()
    }
}