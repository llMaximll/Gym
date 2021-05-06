package com.github.llmaximll.gym.vm

import androidx.lifecycle.ViewModel
import com.github.llmaximll.gym.CosmeticView
import com.github.llmaximll.gym.GymRepository

class LogInVM : ViewModel() {
    private val repository = GymRepository()
    private var post: Map<String, Map<String, Int>>? = null

    var cosmeticView: CosmeticView = CosmeticView()

    fun signIn(username: String, password: String): Map<String, Map<String, Int>>? {
        post = repository.signIn(username, password, cosmeticView)

        return post
    }
}