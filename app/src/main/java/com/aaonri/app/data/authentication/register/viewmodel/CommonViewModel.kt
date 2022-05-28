package com.aaonri.app.data.authentication.register.viewmodel

import androidx.lifecycle.ViewModel
import com.aaonri.app.ui.authentication.register.recyclerview.CommunityRecyclerViewItem

class CommonViewModel : ViewModel() {

    var selectedCommunityList = mutableListOf<CommunityRecyclerViewItem>()
    private set

    val homeList = mutableListOf<CommunityRecyclerViewItem>()

    init{
        homeList.add(CommunityRecyclerViewItem.CommunityItem("Shadab"))
        homeList.add(CommunityRecyclerViewItem.CommunityItem("Shadab"))
        homeList.add(CommunityRecyclerViewItem.CommunityItem("Shadab"))
        homeList.add(CommunityRecyclerViewItem.CommunityItem("Shadab"))
        homeList.add(CommunityRecyclerViewItem.CommunityItem("Shadab"))
        homeList.add(CommunityRecyclerViewItem.CommunityItem("Shadab"))
        homeList.add(CommunityRecyclerViewItem.CommunityItem("Shadab"))
    }

    fun addToCommunityList(value: String) {
        selectedCommunityList.add(CommunityRecyclerViewItem.SelectedCommunityItem(value))
    }

}