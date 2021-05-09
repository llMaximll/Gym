package com.github.llmaximll.gym.fragments.otherfragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.llmaximll.gym.BuildConfig
import com.github.llmaximll.gym.R
import com.github.llmaximll.gym.dataclasses.Exercise
import com.github.llmaximll.gym.vm.PushUpsVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.math.round

private const val KEY_NAME_EX = "key_name_ex"
private const val KEY_NUMBER_EX = "key_number_ex"
private const val KEY_SCORES = "key_scores"
private const val KEY_REPETITION = "key_repetition"
private const val REQUEST_DIALOG_CODE_CANCEL = 1
private const val REQUEST_DIALOG_CODE_CONTINUE = 2

private const val TAG = "PushUpsFragment"

class PushUpsFragment : Fragment() {

    interface Callbacks {
        fun onPushUpsFragment(numberEx: Int?, minutes: Long?, cal: Float?, mode: Int)
    }

    private lateinit var viewModel: PushUpsVM
    private lateinit var gifWebView: WebView
    private lateinit var scoresTextView: TextView
    private lateinit var calTextView: TextView
    private lateinit var tapFrameLayout: ViewGroup
    private lateinit var stopImageButton: ImageButton
    private lateinit var shadowImageViewActivity: ImageView
    private lateinit var chronometer: Chronometer

