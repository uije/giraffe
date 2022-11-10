package com.zettafantasy.giraffe.feature.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.opencsv.CSVWriter
import com.zettafantasy.giraffe.BuildConfig
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.feature.record.RecordWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileWriter

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

    val exportIntent
        get() = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/csv"
            putExtra(Intent.EXTRA_TITLE, "giraffe.csv")
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
