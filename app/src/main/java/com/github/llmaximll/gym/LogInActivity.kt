package com.github.llmaximll.gym

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.github.llmaximll.gym.fragments.otherfragments.LogInFragment
import com.github.llmaximll.gym.fragments.otherfragments.SignUpFragment

private const val NAME_SHARED_PREFERENCES = "shared_preferences"
private const val SP_FIRST_LAUNCH = "first_launch"

class LogInActivity : AppCompatActivity(),
        LogInFragment.Callbacks,
        SignUpFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val sharedPreference =
                getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val firstLaunch = sharedPreference.getBoolean(SP_FIRST_LAUNCH, false)
        if (!firstLaunch) {
            val intent = Intent(this, LaunchActivity::class.java)
            startActivity(intent)
            finish()
        }
        val currentFragment =
                supportFragmentManager.findFragmentById(R.id.container_fragment)
        if (currentFragment == null) {
            val fragment = LogInFragment.newInstance()
            supportFragmentManager.commit {
                add(R.id.container_fragment, fragment)
            }
        }
    }

    override fun onLogInFragment(mode: Int) {
        var fragment: Fragment? = null
        when (mode) {
            0 -> {
                fragment = SignUpFragment.newInstance()
            }
            1 -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        if (fragment != null) {
            changeFragment(fragment, true)
        }
    }

    override fun onSignUpFragment() {
        val fragment = LogInFragment.newInstance()
        changeFragment(fragment, false)
    }

    private fun changeFragment(fragment: Fragment, addBackStack: Boolean) {
        supportFragmentManager.commit {
            setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                    android.R.animator.fade_in, android.R.animator.fade_out)
            replace(R.id.container_fragment, fragment)
            if (addBackStack) {
                addToBackStack(null)
            }
        }
    }
}