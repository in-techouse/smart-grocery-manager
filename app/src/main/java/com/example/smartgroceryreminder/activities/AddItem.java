package com.example.smartgroceryreminder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartgroceryreminder.model.GroceryItems;
import com.example.smartgroceryreminder.R;
import com.example.smartgroceryreminder.model.databasehelper;

public class AddItem extends AppCompatActivity {
    EditText product_name, m_date, e_date;
    databasehelper myDB;
    Button bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        product_name = (EditText) findViewById(R.id.product_name);
        m_date = (EditText) findViewById(R.id.m_date);
        e_date = (EditText) findViewById(R.id.e_date);
        myDB = new databasehelper(this);
        Button bt2 = findViewById(R.id.bt2);

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddItem.this, Dashboard.class);
                startActivity(intent);

                String pname = product_name.getText().toString();
                String manufacture = m_date.getText().toString();
                String expiry = e_date.getText().toString();
                if (pname.length() != 0 && manufacture.length() != 0 && expiry.length() != 0) {
                    GroceryItems groceryItems = new GroceryItems();
                    groceryItems.setPname(pname);
                    groceryItems.setManufacture(manufacture);
                    groceryItems.setExpiry(expiry);
                    myDB.addData(groceryItems);
                    product_name.setText("");
                    m_date.setText("");
                    e_date.setText("");
                } else {
                    Toast.makeText(AddItem.this, "you must put some thing in text field", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }


}

