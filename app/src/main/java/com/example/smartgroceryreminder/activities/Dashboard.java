package com.example.smartgroceryreminder.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.smartgroceryreminder.R;
import com.example.smartgroceryreminder.adapters.ProductAdapter;
import com.example.smartgroceryreminder.director.Helpers;
import com.example.smartgroceryreminder.model.DatabaseHelper;
import com.example.smartgroceryreminder.model.GroceryItems;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView products;
    private SwipeRefreshLayout refreshLayout;
    private List<GroceryItems> list;
    private Helpers helpers;
    private DatabaseHelper databaseHelper;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        refreshLayout = findViewById(R.id.refreshLayout);
        products = findViewById(R.id.products);
        products.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list = new ArrayList<>();
        helpers = new Helpers();
        databaseHelper = new DatabaseHelper(getApplicationContext());
        adapter = new ProductAdapter(getApplicationContext());
        products.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(this);
        loadData();
    }

    private void loadData() {
        refreshLayout.setRefreshing(true);
        new LoadProducts().execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle Dashboard view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_additem) {
            Intent a = new Intent(Dashboard.this, AddItem.class);
            startActivity(a);
        } else if (id == R.id.nav_expiry) {
            Intent e = new Intent(Dashboard.this, Expiry.class);
            startActivity(e);
        } else if (id == R.id.nav_barcode) {
            Intent b = new Intent(Dashboard.this, Barcode.class);
            startActivity(b);
        } else if (id == R.id.nav_help) {
            Intent h = new Intent(Dashboard.this, Help.class);
            startActivity(h);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    class LoadProducts extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            list = databaseHelper.getListContents();
            Log.e("Dashboard", "List size: " + list.size());
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

