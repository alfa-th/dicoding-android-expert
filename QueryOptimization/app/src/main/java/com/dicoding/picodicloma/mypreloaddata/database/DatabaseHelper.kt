package com.dicoding.picodicloma.mypreloaddata.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.picodicloma.mypreloaddata.database.DatabaseContract.MahasiswaColumsn.Companion.NAMA
import com.dicoding.picodicloma.mypreloaddata.database.DatabaseContract.MahasiswaColumsn.Companion.NIM
import com.dicoding.picodicloma.mypreloaddata.database.DatabaseContract.MahasiswaColumsn.Companion._ID
import com.dicoding.picodicloma.mypreloaddata.database.DatabaseContract.TABLE_NAME

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbmahasiswa"
        private const val DATABASE_VERSION = 1
        private val CREATE_TABLE_MAHASISWA =
            "create table $TABLE_NAME ($_ID integer primary key autoincrement, $NAMA text not null, $NIM text not null);"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_MAHASISWA)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists $TABLE_NAME")
        onCreate(db)
    }
}