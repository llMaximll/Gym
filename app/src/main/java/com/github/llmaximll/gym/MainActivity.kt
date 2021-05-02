package com.github.llmaximll.gym

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.github.llmaximll.gym.fragments.LessonsFragment
import com.github.llmaximll.gym.fragments.PlanFragment
import com.github.llmaximll.gym.fragments.ProfileFragment
import com.github.llmaximll.gym.fragments.ReportsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

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
    }
}