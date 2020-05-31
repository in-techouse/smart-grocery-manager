package com.example.smartgroceryreminder.activities;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartgroceryreminder.adapters.AdapterClass;
import com.example.smartgroceryreminder.model.GroceryItems;
import com.example.smartgroceryreminder.R;
import com.example.smartgroceryreminder.model.databasehelper;

import java.util.List;

public class GroceryItemsList extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_items_list);

        listView = findViewById(R.id.list);
        AdapterClass dm = new AdapterClass(GroceryItemsList.this);
        listView.setAdapter(dm);
        databasehelper dbHandler = new databasehelper(GroceryItemsList.this);
        List<GroceryItems> list = dbHandler.fetchAllData();


    }
}

