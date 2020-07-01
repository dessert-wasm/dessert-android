package com.dessert.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.dessert.data.LoginRepository
import com.dessert.data.Result

import com.dessert.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        GlobalScope.launch {
            val result = loginRepository.login(username, password)

            if (result is Result.Success) {
                _loginResult.postValue(LoginResult(success = result.data))
            } else {
                //TODO CHECK LE MESSAGE DERREUR POUR VOIS SI ON A INTERNET
                _loginResult.postValue(LoginResult(error = R.string.login_failed))
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        _loginForm.value = LoginFormState(isDataValid = true)

        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } /*else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        }*/ else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            false
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}