package com.github.llmaximll.gym.vm

import androidx.lifecycle.ViewModel
import com.github.llmaximll.gym.CosmeticView
import com.github.llmaximll.gym.GymRepository

class SignUpVM : ViewModel() {
    private val repository = GymRepository()
    val cosmeticView: CosmeticView = CosmeticView()

    fun signUp(name: String, mail: String, password: String,
               height: String, weight: String): Map<String, Map<String, String>>? {
        val post = repository.signUp(name, mail, password, height, weight, cosmeticView)

        return post
    }
}