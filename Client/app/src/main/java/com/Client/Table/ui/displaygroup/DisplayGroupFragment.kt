package com.Client.Table.ui.displaygroup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.Client.Table.R

class DisplayGroupFragment : Fragment() {

    companion object {
        fun newInstance() = DisplayGroupFragment()
    }

    private lateinit var viewModel: DisplayGroupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragement_display_group, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DisplayGroupViewModel::class.java)
        // TODO: Use the ViewModel
    }

}