package com.Client.Table.ui.bio

import android.app.Activity
import android.content.Intent
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.Client.Table.R


class EditBioFragment : Fragment() {

    private lateinit var bioViewModel: BioViewModel

    val SERVICE_CODE = 1
    lateinit var profile_image: ImageView
    lateinit var edit_image: Button
    lateinit var username: EditText
    lateinit var age: EditText
    lateinit var city: EditText
    lateinit var edit_btn: Button
    lateinit var check_box_1: CheckBox
    lateinit var check_box_2: CheckBox
    lateinit var check_box_3: CheckBox
    lateinit var check_box_4: CheckBox

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        profile_image.setImageURI(uri)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bioViewModel =
            ViewModelProvider(this).get(BioViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_edit_bio, container, false)
        /*val textView: TextView = root.findViewById(R.id.text_slideshow)
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/


        edit_btn = root.findViewById(R.id.edit_picture_button)
        profile_image = root.findViewById(R.id.profile_picture)
        edit_btn.setOnClickListener()
        {
            getContent.launch("image/*")
        }
        return root
    }
}