package com.dicoding.picodicloma.myreadwritefile

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.lang.Exception

internal object FileHelper {

    private val TAG = FileHelper::class.java.simpleName


    fun writeToFile(fileModel: FileModel, context: Context) {
        try {
            val outputStreamWriter =
                OutputStreamWriter(
                    context.openFileOutput(
                        fileModel.filename.toString(),
                        Context.MODE_PRIVATE
                    )
                )
            outputStreamWriter.write(fileModel.data.toString())
            outputStreamWriter.close()
        } catch (e: Exception) {
            Log.e(TAG, "File write failed", e)
        }
    }

    fun readFromFile(context: Context, filename: String): FileModel {
        val fileModel = FileModel()

        try {
            val inputStream = context.openFileInput(filename)

            if (inputStream != null) {
                val receiveString =
                    inputStream
                        .bufferedReader()
                        .use(BufferedReader::readText)
                inputStream.close()
                fileModel.data = receiveString
                fileModel.filename = filename
            }
        } catch (e: Exception) {
            Log.e(TAG, "Read error", e)
        }

        return fileModel
    }
}