package com.github.llmaximll.gym.fragments

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
import com.github.llmaximll.gym.BuildConfig
import com.github.llmaximll.gym.R
import com.github.llmaximll.gym.retrofit.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val NAME_SHARED_PREFERENCES = "shared_preferences"
private const val SP_TOKEN = "sp_t"
private const val TAG = "LogInFragment"

class LogInFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInImageButton: ImageButton

    interface Callbacks {
        fun onLogInFragment(mode: Int)
    }

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

    override fun onStart() {
        super.onStart()
        signInImageButton.setOnClickListener {
            log(TAG, "Button is pressed")
            if (nameEditText.text.toString() != "") {
                if (passwordEditText.text.toString() != "") {
                    NetworkService.instance
                            ?.getJSONApi()
                            ?.signIn(nameEditText.text.toString(), passwordEditText.text.toString())
                            ?.enqueue(object : Callback<Map<String, Map<String, Int>>> {
                                override fun onResponse(call: Call<Map<String, Map<String, Int>>>?, response: Response<Map<String, Map<String, Int>>>?) {
                                    val post = response?.body()
                                    if (post != null) {
                                        saveToken(post)
                                    }
                                    callbacks?.onLogInFragment(1)
                                    toast("Успешно!")
                                    log(TAG, "post=$post")
                                }
                                override fun onFailure(call: Call<Map<String, Map<String, Int>>>?, t: Throwable?) {
                                    when (t?.message) {
                                        "java.lang.NumberFormatException: For input string: \"Error username or password\"" -> {
                                            toast("Неверный логин или пароль")
                                        }
                                        "java.lang.NumberFormatException: For input string: \"User is active\"" -> {
                                            toast("Пользователь уже активен")
                                        }
                                        else -> {
                                            toast(t?.message!!)
                                            t.printStackTrace()
                                        } }
                                }
                            })
                } else {
                    toast("Поле 'Пароль' пустое")
                }
            } else {
                toast("Поле 'Логин' пустое")
            }
        }
        signUp.setOnClickListener {
            val sharedPreference =
                    context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val token = sharedPreference?.getInt(SP_TOKEN, 0)
            log(TAG, "token=$token")
            callbacks?.onLogInFragment(0)
        }
    }

    private fun saveToken(post: Map<String, Map<String, Int>>) {
        val sharedPreference =
                context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreference?.edit()
        val token: Int = post["notice"]?.get("token")!!
        editor?.putInt(SP_TOKEN, token)
        editor?.apply()
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
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