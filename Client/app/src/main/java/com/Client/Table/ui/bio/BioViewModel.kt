package com.Client.Table.ui.bio

import android.nfc.cardemulation.CardEmulation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BioViewModel : ViewModel() {
    private val _username: MutableLiveData<String> = MutableLiveData<String>()
    private val _age: MutableLiveData<Int> by lazy {MutableLiveData<Int>()}
    private val _city = MutableLiveData<String>()

    private val _card_games = MutableLiveData<Boolean>(false)
    private val _board_games = MutableLiveData<Boolean>(false)
    private val _ttrpg = MutableLiveData<Boolean>(false)
    private val _wargames = MutableLiveData<Boolean>(false)

    val bio_username: LiveData<String> get() = _username
    val bio_age: LiveData<Int> get() = _age
    val bio_city: LiveData<String> get() = _city
    val bio_card_games: LiveData<Boolean> get() = _card_games
    val bio_board_games: LiveData<Boolean> get() = _board_games
    val bio_ttrpg: LiveData<Boolean> get() = _ttrpg
    val bio_wargames: LiveData<Boolean> get() = _wargames


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