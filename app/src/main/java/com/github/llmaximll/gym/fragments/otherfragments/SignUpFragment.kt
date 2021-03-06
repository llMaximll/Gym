package com.github.llmaximll.gym.fragments.otherfragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.llmaximll.gym.BuildConfig
import com.github.llmaximll.gym.R
import com.github.llmaximll.gym.vm.SignUpVM

private const val TAG = "SignUpFragment"
private const val NAME_SHARED_PREFERENCES = "shared_preferences"
private const val SP_HEIGHT = "sp_height"
private const val SP_WEIGHT = "sp_weight"

class SignUpFragment : Fragment() {

    interface Callbacks {
        fun onSignUpFragment()
    }

    private lateinit var viewModel: SignUpVM
    private lateinit var nameEditText: EditText
    private lateinit var mailEditText: EditText
    private lateinit var password1EditText: EditText
    private lateinit var password2EditText: EditText
    private lateinit var signUpImageButton: ImageButton

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_sign_up, container, false)

        nameEditText = view.findViewById(R.id.title_textView)
        mailEditText = view.findViewById(R.id.mail_editText)
        password1EditText = view.findViewById(R.id.password_1_textView)
        password2EditText = view.findViewById(R.id.password_2_textView)
        signUpImageButton = view.findViewById(R.id.signUp_imageButton)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    override fun onStart() {
        super.onStart()
        signUpImageButton.setOnClickListener {
            log(TAG, "Button is pressed")
            val sharedPreference =
                    context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val name = nameEditText.text.toString()
            val mail = mailEditText.text.toString()
            val height = sharedPreference?.getInt(SP_HEIGHT, 1).toString()
            val weight = sharedPreference?.getInt(SP_WEIGHT, 1).toString()
            if (checkField(name, mail, password1EditText.text.toString(), password2EditText.text.toString(), height, weight)) {
                viewModel.signUp(name, mail, password2EditText.text.toString(), height, weight)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun checkField(name: String, mail: String, password1: String, password2: String,
                           height: String, weight: String): Boolean {
        var flag = true
        when {
            name == "" -> {
                flag = false
            }
            mail == "" -> {
                flag = false
            }
            password1 == "" -> {
                flag = false
            }
            password2 == "" -> {
                flag = false
            }
            height == "" -> {
                flag = false
            }
            weight == "" -> {
                flag = false
            }
        }

        if (!flag) {
            toast("???????????????????? ?????????????????? ?????? ????????")
        } else {
            if (password1 != password2) {
                flag = false
                toast("???????????? ???? ??????????????????")
            } else {
                var charFlag = false
                mail.forEach { char ->
                    if (char == '@') {
                        charFlag = true
                    }
                }
                if (!charFlag) {
                    flag = false
                    toast("???????????????????????? email")
                }
            }
        }

        return flag
    }

    private fun initObservers() {
        viewModel = ViewModelProvider(this).get(SignUpVM::class.java)
        viewModel.cosmeticView.message.observe(
                viewLifecycleOwner,
                { message ->
                    toast(message)
                }
        )
        viewModel.cosmeticView.isSuccessful.observe(
                viewLifecycleOwner,
                { isSuccessful ->
                    if (isSuccessful) {
                        callbacks?.onSignUpFragment()
                    }
                }
        )
    }

    private fun log(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }

    private fun toast(message: String) {
        val toast = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }

    companion object {
        fun newInstance(): SignUpFragment = SignUpFragment()
    }
}