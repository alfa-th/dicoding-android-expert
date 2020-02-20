package com.dicoding.picodicloma.consumerapp

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodicloma.consumerapp.db.DatabaseContract
import com.dicoding.picodicloma.consumerapp.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.dicoding.picodicloma.consumerapp.db.DatabaseContract.NoteColumns.Companion.DATE
import com.dicoding.picodicloma.consumerapp.entity.Note
import com.dicoding.picodicloma.consumerapp.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_note_add_update.*
import java.text.SimpleDateFormat
import java.util.*

class NoteAddUpdateActivity : AppCompatActivity(), View.OnClickListener {

    private var isEdit = false
    private var note: Note? = null
    private var position: Int = 0
    private lateinit var uriWithId: Uri

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add_update)

        btn_submit.setOnClickListener(this)

        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else
            note = Note()

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + note?.id)
            val cursor = contentResolver.query(
                uriWithId,
                null,
                null,
                null,
                null
            )

            if (cursor != null) {
                note = MappingHelper.mapCursorToObject(cursor)
                cursor.close()
            }

            actionBarTitle = "Change"
            btnTitle = "Update"

            note?.let { it ->
                edt_title.setText(it.title)
                edt_description.setText(it.description)
            }
        } else {
            actionBarTitle = "Add"
            btnTitle = "Save"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_submit.text = btnTitle
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_submit -> {
                val title = edt_title.text.toString().trim()
                val description = edt_description.text.toString().trim()

                if (title.isEmpty()) {
                    edt_title.error = "Field cannot be blank"
                    return
                }

                note?.title = title
                note?.description = description

                val intent = Intent()
                intent.putExtra(EXTRA_NOTE, note)
                intent.putExtra(EXTRA_POSITION, position)

                val values = ContentValues()
                values.put(DatabaseContract.NoteColumns.TITLE, title)
                values.put(DatabaseContract.NoteColumns.DESCRIPTION, description)

                if (isEdit) {
                    contentResolver.update(
                        uriWithId,
                        values,
                        null,
                        null
                    )

                    Toast.makeText(
                        this,
                        "One item has successfully changed",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                    finish()
                } else {
                    values.put(DATE, getCurrentDate())
                    contentResolver.insert(CONTENT_URI, values)

                    Toast.makeText(
                        this,
                        "One item has successfully added",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                }

            }
        }
    }

    private fun getCurrentDate(): String? {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()

        return dateFormat.format(date)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogTitle = "Cancel"
            dialogMessage = "Do you want to cancel the changes written in the form?"
        } else {
            dialogTitle = "Delete note"
            dialogMessage = "Are you really sure to delete this item?"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                if (isDialogClose) {
                    finish()
                } else {
                    contentResolver.delete(
                        uriWithId,
                        null,
                        null
                    )
                    Toast.makeText(
                        this,
                        "One item has successfully deleted",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}
