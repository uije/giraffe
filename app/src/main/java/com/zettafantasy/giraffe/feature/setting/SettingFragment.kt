package com.zettafantasy.giraffe.feature.setting

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.Preferences
import com.zettafantasy.giraffe.feature.remind.NotificationHelper

class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        initClearPreference()
        initNotificationTest()
        initLicense()
        initExport()
    }

    private fun initNotificationTest() {
        findPreference<Preference?>("notification")?.let {
            it.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                NotificationHelper.notifyRemindAlarm(requireContext())
                //todo GiraffeAlarmManager.init(requireContext())
                true
            }
        }
    }

    private fun initLicense() {
        findPreference<Preference?>("license")?.let {
            it.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
                true
            }
        }
    }

    private fun initClearPreference() {
        findPreference<Preference?>("clear_preference")?.let {
            it.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                if (Preferences.clear()) {
                    Toast.makeText(requireContext(), R.string.complete, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), R.string.fail, Toast.LENGTH_SHORT).show()
                }
                true
            }
        }
    }

    private fun initExport() {
        findPreference<Preference?>("export")?.let {
            it.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "text/cvs"
                    putExtra(Intent.EXTRA_TITLE, "giraffe.cvs")
                }
                startActivityForResult(intent, CREATE_FILE)
                true
            }
        }
    }

    companion object {
        const val CREATE_FILE = 1
    }
}