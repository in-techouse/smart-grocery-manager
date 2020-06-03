package com.example.smartgroceryreminder.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartgroceryreminder.R;
import com.example.smartgroceryreminder.model.DatabaseHelper;
import com.example.smartgroceryreminder.model.GroceryItems;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private List<GroceryItems> items;
    private Context context;
    private Activity activity;
    private DatabaseHelper databaseHelper;

    public ProductAdapter(Context c, Activity a) {
        items = new ArrayList<>();
        context = c;
        activity = a;
        databaseHelper = new DatabaseHelper(context);
    }

    public void setItems(List<GroceryItems> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        final GroceryItems item = items.get(position);
        if (item.getImage() != null && item.getImage().length() > 0) {
            Glide.with(context).load(item.getImage()).into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);
        }
        holder.productName.setText(item.getName());
        if (item.getBrand() != null && item.getBrand().length() > 0) {
            holder.brand.setText(item.getBrand());
        }
        if (item.getUseage() != null && item.getUseage().length() > 0) {
            holder.useage.setText(item.getUseage());
        }
        holder.manufactureDate.setText(item.getManufactureDate());
        holder.expiryDate.setText(item.getExpiryDate());
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy hh:mm a");
            Date d = sdf.parse(item.getAlarm());
            String alarmTime = new SimpleDateFormat("EEE, dd, MMM-yyyy hh:mm a").format(d);
            holder.alarmDateTime.setText(alarmTime);
        } catch (Exception e) {
            holder.alarmDateTime.setText(item.getAlarm());
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(item);
            }
        });
    }

    private void deleteItem(final GroceryItems item) {
        MaterialDialog mDialog = new MaterialDialog.Builder(activity)
                .setTitle("DELETE PRODUCT")
                .setMessage("Are you sure to delete " + item.getName().toUpperCase() + "?")
                .setCancelable(false)
                .setPositiveButton("YES", R.drawable.ic_action_delete, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("NO", R.drawable.ic_action_close, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();

        // Show Dialog
        mDialog.show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView productName, brand, useage, manufactureDate, expiryDate, alarmDateTime;
        RelativeLayout delete;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            productName = itemView.findViewById(R.id.productName);
            brand = itemView.findViewById(R.id.brand);
            useage = itemView.findViewById(R.id.useage);
            manufactureDate = itemView.findViewById(R.id.manufactureDate);
            expiryDate = itemView.findViewById(R.id.expiryDate);
            alarmDateTime = itemView.findViewById(R.id.alarmDateTime);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
