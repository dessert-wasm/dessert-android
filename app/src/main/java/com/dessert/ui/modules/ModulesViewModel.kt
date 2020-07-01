package com.dessert.ui.modules

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ModulesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is modules Fragment"
    }
    val text: LiveData<String> = _text
}