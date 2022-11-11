package com.zettafantasy.giraffe.feature.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import com.zettafantasy.giraffe.BuildConfig
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.data.Record
import com.zettafantasy.giraffe.feature.record.RecordWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileReader
import java.io.FileWriter
import java.util.*

/**
 * 기록
 *  내보내기(Export)
 *  가져오기(Import)
 */
class PortManager(val repository: GiraffeRepository, val context: Context) {

    private val emotionInventory: EmotionInventory by lazy {
        EmotionInventory.getInstance(context.resources)
    }

    private val needInventory: NeedInventory by lazy {
        NeedInventory.getInstance(context.resources)
    }

    fun exportCSV(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = writeCSV(uri)
            withContext(Dispatchers.Main) {
                showResultToast(result)
            }
        }
    }

    private fun writeCSV(uri: Uri): Boolean {
        try {
            context.contentResolver.openFileDescriptor(uri, "w").use { fd ->
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
                context.getString(R.string.date),
                context.getString(R.string.stimulus),
                "${context.getString(R.string.emotion)}(1)",
                "${context.getString(R.string.emotion)}(2)",
                "${context.getString(R.string.emotion)}(3)",
                "${context.getString(R.string.need)}(1)",
                "${context.getString(R.string.need)}(2)",
                "${context.getString(R.string.need)}(3)",
            ).toTypedArray()
        )
    }

    private fun showResultToast(result: Boolean) {
        Toast.makeText(
            context,
            if (result) R.string.complete else R.string.fail,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun importCSV(uri: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = readCSV(uri)
            withContext(Dispatchers.Main) {
                //showResultToast(result)
            }
        }
    }

    private fun readCSV(uri: Uri): Int {
        try {
            context.contentResolver.openFileDescriptor(uri, "r").use { fd ->
                FileReader(fd?.fileDescriptor).use { fileReader ->
                    CSVReader(fileReader).use { csvReader ->
                        csvReader.forEach {
                            try {
                                val record: Record = convert(it)
                                Log.d(javaClass.simpleName, "uije $record")
                            } catch (e: Exception) {
                                //nothing
                            }
                        }
                    }
                }
            }
            return 0
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            return 0
        }
    }

    private fun convert(strings: Array<String>): Record {
        if (strings.size != 8) {
            throw IllegalArgumentException("size is not 8: ${strings.size}")
        }

        val date = strings[0].toLong()
        val stimulus = strings[1]
        val emotions = mutableListOf<Int>()
        for (i: Int in 2..4) {
            emotionInventory.getEmotionByName(strings[i])?.let {
                emotions.add(it.getId())
            }
        }
        val needs = mutableListOf<Int>()
        for (i: Int in 5..7) {
            needInventory.getNeedByName(strings[i])?.let {
                needs.add(it.getId())
            }
        }
        return Record(
            emotionIds = emotions,
            needIds = needs, stimulus = stimulus
        ).apply {
            this.date = Date(date)
        }
    }

    val exportIntent
        get() = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/csv"
            putExtra(Intent.EXTRA_TITLE, "giraffe.csv")
        }

    val importIntent
        get() = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/*"
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
