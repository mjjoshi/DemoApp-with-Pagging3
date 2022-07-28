package com.example.myapplication.network

import com.example.myapplication.model.collections.CollectionListResponseItem
import com.example.myapplication.model.photoGrid.PhotoGrideResponseItem
import com.example.myapplication.utils.Appconstants
import com.example.myapplication.utils.Appconstants.API_CLINENT
import com.example.myapplication.utils.Appconstants.API_COLLECTIONS
import com.example.myapplication.utils.Appconstants.API_ID
import com.example.myapplication.utils.Appconstants.API_PAGE
import com.example.myapplication.utils.Appconstants.API_PAGE_PER
import com.example.myapplication.utils.Appconstants.API_PHOTO_COLLECTIONS
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(Appconstants.BASE_URL)
    .build()


interface CollecationApiService {

    @GET(API_COLLECTIONS)
    suspend fun getCollections(
        @Query(API_PAGE) pageno: Int,
        @Query(API_PAGE_PER) pagelimit: Int,
        @Query(API_CLINENT) client_ids: String,
    ): ArrayList<CollectionListResponseItem>

    @GET(API_PHOTO_COLLECTIONS) //get list of photos based on Collections id
    suspend fun getPhotoCollections(
        @Path(API_ID) id: String,
        @Query(API_PAGE) pageno: Int,
        @Query(API_PAGE_PER) pagelimit: Int,
        @Query(API_CLINENT) client_ids: String,
    ): ArrayList<PhotoGrideResponseItem>

//    @GET(API_SEARCH_PHOTO) //this is for photo search . but due to different model we can not use it.
//    suspend fun getPhotoSearch(
//        @Query(API_PAGE) pageno: Int,
//        @Query(API_CLINENT) client_ids: String,
//        @Query(API_QUERY) query: String,
//    ): ArrayList<PhotoGrideResponseItem>
}

object CollecationsApi {
    val retrofitService: CollecationApiService by lazy {
        retrofit.create(CollecationApiService::class.java)
    }
}