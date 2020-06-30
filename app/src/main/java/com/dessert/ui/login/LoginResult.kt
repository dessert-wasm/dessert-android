package com.dessert.ui.login

import com.dessert.data.model.LoggedInUser

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
        val success: LoggedInUser? = null,
        val error: Int? = null
)