package com.wisewolf.midhilaarts.Activity.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.wisewolf.midhilaarts.Activity.data.LoginRepository
import com.wisewolf.midhilaarts.Activity.data.Result
import com.wisewolf.midhilaarts.Activity.data.model.LoggedInUser
import com.wisewolf.midhilaarts.R


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel(),AuthListner {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    var loggedUser = MutableLiveData<Result<LoggedInUser>> ()
    var loggedUserResult:  LiveData<Result<LoggedInUser>>  =loggedUser

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job

      loginRepository.login(username, password,this)
       // loggedUser.value=result.value

     /*   if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        }
       else if (result is Result.Fail) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        }else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }*/
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 1
    }

    override fun onStarted() {
        var a=5
    }

    override fun onSucess(loggedUser: MutableLiveData<Result<LoggedInUser>>) {
        this. loggedUser.value=loggedUser.value
    }


    override fun onFail(message: String?) {
        var a=5
        var loggedUser = MutableLiveData<Result<LoggedInUser>> ()
        val user= LoggedInUser("0" , message.toString())
        loggedUser.value=Result.Fail(user)
       this. loggedUser.value=loggedUser.value
    }
}