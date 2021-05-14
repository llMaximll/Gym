package com.github.llmaximll.gym.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import com.github.llmaximll.gym.GymRepository
import com.github.llmaximll.gym.dataclasses.Exercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "PushUpsVM"

class PushUpsVM : ViewModel() {
    private val repository = GymRepository()

    suspend fun initDB(context: Context) =
            withContext(Dispatchers.IO) { repository.initDBHandler(context) }

    suspend fun getExerciseDB(nameEx: String, numberEx: String): Exercise? =
            withContext(Dispatchers.IO) { repository.getExerciseDB(nameEx, numberEx) }

    suspend fun addExerciseDB(nameEx: String, numberEx: String, scores: Int, minutes: Long, cal: Float): Boolean =
            repository.addExerciseDB(nameEx, numberEx, scores, minutes, cal)

    fun updateExerciseDB(nameEx: String, numberEx: String, scores: Int, minutes: Long, cal: Float): Boolean =
            repository.updateExerciseDB(nameEx, numberEx, scores, minutes, cal)

}