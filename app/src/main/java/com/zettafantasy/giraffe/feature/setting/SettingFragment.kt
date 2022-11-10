package com.zettafantasy.giraffe.feature.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.zettafantasy.giraffe.GiraffeApplication
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.Preferences
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.feature.remind.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingFragment : PreferenceFragmentCompat() {

    private val repository: GiraffeRepository by lazy {
        (activity?.application as GiraffeApplication).repository
    }

    private val portManager: PortManager by lazy {
        PortManager(repository, requireContext())
    }

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
                CoroutineScope(Dispatchers.IO).launch {
                    val hasContent = !repository.isRecordEmpty()
                    withContext(Dispatchers.Main) {
                        if (hasContent) {
                            startActivityForResult(portManager.exportIntent, CREATE_FILE)
                        } else {
                            showToast(R.string.desc_no_record_to_export)
                        }
                    }
                }
                true
            }
        }
    }

    private fun showToast(@StringRes resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                portManager.exportCSV(uri)
            }
        }
    }

    companion object {
        const val CREATE_FILE = 1
    }
}
