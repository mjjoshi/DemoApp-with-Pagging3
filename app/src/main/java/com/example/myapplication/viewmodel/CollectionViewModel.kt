package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myapplication.model.collections.CollectionListResponseItem
import com.example.myapplication.model.photoGrid.PhotoGrideResponseItem
import com.example.myapplication.network.CollecationsApi
import com.example.myapplication.repository.CollectionPagingSource
import com.example.myapplication.utils.Appconstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException


class CollectionViewModel(app: Application) : AndroidViewModel(app) {

    private val collectionsList = MutableLiveData<List<CollectionListResponseItem>>()
    val collections: LiveData<List<CollectionListResponseItem>> get() = collectionsList
    private val photosList = MutableLiveData<List<PhotoGrideResponseItem>>()
    val photos: LiveData<List<PhotoGrideResponseItem>> get() = photosList


    fun getCollections(page: Int) {
        try {
            viewModelScope.launch {
                val list = CollecationsApi.retrofitService.getCollections(
                    page,
                    20,
                    Appconstants.CLIENT_KEYS,
                )
                if (list.isNotEmpty()) {
                    collectionsList.value = list
                }
            }

        } catch (e: Exception) {
            collectionsList.value = ArrayList()
        } catch (e: SocketTimeoutException) {
            collectionsList.value = ArrayList()
        }
    }


    fun getPhotos(id: String, page: Int) {
        viewModelScope.launch {
            try {
                val list = CollecationsApi.retrofitService.getPhotoCollections(
                    id,
                    page,
                    20,
                    Appconstants.CLIENT_KEYS,
                )
                if (list.isNotEmpty()) {
                    photosList.value = list
                }
            } catch (e: Exception) {
                photosList.value = ArrayList()
            } catch (e: SocketTimeoutException) {
                collectionsList.value = ArrayList()
            }
        }
    }


    val getAllCollections: Flow<PagingData<CollectionListResponseItem>> =
        Pager(config = PagingConfig(20, enablePlaceholders = false)) {
            CollectionPagingSource(CollecationsApi.retrofitService)
        }.flow.cachedIn(viewModelScope)


}