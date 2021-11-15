package com.wisewolf.midhilaarts.Activity.ui.login

import androidx.lifecycle.MutableLiveData
import com.wisewolf.midhilaarts.Activity.data.Result
import com.wisewolf.midhilaarts.Activity.data.model.LoggedInUser

interface AuthListner {
    fun onStarted()
    fun onSucess(loggedUser: MutableLiveData<Result<LoggedInUser>>)
    fun onFail(message: String?)
}