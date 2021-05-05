package com.github.llmaximll.gym.fragments.otherfragments

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.github.llmaximll.gym.R

class ProfileBioDialogFragment : DialogFragment() {

    private lateinit var cancelButton: Button
    private lateinit var okButton: Button
    private lateinit var weightEditText: EditText
    private lateinit var heightEditText: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.dialog_profile_bio, container, false)

        cancelButton = view.findViewById(R.id.cancel_button)
        okButton = view.findViewById(R.id.ok_button)
        weightEditText = view.findViewById(R.id.weight_editText)
        heightEditText  = view.findViewById(R.id.height_editText)

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
            if (weightEditText.text.toString() != "" && heightEditText.text.toString() != "") {
                val intent = Intent().apply {
                    putExtra(NAME_TAG_WEIGHT, weightEditText.text.toString().toInt())
                    putExtra(NAME_TAG_HEIGHT, heightEditText.text.toString().toInt())
                }
                targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
                dismiss()
            } else {
                toast("Все поля должны быть заполнены")
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        parentFragmentManager.fragments[0].onResume()
    }

    private fun toast(message: String) {
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

    companion object {
        fun newInstance(): ProfileBioDialogFragment = ProfileBioDialogFragment()
        const val NAME_TAG_WEIGHT = "name_tag_weight"
        const val NAME_TAG_HEIGHT = "name_tag_height"
    }
}