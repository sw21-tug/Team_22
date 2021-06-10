package com.Client.Table.ui.displaygroup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import com.Client.Table.R
import android.widget.*
import androidx.lifecycle.Observer
import com.Client.Table.data.LoginRepository


class DisplayGroupFragment : Fragment() {

    private lateinit var groupViewModel: DisplayGroupViewModel

    lateinit var members : MutableList<String>
    lateinit var groups : MutableList<Pair<Int,String>>
    lateinit var groupName: TextView
    lateinit var memberName: TextView
    lateinit var addGroupBtn: Button
    lateinit var addMemberBtn: Button
    lateinit var membersList: ListView
    lateinit var membersAdapter: ArrayAdapter<String>
    lateinit var groupAdapter: ArrayAdapter<String>
    lateinit var selectGroupSpinner:Spinner
    var errorval: Int = -1




    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragement_display_group, container, false)
        groupViewModel = DisplayGroupViewModel()
        groupName = root.findViewById(R.id.groupNameText)
        memberName = root.findViewById(R.id.addPersonText)
        addGroupBtn = root.findViewById(R.id.saveGroupBtn)
        addMemberBtn = root.findViewById(R.id.addPlayerBtn)
        membersList = root.findViewById(R.id.groupListView)
        selectGroupSpinner = root.findViewById<Spinner>(R.id.selectGroupSpinner)
        addMemberBtn.setOnClickListener {
            groupViewModel.addMember(memberName.text.toString())
            membersAdapter.notifyDataSetChanged()
        }
        membersList.setOnItemLongClickListener { parent, view, position, id ->
            groupViewModel.deleteMember(members.get(id.toInt()))
            membersAdapter.notifyDataSetChanged()
            true
        }
        addGroupBtn.setOnClickListener {
            groupViewModel.addGroup(groupName.text.toString())
            groupAdapter.notifyDataSetChanged()
            // groupViewModel.fetchGroupData(groupName.text.toString())
        }

        groups = ArrayList()
        groupAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1, groups.map { it.second })
        selectGroupSpinner.adapter=groupAdapter
        //taken from: https://stackoverflow.com/questions/46447296/android-kotlin-onitemselectedlistener-for-spinner-not-working
        selectGroupSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                 groupViewModel.fetchGroupData(groups.get(id.toInt()).second, groups.get(id.toInt()).first)
            }

        }
        // end taken
        members = ArrayList()
        membersAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1, members)
        membersList.adapter=membersAdapter
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
           // groupViewModel = ViewModelProvider(this).get(DisplayGroupViewModel::class.java)
            // TODO: Use the ViewModel
            //groupViewModel.group_name.observe(viewLifecycleOwner, Observer { it ->
            //    groupName.text = it
            //})

             groupViewModel.error_.observe(viewLifecycleOwner, Observer { it ->
                 errorval = it
                 if(errorval!=-1){Toast.makeText(this.requireContext(), getString(errorval), Toast.LENGTH_SHORT).show()}
             })

            groupViewModel.group_members.observe(viewLifecycleOwner, Observer { it ->
                members = it
                membersAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1, members)
                membersList.adapter=membersAdapter
                membersAdapter.notifyDataSetChanged()
            })

           groupViewModel.groups_.observe(viewLifecycleOwner, Observer { it ->
                groups = it
                groupAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1,  groups.map { it.second })
                selectGroupSpinner.adapter=groupAdapter
                groupAdapter.notifyDataSetChanged()
            })
           groupViewModel.fetchGroupList(LoginRepository.user!!.displayName)
    }

}