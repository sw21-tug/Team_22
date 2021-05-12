package com.Client.Table.ui.displaygroup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import com.Client.Table.R
import java.lang.reflect.Member

class DisplayGroupFragment : Fragment() {

    companion object {
        fun newInstance() = DisplayGroupFragment()
    }

    private lateinit var viewModel: DisplayGroupViewModel

    // --
    lateinit var members : ArrayList<String>
    var nameList = arrayListOf<String>()
    // --

    lateinit var groupName: EditText
    lateinit var memberName: EditText
    lateinit var saveGroupBtn: Button
    lateinit var addMemberBtn: Button
    lateinit var membersList: ListView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragement_display_group, container, false)

        groupName = root.findViewById(R.id.groupNameText)
        memberName = root.findViewById(R.id.addPersonText)
        saveGroupBtn = root.findViewById(R.id.saveGroupBtn)
        addMemberBtn = root.findViewById(R.id.addPlayerBtn)
        membersList = root.findViewById(R.id.groupListView)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DisplayGroupViewModel::class.java)
        // TODO: Use the ViewModel



    }

}