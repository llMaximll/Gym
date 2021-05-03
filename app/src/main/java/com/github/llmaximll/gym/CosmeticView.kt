package com.github.llmaximll.gym

import androidx.lifecycle.MutableLiveData

class CosmeticView {
    var message: MutableLiveData<String> = MutableLiveData()
    var isSuccessful: MutableLiveData<Boolean> = MutableLiveData()
    var token: MutableLiveData<Int> = MutableLiveData()
}