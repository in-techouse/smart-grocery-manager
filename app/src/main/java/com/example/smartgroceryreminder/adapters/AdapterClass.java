package com.example.smartgroceryreminder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.smartgroceryreminder.model.GroceryItems;
import com.example.smartgroceryreminder.R;
import com.example.smartgroceryreminder.model.databasehelper;

import java.util.ArrayList;
import java.util.List;

public class AdapterClass extends BaseAdapter {
    List<GroceryItems> datalist = new ArrayList<>();
    LayoutInflater inflater;

    AdapterClass(Context c) {
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        databasehelper dbHandler = new databasehelper(c);
        datalist = dbHandler.fetchAllData();
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.content_navigation, null);
        GroceryItems temp = datalist.get(position);
        TextView name = v.findViewById(R.id.name);
        TextView manufacture_date = v.findViewById(R.id.manufacture_date);
        TextView expiry_date = v.findViewById(R.id.expiry_date);
        String name_of_product = temp.getPname();
        String manufacture_date_of_product = temp.getManufacture();
        String expiry_date_of_product = temp.getExpiry();
        name.setText(name_of_product);
        manufacture_date.setText(manufacture_date_of_product);
        expiry_date.setText(expiry_date_of_product);
        return v;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}

