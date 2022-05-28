package com.aaonri.app.ui.authentication.register.recyclerview

sealed class CommunityRecyclerViewItem {

    class CommunityItem(val title: String) : CommunityRecyclerViewItem()
    class SelectedCommunityItem(val title: String) : CommunityRecyclerViewItem()

}
