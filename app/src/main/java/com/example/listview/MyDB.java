package com.example.listview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDB extends SQLiteOpenHelper {
    public static final String TableName = "ContactTable";
    public static final String Id = "Id";
    public static final String Name = "Fullname";
    public static final String Phone = "Phonenumber";
    public static final String Image = "Image";

    public MyDB(@NonNull Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //tao cau sql de tao bang TableContact
        String sqlCreate = "Create table if not exists " + TableName + "("
                + Id + " Integer Primary key, "
                + Image + " Text, "
                + Name + " Text, "
                + Phone + " Text)";
        //chay cau truy van SQL de tao bang
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        //xoa bang TableContact da co
        sqLiteDatabase.execSQL("Drop table if exists " + TableName);
        //tao lai
        onCreate(sqLiteDatabase);
    }
    //lay tat ca ca dong cua bang TableContact tra ve dang ArrayList
    public ArrayList<Contact> getAllContact()
    {
        ArrayList<Contact> list = new ArrayList<>();
        //cau truy van
        String sql = "Select * from " + TableName;
        //lay doi tuong csdl sqlite
        SQLiteDatabase db = this.getReadableDatabase();
        //chay cau truy van tra ve dang Cursor
        Cursor cursor = db.rawQuery(sql, null);
        //tao ArrayList<Contact> de tra ve
        if(cursor!=null)
            while (cursor.moveToNext())
            {
                Contact contact = new Contact(cursor.getInt(0),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3));
                list.add(contact);
            }
        return list;
    }
    //ham them mot contact vao bang TableContact
    public void addContact(Contact contact)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(Id, contact.getId());
        value.put(Image, contact.getImages());
        value.put(Name, contact.getName());
        value.put(Phone, contact.getPhone());
        db.insert(TableName, null,value);
        db.close();
    }
    public void updateContact(int id, Contact contact)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(Id, contact.getId());
        value.put(Image, contact.getImages());
        value.put(Name, contact.getName());
        value.put(Phone, contact.getPhone());
        db.update(TableName, value, Id + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void deleteContact(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "Delete From " + TableName + " Where ID=" + id;
        //db.delete(TableName, ID + "=?", new String[]{String.valueOf(id)});
        db.execSQL(sql);
        db.close();


    }
}
