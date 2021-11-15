package com.wisewolf.midhilaarts.Activity.data

import androidx.lifecycle.MutableLiveData
import com.wisewolf.midhilaarts.Activity.data.model.LoggedInUser
import com.wisewolf.midhilaarts.Activity.ui.login.AuthListner
import com.wisewolf.midhilaarts.Global.GlobalData
import com.wisewolf.midhilaarts.Model.LoginMod
import com.wisewolf.midhilaarts.RetrofitClientInstance
import com.wisewolf.midhilaarts.RetrofitClientInstance.GetDataService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
var authListner:AuthListner ?=null
    fun login(username: String, password: String, authListner: AuthListner)  {
authListner
        this.authListner=authListner
        // TODO: handle loggedInUser authentication
        var loggedUser = MutableLiveData<Result<LoggedInUser>> ()
        try {

            val service = RetrofitClientInstance.getRetrofitInstance().create(
                GetDataService::class.java
            )
            val call = service.loginCheck("/app/api/login/$username/$password")
            call.enqueue(object : Callback<List<LoginMod>?> {
                override fun onResponse(
                    call: Call<List<LoginMod>?>,
                    response: Response<List<LoginMod>?>
                ) {
                    val a = ""
                    if (response.body() != null) {
                        if (response.body()!!.size != 0) {
                            GlobalData.logged = true
                             GlobalData.phone = username
                            GlobalData.username = response.body()!![0].userName
                            GlobalData.Logged_user = response.body()!![0]
                            val user= LoggedInUser(response.body()!![0].id, response.body()!![0].userName)
                            loggedUser.value=Result.Success(user)
                             savedetails(GlobalData.username, GlobalData.Logged_user)
                            this@LoginDataSource.authListner?.onSucess(loggedUser)
                        } else {

                             //   "login error due to wrong credentials",

                            val user= LoggedInUser("0" ,"login error due to wrong credentials")
                            loggedUser.value=Result.Fail(user)
                            this@LoginDataSource.authListner?.onFail("login error due to wrong credentials")

                        }
                    }

                }

                override fun onFailure(call: Call<List<LoginMod>?>, t: Throwable) {
                    val a = ""
                    val user= LoggedInUser("0" ,t.message.toString())
                    loggedUser.value=Result.Fail(user)
                   this@LoginDataSource.authListner?.onFail(t.message)

                }
            })


        } catch (e: Throwable) {

            loggedUser.value=Result.Error(IOException("Error logging in", e))
            this.authListner?.onFail(e.message)

        }
    }

    private fun savedetails(username: String?, loggedUser: LoginMod) {
      //  TODO("Not yet implemented")
    }

    fun logout() {
        // TODO: revoke authentication
    }
}