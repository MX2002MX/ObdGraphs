package org.openobd2.core.logger.ui.preferences

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import org.openobd2.core.logger.R
import org.openobd2.core.logger.bl.datalogger.DataLoggerPreferences
import org.openobd2.core.logger.ui.common.onDoubleClickListener
import org.openobd2.core.logger.ui.preferences.profile.registerProfileListener
import org.openobd2.core.logger.ui.preferences.profile.registerSaveUserPreferences

const val PREFERENCE_SCREEN_KEY = "preferences.rootKey"

class PreferencesFragment : PreferenceFragmentCompat() {

    val preferences: DataLoggerPreferences by lazy { DataLoggerPreferences.instance }

    override fun onNavigateToScreen(preferenceScreen: PreferenceScreen?) {
        super.onNavigateToScreen(preferenceScreen)
        setPreferencesFromResource(R.xml.preferences, preferenceScreen!!.key)
        registerListeners()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        if (arguments == null) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
        } else {
            setPreferencesFromResource(
                R.xml.preferences,
                requireArguments().get(PREFERENCE_SCREEN_KEY) as String
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        registerConnectionTypeListener()
        registerListeners()
        listView.setBackgroundColor(Color.LTGRAY)
        listView.setOnTouchListener(onDoubleClickListener(requireContext()))
        return root
    }

    private fun registerListeners() {
        registerViewsPreferenceChangeListeners()
        registerProfileListener()
        registerSaveUserPreferences()
    }

    private fun registerConnectionTypeListener() {
        val bluetooth = "bluetooth"

        val prefMode = findPreference<ListPreference>(PREFERENCE_CONNECTION_TYPE)
        val p1 = findPreference<Preference>("$PREFERENCE_CONNECTION_TYPE.$bluetooth")
        val p2 = findPreference<Preference>("$PREFERENCE_CONNECTION_TYPE.wifi")

        when (Prefs.getString(PREFERENCE_CONNECTION_TYPE)) {
            bluetooth -> {
                p1?.isVisible = true
                p2?.isVisible = false
            }
            else -> {
                p1?.isVisible = false
                p2?.isVisible = true
            }
        }

        prefMode?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                when (newValue) {
                    bluetooth -> {
                        p1?.isVisible = true
                        p2?.isVisible = false
                    }
                    else -> {
                        p1?.isVisible = false
                        p2?.isVisible = true
                    }
                }
                true
            }
    }
}