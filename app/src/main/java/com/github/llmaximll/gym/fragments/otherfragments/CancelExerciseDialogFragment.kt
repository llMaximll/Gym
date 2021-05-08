package com.github.llmaximll.gym.fragments.otherfragments

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.github.llmaximll.gym.R

class CancelExerciseDialogFragment : DialogFragment() {

    private lateinit var cancelButton: Button
    private lateinit var okButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.dialog_cancel_exercise, container, false)

        cancelButton = view.findViewById(R.id.cancel_button)
        okButton = view.findViewById(R.id.ok_button)

        dialog?.window?.setDimAmount(0f)
        dialog?.window?.setWindowAnimations(android.R.style.Animation_Dialog)

        return view
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

    companion object {
        fun newInstance(): CancelExerciseDialogFragment = CancelExerciseDialogFragment()
        const val NAME_TAG_RESULT = "name_tag_result"
    }
}