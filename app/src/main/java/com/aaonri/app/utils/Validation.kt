package com.aaonri.app.utils

class Validation {
    companion object {
        fun emailValidation(email: String): Boolean {
            val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
            return EMAIL_REGEX.toRegex().matches(email)
        }
    }
}