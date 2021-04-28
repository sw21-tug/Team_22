package com.Client.Table.ui.bio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.Client.Table.R
import com.Client.Table.ui.slideshow.SlideshowViewModel

class EditBioFragment : Fragment() {

    private lateinit var bioViewModel: BioViewModel

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
        return root
    }
}