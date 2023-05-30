package com.shadowflight.model

import javax.inject.Singleton

@Singleton
class UserAuthCredentials {
    var pat: PAT? = null
        private set

    var id: String? = null
        private set

    fun setPAT(pat: PAT) {
        this.pat = pat
    }

    fun setId(id: String) {
        this.id = id
        this.pat = null
    }
}