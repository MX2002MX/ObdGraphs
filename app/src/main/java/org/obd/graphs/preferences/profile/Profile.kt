package org.obd.graphs.preferences.profile

import android.content.SharedPreferences
import android.util.Log
import org.obd.graphs.*
import org.obd.graphs.bl.datalogger.PROFILE_CHANGED_EVENT
import org.obd.graphs.preferences.Prefs
import org.obd.graphs.preferences.getString
import org.obd.graphs.preferences.updateToolbar
import java.util.Properties

const val PROFILE_NAME_PREFIX = "pref.profile.names"
const val LOG_KEY = "Profile"
const val PROFILES_PREF = "pref.profiles"

private const val PROFILE_ID_PREF = "pref.profile.id"
private const val MAX_PROFILES_PREF = "pref.profile.max_profiles"
private const val PROFILE_CURRENT_NAME_PREF = "pref.profile.current_name"
private const val PROFILE_INSTALLATION_KEY = "prefs.installed.profiles"
private const val DEFAULT_MAX_PROFILES = "5"
private const val DEFAULT_PROFILE = "profile_1"

class VehicleProfile {
    private val BUILT_IN_PROFILES = arrayOf(
        "default.properties",
        "alfa_175_tbi.properties",
        "alfa_2_0_gme.properties"
    )

    fun getProfileList() =
        (1..Prefs.getString(MAX_PROFILES_PREF, DEFAULT_MAX_PROFILES)!!.toInt())
            .associate {
                "profile_$it" to Prefs.getString(
                    "$PROFILE_NAME_PREFIX.profile_$it",
                    "Profile $it"
                )
            }

    fun setupProfiles() {
        Log.i(LOG_KEY, "Setup profiles enabled='${Prefs.getBoolean(PROFILE_INSTALLATION_KEY, false)}'")
        if (!Prefs.getBoolean(PROFILE_INSTALLATION_KEY, false)) {
            Log.e(LOG_KEY, "Installing profiles")

            Prefs.edit().let { editor ->
                BUILT_IN_PROFILES.forEach { fileName ->
                    Log.i(LOG_KEY, "Loading profile file='$fileName'")

                    val prop = Properties()
                    prop.load(getContext()!!.assets.open(fileName))
                    prop.forEach { t, u ->
                        val value = u.toString()
                        val key = t.toString()

                        Log.i(LOG_KEY, "Inserting $key=$value")

                        when {
                            value.isBoolean() -> {
                                editor.putBoolean(key, value.toBoolean())
                            }
                            value.isArray() -> {
                                val v = value
                                    .replace("[", "")
                                    .replace("]", "")
                                    .split(",")
                                    .map { it.trim() }
                                    .filter { it.isNotEmpty() }
                                    .toMutableSet()
                                editor.putStringSet(key, v)
                            }
                            value.isNumeric() -> {
                                editor.putInt(key, value.toInt())
                            }
                            else -> {
                                editor.putString(key, value.replace("\"", "").replace("\"", ""))
                            }
                        }
                    }
                }
                editor.putString(PROFILE_ID_PREF, getDefaultProfile())
                editor.putBoolean(PROFILE_INSTALLATION_KEY, true)
                editor.apply()
            }
            val defaultProfile = getDefaultProfile()
            Log.i(LOG_KEY,"Setting default profile to: $defaultProfile")
            loadProfile(getDefaultProfile())
            updateToolbar()
        }
    }

    private fun getDefaultProfile(): String =
        getContext()?.resources?.getString(R.string.DEFAULT_PROFILE) ?: DEFAULT_PROFILE

    internal fun getCurrentProfile(): String = Prefs.getString(PROFILE_ID_PREF)!!

