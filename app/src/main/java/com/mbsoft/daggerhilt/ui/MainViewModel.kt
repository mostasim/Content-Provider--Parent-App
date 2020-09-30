package com.mbsoft.daggerhilt.ui

import android.app.Application
import androidx.lifecycle.*
import androidx.room.Room
import com.mbsoft.daggerhilt.db.SimpleRoomDB
import com.mbsoft.daggerhilt.db.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) :AndroidViewModel(application) {
    private var db = SimpleRoomDB.getInstance(application)

    fun getUserData()=db.userDao().getAll()

    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            db.userDao().deleteAll()
            db.userDao().insertAll(user)
        }
    }
    fun clearData(){
        viewModelScope.launch(Dispatchers.IO) {
            db.userDao().deleteAll()
        }
    }

}