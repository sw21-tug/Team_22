package com.Client.Table

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.Client.Table.ui.displaygroup.DisplayGroupViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock

class GroupModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private var model: DisplayGroupViewModel? = null

    @Before
    fun setUp() {
        model = DisplayGroupViewModel()
    }

    /* @Test
    fun updateGroupName()
    {
        val model: DisplayGroupViewModel = DisplayGroupViewModel()

        val group_name = "Graz"
        val username = "max_mustermann"
        val members: MutableList<String> = emptyList<String>().toMutableList()

        model.setData(group_name, username, members)

        assert(model.group_name.value.equals(group_name))

    }

    @Test
    fun addRemoveGroupMembers()
    {

        val model: DisplayGroupViewModel = DisplayGroupViewModel()

        val group_name = "Graz"
        val username = "max_mustermann"
        val members: MutableList<String> = emptyList<String>().toMutableList()

        model.setData(group_name, username, members)
        model.addMember("max_maximus")
        model.addMember("clar")
        model.addMember("homer")
        model.addMember("anonymous")


        assert(model.group_members.value!!.contains("clar"))
        assert(model.group_members.value!!.contains("homer"))
        assert(model.group_members.value!!.contains("anonymous"))

        assert(model.group_members.value!!.size == 4)

        model.deleteMember("clar")
        model.deleteMember("homer")
        model.deleteMember("anonymous")

        assert(model.group_members.value!!.contains("clar") != true)
        assert(model.group_members.value!!.contains("homer") != true)
        assert(model.group_members.value!!.contains("anonymous") != true)

        assert(model.group_members.value!!.size == 1)

    }

    @Test
    fun addMemberTwice()
    {
        val model: DisplayGroupViewModel = DisplayGroupViewModel()

        val group_name = "Graz"
        val username = "max_mustermann"
        val members: MutableList<String> = emptyList<String>().toMutableList()

        model.setData(group_name, username, members)

        model.addMember(username)
        model.addMember(username)

        assert(model.group_members.value!!.size == 1)
    }

    @Test
    fun addGroup()
    {
        val model: DisplayGroupViewModel = DisplayGroupViewModel()

        val group_name = "Graz"
        val username = "max_mustermann"
        val members: MutableList<String> = emptyList<String>().toMutableList()

        model.addGroup(group_name)
        assert(members.isEmpty())

        model.addMember(username)
        assert(model.group_members.value!!.size == 1)
    }

    @Test
    fun addTooManyMembers()
    {
        val model: DisplayGroupViewModel = DisplayGroupViewModel()

        val group_name = "Graz"
        val username = "max_mustermann"
        val members: MutableList<String> = emptyList<String>().toMutableList()

        model.addGroup("Test")
        model.addMember(username)
        model.addGroup(group_name)
        assert(members.isEmpty())

        for(i : Int in 1..20){
            model.addMember(username + i.toString())
        }
        assert(model.group_members.value!!.size == 6)

    }*/
}