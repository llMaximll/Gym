package com.github.llmaximll.gym.fragments.otherfragments

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.github.llmaximll.gym.R

class GenderDialogFragment : DialogFragment() {

    private lateinit var cancelButton: Button
    private lateinit var okButton: Button
    private lateinit var femaleImageView: ImageView
    private lateinit var maleImageView: ImageView

    private var gender: Boolean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.dialog_gender, container, false)

        cancelButton = view.findViewById(R.id.cancel_button)
        okButton = view.findViewById(R.id.ok_button)
        femaleImageView = view.findViewById(R.id.female_imageView)
        maleImageView = view.findViewById(R.id.male_imageView)

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
                putExtra(NAME_TAG_GENDER, gender)
            }
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            dismiss()
        }
        femaleImageView.setOnClickListener {
            it.setBackgroundResource(R.drawable.button_launch_rectangle_on)
            maleImageView.setBackgroundResource(R.drawable.button_launch_rectangle_off)
            gender = false
        }
        maleImageView.setOnClickListener {
            it.setBackgroundResource(R.drawable.button_launch_rectangle_on)
            femaleImageView.setBackgroundResource(R.drawable.button_launch_rectangle_off)
            gender = true
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        parentFragmentManager.fragments[0].onResume()
    }

    companion object {
        fun newInstance(): GenderDialogFragment = GenderDialogFragment()
        val NAME_TAG_GENDER = "name_tag_gender"
    }
}