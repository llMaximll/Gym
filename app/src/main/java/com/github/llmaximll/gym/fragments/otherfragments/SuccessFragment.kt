package com.github.llmaximll.gym.fragments.otherfragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.llmaximll.gym.R
import kotlin.time.minutes

private const val KEY_SCORES = "key_scores"
private const val KEY_MINUTES = "key_minutes"
private const val KEY_CAL = "key_cal"

class SuccessFragment : Fragment() {

    interface Callbacks {
        fun onSuccessFragment()
    }

    private lateinit var scoresTextView: TextView
    private lateinit var chronometer: Chronometer
    private lateinit var calTextView: TextView
    private lateinit var tapFrameLayout: ViewGroup

    private var callbacks: Callbacks? = null
    private var scores: Int = 0
    private var minutes: Long = 0
    private var cal: Float = 0f

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scores = arguments?.getInt(KEY_SCORES, 0) ?: 0
        minutes = arguments?.getLong(KEY_MINUTES, 0) ?: 0
        cal = arguments?.getFloat(KEY_CAL, 0f) ?: 0f
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_success, container, false)

        scoresTextView = view.findViewById(R.id.count_scores_textView)
        chronometer = view.findViewById(R.id.chronometer)
        calTextView = view.findViewById(R.id.count_cal_textView)
        tapFrameLayout = view.findViewById(R.id.tap_frameLayout)

        chronometer.base = SystemClock.elapsedRealtime() - minutes

        return view
    }

    override fun onStart() {
        super.onStart()
        tapFrameLayout.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    animateView(view, false)
                }
                MotionEvent.ACTION_CANCEL -> {
                    animateView(view, true)
                }
                MotionEvent.ACTION_UP -> {
                    animateView(view, true)
                    callbacks?.onSuccessFragment()
                    view.performClick()
                }
            }
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI(scores, cal)
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun animateView(view: View, reverse: Boolean) {
        if (!reverse) {
            val animatorX = ObjectAnimator.ofFloat(view, "scaleX", 0.9f)
            val animatorY = ObjectAnimator.ofFloat(view, "scaleY", 0.9f)
            AnimatorSet().apply {
                playTogether(animatorX, animatorY)
                duration = 150
                start()
            }
        } else {
            val animatorX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f)
            val animatorY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f)
            AnimatorSet().apply {
                playTogether(animatorX, animatorY)
                duration = 150
                start()
            }
        }
    }

    private fun updateUI(scores: Int, cal: Float) {
        scoresTextView.text = scores.toString()
        calTextView.text = cal.toString()
    }

    companion object {
        fun newInstance(numberEx: Int, minutes: Long, cal: Float): SuccessFragment {
            val args = Bundle().apply {
                putInt(KEY_SCORES, numberEx)
                putLong(KEY_MINUTES, minutes)
                putFloat(KEY_CAL, cal)
            }
            return SuccessFragment().apply {
                arguments = args
            }
        }
    }
}