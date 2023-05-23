package com.kbulab.exam.sqliteexambymutiltbl;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class TABLES {

    String TBLName1;
    String TBLName2;
    String TBLName3;

    public void setTable(String tbl1) {
        this.TBLName1 = tbl1;
    }

    public void setTable(String tbl1, String tbl2) {
        this.TBLName1 = tbl1;
        this.TBLName2 = tbl2;
    }

    public void setTable(String tbl1, String tbl2, String tbl3) {
        this.TBLName1 = tbl1;
        this.TBLName2 = tbl2;
        this.TBLName3 = tbl3;
    }

    public String getTBLName1() {
        return TBLName1;
    }

    public String getTBLName2() {
        return TBLName2;
    }

    public String getTBLName3() {
        return TBLName3;
    }
}

public class DB extends SQLiteOpenHelper {
    private Context context;
    TABLES tables = new TABLES();

    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE IF NOT EXISTS " + tables.getTBLName1() + "(CID TEXT PRIMARY KEY, " + "CNAME TEXT);";
        db.execSQL(sql1);
        String sql2 = "CREATE TABLE IF NOT EXISTS " + tables.getTBLName2() + "(PID TEXT PRIMARY KEY, " + " PNAME TEXT, COST INTEGER);";
        db.execSQL(sql2);
        String sql3 = "CREATE TABLE IF NOT EXISTS " + tables.getTBLName3() + "(ORD_NO TEXT PRIMARY KEY, " + " CID TEXT, PID TEXT, QTY INTEGER, " +
                "FOREIGN KEY (CID) REFERENCES " + tables.getTBLName1() + "(CID)," +
                "FOREIGN KEY (PID) REFERENCES "+ tables.getTBLName2() + "(PID)"+");";
        db.execSQL(sql3);
    }

    public long insertData(@NonNull String mode, String d1, String d2, String d3, int d4) {

        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues contentValues = null;
            String t1 = tables.getTBLName1();
            String t2 = tables.getTBLName2();
            String t3 = tables.getTBLName3();
            if (t1.equals(mode)) {
                contentValues = new ContentValues();
                contentValues.put("CID", d1);
                contentValues.put("CNAME", d2);
            } else if (t2.equals(mode)) {
                contentValues = new ContentValues();
                contentValues.put("PID", d1);
                contentValues.put("PNAME", d2);
                contentValues.put("COST", d4);
            } else if (t3.equals(mode)) {
                contentValues = new ContentValues();
                contentValues.put("ORD_NO", d1);
                contentValues.put("CID", d2);
                contentValues.put("PID", d3);
                contentValues.put("QTY", d4);
            }
            long insertedRowId = database.insertWithOnConflict(mode.equals(t1) ? tables.getTBLName1() : mode.equals(t2) ? tables.getTBLName2() : mode.equals(t3) ? tables.getTBLName3() : "none", null, contentValues, SQLiteDatabase.CONFLICT_NONE);
            database.setTransactionSuccessful();
            return insertedRowId;
        } catch (Exception e) {
            return -1L;
        } finally {
            database.endTransaction();
        }
    }

    public Cursor searchData(String tableName) {
        SQLiteDatabase database = getWritableDatabase();
        String TBL = tableName.equals(tables.getTBLName1()) ? tables.getTBLName1() : tableName.equals(tables.getTBLName2()) ? tables.getTBLName2() : tableName.equals(tables.getTBLName3()) ? tables.getTBLName3() : "none";
        String sql = "SELECT * FROM " + TBL + ";";

        String sql1 = "SELECT " + tables.getTBLName3() + ".ORD_NO, " +
                tables.getTBLName1() + ".CNAME, " +
                tables.getTBLName2() + ".PNAME, " +
                tables.getTBLName3() + ".QTY " +
                "FROM " + tables.getTBLName3() +
                " JOIN " + tables.getTBLName1() + " ON " + tables.getTBLName3() + ".CID = " + tables.getTBLName1() + ".CID " +
                "JOIN " + tables.getTBLName2() + " ON " + tables.getTBLName3() + ".PID = " + tables.getTBLName2() + ".PID;";


        Cursor cursor = database.rawQuery(tableName.equals("SALE") ? sql1 : sql, null);

        return cursor;
    }

    public Cursor onSearchData(String d1){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "SELECT " + tables.getTBLName3() + ".ORD_NO, " +
                tables.getTBLName1() + ".CNAME, " +
                tables.getTBLName2() + ".PNAME, " +
                tables.getTBLName3() + ".QTY " +
                "FROM " + tables.getTBLName3() +
                " JOIN " + tables.getTBLName1() + " ON " + tables.getTBLName3() + ".CID = " + tables.getTBLName1() + ".CID " +
                "JOIN " + tables.getTBLName2() + " ON " + tables.getTBLName3() + ".PID = " + tables.getTBLName2() + ".PID " +
                "WHERE " + tables.getTBLName1() + ".CNAME = '"+d1+"';";

        Cursor cursor = database.rawQuery(sql, null);
        return cursor;
    }



    public void deleteData(int index) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            String sql = "DELETE FROM " + tables.getTBLName1() + " WHERE CODE = ?";
            String[] whereArgs = new String[]{"CD-010" + index};
            database.execSQL(sql, whereArgs);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }

    }

    public void deleteAll() {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            String sql = "DELETE FROM " + tables.getTBLName1();
            database.execSQL(sql);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    @SuppressLint("Range")
    public void onUpdate(float price) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        SQLiteStatement statement = null;
        try {
            String sql = "INSERT OR REPLACE INTO " + tables.getTBLName1() + " (CODE, name, price) VALUES (?, ?, ?)";
            statement = database.compileStatement(sql);

            String selectSql = "SELECT CODE, name, price FROM " + tables.getTBLName1();
            Cursor cursor = database.rawQuery(selectSql, null);

            while (cursor.moveToNext()) {
                String code = cursor.getString(cursor.getColumnIndex("CODE"));
                String name = cursor.getString(cursor.getColumnIndex("name"));

                statement.bindString(1, code);
                statement.bindString(2, name);
                statement.bindDouble(3, Double.parseDouble(String.valueOf(price)));
                statement.execute();
                statement.clearBindings();
            }

            cursor.close();
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            database.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql1 = "DROP TABLE IF EXISTS " + tables.getTBLName1() + ";";
        String sql2 = "DROP TABLE IF EXISTS " + tables.getTBLName2() + ";";
        db.execSQL(sql1);
        db.execSQL(sql2);
        onCreate(db);
    }
}
