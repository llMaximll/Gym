package com.github.llmaximll.gym.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.llmaximll.gym.CosmeticView
import com.github.llmaximll.gym.GymRepository
import com.github.llmaximll.gym.dataclasses.Lessons

class LessonsVM : ViewModel() {
    private val repository = GymRepository()
    val cosmeticView: CosmeticView = CosmeticView()

    fun getLessons(){
        repository.getLessons(cosmeticView)
    }
}