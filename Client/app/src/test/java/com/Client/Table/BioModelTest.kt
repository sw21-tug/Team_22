package com.Client.Table

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.Client.Table.ui.bio.BioViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock


class BioModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private var model: BioViewModel? = null

    @Before
    fun setUp() {
        model = BioViewModel()
    }

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

        model.setData(
            username,
            age,
            city,
            interestedInCardGames,
            interestedInBoardGames,
            interestedInTTRPGGames,
            interestedInWarGames
        )
        assert(model.bio_username.value.equals(username))
        assert(model.bio_age.value!! == age)
        assert(model.bio_city.value.equals(city))
        assert(model.bio_card_games.value!! == interestedInCardGames)
        assert(model.bio_board_games.value!! == interestedInBoardGames)
        assert(model.bio_ttrpg.value!! == interestedInTTRPGGames)
        assert(model.bio_wargames.value!! == interestedInWarGames)
    }
}