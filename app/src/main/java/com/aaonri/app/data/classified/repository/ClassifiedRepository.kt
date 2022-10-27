package com.aaonri.app.data.classified.repository

import com.aaonri.app.data.classified.api.ClassifiedApi
import com.aaonri.app.data.classified.api.DeleteClassifiedApi
import com.aaonri.app.data.classified.api.PostClassifiedApi
import com.aaonri.app.data.classified.model.GetClassifiedByUserRequest
import com.aaonri.app.data.classified.model.LikeDislikeClassifiedRequest
import com.aaonri.app.data.classified.model.PostClassifiedRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ClassifiedRepository @Inject constructor(
    private val classifiedApi: ClassifiedApi,
    private val postClassifiedApi: PostClassifiedApi,
    private val deleteClassifiedApi: DeleteClassifiedApi
) {

    /*suspend fun getAllUserAdsClassified(email: String) = classifiedApi.allUserAdsClassified(email)*/

    suspend fun getFavoriteClassified(userEmail: String) =
        classifiedApi.getFavoriteClassified(userEmail)

    suspend fun getClassifiedCategory() = postClassifiedApi.getClassifiedCategory()

    suspend fun uploadClassifiedPics(
        files: List<MultipartBody.Part>,
        addId: RequestBody,
        dellId: RequestBody
    ) =
        postClassifiedApi.uploadClassifiedPics(files, addId, dellId)

    suspend fun deleteClassifiedPics(
        files: MultipartBody.Part,
        addId: RequestBody,
        dellId: RequestBody
    ) =
        postClassifiedApi.deleteClassifiedPics(files, addId, dellId)

    suspend fun getClassifiedSellerName(email: String) =
        classifiedApi.getClassifiedSellerInfo(email)

    suspend fun getClassifiedAddDetails(addId: Int) =
        classifiedApi.getAddDetails(addId)

    suspend fun getClassifiedByUser(getClassifiedsByUserRequest: GetClassifiedByUserRequest) =
        classifiedApi.getClassifiedByUser(getClassifiedsByUserRequest)

    suspend fun postClassified(postClassifiedRequest: PostClassifiedRequest) =
        postClassifiedApi.postClassified(postClassifiedRequest)

    suspend fun likeDislikeClassified(likeDislikeClassifiedRequest: LikeDislikeClassifiedRequest) =
        classifiedApi.likedDislikeClassified(likeDislikeClassifiedRequest)

    suspend fun getClassifiedLikeDislikeInfo(email: String, addId: Int, service: String) =
        classifiedApi.getClassifiedLikeInfo(addId, service, email)

    suspend fun updateClassified(postClassifiedRequest: PostClassifiedRequest) =
        postClassifiedApi.updateClassified(postClassifiedRequest)

    suspend fun deleteClassified(classifiedId: Int) =
        deleteClassifiedApi.deleteClassified(classifiedId)

    // suspend fun uploadImages(uploadImagesRequest: UploadImagesRequest) = postClassifiedApi.uploadImages(uploadImagesRequest)

}