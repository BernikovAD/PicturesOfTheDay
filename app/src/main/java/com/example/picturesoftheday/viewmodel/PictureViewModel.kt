package com.example.picturesoftheday.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.picturesoftheday.app.App
import com.example.picturesoftheday.repository.LocalRepositoryImpl


class PictureViewModel(
    private val liveDataObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val picturesRepository: LocalRepositoryImpl = LocalRepositoryImpl(App.getPicturesDao())) : ViewModel() {
    fun getLiveData() = liveDataObserver


    fun getAllPictures(){
        liveDataObserver.value = AppState.SuccessDB(picturesRepository.getAllPictures())
    }

}