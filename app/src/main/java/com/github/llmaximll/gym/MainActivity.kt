package com.github.llmaximll.gym

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.github.llmaximll.gym.fragments.otherfragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),
        ProfileFragment.Callbacks,
        PlanFragment.Callbacks,
        PushUpsChoiceFragment.Callbacks {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val currentFragment =
                supportFragmentManager.findFragmentById(R.id.container_fragment)
        if (currentFragment == null) {
            val fragment = PlanFragment.newInstance()
            supportFragmentManager.commit {
                add(R.id.container_fragment, fragment)
            }
        }

        setBottomNavigationView()
    }

    private fun setBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.plan -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    PlanFragment.newInstance()
                }
                R.id.lessons -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    LessonsFragment.newInstance()
                }
                R.id.reports -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    ReportsFragment.newInstance()
                }
                R.id.profile -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    ProfileFragment.newInstance()
                }
                else -> {
                    supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    PlanFragment.newInstance()
                }
            }
            supportFragmentManager.commit {
                replace(R.id.container_fragment, fragment)
            }

            true
        }
        bottomNavigationView.setOnNavigationItemReselectedListener { item ->
            when (item.itemId) {
                R.id.plan ->  { }
                R.id.lessons -> { }
                R.id.reports -> { }
                R.id.profile -> { }
            }
        }
    }

    override fun onProfileFragment() {
        val fragment = WebPolicyFragment.newInstance()
        changeFragment(fragment, true)
    }

    override fun onPlanFragment(category: String) {
        val fragment = PushUpsChoiceFragment.newInstance(category)
        changeFragment(fragment, true)
    }

    override fun onPushUpsChoiceFragment(nameEx: String, numberEx: Int, scores: Int, minutes: Int) {
        val fragment = PushUpsFragment.newInstance(nameEx, numberEx, scores, minutes)
        log(TAG, "scores=$scores")
        changeFragment(fragment, true)
    }

    private fun changeFragment(fragment: Fragment, addToBackStack: Boolean) {
        supportFragmentManager.commit {
            setCustomAnimations(
                    android.R.animator.fade_in,
                    android.R.anim.fade_out,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            )
            if (addToBackStack) {
                addToBackStack(null)
            }
            replace(R.id.container_fragment, fragment)
        }
    }

    private fun log(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }
}