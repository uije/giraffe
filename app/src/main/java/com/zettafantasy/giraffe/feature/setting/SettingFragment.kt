package com.zettafantasy.giraffe.feature.setting

import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.Preferences

class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val clear: Preference? = this.findPreference("clear_preference")
        clear?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            if (Preferences.clear()) {
                Toast.makeText(requireContext(), R.string.complete, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), R.string.fail, Toast.LENGTH_SHORT).show()
            }
            true
        }
    }
}