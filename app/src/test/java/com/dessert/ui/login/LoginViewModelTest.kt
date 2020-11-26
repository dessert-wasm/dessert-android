package com.dessert.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.dessert.R
import com.dessert.mock
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class LoginViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var loginViewModel: LoginViewModel
    private val loginObserver: Observer<LoginResult> = mock()
    private val formObserver: Observer<LoginFormState> = mock()

    @Before
    fun before() {
        loginViewModel = LoginViewModelFactory().create(LoginViewModel::class.java)
        loginViewModel.loginResult.observeForever(loginObserver)
        loginViewModel.loginFormState.observeForever(formObserver)
    }

    @Test
    fun login() {
        loginViewModel.login("lucas.santoni@epitech.eu", "12345")
        Thread.sleep(2500);
        val captor = ArgumentCaptor.forClass(LoginResult::class.java)
        captor.run {
            verify(loginObserver).onChanged(capture())
            assert(value.error == null)
            assert(value.success != null)
            assertEquals(2, value.success!!.userId)
            assertEquals("lucas.santoni@epitech.eu", value.success!!.displayName)
        }
    }

    @Test
    fun loginDataChanged() {
        val captor = ArgumentCaptor.forClass(LoginFormState::class.java)
        loginViewModel.loginDataChanged("lucas@santoni", "12345")

        captor.run {
            verify(formObserver).onChanged(capture())
            assert(!value.isDataValid)
            assertEquals(R.string.invalid_username, value.usernameError)
        }

        loginViewModel.loginDataChanged("lucas.santoni@epitech.eu", "12345")

        captor.run {
            verify(formObserver, times(2)).onChanged(capture())
            assert(value.isDataValid)
        }

    }
}