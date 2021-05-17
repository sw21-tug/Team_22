package com.Client.Table.ui.displaygroup
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.Client.Table.R
import com.Client.Table.data.GroupDataSource
import com.Client.Table.data.Result
import com.Client.Table.data.model.Group
import com.Client.Table.ui.login.LoggedInUserView
import com.Client.Table.ui.login.LoginResult

class DisplayGroupViewModel() : ViewModel() {
    private val dataSource: GroupDataSource = GroupDataSource()
    private val _group_name: MutableLiveData<String> = MutableLiveData<String>("Your Group Name")
    //private val _member_to_add: MutableLiveData<String> = MutableLiveData<String>()
    private val _member_list = MutableLiveData<MutableList<String>>(ArrayList())
    private val _error_val: MutableLiveData<Int> = MutableLiveData<Int>(-1)


    val group_name: LiveData<String> get() = _group_name
    //val member_to_add_name: LiveData<String> get() = _member_to_add
    val group_members: LiveData<MutableList<String>> get() = _member_list
    val error_ :LiveData<Int> get() = _error_val

    fun setData(name: String, member: String, members: MutableList<String>)
    {
        _group_name.value = name
       // _member_to_add.value = member
        _member_list.value = members

    }
    fun fetchGroupData(name: String){
        val data = dataSource.getGroup(name)

        if (data is Result.Success) {
            translateGroupDataSource(data.data)

        } else {
            _error_val.value = R.string.group_not_found

        }

    }

    fun translateGroupDataSource(group: Group){
        _group_name.value = group.GroupName
        // _member_to_add.value = member
        _member_list.value = ArrayList(group.fetchedUserList.map { it.name })
    }

    fun addMember(name:String){
        _member_list.value?.let {
            val mylist: MutableList<String> = ArrayList<String>(_member_list.value)
            if (!mylist.contains(name))
            {
                if(mylist.size>=10){
                    _error_val.value = R.string.too_many_members
                }
                else {
                    mylist.add(name)
                }
            }
            else{
                _error_val.value = R.string.cannot_add_member_twice
            }
            _member_list.value = mylist
        }
    }
    fun deleteMember(id: Int){
        _member_list.value?.let {
            val mylist: MutableList<String> = ArrayList<String>(_member_list.value)
            mylist.removeAt(id)
            _error_val.value = R.string.deleted_member
            _member_list.value = mylist
        }
    }




}