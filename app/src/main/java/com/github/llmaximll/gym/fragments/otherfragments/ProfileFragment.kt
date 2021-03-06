package com.github.llmaximll.gym.fragments.otherfragments

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.llmaximll.gym.BuildConfig
import com.github.llmaximll.gym.R
import com.github.llmaximll.gym.vm.ProfileVM
import java.util.concurrent.TimeUnit

private const val NAME_SHARED_PREFERENCES = "shared_preferences"
private const val SP_USERNAME = "sp_username"
private const val SP_TOKEN = "sp_t"
private const val TAG = "ProfileFragment"
private const val REQUEST_DIALOG_CODE_BIO = 1
private const val REQUEST_DIALOG_CODE_START_DIALOG = 2
private const val REQUEST_DIALOG_CODE_TIMEOUT = 3
private const val SP_HEIGHT = "sp_height"
private const val SP_WEIGHT = "sp_weight"
private const val SP_GENDER = "sp_gender"
private const val SP_NOTIFICATIONS  ="sp_notifications"
private const val SP_TIMEOUT_COUNT = "sp_timeout_count"

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
    private lateinit var shadowImageViewActivity: ImageView
    private lateinit var dialogTextView: TextView
    private lateinit var policyTextView: TextView
    private lateinit var notificationsSwitch: SwitchCompat
    private lateinit var restTimeTextView: TextView

    private var callbacks: Callbacks? = null
    private var notifications: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreference =
                context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        username = sharedPreference?.getString(SP_USERNAME, "null-username")!!
        notifications = sharedPreference.getBoolean(SP_NOTIFICATIONS, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_profile, container, false)

        weightTextView = view.findViewById(R.id.count_scores_textView)
        heightTextView = view.findViewById(R.id.count_minutes_textView)
        nameTextView = view.findViewById(R.id.title_textView)
        genderTextView = view.findViewById(R.id.minutes_textView)
        signOutImageButton = view.findViewById(R.id.stop_imageButton)
        biometricTextView = view.findViewById(R.id.biometric_textView)
        shadowImageViewActivity = activity?.findViewById(R.id.shadow_imageView)!!
        dialogTextView = view.findViewById(R.id.dialog_textView)
        policyTextView = view.findViewById(R.id.policy_textView)
        notificationsSwitch = view.findViewById(R.id.notifications_switch)
        restTimeTextView = view.findViewById(R.id.rest_time_textView)

        initObservers()
        val sharedPreference =
                context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        viewModel.getProfile(sharedPreference?.getInt(SP_TOKEN, 0).toString())

        val rest = sharedPreference?.getLong(SP_TIMEOUT_COUNT, 30L) ?: 30L
        restTimeTextView.text = "${rest.toInt() / 1000} sec"

        notificationsSwitch.isChecked = notifications

        val gender = sharedPreference?.getInt(SP_GENDER, 0)
        genderTextView.text = if (gender == 1) "Male" else "Female"

        return view
    }

    override fun onStart() {
        super.onStart()
        restTimeTextView.setOnClickListener {
            shadowImageViewActivity.isVisible = true
            animateAlpha(shadowImageViewActivity)
            val sharedPreference =
                    context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val rest = sharedPreference?.getLong(SP_TIMEOUT_COUNT, 30L) ?: 30L
            val dialogFragment = TimeoutDialogFragment.newInstance(rest)
            dialogFragment.setTargetFragment(this, REQUEST_DIALOG_CODE_TIMEOUT)
            dialogFragment.show(parentFragmentManager, "TimeoutDialogFragment")
            onPause()
        }
        biometricTextView.setOnClickListener {
            shadowImageViewActivity.isVisible = true
            animateAlpha(shadowImageViewActivity)
            val dialogFragment = ProfileBioDialogFragment.newInstance()
            dialogFragment.setTargetFragment(this, REQUEST_DIALOG_CODE_BIO)
            dialogFragment.show(parentFragmentManager, "ProfileBioDialogFragment")
            onPause()
        }
        dialogTextView.setOnClickListener {
            shadowImageViewActivity.isVisible = true
            animateAlpha(shadowImageViewActivity)
            val dialogFragment = GenderDialogFragment.newInstance()
            dialogFragment.setTargetFragment(this, REQUEST_DIALOG_CODE_START_DIALOG)
            dialogFragment.show(parentFragmentManager, "GenderDialogFragment")
            onPause()
        }
        policyTextView.setOnClickListener {
            callbacks?.onProfileFragment()
        }
        signOutImageButton.setOnClickListener {
            viewModel.signOut(username)
        }
        notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            val sharedPreference =
                    context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            if (isChecked) {
                val result = viewModel.setAlarmManager(requireContext())
                if (result) {
                    val editor = sharedPreference?.edit()
                    editor?.putBoolean(SP_NOTIFICATIONS, true)
                    editor?.apply()
                    toast("?????????????????????? ????????????????")
                } else
                    toast("???? ?????????????? ???????????????? ??????????????????????")
            } else {
                val result = viewModel.stopAlarmManager(requireContext())
                val editor = sharedPreference?.edit()
                editor?.putBoolean(SP_NOTIFICATIONS, false)
                editor?.apply()
                if (result) toast("?????????????????????? ??????????????????")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_DIALOG_CODE_BIO -> {
                    val weight = data?.getIntExtra(ProfileBioDialogFragment.NAME_TAG_WEIGHT, 1)
                    val height = data?.getIntExtra(ProfileBioDialogFragment.NAME_TAG_HEIGHT, 1)
                    if (weight != null && height != null) {
                        val sharedPreference =
                                context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                        viewModel.editProfile(sharedPreference?.getInt(SP_TOKEN, 1).toString(), weight.toString(), height.toString())
                    }
                }
                REQUEST_DIALOG_CODE_START_DIALOG -> {
                    val bool = data?.getBooleanExtra(GenderDialogFragment.NAME_TAG_GENDER, false)
                    if (bool != null) {
                        val gender = if (bool) 1 else 0
                        val sharedPreference =
                                context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                        val editor = sharedPreference?.edit()
                        editor?.putInt(SP_GENDER, gender)
                        editor?.apply()
                        genderTextView.text = if (bool) "Male" else "Female"
                        toast("???????????? ??????????????????")
                    }
                }
                REQUEST_DIALOG_CODE_TIMEOUT -> {
                    val count = data?.getIntExtra(TimeoutDialogFragment.INTENT_TAG_TIMEOUT_COUNT, 30)
                    if (count != null) {
                        val sharedPreference =
                                context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                        val editor = sharedPreference?.edit()
                        editor?.putLong(SP_TIMEOUT_COUNT, TimeUnit.SECONDS.toMillis(count.toLong()))
                        editor?.apply()
                        restTimeTextView.text = "$count sec"
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        shadowImageViewActivity.isVisible = false
        shadowImageViewActivity.alpha = 0f
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
                    val sharedPreference =
                            context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                    viewModel.getProfile(sharedPreference?.getInt(SP_TOKEN, 1).toString())
                    toast(message)
                }
        )
        viewModel.profile.observe(
                viewLifecycleOwner,
                { profile ->
                    if (profile.isNotEmpty()) {
                        val sharedPreference =
                                context?.getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                        val editor = sharedPreference?.edit()
                        if (nameTextView.text.toString() != profile[0].username) {
                            editor?.putString(SP_USERNAME, profile[0].username)
                            nameTextView.text = profile[0].username
                        }
                        weightTextView.text = profile[0].weight
                        heightTextView.text = profile[0].height
                        editor?.putInt(SP_WEIGHT, profile[0].weight.toInt())
                        editor?.putInt(SP_HEIGHT, profile[0].height.toInt())
                        editor?.apply()
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