package com.dicoding.picodicloma.myreadwritefile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var path: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_new.setOnClickListener(this)
        button_open.setOnClickListener(this)
        button_save.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_new -> {
                newFile()
            }
            R.id.button_open -> {
                showList()
            }
            R.id.button_save -> {
                saveFile()
            }
        }
    }

    private fun newFile() {
        edit_title.setText("")
        edit_file.setText("")
        Toast.makeText(
            this,
            "Clearing file",
            Toast.LENGTH_LONG
        )
            .show()
    }

    private fun saveFile() {
        when {
            edit_title.text.isEmpty() -> {
                Toast.makeText(
                    this,
                    "Title harus diisi terlebih dahulu",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            edit_file.text.isEmpty() -> {
                Toast.makeText(
                    this,
                    "Konten harus diisi terlebih dahulu",
                    Toast.LENGTH_SHORT
                )
                    .show()


            }
            else -> {
                val title = edit_title.text.toString()
                val text = edit_file.text.toString()
                val fileModel = FileModel()
                fileModel.filename = title
                fileModel.data = text
                FileHelper.writeToFile(fileModel, this)

                Toast.makeText(
                    this,
                    "Saving ${fileModel.filename} file",
                    Toast.LENGTH_SHORT
                )
                    .show()

                Log.d(TAG, "${edit_file.text}  ${edit_title.text}")
            }
        }
    }

    private fun showList() {
        val arrayList = ArrayList<String>()
        val path: File = filesDir
        Collections.addAll(
            arrayList,
            *path.list() as Array<String>
        )
        val items = arrayList.toTypedArray<CharSequence>()

        val builder = AlertDialog.Builder(this)
        builder
            .setTitle("Pilih file yang diinginkan")
            .setItems(items) { dialog, item ->
                loadData(items[item].toString())
            }
        val alert = builder.create()
        alert.show()
    }

    private fun loadData(title: String) {
        val fileModel = FileHelper.readFromFile(this, title)
        edit_title.setText(fileModel.filename)
        edit_file.setText(fileModel.data)

        Toast.makeText(
            this,
            "Loading ${fileModel.filename} data",
            Toast.LENGTH_SHORT
        )
            .show()
    }
}
