package com.Client.Table.ui.preferenceSearch
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.Client.Table.R
import com.Client.Table.ui.bio.BioViewModel
import com.Client.Table.ui.bio.EditBioFragment
import com.Client.Table.ui.bio.EditBioFragmentDirections

class activity_preference_search : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_profile_preferences)
    }

    companion object {
        val MIN_AGE = 14
        val MAX_AGE = 99
    }

    lateinit var search_city_input: EditText
    lateinit var submit_btn: Button
    lateinit var check_box_1: CheckBox
    lateinit var check_box_2: CheckBox
    lateinit var check_box_3: CheckBox
    lateinit var check_box_4: CheckBox


}



