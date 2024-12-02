package com.example.proc6;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DataBaseHelper dataBaseHelper;
    private ArrayList<Book> bookArray;
    private RecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dataBaseHelper=new DataBaseHelper(this);
        bookArray=new ArrayList<>();
        RecyclerView recyclerView=findViewById(R.id.list_book);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecycleViewAdapter(this, bookArray);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab_add_book);
        fab.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddBookActivity.class)));

        loadBooks();
    }

    private void loadBooks() {
        bookArray.clear();
        Cursor cursor = dataBaseHelper.getAllBooks();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_NAME));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_AUTHOR));
                bookArray.add(new Book(id, author, name));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
    }
}