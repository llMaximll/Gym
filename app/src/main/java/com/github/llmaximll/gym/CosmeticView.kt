package com.github.llmaximll.gym

import androidx.lifecycle.MutableLiveData
import com.github.llmaximll.gym.dataclasses.Lessons

class CosmeticView {
    var message: MutableLiveData<String> = MutableLiveData()
    var isSuccessful: MutableLiveData<Boolean> = MutableLiveData()
    var token: MutableLiveData<Int> = MutableLiveData()
    var lessons: MutableLiveData<List<Lessons>?> = MutableLiveData()
}