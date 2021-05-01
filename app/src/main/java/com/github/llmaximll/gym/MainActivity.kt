package com.github.llmaximll.gym

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.github.llmaximll.gym.fragments.LogInFragment
import com.github.llmaximll.gym.fragments.SignUpFragment

private const val NAME_SHARED_PREFERENCES = "shared_preferences"
private const val FIRST_LAUNCH = "first_launch"

class MainActivity : AppCompatActivity(),
        LogInFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreference =
                getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val firstLaunch = sharedPreference.getBoolean(FIRST_LAUNCH, false)
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

    override fun onLogInFragment() {
        val fragment = SignUpFragment.newInstance()
        changeFragment(fragment)
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