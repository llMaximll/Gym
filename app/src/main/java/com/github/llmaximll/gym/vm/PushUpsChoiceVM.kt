package com.github.llmaximll.gym.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import com.github.llmaximll.gym.GymRepository

class PushUpsChoiceVM : ViewModel() {
    private val repository = GymRepository()

    fun initDB(context: Context) = repository.initDBHandler(context)

    fun getCompletedExercises(): Int = repository.getCompletedExercises()
}