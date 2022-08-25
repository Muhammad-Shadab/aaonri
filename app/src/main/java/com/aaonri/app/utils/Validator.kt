package com.aaonri.app.utils

class Validator {
    companion object {

        fun emailValidation(email: String): Boolean {
            val emailRegex =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            return emailRegex.toRegex().matches(email)
        }

        fun passwordValidation(password: String): Boolean {
            val passwordRegex = "^(?=.*[A-Z])(?=.*[0-9]).{8,}$"
            return passwordRegex.toRegex().matches(password)
        }

        fun urlValidation(url: String): Boolean {
            val passwordRegex =
                "(http|https)://((\\w)|([0-9])|([-|]))+([\\.|/]((\\w)|([0-9]*)|([-|])*))+"
            return passwordRegex.toRegex().matches(url)
        }

    }
}