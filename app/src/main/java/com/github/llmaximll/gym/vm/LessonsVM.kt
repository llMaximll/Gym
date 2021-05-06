package com.github.llmaximll.gym.vm

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.llmaximll.gym.CosmeticView
import com.github.llmaximll.gym.GymRepository
import com.github.llmaximll.gym.dataclasses.Images
import com.github.llmaximll.gym.dataclasses.Lessons

class LessonsVM : ViewModel() {
    private val repository = GymRepository()
    val cosmeticView: CosmeticView = CosmeticView()
    val imageLD = MutableLiveData<Images>()

    fun getLessons(){
        repository.getLessons(cosmeticView)
    }

    fun downloadImages() {
        repository.downloadImage("https://img.youtube.com/vi/vK_vQLSAUeE/sddefault.jpg", "torso", imageLD)
        repository.downloadImage("https://img.youtube.com/vi/8xeMBUCWqbQ/sddefault.jpg", "hands", imageLD)
        repository.downloadImage("https://img.youtube.com/vi/ZoF1FDge03Q/sddefault.jpg", "spine", imageLD)
        repository.downloadImage("https://img.youtube.com/vi/wpsZdY2hT3A/sddefault.jpg", "legs", imageLD)
        repository.downloadImage("https://img.youtube.com/vi/pizmKgtY2AE/sddefault.jpg", "legs", imageLD)
        repository.downloadImage("https://img.youtube.com/vi/zFL53PsevPA/sddefault.jpg", "legs", imageLD)
        repository.downloadImage("https://img.youtube.com/vi/zFL53PsevPA/sddefault.jpg", "spine", imageLD)
        repository.downloadImage("https://img.youtube.com/vi/RWqIqNwQfDc/sddefault.jpg", "spine", imageLD)
        repository.downloadImage("https://img.youtube.com/vi/_tILsRtp7X4/sddefault.jpg", "hands", imageLD)
        repository.downloadImage("https://img.youtube.com/vi/myV-agCvY7g/sddefault.jpg", "hands", imageLD)
        repository.downloadImage("https://img.youtube.com/vi/bnzHECC0Z8A/sddefault.jpg", "torso", imageLD)
        repository.downloadImage("https://img.youtube.com/vi/vK_vQLSAUeE/sddefault.jpg", "torso", imageLD)
    }
}