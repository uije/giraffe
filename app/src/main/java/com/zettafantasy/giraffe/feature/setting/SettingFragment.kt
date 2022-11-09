package com.zettafantasy.giraffe.feature.setting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.opencsv.CSVWriter
import com.zettafantasy.giraffe.BuildConfig
import com.zettafantasy.giraffe.GiraffeApplication
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.Preferences
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.feature.record.RecordWrapper
import com.zettafantasy.giraffe.feature.remind.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileWriter
import java.lang.Exception

class SettingFragment : PreferenceFragmentCompat() {

    private val repository: GiraffeRepository by lazy {
        (activity?.application as GiraffeApplication).repository
    }

    private val emotionInventory: EmotionInventory by lazy {
        EmotionInventory.getInstance(resources)
    }

    private val needInventory: NeedInventory by lazy {
        NeedInventory.getInstance(resources)
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
                startActivityForResult(Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "text/csv"
                    putExtra(Intent.EXTRA_TITLE, "giraffe.csv")
                }, CREATE_FILE)
                true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                CoroutineScope(Dispatchers.IO).launch {
                    val result = exportCSV(uri)
                    withContext(Dispatchers.Main) {
                        showResultToast(result)
                    }
                }
            }
        }
    }

    private fun showResultToast(result: Boolean) {
        Toast.makeText(
            requireContext(),
            if (result) R.string.complete else R.string.fail,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun exportCSV(uri: Uri): Boolean {
        try {
            requireContext().contentResolver.openFileDescriptor(uri, "w").use { fd ->
                FileWriter(fd?.fileDescriptor).use { fileWriter ->
                    CSVWriter(fileWriter).use { csvWriter ->
                        writeHeader(csvWriter)
                        repository.getRecords().stream()
                            .map { RecordWrapper(it, emotionInventory, needInventory) }
                            .forEach { csvWriter.write(it) }
                    }
                }
            }
            return true
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            return false
        }
    }

    private fun writeHeader(csvWriter: CSVWriter) {
        csvWriter.writeNext(
            listOf(
                getString(R.string.date),
                getString(R.string.stimulus),
                "${getString(R.string.emotion)}(1)",
                "${getString(R.string.emotion)}(2)",
                "${getString(R.string.emotion)}(3)",
                "${getString(R.string.need)}(1)",
                "${getString(R.string.need)}(2)",
                "${getString(R.string.need)}(3)",
            ).toTypedArray()
        )
    }

    companion object {
        const val CREATE_FILE = 1
    }
}

private fun CSVWriter.write(record: RecordWrapper) {
    val list = mutableListOf(record.date.time.toString(), record.stimulus)
    for (i: Int in 0..2) {
        list.add(record.emotions.getOrNull(i)?.getName() ?: "")
    }
    for (i: Int in 0..2) {
        list.add(record.needs.getOrNull(i)?.getName() ?: "")
    }
    writeNext(list.toTypedArray())
}
