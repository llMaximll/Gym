package com.github.llmaximll.gym.fragments.otherfragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.github.llmaximll.gym.R

class PlanFragment : Fragment() {

    interface Callbacks {
        fun onPlanFragment(category: String)
    }

    private lateinit var handsFrameLayout: ViewGroup
    private lateinit var spineFrameLayout: ViewGroup
    private lateinit var torsoFrameLayout: ViewGroup
    private lateinit var legsFrameLayout: ViewGroup

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_plan, container, false)

        handsFrameLayout = view.findViewById(R.id.hands_frameLayout)
        spineFrameLayout = view.findViewById(R.id.spine_frameLayout)
        torsoFrameLayout = view.findViewById(R.id.torso_frameLayout)
        legsFrameLayout = view.findViewById(R.id.legs_frameLayout)

        return view
    }

    override fun onStart() {
        super.onStart()
        handsFrameLayout.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    animateView(view, false)
                }
                MotionEvent.ACTION_CANCEL -> {
                    animateView(view, true)
                }
                MotionEvent.ACTION_UP -> {
                    animateView(view, true)
                    callbacks?.onPlanFragment("hands")
                    view.performClick()
                }
            }
            true
        }
        spineFrameLayout.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    animateView(view, false)
                }
                MotionEvent.ACTION_CANCEL -> {
                    animateView(view, true)
                }
                MotionEvent.ACTION_UP -> {
                    animateView(view, true)
                    callbacks?.onPlanFragment("spine")
                    view.performClick()
                }
            }
            true
        }
        torsoFrameLayout.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    animateView(view, false)
                }
                MotionEvent.ACTION_CANCEL -> {
                    animateView(view, true)
                }
                MotionEvent.ACTION_UP -> {
                    animateView(view, true)
                    callbacks?.onPlanFragment("torso")
                    view.performClick()
                }
            }
            true
        }
        legsFrameLayout.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    animateView(view, false)
                }
                MotionEvent.ACTION_CANCEL -> {
                    animateView(view, true)
                }
                MotionEvent.ACTION_UP -> {
                    animateView(view, true)
                    view.performClick()
                }
            }
            true
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun animateView(view: View, reverse: Boolean) {
        if (!reverse) {
            val animatorX = ObjectAnimator.ofFloat(view, "scaleX", 0.95f)
            val animatorY = ObjectAnimator.ofFloat(view, "scaleY", 0.95f)
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

    companion object {
        fun newInstance(): PlanFragment = PlanFragment()
    }
}