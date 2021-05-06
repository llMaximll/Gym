package com.github.llmaximll.gym.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.llmaximll.gym.CosmeticView
import com.github.llmaximll.gym.GymRepository
import com.github.llmaximll.gym.dataclasses.Profile

class ProfileVM : ViewModel() {
    private val repository = GymRepository()

    val cosmeticView = CosmeticView()
    var profile = MutableLiveData<List<Profile>>()

    fun getProfile(token: String) {
        repository.getProfile(token, profile, cosmeticView)
    }

    fun signOut(username: String) {
        repository.signOut(username, cosmeticView)
    }

    fun editProfile(token: String, weight: String, height: String) {
        repository.editProfile(token, weight, height, cosmeticView)
    }
}