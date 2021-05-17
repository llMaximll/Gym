package com.github.llmaximll.gym.fragments.otherfragments

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener2
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.github.llmaximll.gym.BuildConfig
import com.github.llmaximll.gym.R
import com.github.llmaximll.gym.customviews.TrunkInclinationsView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val KEY_NAME_EX = "key_name_ex"
private const val KEY_NUMBER_EX = "key_number_ex"
private const val KEY_SCORES = "key_scores"
private const val KEY_REPETITION = "key_repetition"

private const val TAG = "TrunkInclinationsFragment"

class TrunkInclinationsFragment : Fragment() {

    private lateinit var stopImageButton: ImageButton
    private lateinit var trunkIncView: TrunkInclinationsView
    private lateinit var sensorManager: SensorManager
    private lateinit var sensorListener: SensorEventListener2
    private lateinit var gifWebView: WebView

    private var nameEx: String = "name_exercise"
    private var numberEx: Int = 0
    private var scores: Int = 0
    private var isRepetition = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // arguments
        nameEx = arguments?.getString(KEY_NAME_EX, "name_ex") ?: "name_ex"
        numberEx = arguments?.getInt(KEY_NUMBER_EX, 0) ?: 0
        scores = arguments?.getInt(KEY_SCORES, 0)?.times(2) ?: 404
        isRepetition = arguments?.getBoolean(KEY_REPETITION, false) ?: false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_trunk_inclinations, container, false)

        gifWebView  = view.findViewById(R.id.webView)
        stopImageButton = view.findViewById(R.id.stop_imageButton)
        trunkIncView = view.findViewById(R.id.trunkIncView)

        gifWebView.loadUrl("file:///android_asset/trunk_inclinations.gif")

        return view
    }

    override fun onStart() {
        super.onStart()
        stopImageButton.setOnClickListener {
            trunkIncView.arrowAngle += 0.5f
        }
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            getAccelerometerCounts()
        }
    }

    private fun getAccelerometerCounts() {
        sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            sensorListener = object : SensorEventListener2 {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event?.values?.get(0) != null) {
                        val position = 0 - event.values?.get(0)!!
                        trunkIncView.arrowAngle = position
                    }
                }
                override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

                }
                override fun onFlushCompleted(p0: Sensor?) {

                }
            }
            sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), SensorManager.SENSOR_STATUS_ACCURACY_LOW)
        } else {
            log(TAG, "sensor = null")
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorListener)
    }

    private fun log(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }

    companion object {
        fun newInstance(nameEx: String, numberEx: Int, scores: Int, isRepetition: Boolean): TrunkInclinationsFragment {
            val args = Bundle().apply {
                putString(KEY_NAME_EX, nameEx)
                putInt(KEY_NUMBER_EX, numberEx)
                putInt(KEY_SCORES, scores)
                putBoolean(KEY_REPETITION, isRepetition)
            }
            return TrunkInclinationsFragment().apply {
                arguments = args
            }
        }
    }
}