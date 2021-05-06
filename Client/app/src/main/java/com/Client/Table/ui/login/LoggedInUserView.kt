package com.Client.Table.ui.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
        val displayName: String,
        val jwtToken: String
        //... other data fields that may be accessible to the UI
)