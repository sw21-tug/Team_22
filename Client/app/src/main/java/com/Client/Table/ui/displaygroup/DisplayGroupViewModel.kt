package com.Client.Table.ui.displaygroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DisplayGroupViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _group_name: MutableLiveData<String> = MutableLiveData<String>("Your Group Name")
    //private val _member_to_add: MutableLiveData<String> = MutableLiveData<String>()
    private val _member_list = MutableLiveData<MutableList<String>>(ArrayList())


    val group_name: LiveData<String> get() = _group_name
    //val member_to_add_name: LiveData<String> get() = _member_to_add
    val group_members: LiveData<MutableList<String>> get() = _member_list

    fun setData(name: String, member: String, members: MutableList<String>)
    {
        _group_name.value = name
       // _member_to_add.value = member
        _member_list.value = members

    }
    fun addMember(name:String){
        _member_list.value?.let {
            val mylist: MutableList<String> = ArrayList<String>(_member_list.value)
                mylist.add(name)
            _member_list.value = mylist
        }
    }
    fun deleteMember(id: Int){
        _member_list.value?.let {
            val mylist: MutableList<String> = ArrayList<String>(_member_list.value)
            mylist.removeAt(id)

            _member_list.value = mylist
        }
    }




}