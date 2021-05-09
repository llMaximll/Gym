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
                "($ID INTEGER PRIMARY KEY, $COLUMN_NAME_EX TEXT, $COLUMN_NUMBER_EX TEXT, $COLUMN_SCORES INTEGER, " +
                "$COLUMN_MINUTES TEXT, $COLUMN_CAL REAL)"
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
        values.put(COLUMN_NAME_EX, exercise.name)
        values.put(COLUMN_NUMBER_EX, exercise.numberEx)
        values.put(COLUMN_SCORES, exercise.scores)
        values.put(COLUMN_MINUTES, exercise.minutes.toString())
        values.put(COLUMN_CAL, exercise.cal)
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
                    val nameEx = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EX))
                    val scores = cursor.getString(cursor.getColumnIndex(COLUMN_SCORES))
                    val minutes = cursor.getString(cursor.getColumnIndex(COLUMN_MINUTES))
                    val cal = cursor.getString(cursor.getColumnIndex(COLUMN_CAL))

                    allExercise = "$allExercise\n$id $nameEx $scores $minutes $cal"
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return allExercise
    }

    fun getExercise(nameEx: String, numberEx: String): Exercise? {
        var exercise: Exercise? = null
        val db = readableDatabase
        val selectionArgs = arrayOf(nameEx, numberEx)
        val cursor = db.query(TABLE_NAME, null, "$COLUMN_NAME_EX = ? AND $COLUMN_NUMBER_EX = ?",
                selectionArgs, null, null, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    exercise = Exercise()
                    exercise.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EX))
                    exercise.numberEx = cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER_EX))
                    exercise.scores = cursor.getInt(cursor.getColumnIndex(COLUMN_SCORES))
                    exercise.minutes = cursor.getString(cursor.getColumnIndex(COLUMN_MINUTES)).toLong()
                    exercise.cal = cursor.getFloat(cursor.getColumnIndex(COLUMN_CAL))

                } while (cursor.moveToNext())
            }
        }

        cursor.close()
        db.close()

        return exercise
    }

    fun getCompletedExercises(category: String): Int {
        var count = 0
        val db = readableDatabase
        val selectionArgs = arrayOf(category, "0")
        val cursor = db.query(TABLE_NAME, null, "$COLUMN_NAME_EX = ? AND $COLUMN_SCORES = ?",
                selectionArgs, null, null, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    count++
                } while (cursor.moveToNext())
            }
        }

        cursor.close()
        db.close()

        log(TAG, "count=$count")

        return count
    }

    fun updateExercise(nameEx: String, numberEx: String, scores: Int, minutes: Long, cal: Float): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.apply {
            put(COLUMN_SCORES, scores)
            put(COLUMN_MINUTES, minutes.toString())
            put(COLUMN_CAL, cal)
        }
        val selectionArgs = arrayOf(nameEx, numberEx)
        val success = db.update(TABLE_NAME, values, "$COLUMN_NAME_EX = ? AND $COLUMN_NUMBER_EX = ?",
                selectionArgs)

        db.close()

        return (Integer.parseInt("$success") != -1)
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
        private const val COLUMN_NAME_EX = "name_ex"
        private const val COLUMN_NUMBER_EX = "number_ex"
        private const val COLUMN_SCORES = "scores"
        private const val COLUMN_MINUTES = "minutes"
        private const val COLUMN_CAL  = "cal"
    }
}