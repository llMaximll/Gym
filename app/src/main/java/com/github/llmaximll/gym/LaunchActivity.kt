package com.github.llmaximll.gym

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.github.llmaximll.gym.fragments.Hello1Fragment
import com.github.llmaximll.gym.fragments.Hello2Fragment
import com.github.llmaximll.gym.fragments.Hello3Fragment

private const val NAME_SHARED_PREFERENCES = "first_launch"

class LaunchActivity : AppCompatActivity(),
        Hello1Fragment.Callbacks,
        Hello2Fragment.Callbacks {
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
        supportFragmentManager.commit {
            setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                    android.R.animator.fade_in, android.R.animator.fade_out)
            replace(R.id.container_fragment, fragment)
            addToBackStack(null)
        }
    }

    override fun onHello2Fragment() {
        val fragment = Hello3Fragment.newInstance()
        supportFragmentManager.commit {
            setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                    android.R.animator.fade_in, android.R.animator.fade_out)
            replace(R.id.container_fragment, fragment)
            addToBackStack(null)
        }
    }
}