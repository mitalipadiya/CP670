package com.example.androidassignments;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.androidassignments.utils.ChatDatabaseHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChatDatabaseHelperTest {

    private ChatDatabaseHelper dbHelper;
    private SQLiteDatabase database;

    @Before
    public void setUp() {
        dbHelper = new ChatDatabaseHelper(InstrumentationRegistry.getInstrumentation().getTargetContext());
        database = dbHelper.getWritableDatabase();
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }

    @Test
    public void testInsertMessage() {
        ContentValues values = new ContentValues();
        values.put(ChatDatabaseHelper.KEY_MESSAGE, "Test Message");
        long newRowId = database.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
        assert newRowId != -1;
    }

    @Test
    public void testRetrieveMessages() {
        ContentValues values = new ContentValues();
        values.put(ChatDatabaseHelper.KEY_MESSAGE, "Test Message");
        long newRowId = database.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
        Cursor cursor = database.query(ChatDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
        assert cursor != null;
        assert cursor.getCount() > 0;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
            assert message.equals("Test Message");
            cursor.moveToNext();
        }

        cursor.close();
    }

    @Test
    public void testUpgradeDatabase() {
        int newVersion = ChatDatabaseHelper.VERSION_NUM + 1;
        dbHelper.onUpgrade(database, ChatDatabaseHelper.VERSION_NUM, newVersion);
        Cursor cursor = database.query(ChatDatabaseHelper.TABLE_NAME, null, null, null, null, null, null);
        assert cursor.getCount() == 0;
        cursor.close();
    }
}
