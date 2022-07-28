package com.example.myapplication.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapplication.model.collections.CollectionListResponseItem
import com.example.myapplication.network.CollecationApiService
import com.example.myapplication.utils.Appconstants
import retrofit2.HttpException
import java.io.IOException

class CollectionPagingSource(private val apiService: CollecationApiService) :
    PagingSource<Int, CollectionListResponseItem>() {

    private val DEFAULT_PAGE_INDEX= 1

    override fun getRefreshKey(state: PagingState<Int, CollectionListResponseItem>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionListResponseItem> {
        val page = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val response = apiService.getCollections(page,params.loadSize,Appconstants.CLIENT_KEYS)
            LoadResult.Page(
                response,
                prevKey = if(page == DEFAULT_PAGE_INDEX) null else page-1,
                nextKey = if(response.isEmpty()) null else page+1
            )
        } catch (exception: IOException){
            LoadResult.Error(exception)
        } catch (exception: HttpException){
            LoadResult.Error(exception)
        }
    }
}