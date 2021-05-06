package com.github.llmaximll.gym

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.github.llmaximll.gym.fragments.hellofragments.*

private const val NAME_SHARED_PREFERENCES = "shared_preferences"
private const val FIRST_LAUNCH = "first_launch"

class LaunchActivity : AppCompatActivity(),
        Hello1Fragment.Callbacks,
        Hello2Fragment.Callbacks,
        Hello3Fragment.Callbacks,
        Hello4Fragment.Callbacks,
        Hello5Fragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        val currentFragment =
                supportFragmentManager.findFragmentById(R.id.container_fragment)
        if (currentFragment == null) {
            val fragment = Hello1Fragment.newInstance()
            supportFragmentManager.commit {
                add(R.id.container_fragment, fragment)
            }
        }
    }

    override fun onHello1Fragment() {
        val fragment = Hello2Fragment.newInstance()
        changeFragment(fragment)
    }

    override fun onHello2Fragment(gender: Int) {
        val fragment = Hello3Fragment.newInstance(gender)
        changeFragment(fragment)
    }

    override fun onHello3Fragment() {
        val fragment = Hello4Fragment.newInstance()
        changeFragment(fragment)
    }

    override fun onHello4Fragment() {
        val fragment = Hello5Fragment.newInstance()
        changeFragment(fragment)
    }

    override fun onHello5Fragment() {
        val sharedPreference =
                getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean(FIRST_LAUNCH, true)
        editor.apply()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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