package com.github.llmaximll.gym.fragments.otherfragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.llmaximll.gym.BuildConfig
import com.github.llmaximll.gym.R
import com.github.llmaximll.gym.database.DatabaseHandler
import com.github.llmaximll.gym.dataclasses.Exercise
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val KEY_NAME_EX = "key_name_ex"
private const val KEY_NUMBER_EX = "key_number_ex"
private const val KEY_SCORES = "key_scores"

private const val TAG = "PushUpsFragment"

class PushUpsFragment : Fragment() {

    private lateinit var gifWebView: WebView
    private lateinit var scoresTextView: TextView
    private lateinit var minutesTextView: TextView
    private lateinit var calTextView: TextView
    private lateinit var tapFrameLayout: ViewGroup

    private var nameEx: String = "name_exercise"
    private var numberEx: Int = 0
    private var scores: Int = 0
    private var minutes: Int = 0
    private var cal: Float = 0f
    private var dbHandler: DatabaseHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // arguments
        nameEx = arguments?.getString(KEY_NAME_EX, "name_ex") ?: "name_ex"
        numberEx = arguments?.getInt(KEY_NUMBER_EX, 0) ?: 0
        log(TAG, "numberEx=$numberEx")
        scores = arguments?.getInt(KEY_SCORES, 0) ?: 404
        cal = 4f
        // init DB
        dbHandler = DatabaseHandler(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_push_ups, container, false)

        gifWebView  = view.findViewById(R.id.webView2)
        scoresTextView = view.findViewById(R.id.count_scores_textView)
        minutesTextView = view.findViewById(R.id.count_minutes_textView)
        calTextView = view.findViewById(R.id.count_cal_textView)
        tapFrameLayout = view.findViewById(R.id.tap_frameLayout)

        gifWebView.loadUrl("file:///android_asset/otzimania.gif")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI(scores, 1, 2f)
    }

    override fun onStart() {
        super.onStart()
        tapFrameLayout.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    animateView(view, false)
                }
                MotionEvent.ACTION_CANCEL -> {
                    animateView(view, true)
                }
                MotionEvent.ACTION_UP -> {
                    animateView(view, true)
                    if (scores > 0) {
                        scores--
                        scoresTextView.text = scores.toString()
                        if (scores == 0) {
                            CoroutineScope(Dispatchers.IO).launch {
                                val exercise: Exercise? = getExerciseDB(nameEx, numberEx.toString())
                                if (exercise != null) {
                                    updateExerciseDB(scores, minutes, cal)
                                } else {
                                    addExerciseDB(scores, minutes, cal)
                                }
                            }
                        }
                    }
                    view.performClick()
                }
            }
            true
        }
    }

    private fun getExerciseDB(nameEx: String, numberEx: String): Exercise? =
            dbHandler?.getExercise(nameEx, numberEx)

    private fun addExerciseDB(scores: Int, minutes: Int, cal: Float) {
        val exercise = Exercise()
        exercise.name = nameEx
        exercise.numberEx = numberEx.toString()
        exercise.scores = scores
        exercise.minutes = minutes
        exercise.cal = cal

        val success: Boolean = dbHandler!!.addExercise(exercise)

        if (success)
            log(TAG, "Success | Insert DB")
        else
            log(TAG, "Failed | Insert DB")
    }

    private fun updateExerciseDB(scores: Int, minutes: Int, cal: Float) {
        val success = dbHandler!!.updateExercise(nameEx, numberEx.toString(), scores, minutes, cal)

        if (success)
            log(TAG, "Success | Update DB")
        else
            log(TAG, "Failed | Update DB")
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

    private fun updateUI(scores: Int, minutes: Int, cal: Float) {
        scoresTextView.text = scores.toString()
        minutesTextView.text = minutes.toString()
        calTextView.text = cal.toString()
    }

    private fun toast(message: String) {
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

    private fun log(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }

    companion object {
        fun newInstance(nameEx: String, numberEx: Int, scores: Int): PushUpsFragment {
            val args = Bundle().apply {
                putString(KEY_NAME_EX, nameEx)
                putInt(KEY_NUMBER_EX, numberEx)
                putInt(KEY_SCORES, scores)
            }
            return PushUpsFragment().apply {
                arguments = args
            }
        }
    }
}