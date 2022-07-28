package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewmodel::class.java)) {
            return LoginViewmodel(application) as T
        }else if (modelClass.isAssignableFrom(CollectionViewModel::class.java)){
            return CollectionViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}