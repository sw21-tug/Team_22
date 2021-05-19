package com.Client.Table.ui.preferenceSearch
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.Client.Table.MainViewActivity
import com.Client.Table.R
import com.Client.Table.ui.bio.BioViewModel
import com.Client.Table.ui.bio.EditBioFragment
import com.Client.Table.ui.bio.EditBioFragmentDirections
import com.Client.Table.ui.home.HomeFragment
import com.Client.Table.ui.login.LoginActivity

class activity_preference_search : AppCompatActivity() {


    var submit_search = findViewById<Button>(R.id.SearchPreferencesBtn)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_profile_preferences)

        submit_search.setOnClickListener {
            Toast.makeText(this, "Searching ..", Toast.LENGTH_SHORT).show()
            submit_search.visibility = View.VISIBLE

            // This should redirect to the results of the set search
            // redirects to main view activity until implemented
            val intent = Intent(this, MainViewActivity::class.java).apply{}
            startActivity(intent)
        }


    }

    companion object {
        val MIN_AGE = 14
        val MAX_AGE = 99
    }




}



