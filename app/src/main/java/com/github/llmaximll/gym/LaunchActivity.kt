package com.github.llmaximll.gym

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.github.llmaximll.gym.fragments.*

class LaunchActivity : AppCompatActivity(),
        Hello1Fragment.Callbacks,
        Hello2Fragment.Callbacks,
        Hello3Fragment.Callbacks,
        Hello4Fragment.Callbacks {
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

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                    android.R.animator.fade_in, android.R.animator.fade_out)
            replace(R.id.container_fragment, fragment)
            addToBackStack(null)
        }
    }
}