package com.Client.Table.ui.bio

import android.nfc.cardemulation.CardEmulation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BioViewModel : ViewModel() {
    private val _username = MutableLiveData<String>().apply {
        value = " "
    }
    private val _age = MutableLiveData<Int>().apply {
        value = 18
    }
    private val _city = MutableLiveData<String>().apply {
        value = " "
    }

    private val _card_games = MutableLiveData<Boolean>().apply {
        value = false
    }
    private val _board_games = MutableLiveData<Boolean>().apply {
        value = false
    }
    private val _ttrpg = MutableLiveData<Boolean>().apply {
        value = false
    }
    private val _wargames = MutableLiveData<Boolean>().apply {
        value = false
    }

    val bio_username: LiveData<String> = _username
    val bio_age: LiveData<Int> = _age
    val bio_city: LiveData<String> = _city
    val bio_card_games: LiveData<Boolean> = _card_games
    val bio_board_games: LiveData<Boolean> = _board_games
    val bio_ttrpg: LiveData<Boolean> = _ttrpg
    val bio_wargames: LiveData<Boolean> = _wargames


    fun setData(name: String, age: Int, city: String, card_games: Boolean, board_games: Boolean, ttrpg: Boolean, wargames:Boolean)
    {
        _username.value = name
        _age.value = age
        _city.value = city
        _card_games.value = card_games
        _board_games.value = board_games
        _ttrpg.value = ttrpg
        _wargames.value = wargames

    }


}