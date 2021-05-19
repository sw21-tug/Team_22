package com.Client.Table.ui.bio

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Client.Table.data.LoginRepository
import com.Client.Table.data.model.Bio
import com.Client.Table.data.model.Response
import com.Client.Table.network.BackendApi
import kotlinx.coroutines.runBlocking
import java.lang.Exception


class BioViewModel : ViewModel() {
    private val _bio_id: MutableLiveData<Int> = MutableLiveData<Int>()
    private val _username: MutableLiveData<String> = MutableLiveData<String>()
    private val _age: MutableLiveData<Int> by lazy {MutableLiveData<Int>()}
    private val _city = MutableLiveData<String>()
    private val _profile_picture = MutableLiveData<Bitmap>()

    private val _card_games = MutableLiveData<Boolean>(false)
    private val _board_games = MutableLiveData<Boolean>(false)
    private val _ttrpg = MutableLiveData<Boolean>(false)
    private val _wargames = MutableLiveData<Boolean>(false)

    val bio_id: LiveData<Int> get()= _bio_id
    val bio_username: LiveData<String> get() = _username
    val bio_age: LiveData<Int> get() = _age
    val bio_city: LiveData<String> get() = _city
    val bio_profile_picture: LiveData<Bitmap> get() = _profile_picture
    val bio_card_games: LiveData<Boolean> get() = _card_games
    val bio_board_games: LiveData<Boolean> get() = _board_games
    val bio_ttrpg: LiveData<Boolean> get() = _ttrpg
    val bio_wargames: LiveData<Boolean> get() = _wargames

    init {
        runBlocking {
            try {
                val bio: Bio = BackendApi.retrofitService.getBio(LoginRepository.user!!.jwtToken)
                if (bio.id != null)
                    _bio_id.value = bio.id!!
                if (bio.user_name != null)
                    _username.value = bio.user_name!!
                if (bio.age != null)
                    _age.value = bio.age!!
                if (bio.city != null)
                    _city.value = bio.city!!
                if (bio.card_games != null)
                    _card_games.value = bio.card_games!!
                if (bio.card_games != null)
                    _board_games.value = bio.board_games!!
                if (bio.ttrpg != null)
                    _ttrpg.value = bio.ttrpg!!
                if (bio.wargames != null)
                    _wargames.value = bio.wargames!!
            } catch (e: Exception)
            {
                println("Get bio did not work")
            }
        }
    }


    fun setData(name: String, age: Int, city: String, profile_picture: Bitmap? = null, card_games: Boolean, board_games: Boolean, ttrpg: Boolean, wargames:Boolean): Boolean {
        _username.value = name
        _age.value = age
        _city.value = city
        _card_games.value = card_games
        _board_games.value = board_games
        _ttrpg.value = ttrpg
        _wargames.value = wargames
        if (profile_picture != null)
            _profile_picture.value = profile_picture!!

        return try {
            runBlocking {
                try {
                    val response: Response = BackendApi.retrofitService.updateBio(
                        LoginRepository.user!!.jwtToken, Bio(
                            bio_id.value, bio_username.value,
                            bio_age.value, bio_city.value, bio_card_games.value,
                            bio_board_games.value, bio_ttrpg.value, bio_wargames.value
                        )
                    )
                    return@runBlocking response.response == "success"
                } catch (e: Exception)
                {
                    println("Update bio send request to backend did not work; depending on test case maybe ok")
                    return@runBlocking false
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}