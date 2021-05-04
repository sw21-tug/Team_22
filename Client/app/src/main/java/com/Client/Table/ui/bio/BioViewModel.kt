package com.Client.Table.ui.bio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BioViewModel : ViewModel() {
    private val _username = MutableLiveData<String>().apply {
        value = " "
    }
    private val _age = MutableLiveData<String>().apply {
        value = " "
    }
    private val _city = MutableLiveData<String>().apply {
        value = " "
    }

    val bio_username: LiveData<String> = _username
    val bio_age: LiveData<String> = _age
    val bio_city: LiveData<String> = _city
}