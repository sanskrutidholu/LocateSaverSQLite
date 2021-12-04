package com.example.locateonmap.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.locateonmap.modelClass.LatLang
import com.example.locateonmap.modelClass.UserHistoryModelClass
import com.google.android.gms.maps.model.LatLng


class DBClass(var context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {


    private val DROP_LOCATION_TABLE = "DROP TABLE IF EXISTS $TABLE_LOCATION" //  user table
    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS $TABLE_USER" //  admin table


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_LOCATION) // for creating user table
        db.execSQL(CREATE_TABLE_USER) // for creating admin table

    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP_LOCATION_TABLE) // user table
        db.execSQL(DROP_USER_TABLE) //  admin table
        onCreate(db)
    }


    // database sql queries for users location history
    fun insertUserLocation(address:String, date:String, time:String, long: Double, lat: Double) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(USER_ADDRESS, address)
        cv.put(USER_DATE,date)
        cv.put(USER_TIME,time)
        cv.put(LONGITUDE,long)
        cv.put(LATITUDE,lat)
        db.insert(TABLE_LOCATION, null, cv)
    }


    fun deleteUserLocation(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_LOCATION, USER_KEY_ID + "	= ?", arrayOf(id.toString()))
    }


    fun deleteAllUserLocationHistory() {
        val database = this.writableDatabase
        val query = "DELETE FROM $TABLE_LOCATION"
        database.execSQL(query)
    }

    fun getAllLatLng(): List<UserHistoryModelClass> {
        val database = this.readableDatabase
        val LatLngList: MutableList<UserHistoryModelClass> = ArrayList()
        val getAll = "SELECT * FROM $TABLE_LOCATION"
        val cursor = database.rawQuery(getAll, null)
        if (cursor.moveToFirst()) {
            do {
                val latlng = UserHistoryModelClass()
                latlng.userId = cursor.getInt(0)
                latlng.latitude = cursor.getDouble(1)
                latlng.longitude = cursor.getDouble(2)
                LatLngList.add(latlng)
            } while (cursor.moveToNext())
        }
        return LatLngList
    }

    fun getLatlng() : Cursor {
        val database = this.readableDatabase
        val query = "SELECT $LATITUDE, $LONGITUDE FROM $TABLE_LOCATION"
        return database.rawQuery(query,null,null)
    }

    fun getAll(): List<UserHistoryModelClass> {
        val shopList: MutableList<UserHistoryModelClass> = ArrayList<UserHistoryModelClass>()
        // Select All Query
        val selectQuery = "SELECT $LATITUDE,$LONGITUDE FROM $TABLE_LOCATION"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val shop = UserHistoryModelClass()
                //shop.id = cursor.getInt(0)
                shop.latitude = cursor.getDouble(0)
                shop.longitude = cursor.getDouble(1)
                shopList.add(shop)
            } while (cursor.moveToNext())
        }

        // return contact list
        return shopList
    }


    fun readAllUserLocationHistory(): Cursor? {
        val query = "SELECT * FROM $TABLE_LOCATION"
        val database = this.readableDatabase
        var cursor: Cursor? = null
        if (database != null) {
            cursor = database.rawQuery(query, null)
        }
        return cursor
    }


    //  to insertUser
    fun insertUser(name: String?, email: String?, password: String?){
        val usersDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USER_NAME, name)
        contentValues.put(USER_EMAIL, email)
        contentValues.put(USER_PASSWORD, password)
        usersDB.insert(TABLE_USER, null, contentValues)
    }


    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "UserDB"


        //  location history table for users
        private const val TABLE_LOCATION = "UserHistoryTable"
        var USER_KEY_ID = "id"
        var USER_ADDRESS = "address"
        var USER_DATE = "date"
        var USER_TIME = "time"
        var LONGITUDE = "long"
        var LATITUDE = "lat"
        var CREATE_TABLE_USER = ("CREATE TABLE " + TABLE_LOCATION + " ("
                + USER_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_ADDRESS + " TEXT, "
                + USER_TIME + " TEXT, "
                + LONGITUDE + " TEXT, "
                + LATITUDE + " TEXT, "
                + USER_DATE + " TEXT);")


        // login/register table for users
        private const val TABLE_USER = "AdminTable"
        var USER_NAME = "userName"
        var USER_EMAIL = "userEmail"
        var USER_PASSWORD = "userPassword"
        var CREATE_TABLE_LOCATION = ("CREATE TABLE " + TABLE_USER + " ("
                + USER_KEY_ID + " , "
                + USER_NAME + " TEXT, "
                + USER_EMAIL + " TEXT, "
                + USER_PASSWORD + " TEXT);")

    }
}
