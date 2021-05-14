package com.github.llmaximll.gym.fragments.otherfragments

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.github.llmaximll.gym.R

private const val KEY_ARG_REST = "key_arg_rest"

class TimeoutDialogFragment : DialogFragment() {

    private lateinit var countTextView: TextView
    private lateinit var countSeekBar: SeekBar
    private lateinit var cancelButton: Button
    private lateinit var okButton: Button

    private var rest = 30L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rest = arguments?.getLong(KEY_ARG_REST, 30L) ?: 30L
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.dialog_timeout, container, false)

        countTextView = view.findViewById(R.id.count_textView)
        countSeekBar = view.findViewById(R.id.count_seekBar)
        cancelButton = view.findViewById(R.id.cancel_button)
        okButton = view.findViewById(R.id.ok_button)

        countSeekBar.progress = rest.toInt() / 1000
        countTextView.text = "${countSeekBar.progress} с"

        dialog?.window?.setDimAmount(0f)
        dialog?.window?.setWindowAnimations(android.R.style.Animation_Dialog)

        return view
    }

    override fun onStart() {
        super.onStart()
        countSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) {
                countTextView.text = "$progress с"
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
                // ***
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
                // ***
            }
        })
        cancelButton.setOnClickListener {
            dismiss()
        }
        okButton.setOnClickListener {
            val intent = Intent().apply {
                putExtra(INTENT_TAG_TIMEOUT_COUNT, countSeekBar.progress)
            }
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        parentFragmentManager.fragments[0].onResume()
    }

    companion object {
        fun newInstance(rest: Long): TimeoutDialogFragment {
            val args = Bundle().apply {
                putLong(KEY_ARG_REST, rest)
            }
            return TimeoutDialogFragment().apply {
                arguments = args
            }
        }
        const val INTENT_TAG_TIMEOUT_COUNT = "intent_tag_timeout_count"
    }
}