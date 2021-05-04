package com.github.llmaximll.gym

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.github.llmaximll.gym.fragments.otherfragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),
        ProfileFragment.Callbacks {

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
                R.id.plan -> PlanFragment.newInstance()
                R.id.lessons -> LessonsFragment.newInstance()
                R.id.reports -> ReportsFragment.newInstance()
                R.id.profile -> ProfileFragment.newInstance()
                else -> PlanFragment.newInstance()
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
        val dialogFragment = ProfileBioDialogFragment.newInstance()
        dialogFragment.show(supportFragmentManager, "ProfileBio")
    }
}