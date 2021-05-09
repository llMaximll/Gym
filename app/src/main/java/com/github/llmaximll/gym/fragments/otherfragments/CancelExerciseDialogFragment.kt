package com.github.llmaximll.gym.fragments.otherfragments

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.github.llmaximll.gym.R

private const val KEY_REPETITION = "key_repetition"

class CancelExerciseDialogFragment : DialogFragment() {

    private lateinit var cancelButton: Button
    private lateinit var okButton: Button
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView

    private var isRepetition = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isRepetition = arguments?.getBoolean(KEY_REPETITION, false) ?: false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.dialog_cancel_exercise, container, false)

        cancelButton = view.findViewById(R.id.cancel_button)
        okButton = view.findViewById(R.id.ok_button)
        titleTextView = view.findViewById(R.id.title_textView)
        descriptionTextView = view.findViewById(R.id.description_textView)

        dialog?.window?.setDimAmount(0f)
        dialog?.window?.setWindowAnimations(android.R.style.Animation_Dialog)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI(isRepetition)
    }

    override fun onStart() {
        super.onStart()
        cancelButton.setOnClickListener {
            dismiss()
        }
        okButton.setOnClickListener {
            val intent = Intent().apply {
                putExtra(NAME_TAG_RESULT, true)
            }
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        parentFragmentManager.fragments[0].onResume()
    }

    private fun updateUI(isRepetition: Boolean) {
        if (isRepetition) {
            titleTextView.text = "Repeat Mode"
            descriptionTextView.text = "Are you sure you want to get out? Changes are not saved."
        }
    }

    companion object {
        fun newInstance(isRepetition: Boolean): CancelExerciseDialogFragment {
            val args = Bundle().apply {
                putBoolean(KEY_REPETITION, isRepetition)
            }
            return CancelExerciseDialogFragment().apply {
                arguments = args
            }
        }
        const val NAME_TAG_RESULT = "name_tag_result"
    }
}