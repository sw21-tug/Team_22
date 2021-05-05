package com.Client.Table

import com.Client.Table.ui.bio.BioViewModel
import org.junit.Test

class BioModelTest {
    @Test
    fun updateProfileEditsViewModel()
    {
        val model: BioViewModel = BioViewModel()

        val username = "max_mustermann"
        val age = 21
        val city = "Graz"
        val interestedInCardGames = true
        val interestedInBoardGames = true
        val interestedInTTRPGGames = false
        val interestedInWarGames = false

        model.setData(username, age, city, interestedInCardGames, interestedInBoardGames, interestedInTTRPGGames, interestedInWarGames)
        assert(model.bio_username.value.equals(username))
        assert(model.bio_age.value.equals(age))
        assert(model.bio_city.value.equals(city))
        assert(model.bio_card_games.value.equals(interestedInCardGames))
        assert(model.bio_board_games.value.equals(interestedInBoardGames))
        assert(model.bio_ttrpg.value.equals(interestedInTTRPGGames))
        assert(model.bio_wargames.value.equals(interestedInWarGames))
    }
}