package com.github.llmaximll.gym.fragments.hellofragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.github.llmaximll.gym.R

private const val NAME_SHARED_PREFERENCES = "shared_preferences"
private const val TRAIN = "train"
private const val GENDER = "gender"
private const val KEY_GENDER = "key_gender"

private const val TAG = "Hello3Fragment"

class Hello3Fragment : Fragment() {

    interface Callbacks {
        fun onHello3Fragment()
    }

    private lateinit var handsImageButton: ImageButton
    private lateinit var spineImageButton: ImageButton
    private lateinit var torsoImageButton: ImageButton
    private lateinit var legsImageButton: ImageButton
    private lateinit var humanImageView: ImageView

    private var gender: Int? = null

    private var callbacks: Callbacks? = null
    private var train: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gender = arguments?.getInt(KEY_GENDER) as Int
        Log.i(TAG, "gender=$gender")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_hello_3, container, false)

        handsImageButton = view.findViewById(R.id.hands_imageButton)
        spineImageButton = view.findViewById(R.id.spine_imageButton)
        torsoImageButton = view.findViewById(R.id.torso_imageButton)
        legsImageButton = view.findViewById(R.id.legs_imageButton)
        humanImageView = view.findViewById(R.id.human_imageView)

        if (gender != -1) {
            when (gender) {
                0 -> humanImageView.setBackgroundResource(R.mipmap.woman)
                1 -> humanImageView.setBackgroundResource(R.mipmap.man)
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        handsImageButton.setOnClickListener {
            train = 0
            callbacks?.onHello3Fragment()
        }
        spineImageButton.setOnClickListener {
            train = 1
            callbacks?.onHello3Fragment()
        }
        torsoImageButton.setOnClickListener {
            train = 2
            callbacks?.onHello3Fragment()
        }
        legsImageButton.setOnClickListener {
            train = 3
            callbacks?.onHello3Fragment()
        }
    }

    override fun onStop() {
        super.onStop()
        if (train != null) {
            val sharedPreference =
                    context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val editor = sharedPreference?.edit()
            editor?.putInt(TRAIN, train!!)
            editor?.apply()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance(gender: Int): Hello3Fragment = Hello3Fragment().apply {
            arguments = Bundle().apply { putInt(KEY_GENDER, gender) }
        }
    }
}