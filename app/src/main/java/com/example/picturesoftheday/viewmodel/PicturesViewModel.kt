package com.example.picturesoftheday.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.picturesoftheday.repository.RepositoryPictures
import com.example.picturesoftheday.model.EntityPictures
import com.example.picturesoftheday.room.PicturesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PicturesViewModel(application: Application):AndroidViewModel(application) {

    val readAllData:LiveData<MutableList<EntityPictures>>
    private val repository : RepositoryPictures

    init {
        val picturesDao = PicturesDatabase.getDatabase(application).picturesDao()
        repository = RepositoryPictures(picturesDao)
        readAllData = repository.readAllData
    }

    fun addPictures(entityPOD: EntityPictures){
        viewModelScope.launch(Dispatchers.IO){
            repository.addPictures(entityPOD)
        }
    }
    fun deleteUser(entityPOD: EntityPictures){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePicture(entityPOD)
        }
    }

    fun deleteAllUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllPictures()
        }
    }
}