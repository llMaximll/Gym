package com.github.llmaximll.gym.fragments.otherfragments

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.llmaximll.gym.BuildConfig
import com.github.llmaximll.gym.R
import com.github.llmaximll.gym.vm.ProfileVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val NAME_SHARED_PREFERENCES = "shared_preferences"
private const val SP_USERNAME = "sp_username"
private const val SP_TOKEN = "sp_t"
private const val TAG = "ProfileFragment"

class ProfileFragment : Fragment() {

    interface Callbacks {
        fun onProfileFragment()
    }

    private lateinit var signOutImageButton: ImageButton
    private lateinit var viewModel: ProfileVM
    private lateinit var username: String
    private lateinit var weightTextView: TextView
    private lateinit var heightTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var biometricTextView: TextView
    private lateinit var shadowImageView: ImageView

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreference =
                context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        username = sharedPreference?.getString(SP_USERNAME, "null-username")!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_profile, container, false)

        weightTextView = view.findViewById(R.id.count_weight_textView)
        heightTextView = view.findViewById(R.id.count_height_textView)
        nameTextView = view.findViewById(R.id.name_textView)
        genderTextView = view.findViewById(R.id.gender_textView)
        signOutImageButton = view.findViewById(R.id.signOut_imageButton)
        biometricTextView = view.findViewById(R.id.biometric_textView)
        shadowImageView = view.findViewById(R.id.shadow_imageView)

        initObservers()
        val sharedPreference =
                context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        viewModel.getProfile(sharedPreference?.getInt(SP_TOKEN, 0).toString())

        return view
    }

    override fun onStart() {
        super.onStart()
        biometricTextView.setOnClickListener {
            shadowImageView.isVisible = true
            animateAlpha(shadowImageView)
            callbacks?.onProfileFragment()
            onPause()
        }
        signOutImageButton.setOnClickListener {
            viewModel.signOut(username)
        }
    }

    override fun onResume() {
        super.onResume()
        shadowImageView.isVisible = false
        shadowImageView.alpha = 0f
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun initObservers() {
        viewModel = ViewModelProvider(this).get(ProfileVM::class.java)
        viewModel.cosmeticView.message.observe(
                viewLifecycleOwner,
                { message ->
                    toast(message)
                }
        )
        viewModel.profile.observe(
                viewLifecycleOwner,
                { profile ->
                    if (profile.isNotEmpty()) {
                        nameTextView.text = profile[0].username
                        weightTextView.text = profile[0].weight
                        heightTextView.text = profile[0].height
                    }
                    log(TAG, "profile=$profile")
                }
        )
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
        fun newInstance(): ProfileFragment = ProfileFragment()
    }
}