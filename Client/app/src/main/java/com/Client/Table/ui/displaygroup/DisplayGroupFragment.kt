package com.Client.Table.ui.displaygroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.Client.Table.R

class DisplayGroupFragment : Fragment() {

    companion object {
        fun newInstance() = DisplayGroupFragment()
    }

    private lateinit var groupViewModel: DisplayGroupViewModel

    // --
    lateinit var members : MutableList<String>
    var nameList = arrayListOf<String>()
    // --

    lateinit var groupName: TextView
    lateinit var memberName: TextView
    lateinit var saveGroupBtn: Button
    lateinit var addMemberBtn: Button
    lateinit var membersList: ListView
    lateinit var membersAdapter: ArrayAdapter<String>;



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragement_display_group, container, false)
        groupViewModel = DisplayGroupViewModel()
        groupName = root.findViewById(R.id.groupNameText)
        memberName = root.findViewById(R.id.addPersonText)
        saveGroupBtn = root.findViewById(R.id.saveGroupBtn)
        addMemberBtn = root.findViewById(R.id.addPlayerBtn)
        membersList = root.findViewById(R.id.groupListView)
        addMemberBtn.setOnClickListener {
            if(members.size>=10){
                Toast.makeText(this.requireContext(),"Cannot add more members", Toast.LENGTH_SHORT).show()
            }
            else{
                //members.add(memberName.text.toString())
                    groupViewModel.addMember(memberName.text.toString())
                membersAdapter.notifyDataSetChanged()
            }
        }
        membersList.setOnItemLongClickListener { parent, view, position, id ->
            Toast.makeText(this.requireContext(),"Deleted Member", Toast.LENGTH_SHORT).show()
            //members.removeAt(id.toInt())
            groupViewModel.deleteMember(id.toInt())
            membersAdapter.notifyDataSetChanged()
            true
        }
        members = ArrayList()
        membersAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1, members)
        membersList.adapter=membersAdapter
        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            groupViewModel = ViewModelProvider(this).get(DisplayGroupViewModel::class.java)
            // TODO: Use the ViewModel
            groupViewModel.group_name.observe(viewLifecycleOwner, Observer { it ->
                groupName.text = it
            })

            // groupViewModel.member_to_add_name.observe(viewLifecycleOwner, Observer { it ->
            //     memberName.text = it
            // })


            groupViewModel.group_members.observe(viewLifecycleOwner, Observer { it ->
                members = it
                membersAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_list_item_1, members)
                membersList.adapter=membersAdapter
                membersAdapter.notifyDataSetChanged()
            })


    }
}