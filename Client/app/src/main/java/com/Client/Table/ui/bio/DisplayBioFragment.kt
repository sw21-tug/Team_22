package com.Client.Table.ui.bio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.Client.Table.R

class DisplayBioFragment : Fragment()
{
    private lateinit var bio_username: TextView
    private lateinit var bio_age: TextView
    private lateinit var bio_city: TextView
    private lateinit var bio_profile_picture: ImageView

    private lateinit var bio_preference_1: TextView
    private lateinit var bio_preference_2: TextView
    private lateinit var bio_preference_3: TextView
    private lateinit var bio_preference_4: TextView

    private val bioViewModel: BioViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_display_bio, container, false)

        bio_username = root.findViewById(R.id.bio_username_display)
        bio_age = root.findViewById(R.id.bio_age_display)
        bio_city = root.findViewById(R.id.bio_city_display)
        bio_profile_picture = root.findViewById(R.id.profile_picture_display)

        bio_preference_1 = root.findViewById(R.id.display_bio_preference_1)
        bio_preference_2 = root.findViewById(R.id.display_bio_preference_2)
        bio_preference_3 = root.findViewById(R.id.display_bio_preference_3)
        bio_preference_4 = root.findViewById(R.id.display_bio_preference_4)

        val edit_profile_button: Button = root.findViewById(R.id.edit_profile_button)
        edit_profile_button.setOnClickListener {
            val action = DisplayBioFragmentDirections.actionNavDisplayBioToNavEditBio()
            findNavController().navigate(action)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bioViewModel.bio_username.observe(viewLifecycleOwner, Observer { it ->
            bio_username.text = it
        })

        bioViewModel.bio_age.observe(viewLifecycleOwner, Observer { it ->
            bio_age.text = it.toString()
        })

        bioViewModel.bio_city.observe(viewLifecycleOwner, Observer {
            bio_city.text = it
        })

        bioViewModel.bio_profile_picture.observe(viewLifecycleOwner, Observer {
            bio_profile_picture.setImageBitmap(it)
        })

        bioViewModel.bio_card_games.observe(viewLifecycleOwner, Observer {
            if (it) {
                bio_preference_1.visibility = TextView.VISIBLE
            } else {
                bio_preference_1.visibility = TextView.INVISIBLE
            }

        })

        bioViewModel.bio_board_games.observe(viewLifecycleOwner, Observer {
            if (it) {
                bio_preference_2.visibility = TextView.VISIBLE
            } else {
                bio_preference_2.visibility = TextView.INVISIBLE
            }
        })

        bioViewModel.bio_ttrpg.observe(viewLifecycleOwner, Observer {
            if (it) {
                bio_preference_3.visibility = TextView.VISIBLE
            } else {
                bio_preference_3.visibility = TextView.INVISIBLE
            }
        })

        bioViewModel.bio_wargames.observe(viewLifecycleOwner, Observer {
            if (it) {
                bio_preference_4.visibility = TextView.VISIBLE
            } else {
                bio_preference_4.visibility = TextView.INVISIBLE
            }
        })
    }
}
