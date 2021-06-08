package com.Client.Table.ui.displaygroup
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Client.Table.R
import com.Client.Table.data.GroupDataSource
import com.Client.Table.data.LoginRepository
import com.Client.Table.data.Result
import com.Client.Table.data.model.Group
import com.Client.Table.ui.login.LoggedInUserView
import com.Client.Table.ui.login.LoginResult

class DisplayGroupViewModel(var debug:Boolean=false) : ViewModel() {
    private val dataSource: GroupDataSource = GroupDataSource()
    private val _group_name: MutableLiveData<String> = MutableLiveData<String>("Your Group Name")
    //private val _member_to_add: MutableLiveData<String> = MutableLiveData<String>()
    private val _member_list = MutableLiveData<MutableList<String>>(ArrayList())
    private val _group_name_list = MutableLiveData<MutableList<Pair<Int,String>>>(ArrayList())
    private val _error_val: MutableLiveData<Int> = MutableLiveData<Int>(-1)
    private var _current_group:MutableLiveData<Int> = MutableLiveData<Int>(-1)
    private var _current_group_name: MutableLiveData<String> = MutableLiveData<String>("")


    val group_name: LiveData<String> get() = _group_name
    //val member_to_add_name: LiveData<String> get() = _member_to_add
    val group_members: LiveData<MutableList<String>> get() = _member_list
    val groups_: LiveData<MutableList<Pair<Int,String>>> get() = _group_name_list
    val error_ :LiveData<Int> get() = _error_val

    fun setData(name: String, member: String, members: MutableList<String>)
    {
        _group_name.value = name
       // _member_to_add.value = member
        _member_list.value = members

    }

    fun fetchGroupList(username:String){
        val data = dataSource.fetchUserGroups(username)
        if(data is Result.Success){
            _group_name_list.value?.let {
                _group_name_list.value = data.data
            }
        }

    }

    fun fetchGroupData(name: String, id: Int){
        //we call fetchGroupData form the groupname selector when it is empty. Generally not necessary, but here as a safeguard.
        if(id <= 0)
        {
            return
        }
        //
        val data = dataSource.getGroup(name, id)
        _current_group.value?.let {
            _current_group.value = id
        }
        _current_group_name.value?.let {
            _current_group_name.value = name
        }

        if (data is Result.Success) {
            translateGroupDataSource(data.data)

        } else {
            _error_val.value = R.string.group_not_found
        }
    }

    fun translateGroupDataSource(group: Group){
        _group_name.value?.let {
            _group_name.value = group.GroupName
        }
        // _member_to_add.value = member
        _member_list.value?.let {
            _member_list.value = ArrayList(group.fetchedUserList.map { it.name })
        }
    }

    fun addGroup(name:String){
        group_members.value?.let{
            for(pair in groups_.value!!){
                if (pair.second == name) {
                    _error_val.value = R.string.cannot_create_group_with_same_name
                    return@let
                }
            }

            val data = dataSource.createGroup(name, LoginRepository.user!!.displayName)
            if (data is Result.Success) {
                fetchGroupList(LoginRepository.user!!.displayName)
                //fetchgrouplist always triggers fetchgroupdata on first list element
                fetchGroupData(_current_group_name.value!!,_current_group.value!!)
            } else {
                //maybe other string
                _error_val.value = R.string.cannot_create_group_with_same_name
            }
        }
    }

    fun addMember(name:String){
        _member_list.value?.let {
            val mylist: MutableList<String> = ArrayList<String>(_member_list.value)
            if(mylist.size>=6){
                _error_val.value = R.string.too_many_members
                return@let
            }
            if (mylist.contains(name)) {
                _error_val.value = R.string.cannot_add_member_twice
                return@let
            }
            val data = dataSource.addMemberToGroup(_current_group_name.value!!, _current_group.value!!, name)
            if(data is Result.Error) {
                _error_val.value = R.string.user_not_found
                return@let
            }
            fetchGroupData(_current_group_name.value!!,_current_group.value!!)
        }
    }
    fun deleteMember(username: String){
        _member_list.value?.let {
        val data = dataSource.deleteMemberFromGroup(_current_group_name.value!!, _current_group.value!!, username)
        if(data is Result.Success) {
            _error_val.value = R.string.deleted_member
            fetchGroupData(_current_group_name.value!!,_current_group.value!!)
        }
        }
        if(username == LoginRepository.user!!.displayName)
        {
            fetchGroupList(LoginRepository.user!!.displayName)
            fetchGroupData(_current_group_name.value!!,_current_group.value!!)
        }
    }




}