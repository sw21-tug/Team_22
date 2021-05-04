package com.Client.Table.ui.bio

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.Client.Table.R

class DisplayBioFragment : Fragment() {

    private lateinit var bioViewModel: BioViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bioViewModel =
            ViewModelProvider(this).get(BioViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_display_bio, container, false)
        val bio_username: TextView = root.findViewById(R.id.bio_username_display)
        val bio_age: TextView = root.findViewById(R.id.bio_age_display)
        val bio_city: TextView = root.findViewById(R.id.bio_city_display)

        val bio_preference_1: TextView = root.findViewById(R.id.display_bio_preference_1)
        val bio_preference_2: TextView = root.findViewById(R.id.display_bio_preference_2)
        val bio_preference_3: TextView = root.findViewById(R.id.display_bio_preference_3)
        val bio_preference_4: TextView = root.findViewById(R.id.display_bio_preference_4)

        bioViewModel.bio_username.observe(viewLifecycleOwner, Observer {
            bio_username.text = it
        })

        bioViewModel.bio_age.observe(viewLifecycleOwner, Observer {
            bio_age.text = it
        })

        bioViewModel.bio_city.observe(viewLifecycleOwner, Observer {
            bio_city.text = it
        })

        return root
    }
}