    internal fun saveCurrentProfile() {
        Prefs.edit().let {
            val profileName = getCurrentProfile()
            Log.i(LOG_KEY, "Saving user preference to profile='$profileName'")
            Prefs.all
                .filter { (pref, _) -> !pref.startsWith("profile_") }
                .filter { (pref, _) -> !pref.startsWith(PROFILE_NAME_PREFIX) }
                .filter { (pref, _) -> !pref.startsWith(PROFILE_CURRENT_NAME_PREF) }
                .filter { (pref, _) -> !pref.startsWith(PROFILE_INSTALLATION_KEY) }
                .filter { (pref, _) -> !pref.startsWith(PROFILE_INSTALLATION_KEY) }
                .forEach { (pref, value) ->
                    Log.d(LOG_KEY, "'$profileName.$pref'=$value")
                    it.updatePreference("$profileName.$pref", value)
                }
            it.apply()
        }
    }

    fun loadProfile(profileName: String) {
        Log.i(LOG_KEY, "Loading user preferences from the profile='$profileName'")

        resetCurrentProfile()

        Prefs.edit().let {
            Prefs.all
                .filter { (pref, _) -> pref.startsWith(profileName) }
                .filter { (pref, _) -> !pref.startsWith(PROFILE_NAME_PREFIX) }
                .filter { (pref, _) -> !pref.startsWith(PROFILE_CURRENT_NAME_PREF) }
                .filter { (pref, _) -> !pref.startsWith(PROFILE_INSTALLATION_KEY) }
                .forEach { (pref, value) ->
                    pref.substring(profileName.length + 1).run {
                        Log.d(LOG_KEY, "Loading user preference $this = $value")
                        it.updatePreference(this, value)
                    }
                }
            it.apply()
        }

        updateCurrentProfileValue(profileName)
        sendBroadcastEvent(PROFILE_CHANGED_EVENT)
    }

    private fun resetCurrentProfile() {

        Prefs.edit().let {
            getAvailableModes().forEach { key ->
                it.putString("$MODE_HEADER_PREFIX.$key", "")
                it.putString("$MODE_NAME_PREFIX.$key", "")
            }
            it.putString(PREF_CAN_HEADER_EDITOR, "")
            it.putString(PREF_ADAPTER_MODE_ID_EDITOR, "")

            Prefs.all
                .filter { (pref, _) -> !pref.startsWith("datalogger") }
                .filter { (pref, _) -> !pref.startsWith("profile_") }
                .filter { (pref, _) -> !pref.startsWith(PROFILE_ID_PREF) }
                .filter { (pref, _) -> !pref.startsWith(PROFILE_NAME_PREFIX) }
                .filter { (pref, _) -> !pref.startsWith(PROFILE_CURRENT_NAME_PREF) }
                .filter { (pref, _) -> !pref.startsWith(PROFILE_INSTALLATION_KEY) }
                .forEach { (pref, _) ->
                    it.remove(pref)
                }
            it.apply()
        }
    }

    private fun SharedPreferences.Editor.updatePreference(
        prefName: String,
        value: Any?
    ) {
        when (value) {
            is String -> {
                putString(prefName, value)
            }
            is Set<*> -> {
                putStringSet(prefName, value as MutableSet<String>?)
            }
            is Int -> {
                putInt(prefName, value)
            }
            is Boolean -> {
                putBoolean(prefName, value)
            }
        }
    }


    private fun updateCurrentProfileValue(profileName: String) {
        val prefName =
            Prefs.getString("$PROFILE_NAME_PREFIX.$profileName", profileName.toCamelCase())
        Log.i(LOG_KEY, "Setting $PROFILE_CURRENT_NAME_PREF=$prefName")
        Prefs.edit().putString(PROFILE_CURRENT_NAME_PREF, prefName).apply()
    }


    private fun String.toCamelCase() =
        split('_').joinToString(" ", transform = String::capitalize)

    private fun String.isArray() = startsWith("[") || endsWith("]")
    private fun String.isBoolean(): Boolean = startsWith("false") || startsWith("true")
    private fun String.isNumeric(): Boolean = matches(Regex("-?\\d+"))
    private fun String.toBoolean(): Boolean = startsWith("true")

}

val vehicleProfile = VehicleProfile()