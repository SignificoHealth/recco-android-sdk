package com.recco.internal.core.persistence

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.recco.api.model.ReccoConfig
import com.recco.internal.core.model.authentication.PAT
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val RECCO_SDK_PREFS = "recco_sdk.prefs"

@Singleton
class AuthCredentials @Inject constructor(
    @ApplicationContext context: Context
) {
    lateinit var sdkConfig: ReccoConfig
        private set

    private val prefs by lazy {

        EncryptedSharedPreferences.create(
            context,
            RECCO_SDK_PREFS,
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    var pat: PAT? = null
        private set

    var userId: String? = null
        get() = prefs.getString(USER_ID_KEY, null)
        private set

    var tokenId: String? = null
        get() = prefs.getString(TOKEN_ID_KEY, null)
        private set

    fun init(sdkConfig: ReccoConfig) {
        this.sdkConfig = sdkConfig
    }

    fun setPAT(pat: PAT) {
        this.pat = pat
        prefs.edit { putString(TOKEN_ID_KEY, pat.tokenId) }
    }

    fun setUserId(id: String) {
        prefs.edit { putString(USER_ID_KEY, id) }
        pat = null
        prefs.edit { putString(TOKEN_ID_KEY, null) }
    }

    fun clearCache() {
        prefs.edit { putString(USER_ID_KEY, null) }
        pat = null
        prefs.edit { putString(TOKEN_ID_KEY, null) }
    }

    companion object {
        private const val USER_ID_KEY = "userId"
        private const val TOKEN_ID_KEY = "tokenId"
    }
}
