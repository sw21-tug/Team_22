package com.Client.Table.ui.preferenceSearch
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

import com.Client.Table.R


class SearchPreferenceActivity : AppCompatActivity() {

    private lateinit var submit_search: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_profile_preferences)
        submit_search = findViewById<Button>(R.id.SearchPreferencesBtn)
        submit_search.setOnClickListener {
            Toast.makeText(this, "Searching ..", Toast.LENGTH_SHORT).show()
            submit_search.visibility = View.VISIBLE


            //Please dont create another mainActivity, we already have the main activity in the Background. You just need to return some sort of notifier in this activity such that you get redirected to the results.
            //Or you can just set the preferences in this task and adding the members via search is being done in the group menu.

            // This should redirect to the results of the set search
            // redirects to main view activity until implemented
            //val intent = Intent(this, MainViewActivity::class.java).apply{}
            //startActivity(intent)
            //for now
            finish()
            //maybe use home fragment for displaying search results
        }


    }
    override fun onBackPressed() {
        finish()
    }

    companion object {
        val MIN_AGE = 14
        val MAX_AGE = 99
    }




}



