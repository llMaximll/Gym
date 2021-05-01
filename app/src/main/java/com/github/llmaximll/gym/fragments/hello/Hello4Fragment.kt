package com.github.llmaximll.gym.fragments.hello

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.github.llmaximll.gym.R

private const val NAME_SHARED_PREFERENCES = "shared_preferences"
private const val EXERCISES = "exercises"

class Hello4Fragment : Fragment() {

    interface Callbacks {
        fun onHello4Fragment()
    }

    private lateinit var newbieFrameLayout: ViewGroup
    private lateinit var keepOnFrameLayout: ViewGroup
    private lateinit var advancedFrameLayout: ViewGroup
    private lateinit var nextImageButton: ImageButton

    private var callbacks: Callbacks? = null
    private var exercise: Int? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_hello_4, container, false)

        newbieFrameLayout = view.findViewById(R.id.frameLayout1)
        keepOnFrameLayout = view.findViewById(R.id.frameLayout2)
        advancedFrameLayout = view.findViewById(R.id.frameLayout3)
        nextImageButton = view.findViewById(R.id.next_imageButton)

        return view
    }

    override fun onStart() {
        super.onStart()
        newbieFrameLayout.setOnClickListener {
            exercise = 0
            newbieFrameLayout.setBackgroundResource(R.drawable.button_launch_rectangle_on)
            keepOnFrameLayout.setBackgroundResource(R.drawable.button_launch_rectangle_off)
            advancedFrameLayout.setBackgroundResource(R.drawable.button_launch_rectangle_off)
        }
        keepOnFrameLayout.setOnClickListener {
            exercise = 0
            newbieFrameLayout.setBackgroundResource(R.drawable.button_launch_rectangle_off)
            keepOnFrameLayout.setBackgroundResource(R.drawable.button_launch_rectangle_on)
            advancedFrameLayout.setBackgroundResource(R.drawable.button_launch_rectangle_off)
        }
        advancedFrameLayout.setOnClickListener {
            exercise = 0
            newbieFrameLayout.setBackgroundResource(R.drawable.button_launch_rectangle_off)
            keepOnFrameLayout.setBackgroundResource(R.drawable.button_launch_rectangle_off)
            advancedFrameLayout.setBackgroundResource(R.drawable.button_launch_rectangle_on)
        }
        nextImageButton.setOnClickListener {
            callbacks?.onHello4Fragment()
        }
    }

    override fun onStop() {
        super.onStop()
        if (exercise != null) {
            val sharedPreference =
                    context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val editor = sharedPreference?.edit()
            editor?.putInt(EXERCISES, exercise!!)
            editor?.apply()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance(): Hello4Fragment = Hello4Fragment()
    }
}