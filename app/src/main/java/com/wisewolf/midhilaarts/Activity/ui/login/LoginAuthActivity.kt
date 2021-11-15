package com.wisewolf.midhilaarts.Activity.ui.login

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.wisewolf.midhilaarts.Activity.data.Result
import com.wisewolf.midhilaarts.Activity.data.model.LoggedInUser
import com.wisewolf.midhilaarts.R


import com.wisewolf.midhilaarts.databinding.ActivityLoginAuthBinding

class LoginAuthActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginAuthBinding

    var builder: AlertDialog.Builder? = null
    lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val username = binding.username
        val password = binding.password
        val login = binding.login

        builder = AlertDialog.Builder(this@LoginAuthActivity)
        // create an alert builder
        // create an alert builder
        builder!!.setCancelable(false)
        // set the custom layout
        // set the custom layout
        val customLayout = layoutInflater.inflate(R.layout.alert_loading, null)
        builder!!.setView(customLayout)
        // add a button
        // create and show the alert dialog
        // add a button
        // create and show the alert dialog
        dialog = builder!!.create()
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)


        loginViewModel.loginFormState.observe(this@LoginAuthActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loggedUserResult.observe(this@LoginAuthActivity, Observer {
            val loginResult = it ?: return@Observer
            loginResult.let { it }
            val _loginResult = MutableLiveData<LoginResult>()

           dialog.cancel()
            if (loginResult is Result.Error) {
                _loginResult.value = LoginResult(error = R.string.login_failed)
                showLoginFailed(_loginResult.value!!.error!!)
            }
            if (loginResult is Result.Success) {
                _loginResult.value =
                    LoginResult(success = LoggedInUserView(displayName = loginResult.data.displayName))
                updateUiWithUser(_loginResult.value!!.success!!)
            }
            if (loginResult is Result.Fail) {
                _loginResult.value =
                    LoginResult(success = LoggedInUserView(displayName = loginResult.data.displayName))
                Toast.makeText(
                    applicationContext,
                    "  ${_loginResult.value!!.success?.displayName}",
                    Toast.LENGTH_LONG
                ).show()
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
          //  finish()
        })

 /*       loginViewModel.loginResult.observe(this@LoginAuthActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            if (loginResult.fail != null) {
                updateUiWithUser(loginResult.fail)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            //  finish()
        })*/

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                dialog.show()
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}