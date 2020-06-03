package com.example.smartgroceryreminder.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.smartgroceryreminder.R;
import com.example.smartgroceryreminder.adapters.ProductAdapter;
import com.example.smartgroceryreminder.model.DatabaseHelper;
import com.example.smartgroceryreminder.model.GroceryItems;

import java.util.ArrayList;
import java.util.List;

public class Expiry extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView products;
    private SwipeRefreshLayout refreshLayout;
    private List<GroceryItems> list;
    private DatabaseHelper databaseHelper;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiry);

        refreshLayout = findViewById(R.id.refreshLayout);
        products = findViewById(R.id.products);
        products.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list = new ArrayList<>();
        databaseHelper = new DatabaseHelper(getApplicationContext());
        adapter = new ProductAdapter(getApplicationContext(), Expiry.this);
        products.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        loadData();
    }

    private void loadData() {
        refreshLayout.setRefreshing(true);
        new LoadProducts().execute();
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }

    class LoadProducts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            list = databaseHelper.getItemsSorterByExpiry();
            Log.e("Expiry", "List size: " + list.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.setItems(list);
            refreshLayout.setRefreshing(false);
        }
    }
}
