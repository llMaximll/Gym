package com.github.llmaximll.gym.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.github.llmaximll.gym.R

private const val NAME_SHARED_PREFERENCES = "shared_preferences"
private const val SP_HEIGHT = "sp_height"
private const val SP_WEIGHT = "sp_weight"

class Hello5Fragment : Fragment() {

    interface Callbacks {
        fun onHello5Fragment()
    }

    private lateinit var heightEditText: EditText
    private lateinit var weightEditText: EditText
    private lateinit var nextImageBoolean: ImageButton

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_hello_5, container, false)

        heightEditText = view.findViewById(R.id.height_editText)
        weightEditText = view.findViewById(R.id.weight_editText)
        nextImageBoolean = view.findViewById(R.id.next_imageButton)

        return view
    }

    override fun onStart() {
        super.onStart()
        nextImageBoolean.setOnClickListener {
            callbacks?.onHello5Fragment()
        }
    }

    override fun onStop() {
        super.onStop()
        val height: Int? = try {
            heightEditText.text.toString().toInt()
        } catch (e: Exception) {
            null
        }
        val weight: Int? = try {
            weightEditText.text.toString().toInt()
        } catch (e: Exception) {
            null
        }
        if (height != null && weight != null) {
            val sharedPreference =
                    context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val editor = sharedPreference?.edit()
            editor?.putInt(SP_HEIGHT, height)
            editor?.putInt(SP_WEIGHT, weight)
            editor?.apply()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance(): Hello5Fragment = Hello5Fragment()
    }
}