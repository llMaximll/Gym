package com.github.llmaximll.gym.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.github.llmaximll.gym.BuildConfig
import com.github.llmaximll.gym.dataclasses.Exercise

private const val TAG = "DatabaseHandler"

class DatabaseHandler(context: Context)
    : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME " +
                "($ID INTEGER PRIMARY KEY, $NAME_EX TEXT, $NUMBER_EX INTEGER, $SCORES INTEGER, " +
                "$MINUTES INTEGER, $CAL REAL)"
        log(TAG, "Hello, World!")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Upgrade
    }

    /**
     * Inserting data
     */

    fun addExercise(exercise: Exercise): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME_EX, exercise.name)
        values.put(NUMBER_EX, exercise.numberEx)
        values.put(SCORES, exercise.scores)
        values.put(MINUTES, exercise.minutes)
        values.put(CAL, exercise.cal)
        val success = db.insert(TABLE_NAME, null, values)
        db.close()
        log(TAG, "$success")
        return (Integer.parseInt("$success") != -1)
    }

    /**
     * Get data
     */

    fun getAllExercises(): String {
        var allExercise = ""
        val db = readableDatabase
        val selectAllQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectAllQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getString(cursor.getColumnIndex(ID))
                    val nameEx = cursor.getString(cursor.getColumnIndex(NAME_EX))
                    val scores = cursor.getString(cursor.getColumnIndex(SCORES))
                    val minutes = cursor.getString(cursor.getColumnIndex(MINUTES))
                    val cal = cursor.getString(cursor.getColumnIndex(CAL))

                    allExercise = "$allExercise\n$id $nameEx $scores $minutes $cal"
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return allExercise
    }

    private fun log(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }

    companion object {
        private const val DB_NAME = "ExercisesDB"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "exercises"
        private const val ID = "id"
        private const val NAME_EX = "name_ex"
        private const val NUMBER_EX = "number_ex"
        private const val SCORES = "scores"
        private const val MINUTES = "minutes"
        private const val CAL  = "cal"
    }
}