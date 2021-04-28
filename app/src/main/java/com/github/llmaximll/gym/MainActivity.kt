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

private const val NAME_SHARED_PREFERENCES = "first_launch"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreference =
                getSharedPreferences(NAME_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val firstLaunch = sharedPreference.getBoolean(NAME_SHARED_PREFERENCES, false)
        if (!firstLaunch) {
            val intent = Intent(this, LaunchActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(
                    this,
                    "!first_launch",
                    Toast.LENGTH_LONG
            ).show()
        }
    }
}