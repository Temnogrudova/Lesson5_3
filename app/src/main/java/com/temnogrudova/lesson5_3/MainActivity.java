package com.temnogrudova.lesson5_3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Подключаемся к БД
        DBHelper dbh = new DBHelper(this);
        SQLiteDatabase db = dbh.getWritableDatabase();

        // Описание курсора
        Cursor c;

        // выводим в лог данные по должностям
        Log.d(LOG_TAG, "--- Table position ---");
        c = db.query("position", null, null, null, null, null, null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

        // выводим в лог данные по людям
        Log.d(LOG_TAG, "--- Table people ---");
        c = db.query("people", null, null, null, null, null, null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

        // выводим результат объединения
        // используем rawQuery
        Log.d(LOG_TAG, "--- INNER JOIN with rawQuery---");
        String sqlQuery = "select PL.name as Name, PS.name as Position, salary as Salary "
                + "from people as PL "
                + "inner join position as PS "
                + "on PL.posid = PS.id "
                + "where salary > ?";
        c = db.rawQuery(sqlQuery, new String[] {"12000"});
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

        // выводим результат объединения
        // используем query
        Log.d(LOG_TAG, "--- INNER JOIN with query---");
        String table = "people as PL inner join position as PS on PL.posid = PS.id";
        String columns[] = { "PL.name as Name", "PS.name as Position", "salary as Salary" };
        String selection = "salary < ?";
        String[] selectionArgs = {"12000"};
        c = db.query(table, columns, selection, selectionArgs, null, null, null);
        logCursor(c);
        c.close();
        Log.d(LOG_TAG, "--- ---");

        // закрываем БД
        dbh.close();
    }

    // вывод в лог данных из курсора
    void logCursor(Cursor c) {
        if (c != null) {
            if (c.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : c.getColumnNames()) {
                        str = str.concat(cn + " = " + c.getString(c.getColumnIndex(cn)) + "; ");
                    }
                    Log.d(LOG_TAG, str);
                } while (c.moveToNext());
            }
        } else
            Log.d(LOG_TAG, "Cursor is null");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
