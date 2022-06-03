package com.aaonri.app.utils

class Validator {
    companion object {

        fun emailValidation(email: String): Boolean {
            val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
            return emailRegex.toRegex().matches(email)
        }

        fun passwordValidation(password: String): Boolean {
            val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$"
            return passwordRegex.toRegex().matches(password)
        }

    }
}