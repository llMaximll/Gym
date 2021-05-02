package com.github.llmaximll.gym

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.github.llmaximll.gym.fragments.LogInFragment
import com.github.llmaximll.gym.fragments.MainFragment
import com.github.llmaximll.gym.fragments.SignUpFragment

private const val NAME_SHARED_PREFERENCES = "shared_preferences"
private const val SP_FIRST_LAUNCH = "first_launch"

class MainActivity : AppCompatActivity(),
        LogInFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                fragment = MainFragment.newInstance()
            }
        }
        if (fragment != null) {
            changeFragment(fragment)
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                    android.R.animator.fade_in, android.R.animator.fade_out)
            replace(R.id.container_fragment, fragment)
            addToBackStack(null)
        }
    }
}