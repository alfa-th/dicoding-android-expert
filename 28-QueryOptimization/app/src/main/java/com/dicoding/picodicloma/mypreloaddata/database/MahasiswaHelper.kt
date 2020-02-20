package com.dicoding.picodicloma.mypreloaddata.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.picodicloma.mypreloaddata.database.DatabaseContract.MahasiswaColumsn.Companion.NAMA
import com.dicoding.picodicloma.mypreloaddata.database.DatabaseContract.MahasiswaColumsn.Companion.NIM
import com.dicoding.picodicloma.mypreloaddata.database.DatabaseContract.MahasiswaColumsn.Companion._ID
import com.dicoding.picodicloma.mypreloaddata.database.DatabaseContract.TABLE_NAME
import com.dicoding.picodicloma.mypreloaddata.model.MahasiswaModel
import java.sql.SQLException

class MahasiswaHelper(context: Context) {

    private val databaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private var INSTANCE: MahasiswaHelper? = null

        fun getInstance(context: Context): MahasiswaHelper {
            if (INSTANCE == null) {
                synchronized(SQLiteOpenHelper::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = MahasiswaHelper(context)
                    }
                }
            }

            return INSTANCE as MahasiswaHelper
        }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun beginTransaction() {
        database.beginTransaction()
    }

    fun setTransactionProcess() {
        database.setTransactionSuccessful()
    }

    fun endTransaction() {
        database.endTransaction()
    }

    fun getAllData(): ArrayList<MahasiswaModel> {
        val cursor = database.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC",
            null
        )

        cursor.moveToFirst()
        val arrayList = ArrayList<MahasiswaModel>()
        var mahasiswaModel: MahasiswaModel

        if (cursor.count > 0) {
            do {
                mahasiswaModel = MahasiswaModel()

                with(mahasiswaModel) {
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                    nama = cursor.getString(cursor.getColumnIndexOrThrow(NAMA))
                    nim = cursor.getString(cursor.getColumnIndexOrThrow(NIM))
                }

                arrayList.add(mahasiswaModel)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }

        cursor.close()

        return arrayList
    }

    fun insert(mahasiswaModel: MahasiswaModel): Long {
        val initialValues = ContentValues()
        initialValues.put(NAMA, mahasiswaModel.nama)
        initialValues.put(NIM, mahasiswaModel.nim)

        return database.insert(TABLE_NAME, null, initialValues)
    }

    fun insertTransaction(mahasiswaModel: MahasiswaModel) {
        val sql = ("INSERT INTO $TABLE_NAME ( $NAMA , $NIM ) VALUES ( ? , ? )")
        val stmt = database.compileStatement(sql)
        stmt.bindString(1, mahasiswaModel.nama)
        stmt.bindString(2, mahasiswaModel.nim)
        stmt.execute()
        stmt.clearBindings()
    }

    fun getDataByName(name: String): ArrayList<MahasiswaModel> {
        val cursor = database.query(
            TABLE_NAME,
            null,
            "$NAMA LIKE ?",
            arrayOf(name),
            null,
            null,
            "$_ID ASC",
            null
        )

        cursor.moveToFirst()
        val arrayList = ArrayList<MahasiswaModel>()
        var mahasiswaModel: MahasiswaModel

        if (cursor.count > 0) {
            do {
                mahasiswaModel = MahasiswaModel()

                with(mahasiswaModel) {
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                    nama = cursor.getString(cursor.getColumnIndexOrThrow(NAMA))
                    nim = cursor.getString(cursor.getColumnIndexOrThrow(NIM))
                }

                arrayList.add(mahasiswaModel)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }

        cursor.close()

        return arrayList
    }
}