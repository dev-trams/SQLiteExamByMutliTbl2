package com.kbulab.exam.sqliteexambymutiltbl;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Button btn1, btn2, btn3, btn4, btn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DB db = new DB(this, "homework", null, 1);
        db.tables.setTable("CUSTOMER", "PRODUCT", "SALE");

        SQLiteDatabase database = db.getWritableDatabase();

        db.onUpgrade(database, 1, 2);
        insertCustomer(db);
        insertProduct(db);
        insertSALE(db);

        btn1 = (Button) findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoad(db, "CUSTOMER");
            }
        });
        btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoad(db, "PRODUCT");
            }
        });
        btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoad(db, "SALE");
            }
        });
        btn4 = (Button) findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearch(db, "홍길동");

            }
        });
        btn5 = (Button) findViewById(R.id.btn5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearch(db, "김대한");
            }
        });

    }

    public void onSearch(DB db, String d1) {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("");
        Cursor cursor = db.onSearchData(d1);
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToFirst();
            textView.setText("--- order people ---\n");
            for (int i = 0; i<cursor.getCount(); i++) {
                String ordNo = cursor.getString(0);
                String cid = cursor.getString(1);
                String pid = cursor.getString(2);
                int qty = cursor.getInt(3);
                textView.append("id: " + ordNo + ", 고객명 : " + cid + ", 상품명 : " + pid + ", 수량 : " + qty + "\n");
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    public void onLoad(DB db, String tblName) {
        TextView textView = (TextView) findViewById(R.id.textView);

        Cursor cursor = db.searchData(tblName);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            textView.setText("--- ".concat(tblName.equals("CUSTOMER") ? "customer" : tblName.equals("PRODUCT") ? "product" : tblName.equals("SALE") ? "sale" : "none").concat(" table --- \n"));
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                if(tblName.equals(db.tables.getTBLName1())){
                    String cid = cursor.getString(0);
                    String cname = cursor.getString(1);
                    textView.append("id: " + cid + ", 고객명 : " + cname + "\n");
                } else if(tblName.equals(db.tables.getTBLName2())){
                    String pid = cursor.getString(0);
                    String pname = cursor.getString(1);
                    int cost = cursor.getInt(2);
                    textView.append("id: " + pid + ", 상품명 : " + pname + ", 가격 : " + cost + "\n");
                } else if (tblName.equals(db.tables.getTBLName3())) {
                    String ordNo = cursor.getString(0);
                    String cid = cursor.getString(1);
                    String pid = cursor.getString(2);
                    int qty = cursor.getInt(3);
                    textView.append("id: " + ordNo + ", 고객명 : " + cid + ", 상품명 : " + pid + ", 수량 : " + qty +"\n");
                    Log.d("TAG", "id: " + ordNo + ", 고객명 : " + cid + ", 상품명 : " + pid + ", 수량 : " + qty +"\n");
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    public void insertCustomer(DB db) {
        db.insertData("CUSTOMER", "C001", "홍길동", null, 0);
        db.insertData("CUSTOMER", "C002", "김대한", null, 0);
        db.insertData("CUSTOMER", "C003", "이구슬", null, 0);
    }

    public void insertProduct(DB db) {
        db.insertData("PRODUCT", "P021", "축구공", null, 25000);
        db.insertData("PRODUCT", "P022", "배구공", null, 19000);
        db.insertData("PRODUCT", "P023", "야구공", null, 8000);
    }

    public void insertSALE(DB db) {
        db.insertData("SALE", "O1235", "C001", "P021", 2);
        db.insertData("SALE", "O1236", "C002", "P021", 1);
        db.insertData("SALE", "O1237", "C002", "P023", 5);
        db.insertData("SALE", "O1238", "C003", "P022", 3);
    }
}