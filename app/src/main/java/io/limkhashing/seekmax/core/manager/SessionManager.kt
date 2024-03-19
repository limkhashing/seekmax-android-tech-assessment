package io.limkhashing.seekmax.core.manager

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Named

class SessionManager @Inject constructor(
    @Named(SESSION) private val sessionDataStore: DataStore<Preferences>
) {

    companion object {
        const val SESSION = "SESSION"
        private const val JWT = "JWT"
        val jwt = stringPreferencesKey(JWT)
    }

     fun getJwtSession(): String? {
        return runBlocking {
            sessionDataStore.data.catch {
                emit(emptyPreferences())
            }.map { value: Preferences ->
                value[jwt]
            }.firstOrNull()
        }
    }

    suspend fun setJwtSession(userJWT: String) {
        runBlocking {
            sessionDataStore.edit { preference ->
                preference[jwt] = userJWT
            }
        }
    }

    fun clearSession() {
        runBlocking {
            sessionDataStore.edit { preference ->
                preference.remove(jwt)
            }
        }
    }

}