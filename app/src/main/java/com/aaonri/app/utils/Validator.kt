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
            val passwordRegex = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)"
            return passwordRegex.toRegex().matches(url)
        }

    }
}