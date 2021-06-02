package com.Client.Table.ui.preferenceSearch
import android.content.res.Resources
import android.content.res.loader.ResourcesProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView

import com.Client.Table.R
import com.Client.Table.data.LoginRepository
import com.Client.Table.data.model.Bio
import com.Client.Table.data.model.Response
import com.Client.Table.data.model.SearchPreferences
import com.Client.Table.data.model.UsersSearchResult
import com.Client.Table.network.BackendApi
import com.google.android.material.slider.RangeSlider
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Text
import java.lang.Exception


class SearchPreferenceActivity : AppCompatActivity() {

    private lateinit var submit_search: Button
    private lateinit var card_games: CheckBox
    private lateinit var board_games: CheckBox
    private lateinit var ttrpgs: CheckBox
    private lateinit var war_games: CheckBox
    private lateinit var city: EditText
    private lateinit var age_range: RangeSlider
    private lateinit var error_message_view: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_profile_preferences)

        submit_search = findViewById<Button>(R.id.search_profile_submit_btn)
        card_games = findViewById<CheckBox>(R.id.search_profile_card_games)
        board_games = findViewById<CheckBox>(R.id.search_profile_board_games)
        ttrpgs = findViewById<CheckBox>(R.id.search_profile_ttrpgs)
        war_games = findViewById<CheckBox>(R.id.search_profile_war_games)
        city = findViewById<EditText>(R.id.search_profile_city_input)
        age_range = findViewById<RangeSlider>(R.id.search_profile_age_slider)
        error_message_view = findViewById<TextView>(R.id.search_profile_error_message)

        submit_search.setOnClickListener {
            if (validateInput()) {
                Toast.makeText(this, getString(R.string.search_profile_searching), Toast.LENGTH_SHORT).show()
                submit_search.visibility = View.VISIBLE
                error_message_view.visibility = TextView.INVISIBLE
                //val sampleData = mutableListOf<String>("Josh", "Florian")
                var sampleData : MutableList<String> = ArrayList()
                val recyclerView = findViewById<RecyclerView>(R.id.search_preference_list)
                val range_values = age_range.values

                runBlocking {
                    try {
                        sampleData = BackendApi.retrofitService.getSearchResults(LoginRepository.user!!.jwtToken,
                                SearchPreferences(range_values[0].toInt(), range_values[1].toInt(),
                                        city.text.toString(), card_games.isChecked, board_games.isChecked, ttrpgs.isChecked, war_games.isChecked))
                    }
                    catch (e: Exception) {
                        println(e)
                        println("Get Search Results did not work")
                    }
                }
                println(sampleData)
                recyclerView.adapter = SearchResultAdapter(this, sampleData)

            } else {
                error_message_view.visibility = TextView.VISIBLE
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    fun validateInput() : Boolean {
        return !(!card_games.isChecked && !board_games.isChecked && !ttrpgs.isChecked && !war_games.isChecked)
    }

}