    private var callbacks: Callbacks? = null
    private var nameEx: String = "name_exercise"
    private var numberEx: Int = 0
    private var scores: Int = 0
    private var minutes: Long = 0
    private var cal: Float = 0f
    private var chronometerIsActive = false
    private var exerciseDB: Exercise? = null
    private var isContinued = false
    private var isRepetition = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // arguments
        nameEx = arguments?.getString(KEY_NAME_EX, "name_ex") ?: "name_ex"
        numberEx = arguments?.getInt(KEY_NUMBER_EX, 0) ?: 0
        scores = arguments?.getInt(KEY_SCORES, 0) ?: 404
        isRepetition = arguments?.getBoolean(KEY_REPETITION, false) ?: false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_push_ups, container, false)

        gifWebView  = view.findViewById(R.id.webView2)
        scoresTextView = view.findViewById(R.id.count_scores_textView)
        calTextView = view.findViewById(R.id.count_cal_textView)
        tapFrameLayout = view.findViewById(R.id.tap_frameLayout)
        stopImageButton = view.findViewById(R.id.stop_imageButton)
        shadowImageViewActivity = activity?.findViewById(R.id.shadow_imageView)!!
        chronometer = view.findViewById(R.id.chronometer)

        gifWebView.loadUrl("file:///android_asset/otzimania.gif")

        initDB()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI(scores, cal)
    }

    private fun initDB() {
        viewModel = ViewModelProvider(this).get(PushUpsVM::class.java)
        CoroutineScope(Dispatchers.Default).launch {
            viewModel.initDB(requireContext())
            exerciseDB = viewModel.getExerciseDB(nameEx, numberEx.toString())
            createDialog()
        }
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
                    if (!chronometerIsActive && !isContinued) {
                        chronometer.base = SystemClock.elapsedRealtime()
                        chronometer.start()
                        chronometerIsActive = true
                    }
                    log(TAG, "time=${SystemClock.elapsedRealtime() - chronometer.base}")
                    if (scores > 0) {
                        scores--
                        minutes = SystemClock.elapsedRealtime() - chronometer.base
                        val calendar = GregorianCalendar.getInstance().apply {
                            time = Date(minutes)
                        }
                        val minute = calendar.get(Calendar.MINUTE)
                        val second = calendar.get(Calendar.SECOND)
                        val time: Float = minute + (second / 60f)
                        log(TAG, "minute=$minute, second=$second, time=$time")
                        if (scores != 0) cal = round(time / scores.toFloat() * 100) / 100
                        updateUI(scores, cal)
                        if (scores == 0) {
                            if (!isRepetition) jobWithDB()
                            callbacks?.onPushUpsFragment(numberEx, minutes, cal, 0)
                        }
                    }
                    view.performClick()
                }
            }
            true
        }
        stopImageButton.setOnClickListener {
            minutes = SystemClock.elapsedRealtime() - chronometer.base
            shadowImageViewActivity.isVisible = true
            animateAlpha(shadowImageViewActivity)
            val dialogFragment = CancelExerciseDialogFragment.newInstance(isRepetition)
            dialogFragment.setTargetFragment(this, REQUEST_DIALOG_CODE_CANCEL)
            dialogFragment.show(parentFragmentManager, "CancelExerciseDialogFragment")
            onPause()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_DIALOG_CODE_CANCEL -> {
                    log(TAG, "onActivityResult | REQUEST_DIALOG_CODE_CANCEL")
                    if (!isRepetition) jobWithDB()
                    callbacks?.onPushUpsFragment(null, null, null, 1)
                }
                REQUEST_DIALOG_CODE_CONTINUE -> {
                    log(TAG, "onActivityResult | REQUEST_DIALOG_CODE_CONTINUE")
                    val bool = data?.getBooleanExtra(ResettingExercisesDialogFragment.TAG_RESET_EXERCISES_RESULT, false)
                    if (bool == true) {
                        continueExercise()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        shadowImageViewActivity.isVisible = false
        shadowImageViewActivity.alpha = 0f
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private suspend fun createDialog() {
        if (exerciseDB != null && exerciseDB?.scores != 0) {
            withContext(Dispatchers.Main) {
                shadowImageViewActivity.isVisible = true
                animateAlpha(shadowImageViewActivity)
                val dialogFragment = ResettingExercisesDialogFragment.newInstance()
                dialogFragment.setTargetFragment(this@PushUpsFragment, REQUEST_DIALOG_CODE_CANCEL)
                dialogFragment.show(parentFragmentManager, "ResettingExercisesDialogFragment")
                onPause()
            }
        } else {
            log(TAG, "initDB | exerciseDB = ${exerciseDB?.scores}")
        }
    }

    private fun continueExercise() {
        scores = exerciseDB?.scores ?: 0
        minutes = exerciseDB?.minutes ?: 0
        cal = exerciseDB?.cal ?: 0f
        updateUI(scores, cal)
        chronometer.base = SystemClock.elapsedRealtime() - minutes
        chronometer.start()
        isContinued = true
    }

    private fun animateAlpha(view: View) {
        val animatorAlpha = ObjectAnimator.ofFloat(
                view,
                "alpha",
                1.0f
        )
        animatorAlpha.duration = 500
        animatorAlpha.start()
    }

    private fun jobWithDB() {
        CoroutineScope(Dispatchers.IO).launch {
            val exercise: Exercise? = viewModel.getExerciseDB(nameEx, numberEx.toString())
            if (exercise != null) {
                val success = viewModel.updateExerciseDB(nameEx, numberEx.toString(),
                        scores, minutes, cal)
                if (success)
                    log(TAG, "Success | Update DB")
                else
                    log(TAG, "Failed | Update DB")
            } else {
                val success = viewModel.addExerciseDB(nameEx, numberEx.toString(),
                        scores, minutes, cal)
                if (success)
                    log(TAG, "Success | Insert DB")
                else
                    log(TAG, "Failed | Insert DB")
            }
        }
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
        fun newInstance(nameEx: String, numberEx: Int, scores: Int, isRepetition: Boolean): PushUpsFragment {
            val args = Bundle().apply {
                putString(KEY_NAME_EX, nameEx)
                putInt(KEY_NUMBER_EX, numberEx)
                putInt(KEY_SCORES, scores)
                putBoolean(KEY_REPETITION, isRepetition)
            }
            return PushUpsFragment().apply {
                arguments = args
            }
        }
    }
}