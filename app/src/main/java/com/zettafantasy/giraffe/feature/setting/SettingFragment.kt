package com.zettafantasy.giraffe.feature.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.zettafantasy.giraffe.BuildConfig
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

    private val navController: NavController? by lazy {
        view?.let { Navigation.findNavController(it) }
    }

    private val repository: GiraffeRepository by lazy {
        (activity?.application as GiraffeApplication).repository
    }

    private val portManager: PortManager by lazy {
        PortManager(repository, requireContext())
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        if (BuildConfig.DEBUG) {
            initClearPreference()
            initNotificationTest()
        } else {
            hideDebugMenus()
        }

        initExport()
        initImport()
        initLicense()
        initAppInfo()
        initThanks()
    }

    private fun initThanks() {
        findPreference<Preference?>("thanks")?.let {
            it.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                navController?.navigate(SettingFragmentDirections.actionGoThanks())
                true
            }
        }
    }

    private fun initAppInfo() {
        findPreference<Preference>("app_info")?.let {
            it.title = getString(R.string.app_version, BuildConfig.VERSION_NAME)
        }
    }

    private fun hideDebugMenus() {
        findPreference<PreferenceCategory>("debug")?.let {
            it.isVisible = false
        }
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

    private fun initImport() {
        findPreference<Preference?>("import")?.let {
            it.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                startActivityForResult(portManager.importIntent, PICK_CSV_FILE)
                true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                portManager.exportCSV(uri)
            }
        } else if (requestCode == PICK_CSV_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                portManager.importCSV(uri)
                Log.d(javaClass.simpleName, "import file $uri")
            }
        }
    }

    companion object {
        const val CREATE_FILE = 1
        const val PICK_CSV_FILE = 2
    }
}
