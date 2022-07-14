package com.example.mynotes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class dbClass {
    val dbName="MyNotes"
    val dbTable="Notes"
    val colId="ID"
    val colTitle="title"
    val colDes="Description"
    val dbVersion=1
    //create table if not exist MyNotes(ID INTEGER PRIMARY KEY,title TEXT, Description TEXT);
    val sqlCreateTable="CREATE TABLE IF NOT EXISTS "+ dbTable +" ("+ colId +" INTEGER PRIMARY KEY ,"+
            colTitle+ " TEXT,"+ colDes+ " TEXT);"
    var sqlDB:SQLiteDatabase?=null
    constructor(context:Context){
        var db = DatabaseHelperNotes(context)
        sqlDB=db.writableDatabase

    }
    inner class DatabaseHelperNotes:SQLiteOpenHelper{
        var context:Context?=null
        constructor(context: Context):super(context,dbName,null,dbVersion){
            this.context=context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context,"new database created",Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("DROP TABLE IF EXISTS "+dbTable)
        }

    }


    fun Insert(values:ContentValues):Long{
        val ID= sqlDB!!.insert(dbTable,"",values)
        return ID
    }

    fun Query(projection:Array<String>,selection:String,selectionArgs:Array<String>,sortOrder:String):Cursor{
         //qb=query builder
        val qb=SQLiteQueryBuilder()
        qb.tables=dbTable
        val cursor=qb.query(sqlDB,projection,selection,selectionArgs,null,null,sortOrder)
        return cursor
    }

    fun Delete(selection:String,selectionArgs:Array<String>):Int{
        val count=sqlDB!!.delete(dbTable,selection,selectionArgs)
        return count
    }

    fun Update(values: ContentValues,selection:String,selectionArgs:Array<String>):Int{
        val count=sqlDB!!.update(dbTable,values,selection,selectionArgs)
        return count
    }

}