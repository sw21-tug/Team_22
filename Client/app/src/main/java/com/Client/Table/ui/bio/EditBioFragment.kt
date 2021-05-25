package com.Client.Table.ui.bio

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.Client.Table.R


class EditBioFragment : Fragment() {

    companion object {
        val TAG_IMAGE_ADDED = 1
        val MIN_AGE = 14
        val MAX_AGE = 99
    }

    lateinit var profile_image: ImageView
    lateinit var username: EditText
    lateinit var age: EditText
    lateinit var city: EditText
    lateinit var edit_btn: Button
    lateinit var submit_btn: Button
    lateinit var check_box_1: CheckBox
    lateinit var check_box_2: CheckBox
    lateinit var check_box_3: CheckBox
    lateinit var check_box_4: CheckBox

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null)
        {
            profile_image.setImageURI(uri)
            profile_image.tag = TAG_IMAGE_ADDED
        }
    }

    private val bioViewModel: BioViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_edit_bio, container, false)

        profile_image = root.findViewById(R.id.profile_picture)
        username = root.findViewById(R.id.bio_username_input)
        age = root.findViewById(R.id.bio_age_input)
        city = root.findViewById(R.id.bio_city_input)
        check_box_1 = root.findViewById(R.id.edit_bio_checkbox_1)
        check_box_2 = root.findViewById(R.id.edit_bio_checkbox_2)
        check_box_3 = root.findViewById(R.id.edit_bio_checkbox_3)
        check_box_4 = root.findViewById(R.id.edit_bio_checkbox_4)
        edit_btn = root.findViewById(R.id.edit_picture_button)
        submit_btn = root.findViewById(R.id.edit_bio_submit)

        edit_btn.setOnClickListener()
        {
            getContent.launch("image/*")
        }

        submit_btn.setOnClickListener {
            if (validateInput())
            {
                bioViewModel.setData(username.text.toString(), age.text.toString().toInt(), city.text.toString(), profile_image.drawable?.toBitmap(), check_box_1.isChecked, check_box_2.isChecked, check_box_3.isChecked, check_box_4.isChecked)
                val action = EditBioFragmentDirections.actionNavEditBioToNavDisplayBio()
                findNavController().navigate(action)
            }

        }

        return root
    }

    private fun validateInput(): Boolean
    {
        var isValidInput = true

        val entered_age = age.text.toString().toIntOrNull()
        if (entered_age == null || entered_age < MIN_AGE || entered_age > MAX_AGE) {
            age.setError(resources.getString(R.string.bio_error_message_age_range))
            isValidInput = false
        }


        if (username.text.toString().isBlank()) {
            username.setError(resources.getString(R.string.bio_error_message_empty_username))
            isValidInput = false
        }

        return isValidInput
    }
}