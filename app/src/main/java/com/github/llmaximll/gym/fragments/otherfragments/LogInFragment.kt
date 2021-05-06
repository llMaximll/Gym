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
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.llmaximll.gym.BuildConfig
import com.github.llmaximll.gym.R
import com.github.llmaximll.gym.vm.LogInVM

private const val NAME_SHARED_PREFERENCES = "shared_preferences"
private const val SP_TOKEN = "sp_t"
private const val SP_USERNAME = "sp_username"
private const val TAG = "LogInFragment"

class LogInFragment : Fragment() {

    interface Callbacks {
        fun onLogInFragment(mode: Int)
    }

    private lateinit var viewModel: LogInVM
    private lateinit var nameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInImageButton: ImageButton
    private lateinit var signUp: TextView

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_log_in, container, false)

        nameEditText = view.findViewById(R.id.mail_editText)
        passwordEditText = view.findViewById(R.id.editText2)
        signUp = view.findViewById(R.id.sign_up_textView)
        signInImageButton = view.findViewById(R.id.signUp_imageButton)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    override fun onStart() {
        super.onStart()
        signInImageButton.setOnClickListener {
            if (nameEditText.text.toString() != "") {
                if (passwordEditText.text.toString() != "") {
                    viewModel.signIn(nameEditText.text.toString(), passwordEditText.text.toString())
                } else {
                    toast("Поле 'Пароль' пустое")
                }
            } else {
                toast("Поле 'Логин' пустое")
            }
        }
        signUp.setOnClickListener {
            callbacks?.onLogInFragment(0)
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun initObservers() {
        viewModel = ViewModelProvider(this).get(LogInVM::class.java)
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
                        callbacks?.onLogInFragment(1)
                    }
                }
        )
        viewModel.cosmeticView.token.observe(
                viewLifecycleOwner,
                { token ->
                    if (token != null) {
                        val sharedPreference =
                                context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                        val editor = sharedPreference?.edit()
                        editor?.putInt(SP_TOKEN, token)
                        editor?.putString(SP_USERNAME, nameEditText.text.toString())
                        editor?.apply()
                        log(TAG, "token is saved [$token]")
                    } else {
                        log(TAG, "token = null")
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
        fun newInstance(): LogInFragment = LogInFragment()
    }
}