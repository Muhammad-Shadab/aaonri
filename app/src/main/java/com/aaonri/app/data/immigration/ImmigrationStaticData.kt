package com.aaonri.app.data.immigration

import com.aaonri.app.data.immigration.model.DiscussionCategoryResponse

object ImmigrationStaticData {

    private var immigrationCategory: DiscussionCategoryResponse? = null

    fun setImmigrationCategoryData(value: DiscussionCategoryResponse) {
        immigrationCategory = value
    }

    fun getImmigrationCategorySavedData() = immigrationCategory

}