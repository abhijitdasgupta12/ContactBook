package com.example.contactbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBManager extends SQLiteOpenHelper
{

    private static final String dbname="db_contact";

    public DBManager(@Nullable Context context)
    {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String qry="create table tbl_contact (id integer primary key autoincrement, name text, pcontact text, scontact text, email text)";
        db.execSQL(qry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String qry="DROP TABLE IF EXISTS tbl_contact";
        db.execSQL(qry);
        onCreate(db);
    }

    public String addRecord(String name, String pphone, String sphone, String email)
    {
        //Manually passing object of SQLiteDatabase since the object is not available in the parameter of the method
        SQLiteDatabase db=this.getWritableDatabase();

        //ContentValues
        ContentValues values=new ContentValues();

        //The Syntax: values.put("Column_Name_Of_Database", parameter_used_in_the_method)
        values.put("name",name);
        values.put("pcontact",pphone);
        values.put("scontact",sphone);
        values.put("email",email);

        //Inserting data into database and receiving the result of operation -1 or +1
        float res=db.insert("tbl_contact", null, values);

        //Checking result
        if (res==-1)
            return "Insertion Failed";
        else
            return "Insertion Successful";
    }

    public Cursor fetchRecords()
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String qry="select * from tbl_contact order by id desc";

        //Cursor
        Cursor c_obj=db.rawQuery(qry,null);

        return c_obj;
    }

    public Cursor deleteData()
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String qry="DELETE FROM tbl_contact";

        db.execSQL(qry);

        return null;
    }
}
