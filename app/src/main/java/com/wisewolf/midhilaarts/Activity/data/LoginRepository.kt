package com.wisewolf.midhilaarts.Activity.data

import com.wisewolf.midhilaarts.Activity.data.model.LoggedInUser
import com.wisewolf.midhilaarts.Activity.ui.login.AuthListner

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String, authListner: AuthListner)  {
        // handle login
      dataSource.login(username, password,authListner)

        /*if (result. is Result.Success) {
            setLoggedInUser((result as Result.Success<LoggedInUser>).data)
        }*/


    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}